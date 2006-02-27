/*
 * GraphCreationFactory.java
 *
 * Created on Jul 8, 2004
 *
 * Copyright (C) 2003, 2004 - CIRG@UP 
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science 
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.ACO;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import net.sourceforge.cilib.Container.Graph.Edge;
import net.sourceforge.cilib.Container.Graph.Graph;
import net.sourceforge.cilib.Container.Graph.Vertex;
import net.sourceforge.cilib.Problem.DataSet;

/**
 * @author gpampara
 */
public class GraphCreationFactory {
	private Graph graph;
	
	public GraphCreationFactory() {
		System.out.println("Created the needed factory");
		graph = new Graph();
	}
	
	/**
	 * 
	 * @return
	 */
	public Graph createStaticGraph() {
		Vertex v1 = new Vertex("1");
		Vertex v2 = new Vertex("2");
		Vertex v3 = new Vertex("3");
		Vertex v4 = new Vertex("4");
		Vertex v5 = new Vertex("5");
		Vertex v6 = new Vertex("6");

		Edge e1 = new Edge(v1, v2, 1.0);
		Edge e2 = new Edge(v2, v3, 1.5);
		Edge e3 = new Edge(v3, v4, 2.0);
		Edge e4 = new Edge(v2, v4, 2.5);
		Edge e5 = new Edge(v3, v1, 3.0);
		Edge e6 = new Edge(v4, v5, 14.0);
		Edge e7 = new Edge(v5, v1, 11.0);
		Edge e8 = new Edge(v5, v6, 33.0);
		Edge e9 = new Edge(v6, v4, 12.0);

		graph.addVertex(v1);
		graph.addVertex(v2);
		graph.addVertex(v3);
		graph.addVertex(v4);
		graph.addVertex(v5);
		graph.addVertex(v6);

		graph.addEdge(e1);
		graph.addEdge(e2);
		graph.addEdge(e3);
		graph.addEdge(e4);
		graph.addEdge(e5);
		graph.addEdge(e6);
		graph.addEdge(e7);
		graph.addEdge(e8);
		graph.addEdge(e9);

		return graph;
	}
	
	/**
	 * 
	 * @param filename
	 * @return
	 */
	public Graph createGraphFromFile(String filename) {
		System.out.println("reading the graph from " + filename);
		ArrayList tmpList = new ArrayList();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line = "";
			
			while (line != null) {
				line = reader.readLine();
				if (line == null) break;
				
				//System.out.println(line);
				StringTokenizer tokenizer = new StringTokenizer(line);
				
				while (tokenizer.hasMoreElements()) {
					tmpList.add(tokenizer.nextElement());
				}
			}
		}
		catch (FileNotFoundException f) {
			throw new RuntimeException(f.getMessage());
		}
		catch (EOFException e) {
			System.out.println("Done reading the input file...");
		}
		catch (IOException i) {
			throw new RuntimeException(i.getMessage());
		}
		
		return calculateGraph(tmpList);
	}
	
	/**
	 * @param dataSet
	 * @return
	 */
	public Graph createGraphFromDataSet(DataSet dataSet) {
		ArrayList tmpList = new ArrayList();
		BufferedReader reader = new BufferedReader(new InputStreamReader(dataSet.getInputStream()));
		
		String line = "";
		
		try {
			while (line != null) {
				line = reader.readLine();
				if (line == null) break;
			
				//	System.out.println(line);
				StringTokenizer tokenizer = new StringTokenizer(line);
			
				while (tokenizer.hasMoreElements()) {
					tmpList.add(tokenizer.nextElement());
				}
			}
		}
		catch (IOException i) {
			throw new RuntimeException(i.getMessage()); 
		}
		
		return calculateGraph(tmpList);
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	private Graph calculateGraph(ArrayList list) {
		System.out.println("Calculating Graph...");
		int tmpSize = list.size() / 3;
		
		for (int i = 0; i < tmpSize; i++) {
	 		Vertex v = new Vertex(new Integer(i).toString());
	 		graph.addVertex(v);
	 		System.out.println("Verticies: " + graph.getNumberOfVertices());
	 		//vertexSet.add(v);
	 	}

	    for ( int i = 1; i < tmpSize; i++) {
	    	for (int j = i+1; j <= tmpSize; j++) {
	    		int x1 = Integer.parseInt((String) list.get((i-1)*3+1));
	    		int y1 = Integer.parseInt((String) list.get((i-1)*3+2));
	    		int x2 = Integer.parseInt((String) list.get((j-1)*3+1));
	    		int y2 = Integer.parseInt((String) list.get((j-1)*3+2));
	    		double d = distance2D(x1, y1, x2, y2);
	    		Edge edge = new Edge(graph.getVertexAt(i-1), graph.getVertexAt(j-1), d);
	    		//System.out.println(i*j + " " + edge);
	    		//edgeSet.add(edge);
	    		graph.addEdge(edge);
	    		//System.out.println(edge);
	    	}
	    }
		return graph; 
	}
	
	
	/**
	 * Calculate the required Euclidean Distance in a 2D space, given the coordinated of 2 verticies in the
	 * search space.
	 * 
	 * Uses the formula:
	 * 		result = sqrt((x2-x1)^2 + (y2-y1)^2)
	 *  
	 * @param x1 The x cordinate if the first coordinate pairs
	 * @param y1 The y cordinate if the first coordinate pairs
	 * @param x2 The x cordinate if the second coordinate pairs
	 * @param y2 The y cordinate if the second coordinate pairs
	 * @return The 2D Euclidean distance between the 2 coordinates
	 */
	private double distance2D(double x1, double y1, double x2, double y2) {
		double xComponent = x2 - x1;
		double yComponent = y2 - y1;
		return (Math.sqrt( (xComponent*xComponent) + (yComponent*yComponent) ) );
	}
}
