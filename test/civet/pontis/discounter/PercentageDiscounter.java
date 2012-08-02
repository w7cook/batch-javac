package pontis.discounter;

import pontis.event.An_Event;


public class PercentageDiscounter implements A_Discounter {

	double percent;

	public double getPercent() {
		return percent;
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}

	//	 
	public Double calcDiscountedPrice(An_Event theEvent) {
		return theEvent.getListPrice() * (1 - percent / 100.0);

		// result = result-(result*(percent/100.0));
		// return result;
	}

}
