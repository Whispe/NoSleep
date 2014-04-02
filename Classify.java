package cs475;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

public class Classify {
	
	static public LinkedList<Option> options = new LinkedList<Option>();
	static public int SELECT_ALL_FEATURES = -1;
	
	public static void main(String[] args) throws IOException {
		// Parse the command line.
		String[] manditory_args = {"mode"};
		createCommandLineOptions();
		CommandLineUtilities.initCommandLineParameters(args, Classify.options, manditory_args);
	
		// Get argument values from the command line.
		String mode = CommandLineUtilities.getOptionValue("mode");
		String data = CommandLineUtilities.getOptionValue("data");
		String predictions_file = CommandLineUtilities.getOptionValue("predictions_file");
		String algorithm = CommandLineUtilities.getOptionValue("algorithm");
		String model_file = CommandLineUtilities.getOptionValue("model_file");
		
		int gd_iterations = 20;
		if (CommandLineUtilities.hasArg("gd_iterations"))
			gd_iterations = CommandLineUtilities.getOptionValueAsInt("gd_iterations");
		double gd_eta = 0.01;
		if (CommandLineUtilities.hasArg("gd_eta"))
			gd_eta = CommandLineUtilities.getOptionValueAsFloat("gd_eta");
		int num_features = SELECT_ALL_FEATURES;
		if (CommandLineUtilities.hasArg("num_features_to_select"))
			num_features = CommandLineUtilities.getOptionValueAsInt("num_features_to_select");
		double online_learning_rate = 1.0;
		if (CommandLineUtilities.hasArg("online_learning_rate"))
			online_learning_rate = CommandLineUtilities.getOptionValueAsFloat("online_learning_rate");
		double polynomial_kernel_exponent = 2;
		if (CommandLineUtilities.hasArg("polynomial_kernel_exponent"))
			polynomial_kernel_exponent = CommandLineUtilities.getOptionValueAsFloat("polynomial_kernel_exponent");
		int online_training_iterations = 5;
		if (CommandLineUtilities.hasArg("online_training_iterations"))
			online_training_iterations = CommandLineUtilities.getOptionValueAsInt("online_training_iterations");
		double cluster_lambda = 0.0;
		if (CommandLineUtilities.hasArg("cluster_lambda")) {
			cluster_lambda = CommandLineUtilities.getOptionValueAsFloat("cluster_lambda");
		}
		int num_clusters = 0;
		if (CommandLineUtilities.hasArg("num_clusters")) {
			num_clusters = CommandLineUtilities.getOptionValueAsInt("num_clusters");
		}
		int clustering_training_iterations = 10;
		if (CommandLineUtilities.hasArg("clustering_training_iterations")) {
			clustering_training_iterations = CommandLineUtilities.getOptionValueAsInt("clustering_training_iterations");
		}
		
		if (mode.equalsIgnoreCase("train")) {
			if (data == null || algorithm == null || model_file == null) {
				System.out.println("Train requires the following arguments: data, algorithm, model_file");
				System.exit(0);
			}
			// Load the training data.
			DataReader data_reader = new DataReader(data, true);
			List<Instance> instances = data_reader.readData();
			data_reader.close();
			
			// Train the model.
			Predictor predictor = train(instances, algorithm, gd_iterations, gd_eta, num_features, online_learning_rate,
					online_training_iterations, polynomial_kernel_exponent, cluster_lambda, num_clusters,
					clustering_training_iterations);
			saveObject(predictor, model_file);		
			
		} else if (mode.equalsIgnoreCase("test")) {
			if (data == null || predictions_file == null || model_file == null) {
				System.out.println("Train requires the following arguments: data, predictions_file, model_file");
				System.exit(0);
			}
			
			// Load the test data.
			DataReader data_reader = new DataReader(data, true);
			List<Instance> instances = data_reader.readData();
			data_reader.close();
			
			// Load the model.
			Predictor predictor = (Predictor)loadObject(model_file);
			evaluateAndSavePredictions(predictor, instances, predictions_file);
		} else {
			System.out.println("Requires mode argument.");
		}
	}
	
