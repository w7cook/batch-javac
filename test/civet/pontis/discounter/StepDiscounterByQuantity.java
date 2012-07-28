package pontis.discounter;

import pontis.event.An_Event;


public class StepDiscounterByQuantity implements A_Discounter {

	private long quantity1;
	private long quantity2;
	private long quantity3;
	private A_Discounter discounter1;
	private A_Discounter discounter2;
	private A_Discounter discounter3;

	public long getQuantity1() {
		return quantity1;
	}

	public void setQuantity1(long quantity1) {
		this.quantity1 = quantity1;
	}

	public long getQuantity2() {
		return quantity2;
	}

	public void setQuantity2(long quantity2) {
		this.quantity2 = quantity2;
	}

	public long getQuantity3() {
		return quantity3;
	}

	public void setQuantity3(long quantity3) {
		this.quantity3 = quantity3;
	}

	public A_Discounter getDiscounter1() {
		return discounter1;
	}

	public void setDiscounter1(A_Discounter discounter1) {
		this.discounter1 = discounter1;
	}

	public A_Discounter getDiscounter2() {
		return discounter2;
	}

	public void setDiscounter2(A_Discounter discounter2) {
		this.discounter2 = discounter2;
	}

	public A_Discounter getDiscounter3() {
		return discounter3;
	}

	public void setDiscounter3(A_Discounter discounter3) {
		this.discounter3 = discounter3;
	}

	//	 
	public Double calcDiscountedPrice(An_Event theEvent) {
		double result = theEvent.getListPrice();
		if (theEvent.getQuantity() >= quantity3) {
			result = discounter3.calcDiscountedPrice(theEvent);
		} else if (theEvent.getQuantity() >= quantity2) {
			result = discounter2.calcDiscountedPrice(theEvent);
		} else if (theEvent.getQuantity() >= quantity1) {
			result = discounter1.calcDiscountedPrice(theEvent);
		}
		return result;
	}

}
