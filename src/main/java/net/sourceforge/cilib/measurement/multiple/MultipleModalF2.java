/*
 * Copyright (C) 2003 - 2008
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
package net.sourceforge.cilib.measurement.multiple;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.visitor.RadiusVisitor;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.pso.NichePSO;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * 
 * @author Edrich van Loggerenberg
 */
public class MultipleModalF2 implements Measurement {
	private static final long serialVersionUID = 7069410059006587069L;

	public MultipleModalF2() {
	}

	public MultipleModalF2(MultipleModalF1 copy) {
	}

	public MultipleModalF1 getClone() {
		return new MultipleModalF1(this);
	}

	public String getDomain() {
		return "T";
	}

	public Type getValue() {
		NichePSO nichePSO = (NichePSO) Algorithm.get();
		
		System.out.println("set pointsize 3.0");
		System.out.println("set xrange [0:1]");
		System.out.println("f(x) = (sin(5*pi*x)**6)*exp(-2*log(2)*((x-0.1)/0.8)**2)");
		System.out.print("plot f(x), '-' title 'mainSwarm'");
		for (int i = 0; i < nichePSO.getSubSwarms().size(); ++i) {
			System.out.print(", '-' title 'subSwarm" + i + "' with points");
			System.out.print(", '-' title 'subSwarmRadius" + i + "' with lines");
		}
		
		System.out.println();
	
//		Vector v = new Vector();
		for (Particle main : nichePSO.getMainSwarm().getTopology()) {
			Vector v = (Vector) main.getPosition();
			System.out.println(v.toString('\0', '\0', ' ')+"\t"+function(v));
		}
		
		System.out.println("e");
		int count = 0;
		for (PopulationBasedAlgorithm subSwarm : nichePSO.getSubSwarms()) {
			for (Entity paricle : subSwarm.getTopology()) {
				Vector v = (Vector) paricle.getContents();
				System.out.println(v.toString('\0', '\0', ' ')+"\t"+function(v));
			}
			System.out.println("e");
			
			RadiusVisitor visitor = new RadiusVisitor();
			subSwarm.accept(visitor);
			System.out.println(((0.1+count++)/10.0)+"\t"+"0.0");
			System.out.println(((0.1+count++)/10.0)+"\t"+visitor.getResult());
			System.out.println("e");
		}
		
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return new Real(0);
		
		
		
	/*	Vector v = new Vector();
		Collection<OptimisationSolution> p = (Algorithm.get()).getSolutions();
		
		Hashtable<Double, Vector> solutionsFound = new Hashtable<Double, Vector>();
		ArrayList<Double> opt = new ArrayList<Double>();
		opt.add(0.1);
		opt.add(0.3);
		opt.add(0.5);
		opt.add(0.7);
		opt.add(0.9);
		
		for (Iterator<OptimisationSolution> i = p.iterator(); i.hasNext();) {
			Vector solution = (Vector) i.next().getPosition();
			for (int count = 0; count < opt.size(); count++) {
				double sol = (Double) opt.get(count);
				if (testNear(solution.getReal(0), sol)) {
					if (!solutionsFound.containsKey(sol))
						solutionsFound.put(sol, solution);
					break;
				}
			}
		}
		
		Enumeration<Double> sols = solutionsFound.keys();
		while (sols.hasMoreElements()) {
			// double k = (Double)sols.nextElement();
			Vector s = (Vector) solutionsFound.get(sols.nextElement());
			v.append(s);
			StringType t = new StringType();
			t.setString(String.valueOf(computeDerivative(s.getReal(0))));
			v.append(t);
		}
		
		return v;*/
	}
	
	private double function(Vector x) {
		double r = x.getReal(0);
		return (Math.pow(Math.sin(5*Math.PI*r), 6))*Math.exp(-2*Math.log(2.0)*(Math.pow(((r-0.1)/0.8), 2.0)));
	}

//	private boolean testNear(double solution, double val) {
//		if (val >= (solution - 0.05) && val <= (solution + 0.05))
//			return true;
//		
//		return false;
//	}

//	private double computeDerivative(double x) {
//		double k = (Math.log(0.25)) / 0.64;
//		double gx = k * Math.pow((x - 0.1), 2.0);
//		double dgx = 2.0 * k * (x - 0.1);
//		double df1 = 30.0 * Math.PI * Math.pow(Math.sin(5.0 * Math.PI * x), 5.0) * Math.cos(5.0 * Math.PI * x);
//		double df2 = Math.pow(Math.sin(5.0 * Math.PI * x), 6.0) * dgx * Math.exp(gx) + Math.exp(gx) * df1;
//		
//		return df2;
//	}
}
