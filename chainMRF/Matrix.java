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
		double[][] b = {{1},{2}};
		
		Matrix am = new Matrix(a,2,2);
		Matrix bm = new Matrix(b,2,1);
		
		Matrix res = am.mtxMult(bm);
		
		for (int i=0; i<res.n; i++) {
			for (int j=0; j<res.m; j++) {
				System.out.print(res.getEntry(i,j) + " ");
			}
			System.out.println();
		}
	}
	
	public Matrix mtxMult(Matrix other) {
		
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
	
	public double getEntry(int x, int y) {
		
		return this.mtx[x][y];
	}
}