	/**
	 * This method trains a predictor using a given algorithm and data set.
	 * @param instances The data set of instances to be used for training.
	 * @param algorithm The String representing the type of learning algorithm to be used.
	 * @return The label predictor.
	 */
	private static Predictor train(List<Instance> instances, String algorithm, int numIter,
			double gd_eta, int numFeaturesToSelect, double online_learning_rate, int online_training_iterations,
			double polynomial_kernel_exponent, double cluster_lambda, int num_clusters, int clustering_training_iterations) {
		// TODO Train the model using "algorithm" on "data"
		// TODO Evaluate the model
		
		Predictor returnPredictor;
		AccuracyEvaluator printEvaluator = new AccuracyEvaluator();
		double accuracy;
		
		if (algorithm.equals("majority")) {
			
			returnPredictor = new MajorityPredictor(instances);
		}
		else if (algorithm.equals("even_odd")){
			
			returnPredictor = new EvenOddPredictor();
		}
		else if (algorithm.equals("logistic_regression")){
			
			returnPredictor = new LogisticRegressionPredictor(instances, numIter, gd_eta, numFeaturesToSelect);
		}
		else if (algorithm.equals("margin_perceptron")) {
			
			returnPredictor = new MarginPerceptronPredictor(instances, online_learning_rate, online_training_iterations);
		}
		else if (algorithm.equals("perceptron_linear_kernel")) {
			
			returnPredictor = new MarginDualPerceptronPredictor(instances, online_learning_rate, online_training_iterations, polynomial_kernel_exponent, algorithm);
		}
		else if (algorithm.equals("perceptron_polynomial_kernel")) {
			
			returnPredictor = new MarginDualPerceptronPredictor(instances, online_learning_rate, online_training_iterations, polynomial_kernel_exponent, algorithm);
		}
		else if (algorithm.equals("mira")) {
			
			returnPredictor = new MiraPredictor(instances, online_training_iterations);
		}
		else if (algorithm.equals("lambda_means")) {
			
			returnPredictor = new LambdaMeansPredictor(instances, clustering_training_iterations, cluster_lambda);
		}
		else if (algorithm.equals("ska")) {
			
			returnPredictor = new StochasticKMeansPredictor(instances, clustering_training_iterations, num_clusters);
		}
		else {
			
			returnPredictor = null;
		}
		
		accuracy = printEvaluator.evaluate(instances, returnPredictor);
		System.out.println("Accuracy: " + accuracy);
		
		return returnPredictor;
	}

	/**
	 * This method evaluates the accuracy of a predictor in predicting the labels for
	 * a known set of training data.
	 * @param predictor The label predictor.
	 * @param instances The data instances used in training.
	 * @param predictions_file The text file of label predictions.
	 * @throws IOException
	 */
	private static void evaluateAndSavePredictions(Predictor predictor,
			List<Instance> instances, String predictions_file) throws IOException {
		PredictionsWriter writer = new PredictionsWriter(predictions_file);
		// TODO Evaluate the model if labels are available.
		if (instances.get(0).getLabel() != null) {
			AccuracyEvaluator printEvaluator = new AccuracyEvaluator();
			double accuracy = printEvaluator.evaluate(instances, predictor);
			System.out.println("Accuracy: " + accuracy);
		}
		
		for (Instance instance : instances) {
			Label label = predictor.predict(instance);
			writer.writePrediction(label);
		}
		
		writer.close();
		
	}

	public static void saveObject(Object object, String file_name) {
		try {
			ObjectOutputStream oos =
				new ObjectOutputStream(new BufferedOutputStream(
						new FileOutputStream(new File(file_name))));
			oos.writeObject(object);
			oos.close();
		}
		catch (IOException e) {
			System.err.println("Exception writing file " + file_name + ": " + e);
		}
	}

	/**
	 * Load a single object from a filename. 
	 * @param file_name
	 * @return
	 */
	public static Object loadObject(String file_name) {
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File(file_name))));
			Object object = ois.readObject();
			ois.close();
			return object;
		} catch (IOException e) {
			System.err.println("Error loading: " + file_name);
		} catch (ClassNotFoundException e) {
			System.err.println("Error loading: " + file_name);
		}
		return null;
	}
	
	/**
	 * This method registers a command-line option.
	 * @param option_name The name of the new option.
	 * @param arg_name The variable type of the argument taken by the option.
	 * @param has_arg The boolean indicating whether the new option takes an argument.
	 * @param description The description of the new option.
	 */
	public static void registerOption(String option_name, String arg_name, boolean has_arg, String description) {
		OptionBuilder.withArgName(arg_name);
		OptionBuilder.hasArg(has_arg);
		OptionBuilder.withDescription(description);
		Option option = OptionBuilder.create(option_name);
		
		Classify.options.add(option);		
	}
	
	private static void createCommandLineOptions() {
		registerOption("data", "String", true, "The data to use.");
		registerOption("mode", "String", true, "Operating mode: train or test.");
		registerOption("predictions_file", "String", true, "The predictions file to create.");
		registerOption("algorithm", "String", true, "The name of the algorithm for training.");
		registerOption("model_file", "String", true, "The name of the model file to create/load.");
		registerOption("gd_eta", "int", true, "The step size parameter for GD.");
		registerOption("gd_iterations", "int", true, "The number of GD iterations.");
		registerOption("num_features_to_select", "int", true, "The number of features to select.");
		registerOption("online_learning_rate", "double", true, "The learning rate for perceptron.");
		registerOption("polynomial_kernel_exponent", "double", true, "The exponent of the polynomial kernel.");
		registerOption("online_training_iterations", "int", true, "The number of training iterations for online methods.");
		registerOption("cluster_lambda", "double", true, "The value of lambda in lambda-means.");
		registerOption("num_clusters", "int", true, "The number of clusters in stochastic K-means.");
		registerOption("clustering_training_iterations", "int", true, "The number of clustering iterations.");
		
		// Other options will be added here.
	}
}
