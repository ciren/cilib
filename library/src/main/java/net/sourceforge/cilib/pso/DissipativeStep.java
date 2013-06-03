/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso;


import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * TODO: This needs to be checked
 *
 * <p>
 * This implements the dissipative PSO (DPSO).
 * </p><p>
 * References:
 * </p><ul><li>
 * X Xie, W Zang, Z Yang, "A dissipative swarm optimization"'
 * Proceedings of the IEEE Congress on Evolutionary Computing (CEC 2002),
 * Honolulu, Hawaii USA, May 2002
 * </li></ul>
 *
 */
public class DissipativeStep {

    /** Creates a new instance of DissipativeStep. */
    public DissipativeStep() {
        velocityThreshold = 0.001f;
        positionThreshold = 0.002f;
    }

    public void execute(Particle particle) {
        //Parser parser = Parser.getInstance();
        //net.sourceforge.cilib.Type.Vector domain = (net.sourceforge.cilib.Type.Vector) pso.getOptimisationProblem().getDomain().getBuiltRepresentation();
        Vector domain = (Vector) pso.getOptimisationProblem().getDomain().getBuiltRepresentation();
        //Vector domain = (Vector) parser.getBuiltRepresentation();
        if (Rand.nextFloat() < velocityThreshold) {
            for (int i = 0; i < particle.getDimension(); ++i) {
                //Real component = (Real) domain.getComponent(i);
                net.sourceforge.cilib.type.types.Real component = (net.sourceforge.cilib.type.types.Real) domain.get(i);
                //particle.getVelocity()[i] = randomGenerator.nextFloat()
                //* (component.getUpperBound().doubleValue() - component.getLowerBound().doubleValue());
                //Domain d = Domain.getInstance();


            /*    particle.getVelocity()[i] = randomGenerator.nextFloat() *
                    //(d.getUpperBound() - d.getLowerBound());
                    (component.getUpperBound() - component.getLowerBound());*/
                Vector velocity = (Vector) particle.getVelocity();
                velocity.setReal(i, Rand.nextFloat()*(component.getBounds().getUpperBound() - component.getBounds().getLowerBound()));
            }
        }
        if (Rand.nextFloat() < positionThreshold) {
            for (int i = 0; i < particle.getDimension(); ++i) {
                /*Real component = (Real) domain.getComponent(i);
                particle.getPosition()[i] = randomGenerator.nextDouble()
                * (component.getUpperBound().doubleValue() - component.getLowerBound().doubleValue())
                + component.getLowerBound().doubleValue();*/
                //Domain d = Domain.getInstance();
                net.sourceforge.cilib.type.types.Real component = (net.sourceforge.cilib.type.types.Real) domain.get(i);
                /*particle.getPosition()[i] = randomGenerator.nextDouble()
                //* (d.getUpperBound() - d.getLowerBound()) + d.getLowerBound();
                * (component.getUpperBound() - component.getLowerBound()) + component.getLowerBound();*/
                Vector position = (Vector) particle.getPosition();
                position.setReal(i, Rand.nextDouble()*(component.getBounds().getUpperBound() - component.getBounds().getLowerBound())+ component.getBounds().getLowerBound());
            }
        }
    }

    public void setVelocityThreshold(float velocityThreshold) {
        this.velocityThreshold = velocityThreshold;
    }

    public float getVelocityThreshold() {
        return velocityThreshold;
    }

    public void setPositionThreshold(float positionThreshold) {
        this.positionThreshold = positionThreshold;
    }

    public float getPositionThreshold() {
        return positionThreshold;
    }

    protected void setPSO(PSO pso) {
        this.pso = pso;
    }

    public PSO getPso() {
        return pso;
    }

    private PSO pso;

    private float velocityThreshold;
    private float positionThreshold;

}
