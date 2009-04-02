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
package net.sourceforge.cilib.problem.dataset;

import java.util.ArrayList;

import net.sourceforge.cilib.math.Stats;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.ClusteringUtils;

/**
 * This class "collects" and holds all the patterns of the {@link DataSet}s specified
 * through the {@link #addDataSet(DataSet)} method. It was originally the <code>AssociatedPairDataSetBuilder</code>
 * class, but has since changed quite a lot. Most of the functionality is now handled by the {@link ClusteringUtils}
 * helper class.
 *
 * @author Gary Pampara
 * @author Theuns Cloete
 */
public class StaticDataSetBuilder extends DataSetBuilder {
    private static final long serialVersionUID = -7035524554252462144L;

    protected ArrayList<Pattern> patterns;
    protected Vector cachedMean;
    protected double cachedVariance;
    protected double[] distanceCache;
    protected String identifier;
    protected boolean cached;

    /**
     * Initialise the patterns data structure and set the identifier to be blank.
     */
    public StaticDataSetBuilder() {
        this.patterns = new ArrayList<Pattern>();
        this.identifier = "<unknown built data set>";
        this.cached = true;
    }

    public StaticDataSetBuilder(StaticDataSetBuilder rhs) {
        super(rhs);
        this.patterns = new ArrayList<Pattern>();

        for (Pattern pattern : rhs.patterns) {
            this.patterns.add(pattern.getClone());
        }

        this.identifier = rhs.identifier;
        this.cached = rhs.cached;
    }

    @Override
    public StaticDataSetBuilder getClone() {
        return new StaticDataSetBuilder(this);
    }

    /**
     * This method has three responsibilities:
     * <ol>
     * <li>It takes into account the fact that datasets may already have been parsed by
     * other {@link Simulation}s, {@link Problem}s or {@link Thread}s. It relies on the
     * {@link DataSetManager} singleton to parse and/or retrieve the patterns of the given
     * {@link DataSet}. Then it adds these retrieved patterns to the current
     * {@link #patterns} list. This method also builds up the {@link #identifier} that
     * uniquely identifies this dataset builder. This identifier is used by the
     * {@link DataSetManager} to keep track of this built-up dataset, because it might be
     * used by other {@link Simulation}s, {@link Problem}s or {@link Thread}s as well.</li>
     * <li>It caches both the mean and variance of the built-up data set.</li>
     * <li>It caches the distances from all the patterns to all the other patterns in the
     * built-up data set.</li>
     * </ol>
     *
     * @throws IllegalArgumentException when the given {@link DataSet} is not a
     *         {@link LocalDataSet}. This is only temporary, because I didn't want to change
     *         the more generic {@link DataSet} too much.
     * @throws IllegalArgumentException when the patterns that are currently being added have
     *         different dimensions than the patterns that have already been collected/built
     *         (those in {@link #patterns}).
     * @param ds the {@link DataSet} that represents a dataset that should be used when
     *        building up the list of patterns that should be clustered
     */
    @Override
    public void initialise() {
        for (DataSet dataset : this.dataSets) {
            ArrayList<Pattern> data = DataSetManager.getInstance().getDataFromSet(dataset);

            if (!patterns.isEmpty() && data.get(0).data.getDimension() != patterns.get(0).data.getDimension()) {
                throw new IllegalArgumentException("Cannot combine datasets of different dimensions");
            }
            patterns.addAll(data);
            System.out.println(data.size() + " patterns added");
        }

        this.cacheMeanAndVariance();
        if (this.cached) {
            this.cacheDistances();
        }
    }

    /**
     * Calculate and cached the mean ({@link Vector}) and variance (scalar) of the dataset.
     */
    private void cacheMeanAndVariance() {
        System.out.println("Caching dataset mean and variance");
        cachedMean = Stats.meanVector(patterns);
        System.out.println("Cached mean: " + cachedMean);
        cachedVariance = Stats.variance(patterns, cachedMean);
        System.out.println("Cached variance: " + cachedVariance);
    }

    /**
     * Get the cached mean {@link Vector}.
     *
     * @return the {@link #cachedMean}
     */
    public Vector getMean() {
        return cachedMean;
    }

