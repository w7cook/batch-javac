package sql.dagstuhl;

import batch.Fun;
import batch.sql.Column;
import batch.sql.Entity;
import batch.IncludeInBatch;

@Entity(name="casting")
public class Casting {
	@Column(name="Actor")
	public Actor Actor;
	
	@Column(name="Character")
	public Character Character;
	
	static Fun<Casting, Boolean> isActor(final String actorName) {
		return new Fun<Casting, Boolean>() {
			@IncludeInBatch public Boolean apply(Casting c) {
				return c.Actor.Name == actorName;
			}
		};
	}
}
