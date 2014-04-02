package cs475;

import java.util.List;

/**
 * This abstract class represents an accuracy evaluator which evaluates the
 * accuracy of a given learning algorithm.
 */
public abstract class Evaluator {

	public abstract double evaluate(List<Instance> instances, Predictor predictor);
}
