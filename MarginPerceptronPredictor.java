package cs475;

import java.util.List;

/**
 * This class contains a label predictor with parameters determined by
 * the margin perceptron algorithm.
 * This class is capable of supporting a single perceptron with margin
 */
public class MarginPerceptronPredictor extends Predictor {
	
	double[] w;
	double online_learning_rate;
	int online_training_iterations;
	int numFeatures;
	
	/**
	 * This constructor creates an instance of the MarginPerceptronPredictor class
	 * that uses a single perceptron and trains it.
	 * @param instances The training instances.
	 * @param online_learning_rate The online learning rate.
	 * @param online_training_iterations The number of iterations to run the training through.
	 */
	public MarginPerceptronPredictor(List<Instance> instances, double online_learning_rate, int online_training_iterations) {
		
		this.online_learning_rate = online_learning_rate;
		this.online_training_iterations = online_training_iterations;
		
		FeatureSelector igSelector = new FeatureSelector();
		this.numFeatures = igSelector.countFeatures(instances);
		this.w = new double[this.numFeatures];
		
		for (int i=0; i < this.numFeatures; i++) {
			this.w[i] = 0;
		}
		
		train(instances);
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
	 * This method trains a single perceptron with margin.
	 * @param instances The training instances.
	 */
	public void train(List<Instance> instances) {
		
		int numInstances = instances.size();
		
		for (int i=0; i < this.online_training_iterations; i++) {
			
			for (int j=0; j < numInstances; j++) {
				
				double[] x_i = instances.get(j).getFeatureVector().getVectorAsArray(numFeatures);
				int y_i = Integer.parseInt(instances.get(j).getLabel().toString());
				double margin;
				
				// Sign() returns 1 or -1 instead of 1 or 0.
				if (y_i == 0)
					y_i = -1;
				
				margin = this.calcMargin(y_i, x_i); 
				
				if (margin < 1) {
					this.w = Vector.sumVectors(this.w, Vector.scalarMult(online_learning_rate*y_i, x_i));
				}
			}
		}
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
}
