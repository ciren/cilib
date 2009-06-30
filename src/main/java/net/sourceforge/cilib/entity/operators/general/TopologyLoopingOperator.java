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
package net.sourceforge.cilib.entity.operators.general;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.operators.Operator;
import net.sourceforge.cilib.entity.topologies.TopologyHolder;

/**
 * TODO: Complete this javadoc.
 */
public class TopologyLoopingOperator implements Operator {
    private static final long serialVersionUID = 5726039375836229914L;
    private Operator operator;

    public TopologyLoopingOperator() {

    }

    public TopologyLoopingOperator(TopologyLoopingOperator copy) {
        this.operator = copy.operator.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TopologyLoopingOperator getClone() {
        return new TopologyLoopingOperator(this);
    }

    /**
     * Perform the {@code TopologyLoopingOperator} by looping over the topology and
     * delegate the operation to the wrapped operator.
     * @param topology The {@linkplain Topology} to perform the operations on.
     * @param offspring The {@linkplain Topology} of offspring individuals.
     */
    @Override
//    public void performOperation(Topology<? extends Entity> topology, Topology<Entity> offspring) {
    public void performOperation(TopologyHolder holder) {
        if (operator == null)
            throw new RuntimeException("Cannot perform a loop over the topology. The operator to apply has not been defined");

        Topology<? extends Entity> topology = holder.getTopology();

        for (int i = 0; i < topology.size(); i++)
//            operator.performOperation(topology, offspring);
            operator.performOperation(holder);
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

}
