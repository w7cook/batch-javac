#!/usr/bin/python

import argparse
import sys
import tempfile
import subprocess
import shutil
import os,sys

CIVET_HOME = os.path.dirname(__file__)

if __name__ == '__main__':
  # create the parser
  parser = argparse.ArgumentParser(
      description='Civet: Java Hybrid Partial Evaluator')

  parser.add_argument(
      '-classpath', '-cp', 
      help='Specify where to find user class files and libraries')
  parser.add_argument(
      '-srcpath', '-sp', help='Specify where to find user source files')
  parser.add_argument(
      '-outsourcepath', '-s', 
      help='Specify where to place generated source files')
  parser.add_argument(
      'inputfile', default=sys.stdin,
      help='The input file for hybrid partial evaluation')

  # parse the command line  
  args = parser.parse_args()

  MAIN='com.sun.tools.javac.Main'
  p_cmd = ['java', '-ea', 
      '-classpath', args.classpath+':'+CIVET_HOME+'/dist/lib/classes.jar'+
      ':'+CIVET_HOME+'/lib/batches.jar', 
      MAIN, '-civet', 
      '-cp', args.srcpath+':'+CIVET_HOME+'/dist/lib/classes.jar',
      '-s', args.outsourcepath, args.inputfile]
  subprocess.call(p_cmd)

