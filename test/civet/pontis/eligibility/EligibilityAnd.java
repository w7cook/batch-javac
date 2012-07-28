package pontis.eligibility;

import pontis.event.An_Event;

public class EligibilityAnd implements An_Eligibility {

	private An_Eligibility eligibility1;
	private An_Eligibility eligibility2;

	public An_Eligibility getEligibility1() {
		return eligibility1;
	}

	public void setEligibility1(An_Eligibility eligibility1) {
		this.eligibility1 = eligibility1;
	}

	public An_Eligibility getEligibility2() {
		return eligibility2;
	}

	public void setEligibility2(An_Eligibility eligibility2) {
		this.eligibility2 = eligibility2;
	}

	//	 
	public boolean isEligible(An_Event theEvent) {
		boolean result = false;
		if (eligibility1.isEligible(theEvent)
				&& eligibility2.isEligible(theEvent)) {
			result = true;
		}
		return result;
	}

}
