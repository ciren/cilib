/*
 * Gene.java
 *
 * Created on June 24, 2003, 21:00 PM
 *
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
 *
 */

package net.sourceforge.cilib.EC.EA;

import net.sourceforge.cilib.Domain.Component;
import net.sourceforge.cilib.EC.Mutator.GaussianMutator;
import net.sourceforge.cilib.EC.Mutator.Mutator;

public abstract class Gene { // TODO: Shouldn't this be interface

  protected Mutator mutator = new GaussianMutator();
  public abstract void mutate(double mutationProbability);
  public abstract void initialise(Component domain);
  public abstract double getGeneValue();
  public abstract void setGeneValue(Gene gene);
  public abstract void setMutator(Mutator mutator);
  public abstract Mutator getMutator();
}

