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
package net.sourceforge.cilib.clustering.kmeans;

import com.google.common.collect.Lists;

import java.util.List;

import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * Initialises each centroid with probability proportional to its contribution to the overall potential.
 * Note that no parameter tuning is needed.
 *
 * @InProceedings{ 2007.Arthur.jan, title = "k-means++: The Advantages of Careful Seeding",
 *                 booktitle = "In Proceedings of the eighteenth annual ACM-SIAM symposium on Discrete Algorithms",
 *                 series = "Symposium on Discrete Algorithms", author = "David Arthur and Sergei Vassilvitskii",
 *                 publisher = "Society for Industrial and Applied Mathematics", location = "New Orleans, Louisiana",
 *                 address = "Philadelphia, PA, USA", pages = "1027--1035", month = jan, year = "2007",
 *                 isbn = "978-0-898716-24-5"}
 *
 * @author Theuns Cloete
 */
public class ContributingPotentialCentroidsInitialisationStrategy implements CentroidsInitialisationStrategy {
    private static final long serialVersionUID = -3956593150571608857L;

    private RandomProvider randomPattern;
    private RandomProvider randomProbability;

    public ContributingPotentialCentroidsInitialisationStrategy() {
        this.randomPattern = new MersenneTwister();
        this.randomProbability = new MersenneTwister();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContributingPotentialCentroidsInitialisationStrategy getClone() {
        return new ContributingPotentialCentroidsInitialisationStrategy();
    }

    /**
     * Initialise the centroids as explained in Section 2.2 of the referenced article.
     * {@inheritDoc}
     */
    @Override
    public List<Vector> initialise(DataTable<StandardPattern, TypeList> dataTable, DomainRegistry domainRegistry, DistanceMeasure distanceMeasure, int numberOfCentroids) {
        List<Vector> chosenCentroids = Lists.newArrayListWithCapacity(numberOfCentroids);

        for (int i = 0; i < numberOfCentroids; ++i) {
            Vector candidateCentroid = Vector.copyOf(dataTable.getRow(randomPattern.nextInt(dataTable.size())).getVector());

            if (i > 0) {
                while (this.randomProbability.nextDouble() >= this.calculateProbability(dataTable, distanceMeasure, chosenCentroids, candidateCentroid)) {
                    candidateCentroid = Vector.copyOf(dataTable.getRow(randomPattern.nextInt(dataTable.size())).getVector());
                }
            }
            chosenCentroids.add(candidateCentroid);
        }
        return chosenCentroids;
    }

    /**
     * Remove the unwanted centroid and replace it with a newly chosen centroid, still based on its contribtution to the
     * overall potential.
     * {@inheritDoc}
     */
    @Override
    public Vector reinitialise(List<Vector> centroids, DataTable<StandardPattern, TypeList> dataTable, DomainRegistry domainRegistry, DistanceMeasure distanceMeasure, int which) {
        Vector candidateCentroid = null;

        do {
            candidateCentroid = Vector.copyOf(dataTable.getRow(randomPattern.nextInt(dataTable.size())).getVector());
        }
        while (this.randomProbability.nextDouble() >= this.calculateProbability(dataTable, distanceMeasure, centroids, candidateCentroid));

        centroids.set(which, candidateCentroid);
        return candidateCentroid;
    }

    private double calculateProbability(DataTable<StandardPattern, TypeList> dataTable, DistanceMeasure distanceMeasure, List<Vector> chosenCentroids, Vector candidateCentroid) {
        double probability = 0.0;
        double numerator = Double.MAX_VALUE;

        for (Vector chosenCentroid : chosenCentroids) {
            numerator = Math.min(numerator, distanceMeasure.distance(candidateCentroid, chosenCentroid));
        }

        for (StandardPattern pattern : dataTable) {
            double denominator = Double.MAX_VALUE;

            for (Vector chosenCentroid : chosenCentroids) {
                denominator = Math.min(denominator, distanceMeasure.distance(chosenCentroid, pattern.getVector()));
            }
            probability += denominator;
        }
        return numerator / probability;
    }
}
