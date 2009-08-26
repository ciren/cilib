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
package net.sourceforge.cilib.pso.velocityupdatestrategies;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.functions.activation.Sigmoid;
import net.sourceforge.cilib.math.random.RandomNumber;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.container.Vector;


/**
 * Velocity update for the Coherence PSO.
 * @author Daniel Lowes
 */
public class CoherenceVelocityUpdate extends StandardVelocityUpdate {
    private static final long serialVersionUID = -9051938755796130230L;
    private ControlParameter scalingFactor;
    private RandomNumber randomNumber;
    private Sigmoid sigmoid;

    /**
     * Create an instance of {@linkplain CoherenceVelocityUpdate}.
     */
    public CoherenceVelocityUpdate() {
        super();
        scalingFactor = new ConstantControlParameter(1.0);
        randomNumber = new RandomNumber();
        sigmoid = new Sigmoid();
    }

    /**
     * Copy constructor. Create a copy of the given instance.
     * @param copy The instance to copy.
     */
    public CoherenceVelocityUpdate(CoherenceVelocityUpdate copy) {
        super(copy);
        this.scalingFactor = copy.scalingFactor.getClone();
        this.randomNumber = copy.randomNumber.getClone();
    }

    /**
     * {@inheritDoc}
     */
    public CoherenceVelocityUpdate getClone() {
        return new CoherenceVelocityUpdate(this);
    }

    /**
     * {@inheritDoc}
     */
    public void updateVelocity(Particle particle) {
        Vector velocity = (Vector) particle.getVelocity();
        Vector position = (Vector) particle.getPosition();
        Vector bestPosition = (Vector) particle.getBestPosition();
        Vector nBestPosition = (Vector) particle.getNeighbourhoodBest().getBestPosition();

        double averageParticleVelocity = 0.0;

        Vector averageVelocity = velocity.getClone();
        averageVelocity.reset();
        PSO pso = (PSO) AbstractAlgorithm.get();
        for (Particle p : pso.getTopology()) {
            Vector particleVelocity = (Vector) p.getVelocity();
            averageVelocity = averageVelocity.plus(particleVelocity);
            averageParticleVelocity += particleVelocity.norm();
        }
        averageVelocity = averageVelocity.divide(particle.getDimension());
        averageParticleVelocity /= particle.getDimension();

//        System.out.println("averageVelocity: " + averageVelocity);

        double swarmCenterVelocity = averageVelocity.norm();
        double swarmCoherence = calculateSwarmCoherence(swarmCenterVelocity, averageParticleVelocity);

        double sigmoidValue = sigmoid.evaluate(swarmCoherence);

         for (int i = 0; i < particle.getDimension(); ++i) {
                double value = inertiaWeight.getParameter()*velocity.getReal(i) +
                    (bestPosition.getReal(i) - position.getReal(i)) * cognitiveAcceleration.getParameter() +
                    (nBestPosition.getReal(i) - position.getReal(i)) * socialAcceleration.getParameter();

                double coherenceVelocity = scalingFactor.getParameter() * sigmoidValue * averageVelocity.getReal(i) * randomNumber.getCauchy();
//                System.out.println("swam center: " + swarmCenterVelocity);
//                System.out.println("average particle: " + averageParticleVelocity);
//                System.out.println("sigmoid: " + sigmoidValue);
//                System.out.println(coherenceVelocity);
//                System.out.println("new vlaue: " + (value+coherenceVelocity));
                velocity.setReal(i, value+coherenceVelocity);

                clamp(velocity, i);
            }


//        float social = socialRandomGenerator.nextFloat();
//        float cognitive = cognitiveRandomGenerator.nextFloat();
//
//        //DistanceMeasure adm = new AbsoluteDistanceMeasure();
//        //DistanceMeasure dm = new MetricDistanceMeasure();
//
//        double avgv = 0.0;
//        double swv = 0.0;
//        Topology<Particle> topology = ((PSO)Algorithm.get()).getTopology();
//          Iterator<? extends Particle> it = topology.neighbourhood(null);
//          double[] al = new double[particle.getDimension()];
//           while (it.hasNext()) {
//               Particle pl = it.next();
//               double tmpv = 0.0;
//               //double tmpsv = 0.0;
//               for(int dim = 0; dim < particle.getDimension(); dim++) {
//                al[dim] = al[dim]+((Vector)pl.getVelocity()).getReal(dim);
//                   tmpv += Math.pow(((Vector)pl.getVelocity()).getReal(dim), 2);
//               }
//               tmpv = Math.sqrt(tmpv);
//               avgv += tmpv;
//           }
//           for(int i = 0; i < particle.getDimension(); i++) {
//            //al.set(i, ;
//            swv += (al[i]/topology.size()) * (al[i]/topology.size());
//        }
//        swv = Math.sqrt(swv);
//
//        for (int i = 0; i < particle.getDimension(); ++i) {
//            double tmp = 0.0;
//            tmp = inertiaWeight.getParameter()*velocity.getReal(i)
//                + cognitive * (bestPosition.getReal(i) - position.getReal(i)) * cognitiveAcceleration.getParameter()
//                + social * (nBestPosition.getReal(i) - position.getReal(i)) * socialAcceleration.getParameter();
//
//            double avgdim = 0.0;
//              it = topology.neighbourhood(null);
//               while (it.hasNext()) {
//                   avgdim += ((Vector)(it.next().getVelocity())).getReal(i);
//               }
//            avgdim /= particle.getDimension();
//
//            double cvelocity = MathUtil.sigmoid(swv/avgv)*avgdim*randomNumber.getCauchy();
//
//            System.out.println(cvelocity);
//            tmp += cvelocity;
//
//            velocity.setReal(i, tmp);
//
//            clamp(velocity, i);
//        }
    }

    /**
     * Calculate the swarm coherence.
     * @param swarmCenterVelocity The swarm center velocity.
     * @param averageParticleVelocity The average {@linkplain Particle} velocity.
     * @return The swarm coherence value.
     */
    private double calculateSwarmCoherence(double swarmCenterVelocity, double averageParticleVelocity) {
        if (averageParticleVelocity == 0.0)
            return 0.0;

        return swarmCenterVelocity / averageParticleVelocity;
    }


    /*
     * @return Returns the congnitiveRandomGenerator.
     */
//    public Random getCongnitiveRandomGenerator() {
//        return cognitiveRandomGenerator;
//    }
//
//    /**
//     * @param congnitiveRandomGenerator The congnitiveRandomGenerator to set.
//     */
//    public void setCongnitiveRandomGenerator(Random congnitiveRandomGenerator) {
//        this.cognitiveRandomGenerator = congnitiveRandomGenerator;
//    }
//
//    /**
//     * @return Returns the socialRandomGenerator.
//     */
//    public Random getSocialRandomGenerator() {
//        return socialRandomGenerator;
//    }
//
//    /**
//     * @param socialRandomGenerator The socialRandomGenerator to set.
//     */
//    public void setSocialRandomGenerator(Random socialRandomGenerator) {
//        this.socialRandomGenerator = socialRandomGenerator;
//    }
}
