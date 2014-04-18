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
	
	public static void main(String args[]) {
		
		double[][] a = {{1,2}, {3,4}};
		double[][] b = {{1,2},{3,4}};
		
		double[][] res = Matrix.mtxMult(a, b);
		
		//Matrix am = new Matrix(a,2,2);
		//Matrix bm = new Matrix(b,2,1);
		
		//Matrix res = am.mtxMult(bm);
		
		for (int i=0; i<res.length; i++) {
			for (int j=0; j<res[0].length; j++) {
				System.out.print(res[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public Matrix mtxMultMtx(Matrix other) {
		
		double[][] result = new double[this.n][other.m];
		
		if (this.m == other.n) {
			for (int i=0; i<other.m; i++) {
				for (int j=0; j<this.n; j++) {
					int sum = 0;
					for (int l=0; l<this.m; l++) {
						sum += this.getEntry(j,l) * other.getEntry(l,i);
					}
					result[j][i] = sum;
				}
			}
		}
		return new Matrix(result, this.n, other.m);
	}
	
	public static double[][] mtxMult(double[][] d1, double[][] d2) {
		
//		double[][] result = new double[d1.length][d2[0].length];
//		
//		if (d1[0].length == d2.length) {
//			
//			for (int i=0; i<d2[0].length; i++) {
//				for (int j=0; j<d1.length; j++) {
//					int sum = 0;
//					for (int l=0; l<d1[0].length; l++) {
//						sum += d1[j][l] * d2[l][i];
//					}
//					result[j][i] = sum;
//				}
//			}
//		}
		
		double[][] result = new double[d1.length][1];
		for (int r=0; r<d1.length; r++) {
			result[r][0] = 0;
			for (int c=0; c<d1[0].length; c++) {
				result[r][0] += d1[r][c] * d2[c][0];
			}
		}
		
		return result;
	}
	
	public double getEntry(int x, int y) {
		
		return this.mtx[x][y];
	}
}
