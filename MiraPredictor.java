package cs475;

import java.util.List;

/**
 * This class contains a label predictor with parameters determined by
 * the margin-infused relaxation algorithm.
 */
public class MiraPredictor extends Predictor {

	double[] w;
	int online_training_iterations;
	int numFeatures;
	
	/**
	 * This method constructs an instance of the MiraPredictor class and trains it with
	 * a set of data instances.
	 * @param instances The training instances.
	 * @param online_training_iterations The number of iterations to run the training through.
	 */
	public MiraPredictor(List<Instance> instances, int online_training_iterations) {
		
		this.online_training_iterations = online_training_iterations;
		
		FeatureSelector igSelector = new FeatureSelector();
		this.numFeatures = igSelector.countFeatures(instances);
		w = new double[this.numFeatures];
		
		for (int i=0; i<this.numFeatures; i++) {
			w[i] = 0;
		}
		
		train(instances);
	}

	/**
	 * This method trains the MIRA predictor using training examples.
	 * @param instances The training instances.
	 */
	public void train(List<Instance> instances) {
		
		for (int i=0; i < this.online_training_iterations; i++) {
			
			for (int j=0; j < instances.size(); j++) {
				
				double[] x_i = instances.get(j).getFeatureVector().getVectorAsArray(this.numFeatures);
				int y_i = Integer.parseInt(instances.get(j).getLabel().toString());
				double margin;
				double tau;
				
				// Sign() returns 1 or -1 instead of 1 or 0.
				if (y_i == 0)
					y_i = -1;
				
				margin = this.calcMargin(y_i, x_i);
				tau = this.calcTau(y_i, x_i);
				
				if (margin < 1) {
					
					this.w = Vector.sumVectors(this.w, Vector.scalarMult(tau * y_i, x_i));
				}
			}
		}
	}

	/**
	 * This method predicts the label for a test instance.
	 * @param instance The test instance.
	 * @return The predicted label.
	 */
	public Label predict(Instance instance) {
		
		int y_hat;
		double[] x;
		
		x = instance.getFeatureVector().getVectorAsArray(this.w.length);
		
		if (Vector.dotProduct(this.w, x) >= 0)
			y_hat = 1;
		else
			y_hat = 0;
		
		return new ClassificationLabel(y_hat);
	}
	
	/**
	 * This method calculates the margin of an estimate of y_i for feature vector x_i.
	 * @param y_i The actual label of x_i.
	 * @param x_i The feature vector.
	 * @return The estimate for the label y_i.
	 */
	public double calcMargin(int y_i, double[] x_i) {
		
		return y_i * Vector.dotProduct(this.w, x_i);
	}
	
	/**
	 * This method calculates the value of tau, which is used to help update the weight vector w.
	 * @param y_i The actual label of x_i.
	 * @param x_i The feature vector.
	 * @return The value of tau.
	 */
	public double calcTau(double y_i, double[] x_i) {
		
		double tau;
		double numerator;
		double denominator;
		
		numerator = 1 - y_i * Vector.dotProduct(this.w, x_i);
		denominator = Vector.dotProduct(x_i, x_i);
		tau = numerator / denominator;
		
		return tau;
	}
}