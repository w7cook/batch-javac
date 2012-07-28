#!/usr/bin/env python

DATA_DIR = 'oss-textdb'

import csv, os, re, sys

DATA_RE = re.compile(r'(~[\w\s]+~|\d+),')

def extractColumns(data, columnCount):
    valueStart = valueEnd = 0
    currentValue = None
    results = []
    size = len(data)
    
    while valueEnd < size:
        if data[valueEnd] == '~':                    
            valueStart = valueEnd = valueEnd + 1 # skip the tilde
            
            # search for end of string
            try:
                while (data[valueEnd] != '~'):
                    valueEnd += 1
            except IndexError:
                raise ValueError('Unterminated string:\n\t%s' % data[valueStart:])
            else:
                currentValue = "'%s'" % data[valueStart:valueEnd].replace("'", "\\'")
            
        elif data[valueEnd] == ',' or data[valueEnd] == '\n':
            if not currentValue:
                currentValue = data[valueStart:valueEnd]
            results.append(currentValue)
            
            currentValue = None
            valueStart = valueEnd + 1   # skip the delimiter
            
            if data[valueEnd] == '\n':
                assert len(results) == columnCount, 'Short column'
                
            # add new row to results
            if len(results) == columnCount:
                yield [r.strip() if r else 'NULL' for r in results]
                results = []

        valueEnd += 1
        
    # leftover results?
    #   fails if the file does not contain a trailing newline    
    assert (not (results or currentValue)), (results, currentValue)
    
def toSQL(filename, tableName):
   
    dataFile = open(filename)
    
    dataRow = dataFile.readline()
    columns = ['`%s`' % c.replace('~', '').strip() for c in dataRow.split(',')]
    rows = ['INSERT INTO `%s` (%s) VALUES (%s)' % (tableName, ','.join(columns), ','.join(row))
            for row in extractColumns(dataFile.read(), len(columns))]

    dataFile.close()

    return '\n'.join(['TRUNCATE TABLE `%s`;' % tableName,
                      #'INSERT INTO `%s` (%s) VALUES\n' % (tableName, ','.join(columns)),
                      ';\n'.join(rows) + ';'])

def getColumnsAndRows(filename):
    dataFile = open(filename)
    dataRow = dataFile.readline()
    columns = ['`%s`' % c.replace('~', '').strip() for c in dataRow.split(',')]
    rows = extractColumns(dataFile.read(), len(columns))
    dataFile.close()
    
    return columns, list(rows)

def character_work(charID, workID):
    return "'%s-%s'"  % (charID[1:-1], workID[1:-1])
    
def getCharacterColumnsAndRows(filename):
    columns, rows = getColumnsAndRows(filename)
        
    workColumn = columns.index('`Works`')
    idColumn = columns.index('`CharID`')
    
    #charRows = [row[:workColumn] + row[workColumn+1:] for row in rows]
    #charColumns = columns[:workColumn] + columns[workColumn+1:]
    charColumns = columns[:workColumn] + ['`WorkID`'] + columns[workColumn+1:]
    charRows = []
    
    charWorksColumns = ('`Character`', '`Work`')
    charWorksRows = []
    for row in rows:
        charID = row[idColumn]
        ws = row[workColumn].split(',')
        if len(ws) > 1:
            ws[0] = ws[0]+"'"
            for i in range(1, len(ws)-1):
                ws[i] = "'%s'" % ws[i]
            ws[-1] = "'" + ws[-1] 
        for w in ws:
            newID = character_work(charID, w)
            rowWork = row[:workColumn] + [w] + row[workColumn+1:]
            rowChar = rowWork[:idColumn] + [newID] + rowWork[idColumn+1:]
            charRows.append(rowChar)
            charWorksRows.append((newID, w))
            
    # add stage directions
    nameColumn = columns.index('`CharName`')
    works = set([row[workColumn] for row in charRows])
    works.remove("'asyoulikeit'")
    newChars = [(character_work("'xxx'", work), work) for work in works]
    charWorksRows.extend(newChars)
    for newChar, work in newChars:
        newRow = ['NULL'] * len(charColumns)
        newRow[idColumn] = newChar
        newRow[workColumn] = work
        newRow[nameColumn] = "'(stage directions)'"
        charRows.append(newRow)

    return (charColumns, charRows), (charWorksColumns, charWorksRows)
                      
def writeTable(tableName, columns, data):
    insertExpr = 'INSERT INTO `%s` (%s) VALUES ' % (tableName, ','.join(columns))
    return '\n'.join(['TRUNCATE TABLE `%s`;' % tableName,                     
                      ';\n'.join([insertExpr + '(' + ','.join(row) + ')' for row in data]),
                      ';'])

def toSQL(filename, tableName):
    columns, rows = getColumnsAndRows(filename)
    return writeTable(tableName, columns, rows)
    
def charactersToSQL(filename):
    (charColumns, charRows), (charWorksColumns, charWorksRows) = getCharacterColumnsAndRows(filename)
    return writeTable('characters', charColumns, charRows) #+ '\n' + writeTable('character_works', charWorksColumns, charWorksRows)
        
def paragraphsToSQL(filename, tableName):
    paraColumns, paraRows = getColumnsAndRows(filename)
    workColumn = paraColumns.index('`WorkID`')
    charColumn = paraColumns.index('`CharID`')
    paraRows = [row[:charColumn] + [character_work(row[charColumn], row[workColumn])] + row[charColumn+1:]
                for row in paraRows]
    return writeTable(tableName, paraColumns, paraRows)

def updateCharacters():
    return """UPDATE paragraphs, chapters 
              SET paragraphs.SectionID=chapters.SectionID, paragraphs.ChapterID=chapters.ChapterID
              WHERE paragraphs.WorkID=chapters.WorkID && paragraphs.Section=chapters.Section && paragraphs.Chapter = chapters.Chapter;"""    

def addSections():
    return "INSERT INTO sections (WorkID, Section) SELECT DISTINCT WorkID, Section FROM chapters;"
    
def updateChapters():
    return "UPDATE chapters, sections SET chapters.SectionID=sections.SectionID WHERE sections.WorkID = chapters.WorkID && sections.Section = chapters.Section;"    

def main():
    print('use `shakespeare`;')
    print(toSQL(os.path.join(DATA_DIR, 'Works.txt'), 'works'))
    print(charactersToSQL(os.path.join(DATA_DIR, 'Characters.txt')))
    print(toSQL(os.path.join(DATA_DIR, 'Chapters.txt'), 'chapters'))
    #print(toSQL(os.path.join(DATA_DIR, 'Paragraphs.txt'), 'paragraphs'))
    print(paragraphsToSQL(os.path.join(DATA_DIR, 'Paragraphs.txt'), 'paragraphs'))
    print(addSections())
    print(updateChapters())
    print(updateCharacters())


if __name__ == '__main__': main()