package cs475;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

/**
 * This class contains and tests methods for calculating maximum likelihood for a Bernoulli distribution.
 */
public class BernoulliLikelihood {
	static public LinkedList<Option> options = new LinkedList<Option>();
	
	public static void main(String[] args) throws IOException {
		// Parse the command line.
		String[] manditory_args = { "data"};
		createCommandLineOptions();
		CommandLineUtilities.initCommandLineParameters(args, BernoulliLikelihood.options, manditory_args);
		
		String dataFile = CommandLineUtilities.getOptionValue("data");
		BernoulliLikelihood bl = new BernoulliLikelihood();
		ArrayList<Integer> data = bl.readData(dataFile);
		
		double parameter = bl.computeMaximumLikelihood(data);
		double llhood = bl.computeLogLikelihood(data, parameter);
		System.out.println("Maximum Likelihood Solution: " + Double.toString(parameter));
		System.out.println("Log-likelihood: " + Double.toString(llhood));
	}
	
	/**
	 * This method registers a command line option, so the tag can be recognized and the parameter saved.
	 * @param option_name The name of the new command line option.
	 * @param arg_name The name of the new argument.
	 * @param has_arg The boolean indicating whether the new option takes an argument.
	 * @param description The description of the new option.
	 */
	public static void registerOption(String option_name, String arg_name, boolean has_arg, String description) {
		OptionBuilder.withArgName(arg_name);
		OptionBuilder.hasArg(has_arg);
		OptionBuilder.withDescription(description);
		Option option = OptionBuilder.create(option_name);
		
		BernoulliLikelihood.options.add(option);		
	}
	
	/**
	 * This method creates new command line options.
	 */
	private static void createCommandLineOptions() {
		registerOption("data", "String", true, "The data file to read.");
	}
	
	/**
	 * This method computes the maximum likelihood mu for a set of Integer data.
	 * @param data The ArrayList of Integer data.
	 * @return The maximum likelihood mu.
	 */
	public double computeMaximumLikelihood(ArrayList<Integer> data) {
		// TODO: Filled in here.
		double count = 0;
		double total = 0;
		for (int i=0; i<data.size(); i++) {
			
			count++;
			total += data.get(i);
		}
		double mu = total / count;
		return mu;
	}
	
	/**
	 * This method computes the logarithmic maximum likelihood for a set of Integer data.
	 * @param data The ArrayList of Integer data.
	 * @param parameter The parameter mu, which is the probability of an event occurring.
	 * @return The logarithmic maximum likelihood.
	 */
	public double computeLogLikelihood(ArrayList<Integer> data, double parameter) {
		// TODO: Filled in here.
		double total = 0;
		for (int i=0; i<data.size(); i++) {
			total += Math.log(Math.pow(parameter, data.get(i)) * Math.pow(1-parameter, 1-data.get(i)));
		}
		return total;
	}
	
	/**
	 * This method reads the Integer data from a file and formats it into an ArrayList.
	 * @param filename The name of the file to read the data from.
	 * @return The ArrayList of Integer data.
	 * @throws FileNotFoundException
	 */
	public ArrayList<Integer> readData(String filename) throws FileNotFoundException {
		Scanner scanner = new Scanner(new BufferedInputStream(new FileInputStream(filename)));
		ArrayList<Integer> data = new ArrayList<Integer>();
		
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.trim().length() == 0)
				   continue;
			String result = line.trim();
			int value = Integer.parseInt(result);
			data.add(value);
		}
		
		scanner.close();
		return data;
	}
}
