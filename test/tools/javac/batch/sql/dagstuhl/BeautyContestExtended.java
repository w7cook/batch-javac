package sql.dagstuhl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class BeautyContestExtended extends BeautyContest {
	@Override
	public void test() throws SQLException {
		//actorActs_v1("Colm Feore");			 // run-time error
		//actorActs_v2("Colm Feore");  		     // long execution time
		//actorActsWithSet_v1("Colm Feore"); 	 // run-time error
		//actorActsWithSet_v2("Colm Feore");     // works, but loads extra data
		//actorActsDeclarative("Colm Feore");    // run-time error
		//sidesB("2witch-mac-macbeth");          // works, possibly faster than roleScript
		actorActs_v1("Colm Feore");
		actorActs_v2("Colm Feore");
		sides("2witch-mac-macbeth");		
	}
	
	// run-time error b/c 1-1 relation (Actor-Character) cannot be inverted
	public void actorActsWithSet_v1(String name) {
		Map<String, Set<Integer>> playActs = new HashMap<String, Set<Integer>>(); 
		
		for (Dagstuhl db : connection) {
			Actor actor = db.Actors.id(name);
			if (!playActs.containsKey(actor.Character.Play.Title))
				playActs.put(actor.Character.Play.Title, new HashSet<Integer>());
			Set<Integer> acts = playActs.get(actor.Character.Play.Title);
			for (Paragraph p : actor.Character.Paragraphs)
				acts.add(p.Section.Section);
		}
		
		System.out.print(name + " appears in ");
		if (playActs.isEmpty())
			System.out.println("nothing");
		else {
			System.out.println();
			for (Entry<String, Set<Integer>> e : playActs.entrySet()) {
				System.out.println("\t" + e.getKey());
				for (Integer act : e.getValue())
					System.out.println("\t\tAct " + act);				
			}
		}
	}
	
	// this works, but brings too much data over (it performs the "distinct" aggregation client-side)
	public void actorActsWithSet_v2(String name) {
		Map<String, Set<Integer>> playActs = new HashMap<String, Set<Integer>>(); 
		
		for (Dagstuhl db : connection) {
			Actor actor = db.Actors.id(name);
			for (Casting casting : db.Castings) {
				if (actor.Name == casting.Actor.Name) {
					if (!playActs.containsKey(casting.Character.Play.Title))
						playActs.put(casting.Character.Play.Title, new HashSet<Integer>());
					Set<Integer> acts = playActs.get(casting.Character.Play.Title);
					for (Paragraph p : casting.Character.Paragraphs)
						acts.add(p.Section.Section);
				}
			}						
		}
		
		System.out.print(name + " appears in ");
		if (playActs.isEmpty())
			System.out.println("nothing");
		else {
			System.out.println();
			for (Entry<String, Set<Integer>> e : playActs.entrySet()) {
				System.out.println("\t" + e.getKey());
				for (Integer act : e.getValue())
					System.out.println("\t\tAct " + act);				
			}
		}
	}
	
	// This doesn't work, b/c we can't invert 1-1 relations (Actor-Character) 
	public void actorActs_v1(String actorName) {
		for (Dagstuhl db : connection) {
			for (Section section : db.Sections) {
				if (section.Paragraphs.exists(Paragraph.isActor(actorName))) {
					out.print(actorName);
					out.print(" appears in ");
					out.print(section.Play.Title);
					out.print(": Act ");
					out.println(section.Section);
				}
			}
		}
	}
	
	// This works, but takes forever to execute
	public void actorActs_v2(String actorName) {
		for (Dagstuhl db : connection) {
			for (Section section : db.Sections) {
				if (section.Paragraphs.exists(Paragraph.hasActor(actorName))) {
					//print("{0} appears in {1}: Act {2}", actorName, section.Play.Title, section.Section);
					out.print(actorName);
					out.print(" appears in ");
					out.print(section.Play.Title);
					out.print(": Act ");
					out.println(section.Section);
				}
			}
		}
	}
	
	// Gives runtime error: I think there's a bug in how .where() is implemented
	public void actorActsDeclarative(String actorName) {
		for (Dagstuhl db : connection) {
//			for (Group<Section, Paragraph> g : db.Paragraphs.where(Paragraph.hasActor(actorName)).groupBy(Paragraph.bySection))
//				out.println(g.Key.Section);
		}
	}
	
	// Doesn't compile. For some reason, the JastAddJ compiler can't find the variable casting in the inner loop.
	//    I think I got too clever trying to pass a value to a lambda
	/*public void actorActsHierarchical(String actorName) {
		for (Dagstuhl db : connection) {
			Actor actor = db.Actors.id(actorName);
			for (Casting casting : actor.Casting) {
				for (Section section : casting.Play.Sections)
					if (section.Paragraphs.exists(Paragraph.isCharacter(casting.Character.CharID)))
						print("In {0}: Act {1}", section.Play.Title, section.Section);					
				
			}
		}
	}*/	
	
	// works, but slow
	public void characterActsSlow(String CharID) {
		for (Dagstuhl db : connection) {
			for (Play play : db.Plays)
				for (Section section : play.Sections) {
					if (section.Chapters.exists(Chapter.hasCharacter(CharID))) {
						out.print("In ");
						out.print(section.Play.Title);
						out.print(": Act ");
						out.println(section.Section);
					}
				}
		}
	}
	
	// same as sides, but removes conditions on section by iterating over sections
	public void sidesB(String charID) {
		for (Dagstuhl db : connection) {
			for (Section s : db.Sections) {
				for (Paragraph p : s.Paragraphs.orderBy(Paragraph.byParagraphID)) {
					Paragraph prev = p.Play.Paragraphs.id(p.ParagraphID-1);
					Paragraph next = p.Play.Paragraphs.id(p.ParagraphID+1);
					if ((next.Character.CharID == charID) ||
						p.Character.CharID == charID ||
						(prev.Character.CharID == charID)) {
							out.print(p.Character.CharName);
							out.print(": ");
							out.println(p.PlainText);
					}					    						    
				}
			}
		}
	}
}
