package cs475;

import java.io.*;

/**
 * This class contains a file writer to write the label predictions to a text file.
 */
public class PredictionsWriter {

	private Writer _writer;

	/**
	 * This constructor creates an instance of the PredictionsWriter class.
	 * @param prediction_file The name of the file to which the predictions will be written to.
	 * @throws IOException
	 */
	public PredictionsWriter(String prediction_file) throws IOException {
		this._writer = new BufferedWriter(new FileWriter(prediction_file));
	}

	/**
	 * This method closes the file being written to.
	 * @throws IOException
	 */
	public void close() throws IOException {
		this._writer.close();
	}
	
	/**
	 * This method writes the label prediction of a single instance to the file. 
	 * @param label The label of the prediction to be written.
	 * @throws IOException
	 */
	public void writePrediction(Label label) throws IOException {
		this._writer.write(label.toString());
		this._writer.write("\n");
	}
	
}
