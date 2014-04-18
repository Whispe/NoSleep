package cs475.chainMRF;
import cs475.Vector;
import cs475.chainMRF.*;

public class SumProduct {

	private ChainMRFPotentials potentials;
	// add whatever data structures needed
	double[][][] descXtoF;
	double[][][] descFtoX;
	double[][][] ascXtoF;
	double[][][] ascFtoX;

	public SumProduct(ChainMRFPotentials p) {
		this.potentials = p;
		
		int n = this.potentials.chainLength();
		int k = this.potentials.numXValues();
		
		this.descXtoF = new double[n+1][k+1][1];
		this.descFtoX = new double[n+1][k+1][1];
		this.ascXtoF = new double[n+1][k+1][1];
		this.ascFtoX = new double[n+1][k+1][1];
//		double[][] init = new double[k][1];
//		for (int i=0; i<init.length; i++) {
//			init[i][0] = 0;
//		}
//		this.descXtoF[0] = init;
//		this.descFtoX[0] = init;
//		this.ascXtoF[0] = init;
//		this.ascFtoX[0] = init;
		
		for (int idx=n; idx>=1; idx--) {
			if (idx == n) {
				descXtoF[n] = msgFtoX(n,n);
			} else if (idx == 1) {
				descFtoX[1] = msgFtoX(n+1,1);
			} else {
				descFtoX[idx] = msgFtoX(idx+n, idx);
				descXtoF[idx] = msgXtoF(idx, idx-1+n);
			}
		}
		
		for (int idx=1; idx<=n; idx++) {
			if (idx == 1) {
				ascXtoF[1] = msgXtoF(1,n+1);
			} else if (idx == n) {
				ascFtoX[n] = msgFtoX(idx+n-1,n);
			} else {
				ascFtoX[idx] = msgFtoX(idx-1+n, idx);
				ascXtoF[idx] = msgXtoF(idx, idx+n);
			}
		}
	}
	
	public double[][] msgFtoX(int f_i, int x_i) {
		
		ChainMRFPotentials pt = this.potentials;
		int n = pt.chainLength();
		int k = pt.numXValues();
		double[][] msg = new double[k+1][1];
		
		if (x_i == f_i) {
			msg = this.getUnary(x_i);
		} else if (f_i == x_i + n) { // f to the right of x
			msg = Matrix.mtxMult(this.getBinaryGoRight(f_i), descXtoF[x_i+1]);
		} else if (f_i == x_i + n - 1) { // f to the left of x
			msg = Matrix.mtxMult(this.getBinaryGoLeft(f_i), ascXtoF[x_i-1]);
		}
		
		return msg;
	}
	
	public double[][] msgXtoF(int x_i, int f_i) {
		
		ChainMRFPotentials pt = this.potentials;
		int n = pt.chainLength();
		int k = pt.numXValues();
		double[][] msg = new double[k+1][1];
		
		if (f_i == x_i + n) {
			if (x_i == 1) {
				msg = this.getUnary(1);
			} else {
				msg = Vector.elemByElemMult(ascFtoX[x_i], this.getUnary(x_i));
			}
		} else if (f_i == x_i + n - 1) {
			if (x_i == n) {
				msg = this.getUnary(n);
			} else {
				msg = Vector.elemByElemMult(descFtoX[x_i], this.getUnary(x_i));
			}
		}
		
		return msg;
	}
	
	public double[][] getUnary(int x_i) {
		
		ChainMRFPotentials pt = this.potentials;
		int k = pt.numXValues();
		double[][] unary = new double[k+1][1];
		
		int i = x_i;
		for (int j=1; j<=k; j++) {
			unary[j][0] = pt.potential(i, j);
		}
		
		return unary;
	}

	public double[][] getBinaryGoRight(int f_i) {
		
		ChainMRFPotentials pt = this.potentials;
		int k = pt.numXValues();
		double[][] binary = new double[k+1][k+1];
		
		for (int p=1; p<=k; p++) {
			for (int q=1; q<=k; q++) {
				binary[p][q] = pt.potential(f_i, p, q);
			}
		}
		
		return binary;
	}
	
	public double[][] getBinaryGoLeft(int f_i) {
		
		ChainMRFPotentials pt = this.potentials;
		int k = pt.numXValues();
		double[][] binary = new double[k+1][k+1];
		
		for (int p=1; p<=k; p++) {
			for (int q=1; q<=k; q++) {
				binary[p][q] = pt.potential(f_i, q, p);
			}
		}
		
		return binary;
	}
	
	public double[] marginalProbability(int x_i) {
		
		ChainMRFPotentials pt = this.potentials;
		int n = pt.chainLength();
		int k = pt.numXValues();
		double[] result;
		
		if (x_i == 1) {
			double[][] unaryMsg = msgFtoX(1,1);
			double[][] rightMsg = descFtoX[1];
			double[][] num = Vector.elemByElemMult(unaryMsg, rightMsg);
			double denom = Vector.dotProduct2D(unaryMsg, rightMsg);
			result = Vector.scalarMult2D(1/denom, num);
		} else if (x_i == n){
			double[][] unaryMsg = msgFtoX(n,n);
			double[][] leftMsg = ascFtoX[n];
			double[][] num = Vector.elemByElemMult(unaryMsg, leftMsg);
			double denom = Vector.dotProduct2D(unaryMsg, leftMsg);
			result = Vector.scalarMult2D(1/denom, num);
		} else {
			double[][] unaryMsg = msgFtoX(x_i,x_i);
			double[][] leftMsg = ascFtoX[x_i];
			double[][] rightMsg = descFtoX[x_i];
			double[][] num = Vector.elemByElemMult(Vector.elemByElemMult(unaryMsg, leftMsg), rightMsg);
			double denom = Vector.dotProduct2D(Vector.elemByElemMult(unaryMsg, leftMsg), rightMsg);
			result = Vector.scalarMult2D(1/denom, num);
		}
		
		return result;
	}

}

