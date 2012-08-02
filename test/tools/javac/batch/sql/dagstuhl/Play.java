package sql.dagstuhl;

import batch.sql.Entity;
import batch.sql.Id;
import batch.sql.Inverse;
import batch.sql.Many;

@Entity(name="works")
public class Play {
	@Id
	public String WorkID;
	
	public String Title;
	
	public String LongTitle;
	
	@Inverse("Play")
	public Many<Character> Characters;
	
	@Inverse("Play")
	public Many<Paragraph> Paragraphs;
	
	@Inverse("Play")
	public Many<Chapter> Chapters;
	
	@Inverse("Play")
	public Many<Section> Sections;	
}
