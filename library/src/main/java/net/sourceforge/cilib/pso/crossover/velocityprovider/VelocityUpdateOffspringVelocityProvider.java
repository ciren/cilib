/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.crossover.velocityprovider;

import fj.P1;
import java.util.List;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.pso.particle.Particle;
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

    public VelocityUpdateOffspringVelocityProvider() {
        this.socialAcceleration = ConstantControlParameter.of(1.496180);
        this.cognitiveAcceleration = ConstantControlParameter.of(1.496180);
    }

    private static P1<Number> random() {
        return new P1<Number>() {
            @Override
            public Number _1() {
                return Rand.nextDouble();
            }
        };
    }

    private static P1<Number> cp(final ControlParameter r) {
        return new P1<Number>() {
            @Override
            public Number _1() {
                return r.getParameter();
            }
        };
    }

    @Override
    public StructuredType f(List<Particle> parents, Particle offspring) {
        Vector position = (Vector) offspring.getPosition();
        Vector localGuide = (Vector) new ElitistSelector<Particle>().on(parents).select().getBestPosition();
        Vector globalGuide = (Vector) AbstractAlgorithm.get().getBestSolution().getPosition();

        Vector cognitiveComponent = Vector.copyOf(localGuide).subtract(position).multiply(cp(cognitiveAcceleration)).multiply(random());
        Vector socialComponent = Vector.copyOf(globalGuide).subtract(position).multiply(cp(socialAcceleration)).multiply(random());

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
}
