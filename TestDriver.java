package cs475;

public class TestDriver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		double[] a = new double[620];
		double[] b = new double[620];
		
		for (int i=0; i<620; i++) {
			
			a[i] = 0.6;
			b[i] = 0.2;
		}
		
		double dist = Vector.dist(a,b);
		System.out.println(dist);
		
		
	}
}
