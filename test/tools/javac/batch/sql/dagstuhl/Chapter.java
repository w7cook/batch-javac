package sql.dagstuhl;

import batch.Fun;
import batch.sql.Column;
import batch.sql.Entity;
import batch.sql.Id;
import batch.sql.Inverse;
import batch.sql.Many;
import batch.IncludeInBatch;

@Entity(name="chapters")
public class Chapter {
	@Id
	public int ChapterID;
	
	@Column(name="WorkID")
	public Play Play;
	
	@Column(name="SectionID")
	public Section Section;
	
	public int Chapter;
	
	@Inverse("Chapter")
	public Many<Paragraph> Paragraphs;
	
	static Fun<Chapter, Boolean> hasCharacter(final String CharID) {
		return new Fun<Chapter, Boolean>() {
			@IncludeInBatch public Boolean apply(Chapter c) {
				return c.Paragraphs.exists(Paragraph.isCharacter(CharID));
			}
		};
	}
	
	static Fun<Chapter, Boolean> hasActor(final String name) {
		return new Fun<Chapter, Boolean>() {
			@IncludeInBatch public Boolean apply(Chapter c) {
				return c.Paragraphs.exists(Paragraph.hasActor(name));
			}
		};
	}
}
