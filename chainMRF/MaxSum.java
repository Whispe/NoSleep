package cs475.chainMRF;
import cs475.chainMRF.*;

public class MaxSum {

	private ChainMRFPotentials potentials;
	private int[] assignments;
	// add whatever data structures needed

	public MaxSum(ChainMRFPotentials p) {
		this.potentials = p;
		assignments = new int[p.chainLength()+1];
	}
	
	public int[] getAssignments() {
		return assignments;
	}

	public double maxProbability(int x_i) {
		// TODO
		
		return 0;
	}
}
