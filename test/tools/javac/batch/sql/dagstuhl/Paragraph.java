package sql.dagstuhl;

import batch.Fun;
import batch.sql.Column;
import batch.sql.Entity;
import batch.sql.Id;

@Entity(name = "paragraphs")
public class Paragraph {
	@Id
	public int ParagraphID;

	@Column(name = "WorkID")
	public Play Play;

	public int ParagraphNum;

	@Column(name = "CharID")
	public Character Character;

	@Column(name = "SectionID")
	public Section Section;

	@Column(name = "ChapterID")
	public Chapter Chapter;

	public String PlainText;
	
	/*
	 * @IncludeInBatch public Paragraph prev() { 
	 * return Section.Paragraphs.id(ParagraphID - 1); }
	 */
	
	/*
	 * @IncludeInBatch public Paragraph next() { 
	 * return Section.Paragraphs.id(ParagraphID + 1); }
	 */

	static Fun<Paragraph, Section> bySection = new Fun<Paragraph, Section>() {
		public Section apply(Paragraph p) {
			return p.Section;
		}
	};

	static Fun<Paragraph, Integer> byParagraphID = new Fun<Paragraph, Integer>() {
		public Integer apply(Paragraph p) {
			return p.ParagraphID;
		}
	};

	static Fun<Paragraph, Boolean> isCharacter(final String CharID) {
		return new Fun<Paragraph, Boolean>() {
			public Boolean apply(Paragraph p) {
				return p.Character.CharID == CharID;
			}
		};
	}
	
	// for version 1
	static Fun<Paragraph, Boolean> isActor(final String actorName) {
		return new Fun<Paragraph, Boolean>() {
			public Boolean apply(Paragraph p) {
				return p.Character.Actor != null && p.Character.Actor.Name == actorName;
			}
		};
	}
	
	// for version 2
	static Fun<Paragraph, Boolean> hasActor(final String actorName) {
		return new Fun<Paragraph, Boolean>() {
			public Boolean apply(Paragraph p) {
				return p.Character.Casting.exists(Casting.isActor(actorName));
			}
		};
	}
}
