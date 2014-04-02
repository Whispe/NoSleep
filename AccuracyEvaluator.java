package cs475;

import java.util.List;

/**
 * This class evaluates the accuracy of a learning algorithm if labels are available.
 * The accuracy is measured as the percentage of instances correctly labeled.
 */
public class AccuracyEvaluator extends Evaluator {

	/**
	 * This method determines which learning algorithm was used and calls the
	 * corresponding accuracy evaluator method to calculate its accuracy.
	 * @param instances The data instances used for training.
	 * @param predictor The label predictor created by the learning algorithm.
	 * @return The double value representing the percentage of labels correctly assigned.
	 */
	public double evaluate(List<Instance> instances, Predictor predictor) {
		
		double numTotal = 0;
		double numCorrect = 0;
		double accuracy = 1;
		
		for (Instance instance:instances) {
			
			numTotal++;
			if (instance.getLabel().toString().equals(predictor.predict(instance).toString()))
				numCorrect++;
		}
		
		if (numTotal != 0)
			accuracy = numCorrect / numTotal;
		
		return accuracy;
	}
}
