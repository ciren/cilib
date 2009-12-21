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

import com.google.common.collect.Sets;

import java.util.Set;

import net.sourceforge.cilib.math.Stats;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.type.types.container.Pattern;
import net.sourceforge.cilib.type.types.container.Vector;

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

    protected Set<Pattern<Vector>> patterns;
    protected Vector cachedMean;
    protected double cachedVariance;
    protected String identifier;

    /**
     * Initialise the patterns data structure and set the identifier to be blank.
     */
    public StaticDataSetBuilder() {
        this.patterns = Sets.newHashSet();
        this.identifier = "<unknown built data set>";
    }

    public StaticDataSetBuilder(StaticDataSetBuilder rhs) {
        super(rhs);
        this.patterns = Sets.newHashSet();

        for (Pattern<Vector> pattern : rhs.patterns) {
            this.patterns.add(pattern.getClone());
        }

        this.identifier = rhs.identifier;
    }

    @Override
    public StaticDataSetBuilder getClone() {
        return new StaticDataSetBuilder(this);
    }

    /**
     * This method has three responsibilities:
     * <ol>
     * <li>It takes into account the fact that datasets may already have been parsed by
     * other simulations, {@link Problem}s or {@link Thread}s. It relies on the
     * {@link DataSetManager} singleton to parse and/or retrieve the patterns of the given
     * {@link DataSet}. Then it adds these retrieved patterns to the current
     * {@link #patterns} list. This method also builds up the {@link #identifier} that
     * uniquely identifies this dataset builder. This identifier is used by the
     * {@link DataSetManager} to keep track of this built-up dataset, because it might be
     * used by other simulations, {@link Problem}s or {@link Thread}s as well.</li>
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
            Set<Pattern<Vector>> data = DataSetManager.getInstance().getDataFromSet(dataset);

            if (!patterns.isEmpty() && data.iterator().next().getData().getDimension() != patterns.iterator().next().getData().getDimension()) {
                throw new IllegalArgumentException("Cannot combine datasets of different dimensions");
            }

            this.patterns.addAll(data);
            System.out.printf("#%d patterns added", data.size());
        }

        this.cacheMeanAndVariance();
    }

    /**
     * Calculate and cached the mean ({@link Vector}) and variance (scalar) of the dataset.
     */
    private void cacheMeanAndVariance() {
        System.out.println("#Caching dataset mean and variance");

        this.cachedMean = Stats.meanVector(this.patterns);
        System.out.println("#Cached mean: " + this.cachedMean);
        this.cachedVariance = Stats.variance(this.patterns, this.cachedMean);
        System.out.println("#Cached variance: " + this.cachedVariance);
    }

    /**
     * Get the cached mean {@link Vector}.
     *
     * @return the {@link #cachedMean}
     */
    public Vector getMean() {
        return this.cachedMean;
    }

    /**
     * Get the cached variance (scalar).
     *
     * @return the {@link #cachedVariance}
     */
    public double getVariance() {
        return this.cachedVariance;
    }

    /**
     * Get all the patterns in this constructed/combined/built dataset.
     *
     * @return the {@link #patterns} list
     */
    public Set<Pattern<Vector>> getPatterns() {
        return this.patterns;
    }

    /**
     * Determine how many patterns are in this constructed/combined/built dataset.
     *
     * @return the number of patterns/elements in {@link #patterns}
     */
    public int getNumberOfPatterns() {
        return this.patterns.size();
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
}
