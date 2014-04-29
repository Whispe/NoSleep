package cs475.RBM;
import java.util.Random;

import cs475.Vector;
import cs475.RBM.*;

public class RBMEnergy {
	private RBMParameters _parameters;
	private int numXSame;
	private int numSamples;
	private double[][] x_s;
	private double[][] h_s;
	private Random rand;
	
	// TODO: Add the required data structures and methods.

	public RBMEnergy(RBMParameters parameters, int numSamples) {
		this._parameters = parameters;
		this.numXSame = 0;
		this.numSamples = numSamples;
		this.rand = new Random(0);
		
		this.x_s = new double[this.numSamples][this._parameters.numVisibleNodes()];
		this.h_s = new double[this.numSamples][this._parameters.numHiddenNodes()];
		double[] holdX = new double[this._parameters.numVisibleNodes()];
		double[] holdH = new double[this._parameters.numHiddenNodes()];
		for (int i=0; i<this._parameters.numVisibleNodes(); i++) {
			this.x_s[i] = holdX;
		}
		for (int i=0; i<this._parameters.numHiddenNodes(); i++) {
			this.h_s[i] = holdH;
		}
	}
	
	public double computeMarginal() {
		// TODO: Add code here
		double energy = 0.0;
		
		for (int i=0; i<this.numSamples; i++) {
			int sampNum = i;
			this.h_s[sampNum] = generateH(sampNum);
			this.x_s[sampNum] = generateX(sampNum);
		}
		
		for (int i=0; i<this.numSamples; i++) {
			int sampNum = i;
			this.numXSame += compX(sampNum);
		}
		
		energy = 1.0 * this.numXSame / this.numSamples;
		
		return energy;
	}
	
	public double[] generateH(int sampNum) {
		double[] h_sampNum = new double[this._parameters.numHiddenNodes()];
		
		// Find x^(i-1).		
		double[] x_T = new double[this._parameters.numVisibleNodes()];
		if (sampNum == 0) {
			for (int i=0; i<x_T.length; i++) {
				x_T[i] = this._parameters.visibleNode(i);
			}
		} else {
			x_T = this.x_s[sampNum-1];
		}
		
		for (int i=0; i<this._parameters.numHiddenNodes(); i++) {
			double u = this.rand.nextDouble();
			double p = calcPHGivenX(x_T,i);
			if (u<p) {
				h_sampNum[i] = 0; // This is y.
			} else {
				h_sampNum[i] = 1;
			}
		}
		return h_sampNum;
	}
	
	public double[] generateX(int sampNum) {
		double[] x_sampNum = new double[this._parameters.numVisibleNodes()];
		
		double[] h_T = this.h_s[sampNum];
		
		for (int i=0; i<this._parameters.numVisibleNodes(); i++) {
			double u = this.rand.nextDouble();
			double p = calcPXGivenH(h_T, i);
			if (u<p) {
				x_sampNum[i] = 0; // This is y.
			} else {
				x_sampNum[i] = 1;
			}
		}
		return x_sampNum;
	}
	
	public double calcPHGivenX(double[] x_T, int j) {
		double[] w_j = new double[this._parameters.numVisibleNodes()];
		
		for (int i=0; i<w_j.length; i++) {
			w_j[i] = this._parameters.weight(i, j);
		}
		double z = Vector.dotProduct(x_T, w_j) + this._parameters.hiddenBias(j);
		
		return sigmoidFunc(z);
	}
	
	public double calcPXGivenH(double[] h_T, int i) {
		double[] w_i = new double[this._parameters.numHiddenNodes()];
		
		for (int j=0; j<w_i.length; j++) {
			w_i[j] = this._parameters.weight(i, j);
		}
		double z = Vector.dotProduct(h_T, w_i) + this._parameters.visibleBias(i);
		
		return sigmoidFunc(z);
	}
	
	public double sigmoidFunc(double z) {
		return 1.0 / (1 + Math.exp(-z));
	}
	
	public int compX(int sampNum) {
		int same = 1; // 0 for not the same, 1 otherwise
		for (int i=0; i<this._parameters.numVisibleNodes(); i++) {
			if (this.x_s[sampNum][i] != this._parameters.visibleNode(i)) {
				same = 0;
			}
		}
		return same;
	}
}
