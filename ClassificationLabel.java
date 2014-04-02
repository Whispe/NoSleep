package cs475;

import java.io.Serializable;

/**
 * This class represents a binary label for a feature of a data instance.
 */
public class ClassificationLabel extends Label implements Serializable {

	int label = -1;
	
	/**
	 * This constructor creates an instance of the ClassificationLabel class.
	 * @param label The value 0 or 1 of the binary label.
	 */
	public ClassificationLabel(int label) {
		// TODO Auto-generated constructor stub
		//if (label == 0 || label == 1) {
			this.label = label;
		//}
	}

	/**
	 * This method formats the ClassificationLabel object into a String.
	 */
	public String toString() {
		// TODO Auto-generated method stub
		
		String strLabel = "";
		
		if (label != -1)
			strLabel = Integer.toString(this.label);
		else
			strLabel = "null";
		return strLabel;
	}

}
