package pontis.discounter;

import pontis.event.An_Event;


public class PriceReductionDiscounter implements A_Discounter {

	double reduction;

	public double getReduction() {
		return reduction;
	}

	public void setReduction(double reduction) {
		this.reduction = reduction;
	}

	//	 
	public Double calcDiscountedPrice(An_Event theEvent) {
		double result = theEvent.getListPrice();

		result = result - reduction;

		return result;
	}

}
