package sql.dagstuhl;

import java.io.PrintStream;
import java.sql.SQLException;

import batch.sql.JDBC;
import batch.sql.SQLBatch;

public class BeautyContest {
	public static void main(String[] args) throws SQLException {
		new BeautyContest().run();
	}

	protected SQLBatch<Dagstuhl> connection;	
	static PrintStream out = System.out; 
	
	public void run() throws SQLException {
		String cstr = "jdbc:mysql://localhost/shakespeare?user=root&password=";
		connection = new JDBC<Dagstuhl>(Dagstuhl.class, cstr);
		test();
	}
	
	public void test() throws SQLException {
		actorActs_v1("Colm Feore");
		actorActs_v2("Colm Feore");
		sides("2witch-mac-macbeth");		
	}
	
	public void actorActs_v1(String actorName) {
		for (Dagstuhl db : connection) {
			characterActs(db.Actors.id(actorName).Character.CharID);
		}
	}
	
	public void actorActs_v2(String actorName) {
		for (Dagstuhl db : connection) {
			for (Casting casting : db.Castings)
				if (casting.Actor.Name == actorName) 
					characterActs(casting.Character.CharID);
		}
	}
	
	public void characterActs(String CharID) {
		for (Dagstuhl db : connection) {
			for (Section section : db.Sections)
				if (section.Paragraphs.exists(Paragraph.isCharacter(CharID))) {
					out.print("In "); out.print(section.Play.Title); out.print(": Act "); out.println(section.Section);
				}
		}
	}
	
	public void sides(String charID) {
		for (Dagstuhl db : connection) {
			for (Paragraph p : db.Paragraphs.orderBy(Paragraph.byParagraphID, true)) {
				Paragraph prev = p.Play.Paragraphs.id(p.ParagraphID-1);
				Paragraph next = p.Play.Paragraphs.id(p.ParagraphID+1);
				if ((next.Character.CharID == charID && next.Section == p.Section) ||
					p.Character.CharID == charID ||
					(prev.Character.CharID == charID && prev.Section == p.Section)) {
						out.print(p.Character.CharName); out.print(": "); out.println(p.PlainText);
				}					    				
			}
		}
	}
}
