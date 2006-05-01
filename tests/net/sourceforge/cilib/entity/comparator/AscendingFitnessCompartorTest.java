package net.sourceforge.cilib.entity.comparator;

import static junit.framework.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.MinimisationFitness;

import org.junit.Test;

public class AscendingFitnessCompartorTest {
	
	@Test
	public void simpleDataStructure() {
		
		Fitness x [] = { 
				new MinimisationFitness(0.0),
				new MinimisationFitness(1.0),
				new MinimisationFitness(2.0),
				new MinimisationFitness(3.0)
			};
		
		for (int i = 0; i < x.length-1; i++) {
			assertEquals(1, x[i].compareTo(x[i+1]));
		}
		
		List<Fitness> l = Arrays.asList(x);
		
		Collections.sort(l, new AscendingFitnessComparator());
		
	}
	
	
	/**
	 * 
	 * @author gpampara
	 *
	 */
	private class AscendingFitnessComparator implements Comparator<Fitness>{

		public int compare(Fitness f1, Fitness f2) {
			return f2.compareTo(f1);
		}
		
	}

}
