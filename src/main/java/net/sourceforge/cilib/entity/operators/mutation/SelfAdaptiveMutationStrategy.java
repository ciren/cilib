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

package net.sourceforge.cilib.entity.operators.mutation;

import java.util.List;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.topologies.TopologyHolder;
import net.sourceforge.cilib.math.random.RandomNumber;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author gpampara
 */
public class SelfAdaptiveMutationStrategy extends MutationStrategy {
    private static final long serialVersionUID = -8942505730267916237L;

    private RandomNumber randomSingle;
    private RandomNumber randomDimension;
    private double tau;
    private double tauPrime;

    public SelfAdaptiveMutationStrategy() {
        this.randomSingle = new RandomNumber();
        this.randomDimension = new RandomNumber();
        this.tau = Double.NaN;
        this.tauPrime = Double.NaN;
    }

    @Override
    public MutationStrategy getClone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mutate(List<? extends Entity> offspringList) {
        initializeConstants(offspringList);

        double pre = tauPrime*randomSingle.getGaussian();
        
        for (Entity offspring : offspringList) {
            Vector candidateSolution = (Vector) offspring.getCandidateSolution();
            Vector strategy = (Vector) offspring.getProperties().get(EntityType.STRATEGY_PARAMETERS);

            // Update the offspring
            for (int i = 0; i < candidateSolution.size(); i++) {
                double value = candidateSolution.getReal(i) + strategy.getReal(i)*randomDimension.getGaussian();
                candidateSolution.setReal(i, value);
            }

            // Update the strategy parameters
            for (Numeric n : strategy) {
                double exponent = pre + tau*randomDimension.getGaussian();
                double value = n.getReal() * Math.exp(exponent);
                n.setReal(value);
            }
        }
    }

    @Override
    public void performOperation(TopologyHolder holder) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void initializeConstants(List<? extends Entity> offspringList) {
        Entity first = offspringList.get(0);
        
        if (Double.compare(tau, Double.NaN) == 0) {
            tau = 1.0 / (Math.sqrt(2 * Math.sqrt(first.getDimension())));
            tauPrime = 1.0 / Math.sqrt(2*first.getDimension());
            System.out.println("tau: " + tau + " tauPrime: " + tauPrime);
        }
    }

}
