package cs475;

import java.util.List;

public class MarginDualPerceptronPredictor extends Predictor {

	double[] alpha;
	double online_learning_rate;
	int online_training_iterations;
	String algorithm;
	double polynomial_kernel_exponent;
	int numFeatures;
	List<Instance> instances;
	
	/**
	 * This constructor creates an instance of the MarginPerceptronPredictor class
	 * that uses a polynomial-kernel dual perceptron with margin and trains it.
	 * @param instances The training instances.
	 * @param online_learning_rate The online learning rate.
	 * @param online_training_iterations The number of iterations to run the training through.
	 * @param polynomial_kernel_exponent The polynomial kernel exponent.
	 * @param algorithm The perceptron algorithm to use.
	 */
	public MarginDualPerceptronPredictor(List<Instance> instances, double online_learning_rate, int online_training_iterations, double polynomial_kernel_exponent, String algorithm) {
		
		this.online_learning_rate = online_learning_rate;
		this.online_training_iterations = online_training_iterations;
		this.algorithm = algorithm;
		this.instances = instances;
		this.polynomial_kernel_exponent = polynomial_kernel_exponent;
		
		FeatureSelector igSelector = new FeatureSelector();
		this.numFeatures = igSelector.countFeatures(instances);
		
		this.alpha = new double[instances.size()];
		
		for (int i=0; i < instances.size(); i++) {
			this.alpha[i] = 0;
		}
		
		train(instances);
	}

	public void train(List<Instance> instances) {
		
		int numInstances = instances.size();
		Kernel kernel;
		double[][] gramMatrix;

		if (this.algorithm.equals("perceptron_linear_kernel")) {
			kernel = new LinearKernel(instances, this.numFeatures);
			gramMatrix = kernel.getGramMatrix();
		}
		else {
			
			kernel = new PolynomialKernel(instances, this.numFeatures, this.polynomial_kernel_exponent);
			gramMatrix = kernel.getGramMatrix();
		}
		
		for (int i=0; i < this.online_training_iterations; i++) {
			
			for (int j=0; j < numInstances; j++) {
				
				int y_i = Integer.parseInt(instances.get(j).getLabel().toString());
				double margin;
				
				// Sign() returns 1 or -1 instead of 1 or 0.
				if (y_i == 0)
					y_i = -1;
				
				margin = calcMargin(this.numFeatures, this.alpha, gramMatrix, j, y_i);
				
				if (margin < 1) {
					this.alpha[j]++;
				}
			}
		}
	}

	public Label predict(Instance instance) {
		
		double y_hat = 0;
		double[] x = instance.getFeatureVector().getVectorAsArray(this.numFeatures);
		
		for (int i=0; i < this.instances.size(); i++) {
			
			int yi_other = Integer.parseInt(this.instances.get(i).getLabel().toString());
			if (yi_other == 0)
				yi_other = -1;
			
			if (this.algorithm.equals("perceptron_linear_kernel"))
				y_hat += this.alpha[i] * yi_other * Vector.dotProduct(this.instances.get(i).getFeatureVector().getVectorAsArray(numFeatures), x);
			else if (this.algorithm.equals("perceptron_polynomial_kernel"))
				y_hat += this.alpha[i] * yi_other * Math.pow((1 + Vector.dotProduct(this.instances.get(i).getFeatureVector().getVectorAsArray(numFeatures), x)), this.polynomial_kernel_exponent);
		}
		
		if (y_hat >= 0)
			y_hat = 1;
		else
			y_hat = 0;
		
		return new ClassificationLabel((int) y_hat);
	}
	
	public double calcMargin(int numFeatures, double[] alpha, double[][] gramMatrix, int idx_xi, int y_i) {
		
		double y_hat = 0;
		
		for (int i=0; i < this.instances.size(); i++) {
			
			int yi_other = Integer.parseInt(this.instances.get(i).getLabel().toString());
			if (yi_other == 0)
				yi_other = -1;
			
			y_hat += this.alpha[i] * yi_other * gramMatrix[i][idx_xi];
		}
		
		return y_i*y_hat;
	}
}
