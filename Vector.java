package cs475;

/**
 * This class contains methods to perform basic vector operations.
 */
public class Vector {

	/**
	 * This method computes the sum of two vectors.
	 * @param ra1 The first vector as an array.
	 * @param ra2 The second vector as an array.
	 * @return The sum.
	 */
	public static double[] sumVectors(double[] ra1, double[] ra2) {
		
		if (ra1.length == ra2.length) {
			
			double[] retRa = new double[ra1.length];
			
			for (int i=0; i<ra1.length; i++) {
				
				retRa[i] = ra1[i] + ra2[i];
			}
			
			return retRa;
		}
		else {
			
			System.out.println("Vectors are not of equal length: sumVectors().");
			return null;
		}
	}
	
	public static double[][] sumVectors2D(double[][] ra1, double[][] ra2) {
		
		if (ra1.length == ra2.length) {
			
			double[][] retRa = new double[ra1.length][1];
			
			for (int i=0; i<ra1.length; i++) {
				
				retRa[i][0] = ra1[i][0] + ra2[i][0];
			}
			
			return retRa;
		}
		else {
			
			System.out.println("Vectors are not of equal length: sumVectors().");
			return null;
		}
	}
	
	/**
	 * This method computes a vector multiplied by a scalar.
	 * @param scalar The scalar to multiply by.
	 * @param ra The vector as an array.
	 * @return The resulting vector as an array.
	 */
	public static double[] scalarMult(double scalar, double[] ra) {
		
		double[] retRa = new double[ra.length];
		
		for (int i=0; i<ra.length; i++) {
			
			retRa[i] = scalar * ra[i];
		}
		
		return retRa;
	}
	
	public static double[] scalarMult2D(double scalar, double[][] ra) {
		
		double[] retRa = new double[ra.length];
		
		for (int i=0; i<ra.length; i++) {
			
			retRa[i] = scalar * ra[i][0];
		}
		
		return retRa;
	}
	
	/**
	 * This method computes the dot product of two vectors.
	 * @param ra1 The first vector as an array.
	 * @param ra2 The second vector as an array.
	 * @return The dot product.
	 */
	public static double dotProduct(double[] ra1, double[] ra2) {
		
		if (ra1.length == ra2.length) {
			
			double retVal = 0;
			
			for (int i=0; i<ra1.length; i++) {
				
				retVal += ra1[i] * ra2[i];
			}
			
			return retVal;
		}
		else {
			
			System.out.println("Vectors are not of the same length: dotProduct().");
			return -1;
		}
	}
	
	public static double dotProduct2D(double[][] ra1, double[][] ra2) {
		
		if (ra1.length == ra2.length) {
			
			double retVal = 0;
			
			for (int i=0; i<ra1.length; i++) {
				
				retVal += ra1[i][0] * ra2[i][0];
			}
			
			return retVal;
		}
		else {
			
			System.out.println("Vectors are not of the same length: dotProduct().");
			return -1;
		}
	}
	
	/**
	 * This method computes the square of the Euclidean distance between two vectors.
	 */
	public static double sqrDist(double[] ra1, double[] ra2) {
		
		double dist = 0;
		
		if (ra1.length == ra2.length) {
			
			double[] neg = Vector.scalarMult(-1, ra2);
			double[] diff = Vector.sumVectors(ra1, neg);
			for (int i=0; i<diff.length; i++) {
				
				dist += diff[i] * diff[i];
			}
			return dist;
		}
		else {
			
			System.out.println("Vectors are not of the same length: dist().");
			return -1;
		}
	}
	
	/**
	 * This method computes the Euclidean distance between two vectors.
	 */
	public static double dist(double[] ra1, double[] ra2) {
		
		double dist = 0;
		
		if (ra1.length == ra2.length) {
			
			double[] neg = Vector.scalarMult(-1, ra2);
			double[] diff = Vector.sumVectors(ra1, neg);
			for (int i=0; i<diff.length; i++) {
				
				dist += diff[i] * diff[i];
			}
			dist = Math.sqrt(dist);
			return dist;
		}
		else {
			
			System.out.println("Vectors are not of the same length: dist().");
			return -1;
		}
	}
	
	public static double[][] elemByElemMult(double[][] d1, double[][] d2) {
		
		double[][] result = new double[d1.length][1];
		for (int i=0; i<d1.length; i++) {
			result[i][0] = d1[i][0] * d2[i][0];
		}
		return result;
	}
	
	public static int idxOfMax(double[][] d) {
		
		int idxOfMax = 0;
		
		for (int i=0; i<d.length; i++) {
			if (d[i][0] > d[idxOfMax][0]) {
				idxOfMax = i;
			}
		}
		
		return idxOfMax;
	}
	
	public static double sumComponents(double[][] d) {
		
		double result = 0;
		
		for (int i=0; i<d.length; i++) {
			result += d[i][0];
		}
		
		return result;
	}
}
