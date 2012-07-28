package pontis.promotion;

import pontis.discounter.A_Discounter;
import pontis.eligibility.An_Eligibility;
import pontis.event.An_Event;

public class Promotion implements A_Promotion {

	private A_Discounter discounter;
	private An_Eligibility eligibility;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name, int x) {
		System.out.println(name);
		System.out.println(x);
		setName(name, 23);
	}

	public void setName(String name) {
		this.name = name;
	}

	public A_Discounter getDiscounter() {
		return discounter;
	}

	public void setDiscounter(A_Discounter discounters) {
		this.discounter = discounters;
	}

	public An_Eligibility getEligibility() {
		return eligibility;
	}

	public void setEligibility(An_Eligibility eligibility) {
		this.eligibility = eligibility;
	}

	public void hi() {

	}

	//	 
	public Double calcPromotionalPrice(An_Event theEvent) {
		Double result = null;
		// this.hi();
		if (eligibility.isEligible(theEvent)) {
			result = discounter.calcDiscountedPrice(theEvent);
		} else {
			result = theEvent.getListPrice();
		}
		return result;
	}

}
