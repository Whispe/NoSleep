package cs475.chainMRF;
import cs475.Vector;
import cs475.chainMRF.*;

public class MaxSum {

	private ChainMRFPotentials potentials;
	private int[] assignments;
	// add whatever data structures needed
	double[][][] descXtoF;
	double[][][] descFtoX;
	double[][][] ascXtoF;
	double[][][] ascFtoX;
	int[][] backtrack;

	public MaxSum(ChainMRFPotentials p) {
		this.potentials = p;
		assignments = new int[p.chainLength()+1]; // Ignoring element 0.
		
		int n = this.potentials.chainLength();
		int k = this.potentials.numXValues();
		
		this.descXtoF = new double[n+1][k+1][1];
		this.descFtoX = new double[n+1][k+1][1];
		this.ascXtoF = new double[n+1][k+1][1];
		this.ascFtoX = new double[n+1][k+1][1];
		this.backtrack = new int[n+1][k+1];
		
		double[][] zeros = new double[k+1][1];
		
		for (int idx=n; idx>=1; idx--) {
			if (idx == n) {
				descXtoF[n] = msgXtoF(n,n);
				descFtoX[n] = zeros;
			} else if (idx == 1) {
				descFtoX[1] = msgFtoX(n+1,1);
				descXtoF[1] = Vector.elemByElemMult(msgFtoX(n+1,idx), this.getUnary(idx));
			} else {
				descFtoX[idx] = msgFtoX(idx+n, idx);
				descXtoF[idx] = msgXtoF(idx, idx-1+n);
			}
		}
		
		for (int idx=1; idx<=n; idx++) {
			if (idx == 1) {
				ascXtoF[1] = msgXtoF(1,n+1);
				ascFtoX[1] = this.getUnary(1); // added
			} else if (idx == n) {
				ascFtoX[n] = msgFtoX(idx+n-1,n);
				ascXtoF[n] = Vector.elemByElemMult(msgFtoX(idx-1+n, idx), this.getUnary(idx));
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
			msg = this.mtxMultMax(this.getBinaryGoRight(f_i), descXtoF[x_i+1], x_i);
		} else if (f_i == x_i + n - 1) { // f to the left of x
			msg = this.mtxMultMax(this.getBinaryGoLeft(f_i), ascXtoF[x_i-1], x_i);
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
				msg = Vector.sumVectors2D(ascFtoX[x_i], this.getUnary(x_i)); // Step 2: mu x -> f_s (x) sums
			}
		} else if (f_i == x_i + n - 1) {
			if (x_i == n) {
				msg = this.getUnary(n);
			} else {
				msg = Vector.sumVectors2D(descFtoX[x_i], this.getUnary(x_i)); // Step 2: mu x -> f_s (x) sums
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
			unary[j][0] = Math.log(pt.potential(i, j)); // Step 1: mu f_s -> x (x) returns f_s(x) if x only neighbor of f_s
		}
		
		return unary;
	}

	public double[][] getBinaryGoRight(int f_i) {
		
		ChainMRFPotentials pt = this.potentials;
		int k = pt.numXValues();
		double[][] binary = new double[k+1][k+1];
		
		for (int p=1; p<=k; p++) {
			for (int q=1; q<=k; q++) {
				binary[p][q] = Math.log(pt.potential(f_i, p, q));
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
				binary[p][q] = Math.log(pt.potential(f_i, q, p));
			}
		}
		
		return binary;
	}
	public double maxProbability(int x_i) {

		int n = this.potentials.chainLength();
		
		double result = 0;
		for (int i=1; i<=n; i++) {
			result = maxProbabilityLoopy(i);
		}
		
		return result;
	}
	public double maxProbabilityLoopy(int x_i) {
		// TODO
		
		double[] result;
		ChainMRFPotentials pt = this.potentials;
		int n = pt.chainLength();
		int k = pt.numXValues();
		
		double[][] unaryMsg = msgFtoX(x_i,x_i);
		double[][] leftMsg = ascFtoX[x_i];
		double[][] rightMsg = descFtoX[x_i];
		double[][] num = Vector.sumVectors2D(Vector.sumVectors2D(unaryMsg, leftMsg), rightMsg);
		double max = Double.NEGATIVE_INFINITY;
		
		for (int i=1; i<=k; i++) {
			double numD = num[i][0];
			//System.out.println("numD for k=" + i + ": " + numD);
			if (numD > max) {
				max = numD;
				assignments[x_i] = i;
				//System.out.println("\tNew max: " + max + " assigned.");
			}
		}
		
		SumProduct sp = new SumProduct(this.potentials);
		double[][] unaryMsgSp = sp.getUnary(n);
		double[][] leftMsgSp = sp.ascFtoX[n];
//		double[][] rightMsgSp = sp.descFtoX[x_i];
		double hold = 0;
		double[][] probs;
		
//		if (x_i == 1) {
//			probs = Vector.elemByElemMult(rightMsgSp, unaryMsgSp);
//		} else if (x_i == n) {
//			probs = Vector.elemByElemMult(leftMsgSp, unaryMsgSp);
//		} else {
//			probs = Vector.elemByElemMult(Vector.elemByElemMult(leftMsgSp, rightMsgSp), unaryMsgSp);
//		}
		
		probs = Vector.elemByElemMult(leftMsgSp, unaryMsgSp);
		for (int i=1; i<=k; i++) {
			hold += probs[i][0];
		}
		
		return max - Math.log(hold);
	}
	
	public double[][] mtxMultMax(double[][] d1, double[][] d2, int x_i) {
		
		int k = this.potentials.numXValues();
		
		double[][] result = new double[d1.length][1];
		for (int r=1; r<d1.length; r++) {
			result[r][0] = Double.NEGATIVE_INFINITY;
			for (int c=1; c<d1[0].length; c++) {
				if (d1[r][c] + d2[c][0] > result[r][0] && (d1[r][c] + d2[c][0]) != 0) {
					result[r][0] = d1[r][c] + d2[c][0];
					this.backtrack[x_i - 1][k] = c;
				}
			}
		}
		
		return result;
	}
	
	public int[] getAssignments() {
		return assignments;
	}
}
