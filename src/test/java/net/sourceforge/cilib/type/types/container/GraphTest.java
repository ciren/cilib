package net.sourceforge.cilib.type.types.container;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GraphTest {
	
	@Test
	public void creation() {
		Graph<Double> graph = new StandardGraph<Double>();
		
		graph.add(2.0);
		graph.add(1.0);
		graph.add(3.0);
		
		graph.addEdge(1.0, 2.0);
		
		assertEquals(1, graph.edges());
		
		graph.remove(1.0);
		
		assertEquals(0, graph.edges());
	}
	
	@Test
	public void numberOfEgdesInGraph() {
		Graph<Double> g = new StandardGraph<Double>();
		
		g.add(1.0);
		g.add(2.0);
		g.add(3.0);
		g.add(4.0);
		
		g.addEdge(1.0, 2.0);
		g.addEdge(2.0, 3.0);
		g.addEdge(3.0, 4.0);
		g.addEdge(2.0, 4.0);
		g.addEdge(1.0, 4.0);
		
		assertEquals(5, g.edges());
	}
	
	@Test
	public void numberOfVerticies() {
		Graph<Double> g = new StandardGraph<Double>();
		g.add(1.0);
		g.add(2.0);
		g.add(3.0);
		g.add(4.0);
		g.add(5.0);
		
		assertEquals(5, g.vertices());
	}
	
	@Test
	public void connections() {
		Graph<Double> g = new StandardGraph<Double>();
		g.add(1.0);
		g.add(2.0);
		g.add(4.0);
	}
	
	@Test
	public void connectVertecies() {
		Graph<Double> g = new StandardGraph<Double>();
		g.add(1.0);
		g.add(2.0);
		
		assertFalse(g.isConnected(1.0, 2.0));
		
		g.addEdge(1.0, 2.0);

		assertTrue(g.isConnected(1.0, 2.0));
	}
	
	@Test
	public void simpleDistanceToVertex() {
		StandardGraph<Double> g = new StandardGraph<Double>();
		g.add(1.0);
		g.add(2.0);
		g.addEdge(1.0, 2.0);
		
		assertEquals(1.0, g.distance(1.0, 2.0), 9);
	}

}
