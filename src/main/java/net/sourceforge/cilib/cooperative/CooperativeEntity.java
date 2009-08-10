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
package net.sourceforge.cilib.cooperative;

import net.sourceforge.cilib.entity.AbstractEntity;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author Theuns Cloete
 */
public class CooperativeEntity extends AbstractEntity {
    private static final long serialVersionUID = -8298684370426283216L;

    protected Vector context = null;
    protected Fitness fitness = null;

    public CooperativeEntity() {
        context = new Vector();
        fitness = InferiorFitness.instance();
    }

    public CooperativeEntity(CooperativeEntity rhs) {
        context = rhs.context.getClone();
        fitness = rhs.fitness;
    }

    @Override
    public CooperativeEntity getClone() {
        return new CooperativeEntity(this);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if ((object == null) || (this.getClass() != object.getClass()))
            return false;

        CooperativeEntity other = (CooperativeEntity) object;
        return super.equals(other) &&
            (this.context.equals(other.context)) &&
            (this.fitness.equals(other.fitness));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + super.hashCode();
        hash = 31 * hash + (this.context == null ? 0 : this.context.hashCode());
        hash = 31 * hash + (this.fitness == null ? 0 : this.fitness.hashCode());
        return hash;
    }

    public void reset() {
        context.clear();
    }

    public int compareTo(Entity o) {
        return getFitness().compareTo(o.getFitness());
    }

    public void append(Type value) {
        if(value instanceof Vector)
            context.append((Vector) value);
        else
            context.append((Numeric) value);
    }

    public void append(Entity entity) {
        append(entity.getCandidateSolution());
    }

    public void update(Entity src, int srcPos, int dstPos, int length) {
        for(int i = dstPos; i < dstPos + length; ++i) {
            context.setReal(i, ((Vector) src.getCandidateSolution()).getReal(srcPos + i - dstPos));
        }
    }

    public StructuredType getCandidateSolution() {
        return context;
    }

    public void setCandidateSolution(Type type) {
        context.clear();
        append(type);
    }

    public int getDimension() {
        return context.getDimension();
    }

    public void setDimension(int dim) {
        throw new UnsupportedOperationException("The dimension of a CooperativeEntity is determined by its context");
    }

    public Fitness getFitness() {
        return fitness;
    }

    public void setFitness(Fitness f) {
        fitness = f;
    }

    public void initialise(OptimisationProblem problem) {
        context = (Vector) problem.getDomain().getBuiltRepresenation().getClone();
    }

    public void reinitialise() {
        throw new UnsupportedOperationException("Methd not implemented");
    }

    @Override
    public void calculateFitness() {
        fitness = getFitnessCalculator().getFitness(this);
    }
}