    /**
     * Get the cached variance (scalar).
     *
     * @return the {@link #cachedVariance}
     */
    public double getVariance() {
        return cachedVariance;
    }

    /**
     * Calculate and cache the distances from all patterns to all other patterns. The cached
     * structure looks like this (x represents a distance):
     *   0 1 2 3 4 5
     * 0 0 x x x x x
     * 1   0 x x x x
     * 2     0 x x x
     * 3       0 x x
     * 4         0 x
     * 5           0
     * Although it seems like a 2D structure, it is a 1D array. The zero-values are not
     * stored, because zero can be returned when the indices are equal. Only the x values are
     * stored.
     */
    private void cacheDistances() {
        System.out.println("Caching distances between patterns of dataset");
        ClusteringUtils helper = ClusteringUtils.get();
        int numPatterns = getNumberOfPatterns();
        int cacheSize = (numPatterns * (numPatterns - 1)) / 2;
        distanceCache = new double[cacheSize];
        int index = 0;
        int jump = 0;

        for (int y = 0; y < numPatterns - 1; y++) {
            Vector rhs = patterns.get(y).data;
            for (int x = y + 1; x < numPatterns; x++) {
                Vector lhs = patterns.get(x).data;
                index = x + (numPatterns * y) - (((y + 1) * (y + 2)) / 2);
                distanceCache[index] = helper.calculateDistance(lhs, rhs);
            }

            int percentageComplete = 100 * index / cacheSize;
            if (percentageComplete == jump) {
                System.out.println(percentageComplete + "% of distances cached");
                jump += 10;
            }
        }
    }

    /**
     * Retrieve the cached distance between the given patterns.
     *
     * @throws IllegalArgumentException when either <code>x</code> or <code>y</code> is
     *         negative.
     * @param x index of the one pattern
     * @param y index of the other pattern
     * @return the cached distance between the two given patterns
     */
    public double getCachedDistance(int x, int y) {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("No pattern at (" + x + ", " + y + ")");
        }

        if (x == y) {
            return 0.0;
        }

        if (y > x) {
            return getCachedDistance(y, x); // use recursion to swap the x and y index
        }
        return distanceCache[x + (getNumberOfPatterns() * y) - (((y + 1) * (y + 2)) / 2)];
    }

    /**
     * Get all the patterns in this constructed/combined/built dataset.
     *
     * @return the {@link #patterns} list
     */
    public ArrayList<Pattern> getPatterns() {
        return patterns;
    }

    /**
     * Get the pattern that is represented by the given index.
     *
     * @param i the index representing a pattern in the {@link #patterns}
     * @return pattern i of {@link #patterns}
     */
    public Pattern getPattern(int i) {
        return patterns.get(i);
    }

    /**
     * Determine how many patterns are in this constructed/combined/built dataset.
     *
     * @return the number of patterns/elements in {@link #patterns}
     */
    public int getNumberOfPatterns() {
        return patterns.size();
    }

    /**
     * Get the identifier that uniquely identifies this constructed/combined/built data set. The returned string is
     * created on the fly that lists all the {@link DataSet data sets} that comprises this built data set.
     *
     * @return the {@link #identifier}
     */
    public String getIdentifier() {
        String identifiedAs = this.identifier + " = {";

        for (int i = 0; i < this.dataSets.size(); ++i) {
            identifiedAs += this.dataSets.get(i).getIdentifier();

            if (i < this.dataSets.size() - 1) {
                identifiedAs += ",";
            }
        }
        return identifiedAs + "}";
    }

    /**
     * Set the identifier that will initially uniquely identify this constructed/combined/built dataset that should
     * still be constructed.
     *
     * @param id the identifying string that should be used.
     */
    public void setIdentifier(String id) {
        this.identifier = id;
    }

    /**
     * Checker whether the distances between every pattern (in this built-up data set) were cached?
     * @return true/false
     */
    public boolean getCached() {
        return this.cached;
    }

    /**
     * Sets whether the distances between every pattern (in this built-up data set) should be cached.
     * @param c true/false
     */
    public void setCached(boolean c) {
        this.cached = c;
    }
}
