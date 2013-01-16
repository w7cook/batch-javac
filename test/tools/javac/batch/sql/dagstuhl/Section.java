package sql.dagstuhl;

import batch.Fun;
import batch.sql.Column;
import batch.sql.Entity;
import batch.sql.Id;
import batch.sql.Inverse;
import batch.sql.Many;
import batch.IncludeInBatch;

@Entity(name="sections")
public class Section {
	@Id
	public int SectionID;
	
	@Column(name="WorkID")
	public Play Play;
	
	public int Section;
	
	@Inverse("Section")
	public Many<Chapter> Chapters;	
	
	@Inverse("Section")
	public Many<Paragraph> Paragraphs;	
	
	static Fun<Section, Boolean> hasCharacter(final String CharID) {
		return new Fun<Section, Boolean>() {
			@IncludeInBatch public Boolean apply(Section s) {
				return s.Chapters.exists(Chapter.hasCharacter(CharID));
			}
		};
	}
	
	static Fun<Section, Boolean> hasActor(final String name) {
		return new Fun<Section, Boolean>() {
			@IncludeInBatch public Boolean apply(Section s) {
				return s.Chapters.exists(Chapter.hasActor(name));
			}
		};
	}
}
