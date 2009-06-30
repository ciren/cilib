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
package net.sourceforge.cilib.pso;

import java.util.Random;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.generator.KnuthSubtractive;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * TODO: This needs to be checked
 *
 * <p>
 * This implements the dissipative PSO (DPSO). Apply this to the standard PSO
 * using {@link PSO#setDissipativeStep(DissipativeStep)}.
 * </p><p>
 * References:
 * </p><ul><li>
 * X Xie, W Zang, Z Yang, "A dissipative swarm optimization"'
 * Proceedings of the IEEE Congress on Evolutionary Computing (CEC 2002),
 * Honolulu, Hawaii USA, May 2002
 * </li></ul>
 *
 * @author  Edwin Peer
 */
public class DissipativeStep {

    /** Creates a new instance of DissipativeStep. */
    public DissipativeStep() {
        randomGenerator = new KnuthSubtractive();
        velocityThreshold = 0.001f;
        positionThreshold = 0.002f;
    }

    public void execute(Particle particle) {
        //DomainParser parser = DomainParser.getInstance();
        //net.sourceforge.cilib.Type.Vector domain = (net.sourceforge.cilib.Type.Vector) pso.getOptimisationProblem().getDomain().getBuiltRepresentation();
        Vector domain = (Vector) pso.getOptimisationProblem().getDomain().getBuiltRepresenation();
        //Vector domain = (Vector) parser.getBuiltRepresentation();
        if (randomGenerator.nextFloat() < velocityThreshold) {
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
                velocity.setReal(i, randomGenerator.nextFloat()*(component.getBounds().getUpperBound() - component.getBounds().getLowerBound()));
            }
        }
        if (randomGenerator.nextFloat() < positionThreshold) {
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
                position.setReal(i, randomGenerator.nextDouble()*(component.getBounds().getUpperBound() - component.getBounds().getLowerBound())+ component.getBounds().getLowerBound());
            }
        }
    }

    public void setRandomGenerator(Random randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    public Random getRandomGenerator() {
        return randomGenerator;
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

    private Random randomGenerator;
    private float velocityThreshold;
    private float positionThreshold;

}
