/**
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

package net.sourceforge.cilib.moo.archive.constrained;

import java.util.Collection;

import net.sourceforge.cilib.moo.archive.Archive;
import net.sourceforge.cilib.problem.OptimisationSolution;

/**
 * <p>
 * An {@link Archive} constrained by the number of solutions that it can store.
 * </p>
 *
 * @author Wiehann Matthysen
 */
public abstract class ConstrainedArchive extends Archive {
	
	private int capacity;
	
	public ConstrainedArchive() {
		this.capacity = 1000;
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public int getCapacity() {
		return this.capacity;
	}
	
	@Override
	public void accept(Collection<OptimisationSolution> candidateSolutions) {
		// For each candidate solution, try to add it to the archive.
		for (OptimisationSolution candidateSolution : candidateSolutions) {
			accept(candidateSolution);
		}
		// If the size of the archive is too large then invoke pruning method.
		if (size() > getCapacity()) {
			prune();
		}
	}
	
	protected void accept(OptimisationSolution candidateSolution) {
		// If no solution in the archive dominates the candidate solution then proceed...
		if (this.dominates(candidateSolution).size() == 0) {
			// Remove all the solutions in the archive that is dominated by the candidate solution.
			removeAll(this.isDominatedBy(candidateSolution));
			// Add the candidate solution to the archive.
			add(candidateSolution);
		}
	}

    /**
     * This method needs to be implemented by all subclasses to provide a way
     * in which certain solutions will be removed if the archive grows too big.
     */
	protected abstract void prune();
}
