NOTE: The example requires MySQL and Java 1.6 to execute.

It takes three steps to run the example:
   1) Build the database
   2) Compile the code
   3) Run the code

============================
1) Create the database
============================
Use the included .sql files to create the database.
NOTE: You'll need to increase the server's max_allowed_packet, because the data contains large entries. One way to do this is:
	sudo mysqladmin shutdown; sudo mysqld_safe --max_allowed_packet=16777216 &

You can generate the .sql files yourself, from the sources by executing the following commands in the data subdirectory:
	./buildworks.py > shakespeare_works.sql
	./buildensemble.py > shakespeare_ensemble.sql 
	   (This script requires the IMDB Python package (http://imdbpy.sourceforge.net/), and an internet connection. It may print some errors; ignore them.)

	mysql -u root < shakespeare_schema.sql
	mysql -u root < shakespeare_works.sql
	mysql -u root < shakespeare_ensemble.sql
	
============================
2) Compile the code
============================
java -classpath ./jaba-compiler.jar:./jaba-runtime.jar Jabac -d build src/tests/dagstuhl/*.java

NOTE: The compiler outputs some debug information. You can ignore it.

============================
3) Run the code
============================
java -classpath jaba-runtime.jar:mysql-connector-java-5.1.10.jar:build tests.dagstuhl.BeautyContest

NOTE: The runtime outputs some debug information to stderr, specifically a description of each batch executed and its corresponding SQL query. Some of this output may be intermingled with the program output, but you can filter it by redirecting stderr.	
