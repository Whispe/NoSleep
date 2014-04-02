package cs475;

import java.io.Serializable;

/**
 * This class represents a double label for a feature of a data instance.
 */
public class RegressionLabel extends Label implements Serializable {

	double label;
	
	/**
	 * This constructor creates an instance of the RegressionLabel class.
	 * @param label The value of the label.
	 */
	public RegressionLabel(double label) {
		// TODO Auto-generated constructor stub
		this.label = label;
	}

	/**
	 * This method formats the RegressionLabel object into a String.
	 */
	public String toString() {
		// TODO Auto-generated method stub
		String strLabel = Double.toString(this.label);
		return strLabel;
	}

}
