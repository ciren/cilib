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

import java.util.Collection;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Multiple fitness.
 *
 * <p>Title: CILib</p>
 * <p>Description: CILib (Computational Intelligence Library)</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Clive Naicker
 * @version 1.0
 * @deprecated This class is no longer valid. A combination of the
 *             {@link CompositeMeasurement} and {@link net.sourceforge.cilib.measurement.single.Fitness}
 *             should be used instead
 */
@Deprecated
public class MultipleFitness implements Measurement {
	private static final long serialVersionUID = -255308745515061075L;

	/**
	 * Create an instance of {@linkplain MultipleFitness}.
	 */
	public MultipleFitness() {
	}

	/**
	 * Copy constructor. Create a copy of the provided instance.
	 * @param copy The instance to copy.
	 */
	public MultipleFitness(MultipleFitness copy) {
	}

	/**
	 * {@inheritDoc}
	 */
	public MultipleFitness getClone() {
		return new MultipleFitness(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getDomain() {
		//return "?^N";
		//return "R";
		return "T";
	}

	/**
	 * {@inheritDoc}
	 */
	public Type getValue(Algorithm algorithm) {
		/*Vector<Comparable> fitnessValues = new Vector<Comparable>();
		Collection<OptimisationSolution> solutions = ((OptimisationAlgorithm) Algorithm.get()).getSolutions();
		for (Iterator i=solutions.iterator(); i.hasNext(); ) {
			Comparable fitness = ((OptimisationSolution)i.next()).getFitness().getValue();
			fitnessValues.add(fitness);
		}
		return fitnessValues.toArray();*/

		Vector fitnessValues = new Vector();
		Collection<OptimisationSolution> solutions = algorithm.getSolutions();

		for (OptimisationSolution solution : solutions) {
			Double fitness = solution.getFitness().getValue();
			fitnessValues.add(new Real(fitness));
		}

		return fitnessValues;
	}

}
