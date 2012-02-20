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
package net.sourceforge.cilib.pso.velocityprovider;

import java.util.HashMap;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.functions.activation.Sigmoid;
import net.sourceforge.cilib.math.random.CauchyDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.ParametizedParticle;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;

/**
 * Velocity update for the Coherence PSO.
 */
public class CoherenceVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = -9051938755796130230L;
    private ControlParameter scalingFactor;
    private ProbabilityDistributionFuction randomNumber;
    private Sigmoid sigmoid;
    private VelocityProvider delegate;

    /**
     * Create an instance of {@linkplain CoherenceVelocityProvider}.
     */
    public CoherenceVelocityProvider() {
        this.scalingFactor = ConstantControlParameter.of(1.0);
        this.randomNumber = new CauchyDistribution();
        this.sigmoid = new Sigmoid();
        this.delegate = new StandardVelocityProvider();
    }

    /**
     * Copy constructor. Create a copy of the given instance.
     * @param copy The instance to copy.
     */
    public CoherenceVelocityProvider(CoherenceVelocityProvider copy) {
        this.scalingFactor = copy.scalingFactor.getClone();
        this.randomNumber = copy.randomNumber;
        this.sigmoid = new Sigmoid();
        this.sigmoid.setOffset(copy.sigmoid.getOffset());
        this.sigmoid.setSteepness(copy.sigmoid.getSteepness());
        this.delegate = copy.delegate.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CoherenceVelocityProvider getClone() {
        return new CoherenceVelocityProvider(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector get(Particle particle) {
        double averageParticleVelocity = 0.0;

        Vector averageVelocity = null;//velocity.getClone();
//        averageVelocity.reset();
        PSO pso = (PSO) AbstractAlgorithm.get();
        for (Particle p : pso.getTopology()) {
            if (averageVelocity == null) {
                averageVelocity = (Vector) p.getVelocity();
                continue;
            }
            Vector particleVelocity = (Vector) p.getVelocity();
            averageVelocity = averageVelocity.plus(particleVelocity);
            averageParticleVelocity += particleVelocity.norm();
        }
        averageVelocity = averageVelocity.divide(particle.getDimension());
        averageParticleVelocity /= particle.getDimension();

        double swarmCenterVelocity = averageVelocity.norm();
        double swarmCoherence = calculateSwarmCoherence(swarmCenterVelocity, averageParticleVelocity);

        double sigmoidValue = this.sigmoid.apply(swarmCoherence);

        Vector standardVelocity = this.delegate.get(particle);

        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < particle.getDimension(); ++i) {
            double coherenceVelocity = this.scalingFactor.getParameter() * sigmoidValue * averageVelocity.doubleValueOf(i) * this.randomNumber.getRandomNumber();
            builder.add(coherenceVelocity);
        }
        Vector coherence = builder.build();

        return Vectors.sumOf(standardVelocity, coherence);


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
        if (averageParticleVelocity == 0.0) {
            return 0.0;
        }

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
    
    /*
     * Not applicable
     */
    @Override
    public void setControlParameters(ParametizedParticle particle) {
        //not applicable
    }
    
    /*
     * Not applicable
     */
    @Override
    public HashMap<String, Double> getControlParameterVelocity(ParametizedParticle particle){
        //not applicable
        return null;
    }
}
