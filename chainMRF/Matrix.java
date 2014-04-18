package cs475.chainMRF;

public class Matrix {

	double[][] mtx;
	int n; // # of rows
	int m; // # of columns
	
	public Matrix(double[][] mtx, int n, int m) {
		
		this.mtx = mtx;
		this.n = n;
		this.m = m;
	}
	
//	public static void main(String args[]) {
//		
//		double[][] a = {{1,2}, {3,4}};
//		double[][] b = {{1,2},{3,4}};
//		
//		double[][] res = Matrix.mtxMult(a, b);
//		
//		//Matrix am = new Matrix(a,2,2);
//		//Matrix bm = new Matrix(b,2,1);
//		
//		//Matrix res = am.mtxMult(bm);
//		
//		for (int i=0; i<res.length; i++) {
//			for (int j=0; j<res[0].length; j++) {
//				System.out.print(res[i][j] + " ");
//			}
//			System.out.println();
//		}
//	}
	
	public static double[][] mtxMult(double[][] d1, double[][] d2) {
		
		double[][] result = new double[d1.length][1];
		for (int r=0; r<d1.length; r++) {
			result[r][0] = 0;
			for (int c=0; c<d1[0].length; c++) {
				result[r][0] += d1[r][c] * d2[c][0];
			}
		}
		
		return result;
	}
	
	public static double[][] mtxMultMax(double[][] d1, double[][] d2) {
		
		double[][] result = new double[d1.length][1];
		for (int r=0; r<d1.length; r++) {
			result[r][0] = Double.NEGATIVE_INFINITY;
			for (int c=0; c<d1[0].length; c++) {
				if (d1[r][c] + d2[c][0] > result[r][0] && (d1[r][c] + d2[c][0]) != 0) {
					result[r][0] = d1[r][c] + d2[c][0];
				}
			}
		}
		
		return result;
	}
}
