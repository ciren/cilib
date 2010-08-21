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
package net.sourceforge.cilib.type.types.container;

import com.google.common.collect.ForwardingSet;
import com.google.common.collect.Sets;

import java.util.Set;

import net.sourceforge.cilib.container.visitor.Visitor;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.math.Stats;
import net.sourceforge.cilib.math.random.generator.RandomProvider;

/**
 * @author Theuns Cloete
 */
public class Cluster extends ForwardingSet<StandardPattern> implements StructuredType<StandardPattern> {
    private static final long serialVersionUID = 9136220224825090793L;

    // IDEA: Give the cluster a blackboard where it can keep track of all its own stats like compactness, scatter, mean, etc
    private Set<StandardPattern> patterns;
    private Vector centroid;

    public Cluster() {
        this.patterns = Sets.newHashSet();
    }

    public Cluster(Vector centroid) {
        this.patterns = Sets.newHashSet();
        this.centroid = centroid;
    }

    public Cluster(Cluster rhs) {
        this.patterns = Sets.newHashSet(rhs.patterns);
        this.centroid = Vector.copyOf(rhs.centroid);
    }

    @Override
    public Set<StandardPattern> delegate() {
        return this.patterns;
    }

    @Override
    public Cluster getClone() {
        return new Cluster(this);
    }

    @Override
    public void accept(Visitor<StandardPattern> visitor) {
        for (StandardPattern pattern : patterns) {
            visitor.visit(pattern);
        }
    }

    @Override
    public void randomize(RandomProvider random) {
        throw new UnsupportedOperationException("No use in randomizing a set that does not contain order information");
    }

    public void setCentroid(Vector centroid) {
        this.centroid = centroid;
    }

    public Vector getCentroid() {
        return this.centroid;
    }

    public Vector getMean() {
        return Stats.meanVector(this);
    }

    public Vector getVarianceVector() {
        return Stats.varianceVector(this, this.centroid);
    }

    public Vector getVarianceVector(Vector center) {
        return Stats.varianceVector(this, center);
    }

    public double getVariance() {
        return Stats.variance(this, this.centroid);
    }

    public double getVariance(Vector center) {
        return Stats.variance(this, center);
    }

    public Vector getStdDeviationVector() {
        return Stats.stdDeviationVector(this, this.centroid);
    }

    public Vector getStdDeviationVector(Vector center) {
        return Stats.stdDeviationVector(this, center);
    }

    public double getStdDeviation() {
        return Stats.stdDeviation(this, this.centroid);
    }

    public double getStdDeviation(Vector center) {
        return Stats.stdDeviation(this, center);
    }
}
