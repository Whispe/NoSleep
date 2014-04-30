package cs475.RBM;
import java.util.Random;

import cs475.Vector;
import cs475.RBM.*;

public class RBMEnergy {
	private RBMParameters _parameters;
	private int numSame;
	private int numSamples;
	private double[] x_T;
	private double[] h_T;
	private double[] x_m;
	private Random rand;
	
	// TODO: Add the required data structures and methods.

	public RBMEnergy(RBMParameters parameters, int numSamples) {
		
		this._parameters = parameters;
		this.numSame = 0;
		this.numSamples = numSamples;
		this.rand = new Random(0);
		this.x_m = new double[this._parameters.numVisibleNodes()];
		this.x_T = new double[this._parameters.numVisibleNodes()];
		this.h_T = new double[this._parameters.numHiddenNodes()];
		
		for (int i=0; i<this.x_m.length; i++) {
			x_m[i] = this._parameters.visibleNode(i);
		}
		this.x_T = this.x_m;
	}
	
	public double computeMarginal() {
		
		for (int sampNum=0; sampNum<this.numSamples; sampNum++) {
			this.h_T = generateH_i(sampNum);
			this.x_T = generateX_i(sampNum);
//			System.out.println("Sample num: " + sampNum + ":");
//			System.out.println("h_s: ");
//			for (int k=0; k<this.h_T.length; k++) {
//				System.out.print(h_T[k] + " ");
//			}
//			System.out.println();
//			System.out.println("x_s: ");
//			for (int k=0; k<this.x_T.length; k++) {
//				System.out.print(x_T[k] + " ");
//			}
//			System.out.println();
			if (xSame()) {
				this.numSame++;
			}
//			System.out.println("numSame: " + numSame);
//			System.out.println();
		}
		
		return 1.0 * this.numSame / this.numSamples;
	}
	
	public double[] generateH_i(int sampNum) {
		
		double[] h = new double[this._parameters.numHiddenNodes()];
		for (int pos=0; pos<this._parameters.numHiddenNodes(); pos++) {
			double u = this.rand.nextDouble();
			double p = calcPhGivenx(pos);
			if (u<p) {
				h[pos] = 0;
			} else {
				h[pos] = 1;
			}
		}
		
		return h;
	}
	
	public double[] generateX_i(int sampNum) {
		
		double[] x = new double[this._parameters.numVisibleNodes()];
		for (int pos=0; pos<this._parameters.numVisibleNodes(); pos++) {
			double u = this.rand.nextDouble();
			double p = calcPxGivenh(pos);
			if (u<p) {
				x[pos] = 0;
			} else {
				x[pos] = 1;
			}
		}
		
		return x;
	}
	
	public double calcPhGivenx(int pos) {
		
		double[] w_j = new double[this._parameters.numVisibleNodes()];
		for (int idx=0; idx<w_j.length; idx++) {
			w_j[idx] = this._parameters.weight(idx, pos);
		}
		double z = Vector.dotProduct(this.x_T, w_j) + this._parameters.hiddenBias(pos);
		
		return sigmoidFunc(z);
	}
	
	public double calcPxGivenh(int pos) {
		
		double[] w_i = new double[this._parameters.numHiddenNodes()];
		for (int idx=0; idx<w_i.length; idx++) {
			w_i[idx] = this._parameters.weight(pos, idx);
		}
		double z = Vector.dotProduct(this.h_T, w_i) + this._parameters.visibleBias(pos);
		
		return sigmoidFunc(z);
	}
	
	public double sigmoidFunc(double z) {
		
		return 1.0 / (1.0 + Math.exp(-z));
	}
	
	public boolean xSame() {
		
		for (int i=0; i<this.x_m.length; i++) {
			if (this.x_m[i] != this.x_T[i]) {
				return false;
			}
		}
		return true;
	}
}