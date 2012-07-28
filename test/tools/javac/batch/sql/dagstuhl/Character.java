package sql.dagstuhl;

import batch.sql.Column;
import batch.sql.Entity;
import batch.sql.Id;
import batch.sql.Inverse;
import batch.sql.Many;

@Entity(name="characters")
public abstract class Character {
	@Id
	public String CharID;
	
	public String CharName;
	
	@Column(name="WorkID")
	public Play Play;
	
	@Inverse("Character")
	public Actor Actor;
	
	@Inverse("Character")
	public Many<Paragraph> Paragraphs;
	
	@Inverse("Character")
	public Many<Casting> Casting;
}
