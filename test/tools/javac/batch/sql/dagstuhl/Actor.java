package sql.dagstuhl;

import batch.sql.Column;
import batch.sql.Entity;
import batch.sql.Id;
import batch.sql.Inverse;
import batch.sql.Many;

@Entity(name="actors")
public abstract class Actor {
	@Id
	public String Name;
	
	@Column(name="Character")
	public Character Character;

	@Inverse("Actor")
	public Many<Casting> Casting;	
	
}

