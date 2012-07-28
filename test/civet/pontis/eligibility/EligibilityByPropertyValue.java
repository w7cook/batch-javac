package pontis.eligibility;

import pontis.event.An_Event;

public class EligibilityByPropertyValue implements An_Eligibility {

	String propertyName;
	String value;

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String valueContains) {
		this.value = valueContains;
	}

	//	 
	public boolean isEligible(An_Event theEvent) {
		boolean result = false;
		try {
			String propertyValue = (String) theEvent.getClass().getMethod(
					"get" + propertyName).invoke(theEvent);
			if (propertyValue.contains(value)) {
				result = true;
			}
		} catch (Exception e) {
			// absorb
		}

		return result;
	}

}
