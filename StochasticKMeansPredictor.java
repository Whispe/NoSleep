package cs475;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains a label predictor that uses the
 * unsupervised, stochastic K-means algorithm.
 */
public class StochasticKMeansPredictor extends Predictor {

	int numInstances;
	int numIters;
	int numFeatures;
	int numClusters;
	double[][] mu_k; // Length numClusters.
	int[] whichCluster; // Length numInstances.
	int[] perCluster; // Length numClusters.
	
	public StochasticKMeansPredictor(List<Instance> instances, int clustering_training_iterations, int num_clusters) {
		
		this.numInstances = instances.size();
		this.numIters = clustering_training_iterations;
		this.numClusters = num_clusters;
		FeatureSelector igSelector = new FeatureSelector();
		this.numFeatures = igSelector.countFeatures(instances);
		
		this.mu_k = new double[this.numClusters][this.numFeatures];
		for (int i=0; i<this.mu_k.length ; i++) {
			double[] x_i = instances.get(i).getFeatureVector().getVectorAsArray(this.numFeatures);
			this.mu_k[i] = x_i;
		}
		
		this.whichCluster = new int[this.numInstances];
		for (int i=0; i<this.whichCluster.length; i++) {
			this.whichCluster[i] = -1;
		}
		
		this.perCluster = new int[this.numClusters];
		for (int i=0; i<this.perCluster.length; i++) {
			this.perCluster[i] = 0;
		}
	}

	public void train(List<Instance> instances) {
		
		for (int i=0; i<numIters; i++) {
			
			for (int j=0; j < numInstances; j++) {
				
				double minDist = Double.POSITIVE_INFINITY;
				int min_k = 0;
				double[] x_i = instances.get(i).getFeatureVector().getVectorAsArray(this.numFeatures);
					
				for (int k=0; k<this.mu_k.length; k++) {
					
					double dist = Vector.dist(this.mu_k[k], x_i);
					if (dist < minDist) {
						
						minDist = dist;
						min_k = k;
					}
				}
				int old_k = whichCluster[i];
				int new_k = min_k;
				this.perCluster[old_k]--;
				this.perCluster[new_k]++;
				this.whichCluster[i] = new_k;
				
				// Update mu_k for old and new cluster.
				double[] newMu_k = new double[this.numFeatures];
				
				// Old cluster first.
				// Initialize newMu_k to zero vector.
				for (int k=0; k<this.numFeatures; k++) {
					
					newMu_k[k] = 0;
				}
				
				for (int k=0; k<this.whichCluster.length; k++) {
					
					if (this.whichCluster[k] == old_k) {
						x_i = instances.get(k).getFeatureVector().getVectorAsArray(this.numFeatures);
						newMu_k = Vector.sumVectors(newMu_k, x_i);
					}
				}
				if (this.perCluster[old_k] == 0) {
					this.mu_k[old_k] = newMu_k;
				} else {
					newMu_k = Vector.scalarMult(1.0/this.perCluster[old_k], newMu_k);
					this.mu_k[old_k] = newMu_k;
				}
				
				// New cluster next.
				// Initialize newMu_k to zero vector.
				for (int k=0; k<this.numFeatures; k++) {
					
					newMu_k[k] = 0;
				}
				
				for (int k=0; k<this.whichCluster.length; k++) {
					
					if (this.whichCluster[k] == new_k) {
						x_i = instances.get(k).getFeatureVector().getVectorAsArray(this.numFeatures);
						newMu_k = Vector.sumVectors(newMu_k, x_i);
					}
				}
				if (this.perCluster[new_k] == 0) {
					this.mu_k[new_k] = newMu_k;
				} else {
					newMu_k = Vector.scalarMult(1.0/this.perCluster[new_k], newMu_k);
					this.mu_k[new_k] = newMu_k;
				}
			}
		}
	}

	public Label predict(Instance instance) {

		double minDist = Double.POSITIVE_INFINITY;
		int min_k = 0;
		double[] x_i = instance.getFeatureVector().getVectorAsArray(this.numFeatures);
		
		for (int k=0; k<this.mu_k.length; k++) {
			
			double dist = Vector.dist(x_i, this.mu_k[k]);
			
			if (dist < minDist) {
				
				minDist = dist;
				min_k = k;
			}
		}
		
		return new ClassificationLabel(min_k);
	}
}
