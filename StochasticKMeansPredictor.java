package cs475;

import java.util.ArrayList;
import java.util.List;

public class StochasticKMeansPredictor extends Predictor {

	int numInstances;
	int numFeatures;
	int numIters;
	int numClusters;
	ArrayList<double[]> mu_k = new ArrayList<double[]>();
	ArrayList<ArrayList<Integer>> r_nk = new ArrayList<ArrayList<Integer>>(); // Length numClusters.
	int[] whichCluster; // Length numInstances.
	
	public StochasticKMeansPredictor (List<Instance> instances, int clustering_training_iterations, int num_clusters){
		
		this.numInstances = instances.size();
		this.numIters = clustering_training_iterations;
		FeatureSelector igSelector = new FeatureSelector();
		this.numFeatures = igSelector.countFeatures(instances);
		this.numClusters = num_clusters;
		double[] muHold;
		
		// Set mu_ks to first k instances.
		ArrayList<Integer> newCluster;
		for (int i=0; i<this.numClusters; i++) {
			
			muHold = instances.get(i).getFeatureVector().getVectorAsArray(this.numFeatures);
			this.mu_k.add(muHold);
			newCluster = new ArrayList<Integer>();
			newCluster.add(i);
			this.r_nk.add(i, newCluster);
		}
		
		// Instances haven't been assigned to clusters yet.
		this.whichCluster = new int[this.numInstances];
		for (int i=0; i<this.whichCluster.length; i++) {
			this.whichCluster[i] = -1;
		}
		
		train(instances);
	}
	
	public void train(List<Instance> instances) {
		
		for (int i=0; i<this.numIters; i++) {
			
			for (int j=0; j<this.numInstances; j++) {
				
				double minDist = Double.POSITIVE_INFINITY;
				int min_k = -1;
				double[] x_i = instances.get(j).getFeatureVector().getVectorAsArray(this.numFeatures);
				
				// Determine which cluster's mu_k instance j is closest to.
				for (int k=0; k<this.numClusters; k++) {
					
					double dist = Vector.sqrDist(this.mu_k.get(k), x_i);
					if (dist < minDist) {
						
						minDist = dist;
						min_k = k;
					}
				}
				int old_k = this.whichCluster[j];
				int new_k = min_k;
				if (old_k != -1) { // If instance j had been assigned to a different cluster previously.
					this.r_nk.get(old_k).remove(new Integer(j)); // Take out of old cluster.
					updateMu_k(old_k, instances);
				}
				this.r_nk.get(new_k).add(j); // Add instance to new cluster.
				this.whichCluster[j] = new_k;
				updateMu_k(new_k, instances);
			}
		}
	}
	
	public void updateMu_k(int k, List<Instance> instances) {
		
		double[] newMu_k = new double[this.numFeatures];
		int numInClusterk = this.r_nk.get(k).size();
		
		// Initialize newMu_k to zero vector.
		for (int i=0; i<this.numFeatures; i++) {
			newMu_k[i] = 0;
		}

		for(int d : this.r_nk.get(k)) {
						
			double[] x_i = instances.get(d).getFeatureVector().getVectorAsArray(this.numFeatures);
			newMu_k = Vector.sumVectors(newMu_k, x_i);
		}
		if (this.r_nk.get(k).size() == 0) {
			this.mu_k.set(k, newMu_k);
		} else {
			newMu_k = Vector.scalarMult(1.0/numInClusterk, newMu_k);
			this.mu_k.set(k, newMu_k);
		}
	}

	public Label predict(Instance instance) {
		
		double minDist = Double.POSITIVE_INFINITY;
		int min_k = 0;
		double[] x_i = instance.getFeatureVector().getVectorAsArray(this.numFeatures);
		
		for (int k=0; k<this.mu_k.size(); k++) {
			
			double dist = Vector.sqrDist(x_i, this.mu_k.get(k));
			
			if (dist < minDist) {
				
				minDist = dist;
				min_k = k;
			}
		}
		
		return new ClassificationLabel(min_k);
	}

	
}
