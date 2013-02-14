#!/usr/bin/python

import argparse
import sys
import tempfile
import subprocess
import shutil
import os, fnmatch
import fileinput


import os,sys

CIVET_HOME = os.path.abspath(os.path.dirname(__file__))

TESTOUTDIR=CIVET_HOME+'/civet-test-output'
TESTDIR=CIVET_HOME+'/test/civet'
CIVET=CIVET_HOME+'/civet.py'


def find(pattern, path):
    result = []
    for root, dirs, files in os.walk(path):
        for name in files:
            if fnmatch.fnmatch(name, pattern):
                result.append(os.path.join(root, name))
    return result


if __name__ == '__main__':

  os.chdir(TESTDIR)
  testfiles = find('TESTS', './')
  testcases = fileinput.input(testfiles);
  if os.path.exists(TESTOUTDIR):
    shutil.rmtree(TESTOUTDIR)
  os.makedirs(TESTOUTDIR)
  if not os.path.exists("classes"):
    os.makedirs("classes")
  
  totalCases = 0
  passedCases = 0
  for line in testcases:
    totalCases = totalCases + 1
    targetPackage = os.path.dirname(fileinput.filename())        
    testcase = line.strip().split('=')
    sourceDir = TESTDIR+'/'+targetPackage+'/'
    targetDir = TESTOUTDIR+'/'+targetPackage+'/'
    print "Processing... "+sourceDir+testcase[0]+".java"
    p_cmd = [CIVET, 
             '-cp', CIVET_HOME+'/bin',
             '-d', 'classes',
             '-sourcepath', TESTDIR,
             '-outputsource', TESTOUTDIR,
             sourceDir+testcase[0]+".java"]
    print " ".join(p_cmd)
    subprocess.call(p_cmd)

    passed = True
    notMatched = ""
    for expected in testcase[1].split(','):
      diffCmd = subprocess.Popen(
        ['diff', '-E', '-b', '-w', '-B',
        sourceDir+expected+".java.expected", 
        targetDir+expected+".java"], 
        shell=False, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
      (diffResult, diffErr) = diffCmd.communicate()
      if diffResult != '' or diffErr != '':
        notMatched += "`"+sourceDir+expected+".java.expected' did not match!\n"
        passed = False
    if passed:
      passedCases = passedCases + 1
      print sourceDir+testcase[0]+' passed :)'
    else:
      print sourceDir+testcase[0]+' failed :('
      print "---------------------------"
      print notMatched
      print "---------------------------"
  
  print "============================================"
  print "Total test cases: "+str(totalCases)
  print "Passed test cases: "+str(passedCases)
  print "Failed test cases: "+str(totalCases - passedCases)
  







