/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.velocityprovider;

import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.LinearlyVaryingControlParameter;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Implementation of the prey velocity provider.
 * <p>
 * References:
 * <p>
 * <ul><li>
 * A. Silva, A. Nevers, and E. Costa. "An empirical comparison of particle swarm
 * and predator prey optimisation." In Artificial Intelligence and Cognitive
 * Science, pp. 103-110. Springer Berlin Heidelberg, 2002.
 * </li>
 * <li>
 * M. Higashitani, A. Ishigame, and K. Yasuda. "Particle swarm optimization
 * considering the concept of predator-prey behavior." Evolutionary Computation,
 * 2006. CEC 2006. IEEE Congress on. IEEE, 2006.
 * </li></ul>
 */
public final class PreyVelocityProvider implements VelocityProvider {
    protected VelocityProvider delegate;
    protected ControlParameter acceleration;
    protected ControlParameter fear;
    protected ControlParameter a;
    protected ControlParameter b;

    /** Creates a new instance of PreyVelocityProvider. */
    public PreyVelocityProvider() {
        delegate = new StandardVelocityProvider(
            new LinearlyVaryingControlParameter(0.5, 0.0),
            ConstantControlParameter.of(2),
            ConstantControlParameter.of(2));
        this.acceleration = ConstantControlParameter.of(4.1);
        this.fear = ConstantControlParameter.of(0.0005);
        this.a = ConstantControlParameter.of(1.0);
        this.b = ConstantControlParameter.of(1.0);
    }

    /**
     * Copy constructor.
     * @param copy The object to copy.
     */
    public PreyVelocityProvider(PreyVelocityProvider copy) {
        this.delegate = copy.delegate.getClone();
        this.acceleration = copy.acceleration.getClone();
        this.fear = copy.fear.getClone();
        this.a = copy.a.getClone();
        this.b = copy.b.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreyVelocityProvider getClone() {
        return new PreyVelocityProvider(this);
    }

    /**
     * Update the prey particle's velocity using {@link StandardVelocityProvider}
     * and determine any repulsive effects from nearby predator particles.
     *
     * @param particle  the prey particle.
     * @return          the updated velocity.
     */
    @Override
    public Vector get(Particle particle) {
        Vector position = (Vector) particle.getPosition();
        Vector standardVelocity = delegate.get(particle);
        Vector.Builder builder = Vector.newBuilder();
        List<Particle> predators = getPredators();

        if (predators.isEmpty()) {
            return standardVelocity;
        }

        for (int i = 0; i < particle.getDimension(); i++) {
            double v = standardVelocity.doubleValueOf(i);
            double r = Rand.nextDouble();

            if (r > fear.getParameter()) {
                builder.add(v);
            } else {
                double x = position.doubleValueOf(i);
                double min = Double.MAX_VALUE;
                double predatorPosition = 0;

                for (Particle p : predators) {
                    Vector predatorPos = (Vector)p.getPosition();
                    double xp = predatorPos.doubleValueOf(i);
                    if (Math.abs(x - xp) < min) {
                        predatorPosition = xp;
                    }
                }

                double D = repulsion(predatorPosition, x) * acceleration.getParameter();
                builder.add(v + D);
            }
        }

        return builder.build();
    }

    /**
     * Calculate the repulsive effect that a predator has on a prey.
     * The influence of the predator grows exponentially with proximity.
     *
     * @param predator  the position of the predator in a specific dimension.
     * @param prey      the position of the prey in a specific dimension.
     * @return          the repulsive effect caused by the predator on the prey.
     */
    private double repulsion(double predator, double prey) {
        double distance = Math.abs(predator - prey);
        return a.getParameter() * Math.exp(-b.getParameter() * distance);
    }

    /**
     * Find all predator {@link Particle}s within the {@link Topology}.
     *
     * @return a list of predator particles.
     */
    private List<Particle> getPredators() {
        PSO pso = (PSO) AbstractAlgorithm.get();
        List<Particle> predators = Lists.newArrayList();

        for (Particle p : pso.getTopology()) {
            if (p.getVelocityProvider() instanceof PredatorVelocityProvider) {
                predators.add(p);
            }
        }

        return predators;
    }

    /**
     * Set the {@code fear} {@linkplain ControlParameter}. The {@code fear}
     * component specifies how much a prey particle is affected by a predator.
     *
     * @param fear The {@code fear} component to set.
     */
    public void setFear(ControlParameter fear) {
        this.fear = fear;
    }

    /**
     * Set the {@code a} {@linkplain ControlParameter}. The {@code a} component
     * represents the maximum amplitude of the predator effect over a prey.
     *
     * @param a The {@code a} component to set.
     */
    public void setA(ControlParameter a) {
        this.a = a;
    }

    /**
     * Set the {@code b} {@linkplain ControlParameter}. The {@code b} component
     * allows one to control the distance at which the predator effect is
     * significant.
     *
     * @param b The {@code b} component to set.
     */
    public void setB(ControlParameter b) {
        this.b = b;
    }

    /**
     * Set the delegate {@linkplain VelocityProvider}. The delegate velocity
     * provider is used to provide the standard updated velocity before the
     * effects of the predator(s) are considered. By default,
     * {@link StandardVelocityProvider} is used as the delegate.
     *
     * @param delegate the delegate {@linkplain VelocityProvider}.
     */
    public void setDelegate(VelocityProvider delegate) {
        this.delegate = delegate;
    }
}
