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
		
		this.descXtoF = new double[n+1][k][1];
		this.descFtoX = new double[n+1][k][1];
		this.ascXtoF = new double[n+1][k][1];
		this.ascFtoX = new double[n+1][k][1];
		double[][] init = new double[k][1];
		for (int i=0; i<init.length; i++) {
			init[i][0] = 0;
		}
		this.descXtoF[0] = init;
		this.descFtoX[0] = init;
		this.ascXtoF[0] = init;
		this.ascFtoX[0] = init;
		
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
		double[][] hold = new double[k+1][k+1];
		Matrix msgMtx = new Matrix(msg, k+1, 1);
		
		if (x_i == f_i) {
			int i = x_i;
			for (int j=1; j<=k; j++) {
				msg[j][0] = pt.potential(i, j);
			}
		} else if (f_i == x_i + n) { // f to the right of x
			for (int p=1; p<=k; p++) {
				for (int q=1; q<=k; q++) {
					hold[p][q] = pt.potential(f_i, p, q);
				}
			}
			Matrix descXtoFMtx = new Matrix(descXtoF[x_i+1], k+1, 1);
			Matrix holdMtx = new Matrix(hold, k+1, k+1);
			msg = holdMtx.mtxMult(descXtoFMtx).mtx;
		} else if (f_i == x_i + n - 1) { // f to the left of x
			for (int p=1; p<=k; p++) {
				for (int q=1; q<=k; q++) {
					hold[p][q] = pt.potential(f_i, p, q);
				}
			}
			Matrix ascXtoFMtx = new Matrix(ascXtoF[x_i-1], k+1, 1);
			msg = msgMtx.mtxMult(ascXtoFMtx).mtx;
		}
		
		return msg;
	}
	
	public double[][] msgXtoF(int x_i, int f_i) {
		
		ChainMRFPotentials pt = this.potentials;
		int n = pt.chainLength();
		int k = pt.numXValues();
		double[][] msg = new double[k+1][1];
		Matrix msgMtx = new Matrix(msg, k, 1);
		
		if (f_i == x_i + n) {
			if (x_i == 1) {
				int i=1;
				for (int j=1; j<=k; j++) {
					msg[j][0] = pt.potential(i, j);
				}
			} else {
				for (int j=1; j<=k; j++) {
					msg[j][0] = pt.potential(x_i, j); // unary
				}
				for (int j=1; j<=k; j++) {
					msg[j][0] = msg[j][1] * ascFtoX[x_i][j][1];
				}
			}
		} else if (f_i == x_i + n - 1) {
			if (x_i == n) {
				int i=n;
				for (int j=1; j<=k; j++) {
					msg[j][0] = pt.potential(i, j);
				}
			} else {
				for (int j=1; j<=k; j++) {
					msg[j][0] = pt.potential(x_i,j);
				}
				Matrix descFtoXMtx = new Matrix(ascXtoF[x_i-1], k, 1);
				msg = msgMtx.mtxMult(descFtoXMtx).mtx;
			}
		}
		
		return msg;
	}

	public double[] marginalProbability(int x_i) {
		
		ChainMRFPotentials pt = this.potentials;
		int n = pt.chainLength();
		int k = pt.numXValues();
		
		double[] probs1tok = new double[k+1];
		double[][] unary = new double[k+1][1];
		for (int j=1; j<=k; j++) {
			unary[j][0] = pt.potential(x_i, j);
		}
		
		if (x_i == 1) {
			for (int j=1; j<=k; j++) {
				probs1tok[j] = unary[j][0] * descFtoX[1][j][0];
			}
		} else if (x_i == n) {
			for (int j=1; j<=k; j++) {
				probs1tok[j] = unary[j][0] * ascFtoX[n][j][0];
			}
		} else {
			for (int j=1; j<=k; j++) {
				probs1tok[j] = unary[j][0] * ascFtoX[x_i][j][0] * descFtoX[x_i][j][0];
			}
		}
		
		return probs1tok;
	}

}

