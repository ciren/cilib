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
package net.sourceforge.cilib.stoppingcondition;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.entity.visitor.DiameterVisitor;
import net.sourceforge.cilib.pso.PSO;

/**
 * @author Edwin Peer
 */
public class MinimumSwarmDiameter implements StoppingCondition {
    private static final long serialVersionUID = -1570485054918077401L;

    double minimumSwarmDiameter;
    PSO algorithm;

    /** Creates a new instance of MinimumSwarmDiameterIndicator. */
    public MinimumSwarmDiameter() {
        minimumSwarmDiameter = 0.0001;
    }

    public MinimumSwarmDiameter(double minimumSwarmDiameter) {
        this.minimumSwarmDiameter = minimumSwarmDiameter;
    }

    public MinimumSwarmDiameter(MinimumSwarmDiameter copy) {
        this.minimumSwarmDiameter = copy.minimumSwarmDiameter;
        this.algorithm = copy.algorithm;
    }

    public MinimumSwarmDiameter getClone() {
        return new MinimumSwarmDiameter(this);
    }

    public void setDiameter(double minimumSwarmDiameter) {
        this.minimumSwarmDiameter = minimumSwarmDiameter;
    }

    public double getDiameter() {
        return minimumSwarmDiameter;
    }

    public double getPercentageCompleted() {
        DiameterVisitor diameterVisitor = new DiameterVisitor();
        algorithm.accept(diameterVisitor);
        double diameter = diameterVisitor.getResult();

        if (diameter <= minimumSwarmDiameter) {
            return 1;
        }
        return minimumSwarmDiameter / diameter;
    }

    public boolean isCompleted() {
        DiameterVisitor diameterVisitor = new DiameterVisitor();
        algorithm.accept(diameterVisitor);
        return (diameterVisitor.getResult() <= minimumSwarmDiameter);
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = (PSO) algorithm;
    }
}
