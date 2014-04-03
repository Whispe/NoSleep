package cs475;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class contains a label predictor that uses the
 * unsupervised, stochastic K-means algorithm.
 */
public class StochasticKMeansPredictorOld extends Predictor {

	int numInstances;
	int numIters;
	int numFeatures;
	int numClusters;
	double[][] mu_k; // Length numClusters.
	int[] whichCluster; // Length numInstances.
	int[] inCluster; // Length numClusters.
	
	public StochasticKMeansPredictorOld(List<Instance> instances, int clustering_training_iterations, int num_clusters) {
		
		this.numInstances = instances.size();
		this.numIters = clustering_training_iterations;
		this.numClusters = num_clusters;
		FeatureSelector igSelector = new FeatureSelector();
		this.numFeatures = igSelector.countFeatures(instances);
		
		// Set mu_ks to first k instances.
		this.mu_k = new double[this.numClusters][this.numFeatures];
		for (int i=0; i<this.numClusters; i++) {
			double[] x_i = instances.get(i).getFeatureVector().getVectorAsArray(this.numFeatures);
			this.mu_k[i] = x_i;
		}
		
		// Instances haven't been assigned to clusters yet.
		this.whichCluster = new int[this.numInstances];
		for (int i=0; i<this.whichCluster.length; i++) {
			this.whichCluster[i] = -1;
		}
		
		this.inCluster = new int[this.numClusters];
		for (int i=0; i<this.inCluster.length; i++) {
			this.inCluster[i] = 0;
		}
		
		train(instances);
	}

	public void train(List<Instance> instances) {
		
		for (int i=0; i<numIters; i++) {
		
			for (int j=0; j < numInstances; j++) {
				
				double minDist = Double.POSITIVE_INFINITY;
				int min_k = 0;
				double[] x_i = instances.get(j).getFeatureVector().getVectorAsArray(this.numFeatures);
				
				// Determine which cluster's mu_k instance j is closest to.
				for (int k=0; k<this.numClusters; k++) {
					
					double dist = Vector.sqrDist(this.mu_k[k], x_i);
					if (dist < minDist) {
						
						minDist = dist;
						min_k = k;
					}
				}
				
				int old_k = this.whichCluster[j];
				int new_k = min_k;
				if (old_k != -1) { // If instance j had been assigned to a different cluster previously.
					this.inCluster[old_k]--; // One less instance in the new_k cluster.
				}
				this.inCluster[new_k]++; // One more instance in the new_k cluster.
				this.whichCluster[j] = new_k; // Instance j is in the new_k cluster now.
				
				for (int m=0; m<whichCluster.length; m++) {
					System.out.print(whichCluster[m] + " ");
				}
				System.out.println();
				for (int m=0; m<inCluster.length; m++) {
					System.out.print(inCluster[m] + " ");
				}
				System.out.println();
				Scanner s = new Scanner(System.in);
				String st = s.next();
				
				// Update mu_k for old and new cluster.
				double[] newMu_k = new double[this.numFeatures];
				
				int numInCluster = 0;
				// Old cluster first.
				if (old_k != -1) { // If it belonged to a different cluster before.
					
					// Initialize newMu_k to zero vector.
					for (int k=0; k<this.numFeatures; k++) {
					
						newMu_k[k] = 0;
					}
				
					for (int k=0; k<this.numInstances; k++) {
					
						if (this.whichCluster[k] == old_k) { // If an instance belongs to the old cluster.
							x_i = instances.get(k).getFeatureVector().getVectorAsArray(this.numFeatures);
							newMu_k = Vector.sumVectors(newMu_k, x_i);
							numInCluster++;
						}
					}
					if (this.inCluster[old_k] == 0) {
						this.mu_k[old_k] = newMu_k;
					} else {
						newMu_k = Vector.scalarMult(1.0/numInCluster, newMu_k);
						this.mu_k[old_k] = newMu_k;
					}
				}
				
				// New cluster next.
				// Clear newMu_k to zero vector.
				for (int k=0; k<this.numFeatures; k++) {
					
					newMu_k[k] = 0;
				}
				
				numInCluster = 0;
				for (int k=0; k<this.numInstances; k++) {
					
					if (this.whichCluster[k] == new_k) {
						x_i = instances.get(k).getFeatureVector().getVectorAsArray(this.numFeatures);
						newMu_k = Vector.sumVectors(newMu_k, x_i);
						numInCluster++;
					}
				}
				if (this.inCluster[new_k] == 0) {
					this.mu_k[new_k] = newMu_k;
				} else {
					newMu_k = Vector.scalarMult(1.0/numInCluster, newMu_k);
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
			
			double dist = Vector.sqrDist(x_i, this.mu_k[k]);
			
			if (dist < minDist) {
				
				minDist = dist;
				min_k = k;
			}
		}
		
		return new ClassificationLabel(min_k);
	}
}
