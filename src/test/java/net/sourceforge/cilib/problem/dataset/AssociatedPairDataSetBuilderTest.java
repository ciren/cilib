package net.sourceforge.cilib.problem.dataset;

import static org.junit.Assert.assertEquals;
import net.sourceforge.cilib.problem.ClusteringProblem;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class AssociatedPairDataSetBuilderTest {
	private static AssociatedPairDataSetBuilder dataSetBuilder = null;

	@BeforeClass
	public static void intialise() {
		dataSetBuilder = new AssociatedPairDataSetBuilder();
		dataSetBuilder.addDataSet(new MockClusteringStringDataSet());
		ClusteringProblem problem = new ClusteringProblem();
		problem.setDataSetBuilder(dataSetBuilder);
	}

	@AfterClass
	public static void destroy() {
		dataSetBuilder = null;
	}

	@Test
	public void testNumberOfPatterns() {
		assertEquals(93, dataSetBuilder.getNumberOfPatterns());
	}
}
