package pontis.promotion;

import pontis.event.An_Event;

public interface A_Promotion {

	public String getName();

	public Double calcPromotionalPrice(An_Event theEvent);

}
