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
package net.sourceforge.cilib.pso.crossover.velocityprovider;

import java.util.List;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;
import net.sourceforge.cilib.util.selection.recipes.ElitistSelector;

/**
 * This OffspringVelocityProvider sets an offspring's velocity according to a 
 * modification to a the basic velocity update equation: 
 * c1*r1*(bestParent_pBest - x') + c2*r2*(gBest - x').
 */
public class VelocityUpdateOffspringVelocityProvider extends OffspringVelocityProvider {
    
    protected ControlParameter socialAcceleration;
    protected ControlParameter cognitiveAcceleration;
    protected RandomProvider r1;
    protected RandomProvider r2;

    public VelocityUpdateOffspringVelocityProvider() {
        this.socialAcceleration = ConstantControlParameter.of(1.496180);
        this.cognitiveAcceleration = ConstantControlParameter.of(1.496180);
        this.r1 = new MersenneTwister();
        this.r2 = new MersenneTwister();
    }

    @Override
    public StructuredType f(List<Particle> parents, Particle offspring) {
        Vector position = (Vector) offspring.getPosition();
        Vector localGuide = (Vector) new ElitistSelector<Particle>().on(parents).select().getBestPosition();
        Vector globalGuide = (Vector) AbstractAlgorithm.get().getBestSolution().getPosition();

        Vector cognitiveComponent = Vector.copyOf(localGuide).subtract(position).multiply(cognitiveAcceleration.getParameter()).multiply(r1.nextDouble());
        Vector socialComponent = Vector.copyOf(globalGuide).subtract(position).multiply(socialAcceleration.getParameter()).multiply(r2.nextDouble());
        
        return Vectors.sumOf(cognitiveComponent, socialComponent);
    }

    public void setCognitiveAcceleration(ControlParameter cognitiveComponent) {
        this.cognitiveAcceleration = cognitiveComponent;
    }

    public ControlParameter getSocialAcceleration() {
        return socialAcceleration;
    }

    public void setSocialAcceleration(ControlParameter socialComponent) {
        this.socialAcceleration = socialComponent;
    }

    public RandomProvider getR1() {
        return r1;
    }

    public void setR1(RandomProvider r1) {
        this.r1 = r1;
    }

    public RandomProvider getR2() {
        return r2;
    }

    public void setR2(RandomProvider r2) {
        this.r2 = r2;
    }
}
