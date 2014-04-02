package cs475;

import java.util.Arrays;
import java.util.List;

public class FeatureSelector {
	
	public int[] selectByInformationGain(List<Instance> instances, int numFeaturesToSelect) {
		
		// Count total number of features.
		int totFeatures = this.countFeatures(instances);
		
		// Allocate array to store info gain of each feature.
		double[] igRa = new double[totFeatures];
		
		// For each feature, calculate info gain, store to array where index represents feature index - 1.
		for (int i=1; i<=totFeatures; i++) {
			
			igRa[i-1] = this.calcInfoGain(instances, i);
		}
		
		/* Print information gain vector.
		for (int i=0; i<igRa.length; i++) {
			System.out.println(i + ": " + igRa[i]);
		}*/
		
		// For the number of features to be selected, iterate through igRa and grab the max, set to -infty.
		int[] idcOfSelectedFeatures = new int[numFeaturesToSelect];
		for (int i=0; i<numFeaturesToSelect; i++) {
			
			double maxIg = igRa[0];
			int idxOfMaxIg = 0;
			
			// Get the index of the feature with the ith largest info gain.
			for (int j=0; j<igRa.length; j++) {
				
				if (igRa[j] >= maxIg) {
					
					idxOfMaxIg = j;
					maxIg = igRa[j];
				}
			}
			
			idcOfSelectedFeatures[i] = idxOfMaxIg + 1;
			// Print the largest information gains.
			// System.out.println(idxOfMaxIg+1 + ": "+ igRa[idxOfMaxIg]);
			igRa[idxOfMaxIg] = Double.NEGATIVE_INFINITY;
		}
		
		Arrays.sort(idcOfSelectedFeatures);
		
		/*for (int i=0; i<idcOfSelectedFeatures.length; i++) {
			
			System.out.println(idcOfSelectedFeatures[i]);
		}*/
		
		return idcOfSelectedFeatures;
	}
	
	public int countFeatures(List<Instance> instances) {
		
		// Initialize largest feature index to first.
		int highestFeature = 1;
		
		// Go through each instance and look for the highest feature index.
		for (int i=0; i<instances.size(); i++) {
			
			//if ((int) instances.get(i).getFeatureVector().getMap().lastKey() > highestFeature)
			if (Integer.parseInt(instances.get(i).getFeatureVector().getMap().lastKey().toString()) > highestFeature)
				//highestFeature = (int) instances.get(i).getFeatureVector().getMap().lastKey();
				highestFeature = Integer.parseInt(instances.get(i).getFeatureVector().getMap().lastKey().toString());
		}
		
		return highestFeature;
	}
	
	public double calcInfoGain(List<Instance> instances, int currentFeatureIdx) {
		
		double mean = this.calcMean(instances, currentFeatureIdx);
		
		// Calculate p(y=0, x<mean).
		double probForY0 = this.calcProbForY(instances, 0, mean, currentFeatureIdx);
		// Calculate p(y=1, x<mean).
		double probForY1 = this.calcProbForY(instances, 1, mean, currentFeatureIdx);
		// Calculate p(x<mean) = sum above.
		double probXLess = probForY0 + probForY1;
		double probForY1G = this.calcProbForYG(instances, 1, mean, currentFeatureIdx);
		double probForY0G = this.calcProbForYG(instances, 0, mean, currentFeatureIdx);
		
		//WHISPE
		double infoGain = 1 * (probForY0 * Math.log(probForY0 / probXLess) + probForY1 * Math.log(probForY1 / probXLess));

		return infoGain;
	}
	
	public double calcMean(List<Instance> instances, int currentFeatureIdx) {
		
		// Initialize sum of the values of the instance's currentFeatureIdx_th feature.
		double sum = 0;
		double mean = 0;
		
		// Go through all of the instances i.
		for (int i=0; i<instances.size(); i++) {
			
			// Sum the values of each instance's currentFeatureIdx_th feature.
			sum += instances.get(i).getFeatureVector().get(currentFeatureIdx);
		}
		
		mean = sum / instances.size();
		return mean;
	}
	
	public double calcProbForYG(List<Instance> instances, int y, double mean, int currentFeatureIdx) {
		
		double sum = 0;
		double prob = 0;
		
		// Go through the instances and take the value of the currentFeatureIdx_th feature.
		for (int i=0; i<instances.size(); i++) {
			
			int currentY = Integer.parseInt(instances.get(i).getLabel().toString());
			double currentX = instances.get(i).getFeatureVector().get(currentFeatureIdx);
			
			if (currentY == y && currentX > mean)
				sum++;
		}
		
		prob = sum / instances.size();
		
		return prob;
	}
	
	public double calcProbForY(List<Instance> instances, int y, double mean, int currentFeatureIdx) {
		
		double sum = 0;
		double prob = 0;
		
		// Go through the instances and take the value of the currentFeatureIdx_th feature.
		for (int i=0; i<instances.size(); i++) {
			
			int currentY = Integer.parseInt(instances.get(i).getLabel().toString());
			double currentX = instances.get(i).getFeatureVector().get(currentFeatureIdx);
			
			if (currentY == y && currentX <= mean)
				sum++;
		}
		
		prob = sum / instances.size();
		
		return prob;
	}
}
