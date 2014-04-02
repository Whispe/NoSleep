package cs475;

import java.util.List;
import java.util.ArrayList;

/**
 * This class contains a label predictor that uses the
 * unsupervised lambda-means algorithm.
 */
public class LambdaMeansPredictor extends Predictor {

	double lambda;
	int numInstances;
	int numFeatures;
	int numIters;
	ArrayList<double[]> mu_k = new ArrayList<double[]>();
	ArrayList<ArrayList<Integer>> r_nk = new ArrayList<ArrayList<Integer>>();
	
	public LambdaMeansPredictor(List<Instance> instances, int clustering_training_iterations, double cluster_lambda) {
		
		this.numInstances = instances.size();
		this.numIters = clustering_training_iterations;
		FeatureSelector igSelector = new FeatureSelector();
		this.numFeatures = igSelector.countFeatures(instances);
		double[] mu1 = new double[this.numFeatures];
		
		// Initialize mu1 to zero vector.
		for (int i=0; i<this.numFeatures; i++) {
			
			mu1[i] = 0;
		}
		
		// Find the average distance of each training instance to mean of training data.
		for (int i=0; i<this.numInstances; i++) {
			
			double[] x = instances.get(i).getFeatureVector().getVectorAsArray(this.numFeatures);
			mu1 = Vector.sumVectors(mu1, x);
		}
		mu1 = Vector.scalarMult(1.0/this.numInstances, mu1);
		this.mu_k.add(mu1);
		
		// Set lambda to average distance of each training instance to mean of training data
		// if cluster_lambda is 0 (not given in command line).
		this.lambda = cluster_lambda;
		//System.out.println("Lambda before: " + this.lambda);
		
		double sum = 0;
		if (cluster_lambda == 0) {
			
			for (int i=0; i<this.numInstances; i++) {
				
				double[] x = instances.get(i).getFeatureVector().getVectorAsArray(this.numFeatures);
				sum += Vector.sqrDist(x, mu1);
			}
			this.lambda = sum;
			//System.out.println("Lambda without norm: " + this.lambda);
			this.lambda = sum / this.numInstances;
			System.out.println("Lambda with norm: " + this.lambda);
		}
		
		this.r_nk.add(new ArrayList<Integer>());
		
		train(instances);
	}

	public void train(List<Instance> instances) {
		
		for (int i=0; i<this.numIters; i++) {
			EStep(instances);
			MStep(instances);
		}
	}
	
	public void EStep(List<Instance> instances) {
		
		for (int i=0; i<this.r_nk.size(); i++) {
			
			this.r_nk.get(i).clear();
		}
		
		for (int i=0; i<this.numInstances; i++) {
			
			double minDist = Double.POSITIVE_INFINITY;
			int min_k = 0;
			double[] x_i = instances.get(i).getFeatureVector().getVectorAsArray(this.numFeatures);
				
			for (int k=0; k<this.mu_k.size(); k++) {
				
				double dist = Vector.sqrDist(this.mu_k.get(k), x_i);
				if (dist < minDist) {
					
					minDist = dist;
					min_k = k;
				}
			}
			if (minDist <= this.lambda) {
				
				this.r_nk.get(min_k).add(i);
			}
			else {
				ArrayList<Integer> newCluster = new ArrayList<Integer>();
				newCluster.add(i);
				this.r_nk.add(newCluster);
				this.mu_k.add(x_i);
			}
		}
	}
	
	public void MStep(List<Instance> instances) {
		
		for (int k=0; k<this.mu_k.size(); k++) {
			
			int numInClusterk = 0;
			double[] newMu_k = new double[this.numFeatures];
			
			// Initialize newMu_k to zero vector.
			for (int i=0; i<this.numFeatures; i++) {
				
				newMu_k[i] = 0;
			}
			
			for(int d : this.r_nk.get(k)) {
				
				double[] x_i = instances.get(d).getFeatureVector().getVectorAsArray(this.numFeatures);
				newMu_k = Vector.sumVectors(newMu_k, x_i);
				numInClusterk++;
			}
			if (this.r_nk.get(k).size() == 0) {
				
				//newMu_k = new double[this.numFeatures];
				//for (int j=0; j<newMu_k.length; j++) {
					
				//	newMu_k[j] = 0;
				//}
				this.mu_k.set(k, newMu_k);
			} else {
				newMu_k = Vector.scalarMult(1.0/numInClusterk, newMu_k);
				this.mu_k.set(k, newMu_k);
			}
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
