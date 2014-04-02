package cs475;

import java.io.Serializable;
import java.util.List;
import java.util.TreeMap;
import java.util.Iterator;

/**
 * This class represents the feature vector of an instance within a data set.
 * The feature vector is a data structure containing the values of each feature
 * that an instance has.
 */
public class FeatureVector implements Serializable {

	private TreeMap map = new TreeMap();
	
	/**
	 * This method adds a feature and its value to the feature vector.
	 * @param index The index of the feature.
	 * @param value The value that the feature takes for the given instance.
	 */
	public void add(int index, double value) {
		// TODO Auto-generated method stub
		map.put(index, value);
	}
	
	/**
	 * This method returns the value of a feature for the given instance.
	 * @param index The index of the feature whose value is to be returned.
	 * @return The value of the feature.
	 */
	public double get(int index) {
		// TODO Auto-generated method stub
		double val;
		if (map.get(index) != null) {
			//val = (double) map.get(index);
			val = Double.parseDouble(map.get(index).toString());
		}
		else {
			return val = 0;
		}
		return val;
	}

	/**
	 * Returns the TreeMap mapping features to their values.
	 * @return The TreeMap.
	 */
	public TreeMap getMap() {
		
		return this.map;
	}
	
	/**
	 * Returns an Iterator that iterates over the TreeMap of features and their values.
	 * @return The Iterator.
	 */
	public Iterator getMapIterator() {
		
		return this.map.entrySet().iterator();
	}
	
	/**
	 * This method returns the length of the FeatureVector, i.e. the number of features.
	 * @return 
	 */
	/*public int size() {
		
		return (int) this.getMap().lastKey();
	}*/
	
	/**
	 * This method returns the feature vector as a 1D array.
	 * @param size The size of the vector, equivalent to the number of features.
	 * @return The feature vector as an array.
	 */
	public double[] getVectorAsArray(int size) {
		
		double[] x = new double[size];
		
		for (int i=1; i <= x.length; i++) {
			
			if (this.getMap().get(i) != null)
				x[i-1] = Double.parseDouble(this.getMap().get(i).toString());
				//x[i-1] = (double) this.getMap().get(i);
			else
				x[i-1] = 0;
		}
		
		return x;
	}
}
