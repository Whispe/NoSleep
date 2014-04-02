package cs475;

import java.io.Serializable;
import java.util.List;

/**
 * This abstract class represents a label predictor whose parameters are
 * determined by a learning algorithm.
 */
public abstract class Predictor implements Serializable {
	private static final long serialVersionUID = 1L;

	public abstract void train(List<Instance> instances);
	
	public abstract Label predict(Instance instance);
}
