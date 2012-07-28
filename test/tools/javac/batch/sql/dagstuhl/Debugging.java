package sql.dagstuhl;

import java.io.PrintStream;
import java.sql.SQLException;

import batch.sql.Group;
import batch.sql.JDBC;
import batch.sql.SQLBatch;

public class Debugging {
	public static void main(String[] args) throws SQLException {
		new Debugging().run();
	}

	protected SQLBatch<Dagstuhl> connection;	

	static PrintStream out = System.out;
	
	public void run() throws SQLException {
		String cstr = "jdbc:mysql://localhost/shakespeare?user=root&password=";
		connection = new JDBC<Dagstuhl>(Dagstuhl.class, cstr);
		test();
	}
	
	public void testID() {
		for (Dagstuhl db : connection) {
			out.println(db.Plays.id("asyoulikeit").LongTitle);
		}
	}
	
	public void testSingleLoop() {
		for (Dagstuhl db : connection) {
			for (Play play : db.Plays)
				out.println(play.Title);
		}
	}
	
	public void testMultiLoop() {
		for (Dagstuhl db : connection) {
			for (Play play : db.Plays) {
				//out.println(play.Title);
				for (Character character : play.Characters) {
					//out.print("\t");
					out.println(character.CharName);
				}
			}
		}
	}
	
	public void testIdLoop() {
		for (Dagstuhl db : connection) {
			Play play = db.Plays.id("asyoulikeit");
			for (Section section : play.Sections)
				out.println(section.Section);
		}
	}
	
	public void testLoopCondition(String actorName) {
		for (Dagstuhl db : connection)
			for (Actor actor : db.Actors)
				if (actor.Name == actorName)
					for (Paragraph paragraph : actor.Character.Paragraphs)
						out.print(paragraph.PlainText);
	}
	
	public void dramatisPersonae(String playID) {
		for (Dagstuhl db : connection) {
			for (Casting casting : db.Castings)
				if (casting.Character.Play.WorkID == playID) {
						out.print(casting.Character.CharName);
						out.print("...................");
						out.println(casting.Actor.Name);
				}								
		}
	}
	
	public void actors() {
		for (Dagstuhl db : connection) {
			for (Actor actor : db.Actors)
				out.println(actor.Name);
		}
	}

	public void works() {
		for (Dagstuhl db : connection) {
			for (Play play : db.Plays)
				out.println(play.Title);
		}
	}

	
	// runtime error
	public void actorActs2(String actorName) {
		for (Dagstuhl db : connection) {
			Actor actor = db.Actors.id(actorName);
			for (Casting casting : actor.Casting) {
					//print("\tas {0} in {1}", casting.Character.CharName,
					//		casting.Character.Play.Title);
					
					for (Group<Section, Paragraph> g : casting.Character.Paragraphs.groupBy(Paragraph.bySection)) { 
						out.print("\t\t Act");
						out.println(g.Key.Section);						
					}
			}
			
		}
	}
	
	public void test() throws SQLException {
		//testID();
		//testSingleLoop();
		//testMultiLoop();
		//testIdLoop();
		//testLoopCondition("Colm Feore");
		//dramatisPersonae("romeojuliet");
		//actors();
		works();
		//actorActs2("Colm Feore");
	}
}
