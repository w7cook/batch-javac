#!/usr/bin/env python

DB_NAME     = 'shakespeare'
DB_HOST     = 'localhost'
DB_USER     = 'root'
DB_PASSWD   = ''

# play id -> IMDB id
TITLES = {
            'asyoulikeit' : ('1300565',),
            'romeojuliet' : ('0152225',),
            'tamingshrew' : ('0186602',),
         }

import imdb, MySQLdb, itertools

def character_work(charID, workID):
    return "'%s-%s'"  % (charID[1:-1], workID[1:-1])

def getRoles(playID):
    # fetch roles from DB
    sqldb = MySQLdb.connect(host=DB_HOST, user=DB_USER, passwd=DB_PASSWD, db=DB_NAME)
    cursor = sqldb.cursor()
    #cursor.execute("""SELECT character_works.Character, characters.CharName
    #FROM characters
    #INNER JOIN character_works
    #ON character_works.Character = characters.CharID
    #WHERE character_works.Work='%s'""" % (playID,))
    cursor.execute("""SELECT CharID, CharName FROM characters WHERE WorkID='%s'""" % (playID,))
    roles = cursor.fetchall()
    sqldb.close()
    
    return roles
    
def getActors(playID):
    # fetch actors from IMDB
    db = imdb.IMDb()
    for movieID in TITLES[playID]:
        movie = db.get_movie(movieID)
        actors = []
        for member in movie['cast']:
            longName = member['long imdb name']
            role = member.currentRole
            if type(role) is imdb.utils.RolesList:
                actors.extend( [(longName, playID, r['name']) for r in role] )
            else:
                actors.append( (longName, playID, role['name']) )
                
    return actors

def main():
    actors = []
    for playID in TITLES:        
        roles = dict([(r[1], r[0]) for r in getRoles(playID)])
        for actorName, workName, roleName in getActors(playID):
            if roleName in roles:
                #actors.append( map(lambda s: "'%s'" % s.replace("'", "\\'"), (actorName, workName, roles[roleName])) )
                actors.append( map(lambda s: "'%s'" % s.replace("'", "\\'"), (actorName, roles[roleName])) )

    singleRoles = {}
    for actorName, actorRoles in itertools.groupby(actors, lambda a: a[0]):
        firstRole = list(actorRoles)[0]
        #singleRoles[actorName] = (firstRole[1], firstRole[2])
        singleRoles[actorName] = (firstRole[0], firstRole[1])

    print('use `%s`;' % DB_NAME)
    
    print('TRUNCATE TABLE `actors`;')
    #print('INSERT INTO `actors` (`Name`) VALUES\n')    
    print('INSERT INTO `actors` (`Name`, `Character`) VALUES\n')
    #print(',\n'.join(set(['(' + actor[0] + ')' for actor in actors])))
    print(',\n'.join(['(%s,%s)' % (actorName, actorRole[1]) for actorName, actorRole in singleRoles.items()]))  
    print(';')
    
    print('TRUNCATE TABLE `casting`;')
    #print('INSERT INTO `casting` (`Actor`, `Work`, `Character`) VALUES\n')
    print('INSERT INTO `casting` (`Actor`, `Character`) VALUES\n')
    print(',\n'.join(['(' + ','.join(actor) + ')' for actor in actors]))
    print(';')

if __name__ == '__main__': main()