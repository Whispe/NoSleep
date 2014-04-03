package cs475;

import java.util.ArrayList;
import java.util.List;

public class StochasticKMeansPredictor extends Predictor {

	int numInstances;
	int numFeatures;
	int numIters;
	int numClusters;
	ArrayList<double[]> mu_k = new ArrayList<double[]>();
	ArrayList<ArrayList<Integer>> r_nk = new ArrayList<ArrayList<Integer>>();
	
	public StochasticKMeansPredictor (List<Instance> instances, int clustering_training_iterations, int num_clusters){
		
		this.numInstances = instances.size();
		this.numIters = clustering_training_iterations;
		FeatureSelector igSelector = new FeatureSelector();
		this.numFeatures = igSelector.countFeatures(instances);
		double[] muHold = new double[this.numFeatures];
		
		
	}
	
	public void train(List<Instance> instances) {
		
		
	}

	public Label predict(Instance instance) {
		
		
		
		return null;
	}

	
}
