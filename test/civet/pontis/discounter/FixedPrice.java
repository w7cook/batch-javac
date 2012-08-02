package pontis.discounter;

import pontis.event.An_Event;

public class FixedPrice implements A_Discounter {

	private double fixedPrice;

	public double getFixedPrice() {
		return fixedPrice;
	}

	public void setFixedPrice(double fixedPrice) {
		this.fixedPrice = fixedPrice;
	}

	//	 
	public Double calcDiscountedPrice(An_Event theEvent) {
		return fixedPrice;
	}

}
