package cs475;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class contains a label predictor with parameters determined by
 * the even_odd learning algorithm.
 */
public class EvenOddPredictor extends Predictor {
	
	/**
	 * This method trains the even-odd predictor.
	 * @param instances The training instances.
	 */
	public void train(List<Instance> instances) {
		
		return;
	}

	/**
	 * This method predicts the label of a test example.
	 * @param instance The test instance.
	 * @return The predicted label.
	 */
	public Label predict(Instance instance) {
		
		Iterator mapIterator = instance.getFeatureVector().getMapIterator();
		double evenSum = 0;
		double oddSum = 0;
		int finalLabel;
		
		while (mapIterator.hasNext()) {
			
			Map.Entry pairs = (Map.Entry) mapIterator.next();
			
			//if ((int) pairs.getKey() % 2 == 0)
			if (Integer.parseInt(pairs.getKey().toString()) % 2 == 0)
				evenSum += Double.parseDouble(pairs.getValue().toString());
				//evenSum += (double) pairs.getValue();
			else
				oddSum += Double.parseDouble(pairs.getValue().toString());
				//oddSum += (double) pairs.getValue();
		}
		
		if (evenSum >= oddSum)
			finalLabel = 1;
		else
			finalLabel = 0;
		
		return new ClassificationLabel(finalLabel);
	}

}
