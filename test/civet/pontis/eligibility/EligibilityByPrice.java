package pontis.eligibility;

import pontis.event.An_Event;

public class EligibilityByPrice implements An_Eligibility {

	private double price;

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	//	 
	public boolean isEligible(An_Event theEvent) {
		boolean result = false;

		if (theEvent.getListPrice() >= price) {
			result = true;
		}
		return result;
	}

}
