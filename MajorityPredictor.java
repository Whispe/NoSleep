package cs475;

import java.util.List;

/**
 * This class contains a label predictor with parameters determined by
 * the majority learning algorithm.
 */
public class MajorityPredictor extends Predictor {

	int majorityVal;
	
	/**
	 * This constructor creates an instance of the MajorityPredictor class
	 * and trains it with a set of data instances.
	 * @param instances The training instances.
	 */
	public MajorityPredictor(List<Instance> instances) {
		
		train(instances);
	}
	
	/**
	 * This method trains the majority predictor.
	 * @param instances The training instances.
	 */
	public void train(List<Instance> instances) {
		
		int num0 = 0; // Number of "0" labels encountered.
		int num1 = 0; // Number of "1" labels encountered.
		
		for (Instance instance : instances) {
			
			if (instance.getLabel().toString().equals("0"))
				num0++;
			else if (instance.getLabel().toString().equals("1"))
				num1++;
		}
		
		if (num0 >= num1)
			this.majorityVal = 0;
		else
			this.majorityVal = 1;
	}

	/**
	 * This method predicts the label for a test instance.
	 * @param instance The test instance.
	 * @return The predicted label.
	 */
	public Label predict(Instance instance) {
		
		return new ClassificationLabel(this.majorityVal);
	}

}
