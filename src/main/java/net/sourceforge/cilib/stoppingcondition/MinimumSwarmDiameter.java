/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.stoppingcondition;

import net.sourceforge.cilib.entity.visitor.DiameterVisitor;
import net.sourceforge.cilib.pso.PSO;

/**
 * @author Edwin Peer
 */
public class MinimumSwarmDiameter implements StoppingCondition<PSO> {
    private static final long serialVersionUID = -1570485054918077401L;

    private double minimumSwarmDiameter;

    /** Creates a new instance of MinimumSwarmDiameterIndicator. */
    public MinimumSwarmDiameter() {
        minimumSwarmDiameter = 0.0001;
    }

    public MinimumSwarmDiameter(double minimumSwarmDiameter) {
        this.minimumSwarmDiameter = minimumSwarmDiameter;
    }

    public void setDiameter(double minimumSwarmDiameter) {
        this.minimumSwarmDiameter = minimumSwarmDiameter;
    }

    public double getDiameter() {
        return minimumSwarmDiameter;
    }

    @Override
    public double getPercentageCompleted(PSO algorithm) {
        DiameterVisitor diameterVisitor = new DiameterVisitor();
        algorithm.accept(diameterVisitor);
        double diameter = diameterVisitor.getResult();

        if (diameter <= minimumSwarmDiameter) {
            return 1;
        }
        return minimumSwarmDiameter / diameter;
    }

    @Override
    public boolean apply(PSO input) {
        DiameterVisitor diameterVisitor = new DiameterVisitor();
        input.accept(diameterVisitor);
        return (diameterVisitor.getResult() <= minimumSwarmDiameter);
    }

}
