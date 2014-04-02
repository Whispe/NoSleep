package cs475;

import java.util.List;

public class PolynomialKernel extends Kernel {

	double polynomial_kernel_exponent;
	double[][] gramMatrix;
	
	public PolynomialKernel(List<Instance> instances, int numFeatures, double polynomial_kernel_exponent) {
		
		this.polynomial_kernel_exponent = polynomial_kernel_exponent;
		this.gramMatrix = calcKernel(instances, numFeatures);
	}
	
	public double[][] calcKernel(List<Instance> instances, int numFeatures) {

		int numInstances = instances.size();
		double[][] gramMatrix = new double[numInstances][numInstances];
		
		for (int i=0; i < numInstances; i++) {
			
			double[] x_i = instances.get(i).getFeatureVector().getVectorAsArray(numFeatures);
			
			for (int j=0; j < numInstances; j++) {
				
				double[] x_j = instances.get(j).getFeatureVector().getVectorAsArray(numFeatures);
				
				gramMatrix[i][j] = Math.pow(1 + Vector.dotProduct(x_i, x_j), this.polynomial_kernel_exponent);
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
