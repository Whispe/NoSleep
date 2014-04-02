package cs475;

import java.util.List;

/**
 * This class contains a label predictor with parameters determined by
 * the logistic regression predictor.
 */
public class LogisticRegressionPredictor extends Predictor {

	static public int SELECT_ALL_FEATURES = -1;
	
	double[] w;
	int numIter;
	double eta;
	int numFeaturesToSelect;
	int[] selectedFeatureIdc;
	
	/**
	 * This constructor creates an instance of the LogisticRegressionPredictor class and trains it.
	 * @param instances The training instances.
	 * @param numIter The number of iterations to run the training through.
	 * @param eta The gradient descent learning rate.
	 * @param numFeaturesToSelect The number of features to select for training.
	 */
	public LogisticRegressionPredictor(List<Instance> instances, int numIter, double eta, int numFeaturesToSelect) {
		
		FeatureSelector igSelector = new FeatureSelector();
		if (numFeaturesToSelect != SELECT_ALL_FEATURES)
			this.numFeaturesToSelect = numFeaturesToSelect;
		else
			this.numFeaturesToSelect = igSelector.countFeatures(instances);
		
		this.numIter = numIter;
		this.eta = eta;
		
		this.selectedFeatureIdc = igSelector.selectByInformationGain(instances, this.numFeaturesToSelect);
		
		w = new double[this.numFeaturesToSelect];
		
		for (int i=0; i<this.numFeaturesToSelect; i++) {
			w[i] = 0;
		}
		
		train(instances);
	}
	
	/**
	 * This method trains the logistic regression predictor.
	 * @param instances The training instances.
	 */
	public void train(List<Instance> instances) {
		
		for (int i=0; i < this.numIter; i++) {
			
			double[] logisticFunc = this.calcLogistic(instances);
			double[] dw = this.calcUpdateW(logisticFunc);
			this.w = Vector.sumVectors(this.w, dw);
		}
		// Print weights.
		/*for (int i=0; i<w.length; i++) {
			
			System.out.println(w[i]);
		}*/
	}

	/**
	 * This method predicts the label for a test instance.
	 * @param instance The test instance.
	 * @return The predicted label.
	 */
	public Label predict(Instance instance) {
		
		double[] x = new double[this.selectedFeatureIdc.length];
		for (int i=0; i < this.selectedFeatureIdc.length; i++) {
			
			x[i] = instance.getFeatureVector().get(selectedFeatureIdc[i]);
		}
		
		double y = this.calcLinkFunc(Vector.dotProduct(this.w, x));
		int y_new;
		if (y >= 0.5)
			y_new = 1;
		else
			y_new = 0;
		
		return new ClassificationLabel(y_new);
	}

	/**
	 * This method calculates the value of the link function for a double.
	 * @param wx The double.
	 * @return The link function of the double.
	 */
	public double calcLinkFunc(double wx) {
		
		double retVal = 1 / (1 + Math.exp(-1 * wx));
		return retVal;
	}
	
	/**
	 * This method calculates the del l function.
	 * @param instances The training instances.
	 * @return The vector result of the del l function.
	 */
	public double[] calcLogistic(List<Instance> instances) {
		
		int numInstances = instances.size();
		double[] retLogistic = new double[this.selectedFeatureIdc.length];
		
		for (int i=0; i < this.selectedFeatureIdc.length; i++) {
			
			retLogistic[i] = 0;
		}
		
		for (int i=0; i<numInstances; i++) {
			
			double[] x_i = new double[this.selectedFeatureIdc.length];
			double y_i = Integer.parseInt(instances.get(i).getLabel().toString()); 
			for (int j=0; j < x_i.length; j++) {
				
				x_i[j] = instances.get(i).getFeatureVector().get(selectedFeatureIdc[j]);
			}
			
			retLogistic = Vector.sumVectors(retLogistic, Vector.sumVectors(Vector.scalarMult(y_i * this.calcLinkFunc(-1 * Vector.dotProduct(this.w, x_i)), x_i), Vector.scalarMult((1-y_i) * this.calcLinkFunc(Vector.dotProduct(this.w, x_i)), Vector.scalarMult(-1, x_i))));
		}
		
		return retLogistic;
	}
	
	/**
	 * This method calculates the update vector for w.
	 * @param logisticFunc The del l logistic function.
	 * @return The vector with which to update w.
	 */
	public double[] calcUpdateW(double[] logisticFunc) {
		
		double[] dw = Vector.scalarMult(this.eta, logisticFunc);
		
		return dw;
	}
}
