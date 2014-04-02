package cs475;

import java.util.List;

/**
 * This class contains 
 */
public class LinearKernel extends Kernel {

	double[][] gramMatrix;
	
	public LinearKernel(List<Instance> instances, int numFeatures) {
		
		this.gramMatrix = calcKernel(instances, numFeatures);
	}
	
	public double[][] calcKernel(List<Instance> instances, int numFeatures) {

		int numInstances = instances.size();
		double[][] gramMatrix = new double[numInstances][numInstances];
		
		for (int i=0; i < numInstances; i++) {
			
			double[] x_i = instances.get(i).getFeatureVector().getVectorAsArray(numFeatures);
			
			for (int j=0; j < numInstances; j++) {
				
				double[] x_j = instances.get(j).getFeatureVector().getVectorAsArray(numFeatures);
				
				gramMatrix[i][j] = Vector.dotProduct(x_i, x_j);
			}
		}
		return gramMatrix;
	}
	
	/**
	 * This method returns the Gram matrix as a 2D array.
	 * @return The Gram matrix.
	 */
	public double[][] getGramMatrix() {
		
		return this.gramMatrix;
	}
}
