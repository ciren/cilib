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
package cilib.algorithm;

import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.algorithm.Stoppable;
import net.sourceforge.cilib.stoppingcondition.StoppingCondition;

/**
 *
 * @author gpampara
 */
public class StoppableAlgorithm implements Algorithm, Stoppable {

    private final Algorithm algorithm;
    private final List<StoppingCondition<net.sourceforge.cilib.algorithm.Algorithm>> stoppingConditions;

    public StoppableAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
        this.stoppingConditions = Lists.newArrayList();
    }

    @Override
    public void performIteration() {
        this.algorithm.performIteration();
    }

    @Override
    public void addStoppingCondition(StoppingCondition<net.sourceforge.cilib.algorithm.Algorithm> condition) {
        this.stoppingConditions.add(condition);
    }

    @Override
    public void removeStoppingCondition(StoppingCondition<net.sourceforge.cilib.algorithm.Algorithm> condition) {
        this.stoppingConditions.remove(condition);
    }

    boolean isFinished() {
        boolean result = true;
        for (StoppingCondition<net.sourceforge.cilib.algorithm.Algorithm> condition : stoppingConditions) {
            if (!condition.apply(null)) {
                return false;
            }
        }
        return result;
    }
}
