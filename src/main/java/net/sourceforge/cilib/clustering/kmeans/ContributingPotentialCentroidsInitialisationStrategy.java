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

import java.util.ArrayList;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.problem.ClusteringProblem;
import net.sourceforge.cilib.problem.dataset.StaticDataSetBuilder;
import net.sourceforge.cilib.type.types.container.Pattern;
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
    private static final long serialVersionUID = -1475341727508334776L;
    private ArrayList<Pattern<Vector>> patterns;
    private DistanceMeasure distanceMeasure;

    public ContributingPotentialCentroidsInitialisationStrategy() {
        patterns = null;
        distanceMeasure = null;
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
    public ArrayList<Vector> initialise(ClusteringProblem problem, StaticDataSetBuilder dataset) {
        RandomProvider randomPattern = new MersenneTwister();
        RandomProvider randomProbability = new MersenneTwister();
        int numberOfClusters = problem.getNumberOfClusters();

        this.patterns = dataset.getPatterns();
        this.distanceMeasure = problem.getDistanceMeasure();
        ArrayList<Vector> chosenCentroids = new ArrayList<Vector>();

        for (int i = 0; i < numberOfClusters; ++i) {
            Vector candidateCentroid = Vector.copyOf(patterns.get(randomPattern.nextInt(patterns.size())).getData());

            if (i > 0) {
                while (randomProbability.nextDouble() >= this.calculateProbability(chosenCentroids, candidateCentroid)) {
                    candidateCentroid = Vector.copyOf(patterns.get(randomPattern.nextInt(patterns.size())).getData());
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
    public Vector reinitialise(ArrayList<Vector> centroids, int which) {
        Vector candidateCentroid = null;
        RandomProvider randomPattern = new MersenneTwister();
        RandomProvider randomProbability = new MersenneTwister();

        do {
            candidateCentroid = Vector.copyOf(patterns.get(randomPattern.nextInt(patterns.size())).getData());
        }
        while (randomProbability.nextDouble() >= this.calculateProbability(centroids, candidateCentroid));

        centroids.set(which, candidateCentroid);
        return candidateCentroid;
    }

    private double calculateProbability(ArrayList<Vector> chosenCentroids, Vector candidateCentroid) {
        double probability = 0.0;
        double numerator = Double.MAX_VALUE;

        for (Vector chosenCentroid : chosenCentroids) {
            numerator = Math.min(numerator, this.distanceMeasure.distance(candidateCentroid, chosenCentroid));
        }

        for (Pattern pattern : this.patterns) {
            double denominator = Double.MAX_VALUE;

            for (Vector chosenCentroid : chosenCentroids) {
                denominator = Math.min(denominator, this.distanceMeasure.distance(chosenCentroid, pattern.getData()));
            }
            probability += denominator;
        }
        return numerator / probability;
    }
}
