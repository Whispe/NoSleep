package cs475;

import java.util.List;

/**
 * This abstract class represents a kernel function used by a dual perceptron.
 */
public abstract class Kernel {

	public abstract double[][] calcKernel(List<Instance> instances, int numFeatures);

	public abstract double[][] getGramMatrix();
}
