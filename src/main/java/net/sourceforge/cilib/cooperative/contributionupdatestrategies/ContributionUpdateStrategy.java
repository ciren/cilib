/**
 * Copyright (C) 2003 - 2009
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
package net.sourceforge.cilib.cooperative.contributionupdatestrategies;

import net.sourceforge.cilib.cooperative.CooperativeEntity;
import net.sourceforge.cilib.entity.Entity;

/**
 * TODO: Complete this javadoc.
 */
public interface ContributionUpdateStrategy {
    //TODO this class will need the following things:
    //1. The entity (which stores it's own fitness)
    //2. The participants
    public void updateContribution(Entity src, int srcPos, CooperativeEntity dst, int dstPos, int length);
}
