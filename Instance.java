package cs475;

import java.io.Serializable;

/**
 * This class represents an instance or example in a data set.
 */
public class Instance implements Serializable {

	Label _label = null;
	FeatureVector _feature_vector = null;

	/**
	 * This constructor creates an instance of the Instance class.
	 * @param feature_vector The feature vector containing the values of the
	 * features of an instance.
	 * @param label The label of the instance.
	 */
	public Instance(FeatureVector feature_vector, Label label) {
		this._feature_vector = feature_vector;
		this._label = label;
	}

	/**
	 * This method returns the label of an Instance object.
	 * @return The Label.
	 */
	public Label getLabel() {
		return _label;
	}

	/**
	 * This method sets the label of an Instance object.
	 * @param label The Label.
	 */
	public void setLabel(Label label) {
		this._label = label;
	}

	/**
	 * This method returns the feature vector of an Instance object.
	 * @return The FeatureVector.
	 */
	public FeatureVector getFeatureVector() {
		return _feature_vector;
	}

	/**
	 * This method sets the feature vector of an Instance object.
	 * @param feature_vector The FeatureVector.
	 */
	public void setFeatureVector(FeatureVector feature_vector) {
		this._feature_vector = feature_vector;
	}
	
	
}
