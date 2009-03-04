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

package net.sourceforge.cilib.pso.niching;

import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.visitor.RadiusVisitor;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author gpampara
 */
public class StandardMergeStrategy implements MergeStrategy {

    private double threshold = 10e-3;

    public StandardMergeStrategy() {
    }

    @Override
    public void merge(MultiPopulationBasedAlgorithm algorithm) {
        RadiusVisitor radiusVisitor = new RadiusVisitor();

        for (int i = 0; i < algorithm.getPopulations().size(); i++) {
            PopulationBasedAlgorithm k1 = algorithm.getPopulations().get(i);

            k1.accept(radiusVisitor);
            double k1Radius = ((Double) radiusVisitor.getResult()).doubleValue();

            for (int j = 0; j < algorithm.getPopulations().size(); j++) {
                PopulationBasedAlgorithm k2 = algorithm.getPopulations().get(j);

                k2.accept(radiusVisitor);
                double k2Radius = ((Double) radiusVisitor.getResult()).doubleValue();
                Vector vectorK1 = (Vector) k1.getTopology().getBestEntity().getCandidateSolution();
                Vector vectorK2 = (Vector) k2.getTopology().getBestEntity().getCandidateSolution();

                Vector normalK1 = vectorK1.normalize();
                Vector normalK2 = vectorK2.normalize();

                double distance = Math.abs(normalK1.subtract(normalK2).norm());

                if (k1Radius == k2Radius && k1Radius == 0) {
                    if (distance < threshold)
                        mergeSwarms(algorithm, k1, k2);

                    continue;
                }

                if (distance < (k1Radius + k2Radius)) {
                    mergeSwarms(algorithm, k1, k2);
                }
            }
        }
    }

    private void mergeSwarms(MultiPopulationBasedAlgorithm algorithm, PopulationBasedAlgorithm k1, PopulationBasedAlgorithm k2) {
        // migrate all entities from k2 into k1
        for (int i = 0; i < k2.getTopology().size(); i++) {
            Particle p = (Particle) k2.getTopology().get(i);

            Topology<Particle> topology = (Topology<Particle>) k1.getTopology();
            topology.add(p);

            p.setNeighbourhoodBest(topology.getBestEntity());
        }

        algorithm.getPopulations().remove(k2);
    }

}
