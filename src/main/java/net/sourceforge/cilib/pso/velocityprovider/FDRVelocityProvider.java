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
import java.util.Iterator;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.LinearlyVaryingControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.ParameterizedParticle;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Implementation of the FDR-PSO velocity update equation.
 *
 * <p>
 * BibTex entry:<br/>
 * <code>
 * &#64;ARTICLE{1202264,<br>
 * title={Fitness-distance-ratio based particle swarm optimization},<br>
 * author={Peram, T. and Veeramachaneni, K. and Mohan, C.K.},<br>
 * journal={Swarm Intelligence Symposium, 2003. SIS '03. Proceedings of the 2003 IEEE},<br>
 * year={2003},<br>
 * month={April},<br>
 * volume={},<br>
 * number={},<br>
 * pages={ 174-181},<br>
 * abstract={ This paper presents a modification of the particle swarm optimization algorithm (PSO) intended to combat the problem of premature convergence observed in many applications of PSO. The proposed new algorithm moves particles towards nearby particles of higher fitness, instead of attracting each particle towards just the best position discovered so far by any particle. This is accomplished by using the ratio of the relative fitness and the distance of other particles to determine the direction in which each component of the particle position needs to be changed. The resulting algorithm (FDR-PSO) is shown to perform significantly better than the original PSO algorithm and some of its variants, on many different benchmark optimization problems. Empirical examination of the evolution of the particles demonstrates that the convergence of the algorithm does not occur at an early phase of particle evolution, unlike PSO. Avoiding premature convergence allows FDR-PSO to continue search for global optima in difficult multimodal optimization problems.},<br>
 * keywords={ convergence of numerical methods, evolutionary computation, optimisation, search problems FDR-PSO, fitness-distance ratio, global optima search, multimodal optimization problems, particle position, particle swarm optimization, premature convergence, relative fitness},<br>
 * doi={10.1109/SIS.2003.1202264},<br>
 * ISSN={ }, }<br>
 * </code>
 * </p>
 *
 */
public class FDRVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = -7117135203986406944L;

    private ControlParameter fdrMaximizerAcceleration;
    private RandomProvider randomProvider;

    private StandardVelocityProvider delegate;

    public FDRVelocityProvider() {
        this.fdrMaximizerAcceleration = ConstantControlParameter.of(2);
        this.randomProvider = new MersenneTwister();

        this.delegate = new StandardVelocityProvider();
        //TODO: recheck this inertia, the original paper has some weird values that become negative early on
        LinearlyVaryingControlParameter inertia = new LinearlyVaryingControlParameter();
        inertia.setInitialValue(0.9);
        inertia.setFinalValue(0.4);
        this.delegate.setInertiaWeight(inertia);
        this.delegate.setCognitiveAcceleration(ConstantControlParameter.of(1));
        this.delegate.setSocialAcceleration(ConstantControlParameter.of(2));
    }

    public FDRVelocityProvider(FDRVelocityProvider copy) {
        this.fdrMaximizerAcceleration = copy.fdrMaximizerAcceleration.getClone();
        this.randomProvider = new MersenneTwister();
        this.delegate = copy.delegate.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FDRVelocityProvider getClone() {
        return new FDRVelocityProvider(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector get(Particle particle) {
        Vector position = (Vector) particle.getPosition();

        Vector standardVelocity = this.delegate.get(particle);

        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < particle.getDimension(); ++i) {
            Topology<Particle> topology = ((PSO) AbstractAlgorithm.get()).getTopology();
            Iterator<Particle> swarmIterator = topology.iterator();
            Particle fdrMaximizer = swarmIterator.next();
            double maxFDR = 0.0;

            while (swarmIterator.hasNext()) {
                Particle currentTarget = swarmIterator.next();

                if (currentTarget.getId() != particle.getId()) {
                    Fitness currentTargetFitness = currentTarget.getBestFitness();
                    Vector currentTargetPosition = (Vector) currentTarget.getBestPosition();

                    double fitnessDifference = (currentTargetFitness.getValue() - particle.getFitness().getValue());
                    double testFDR = fitnessDifference / Math.abs(position.doubleValueOf(i) - currentTargetPosition.doubleValueOf(i));

                    if (testFDR > maxFDR) {
                        maxFDR = testFDR;
                        fdrMaximizer = currentTarget;
                    }
                }
            }

            Vector fdrMaximizerPosition = (Vector) fdrMaximizer.getBestPosition();
            builder.add(standardVelocity.doubleValueOf(i) + this.fdrMaximizerAcceleration.getParameter() * this.randomProvider.nextDouble()
                    * (fdrMaximizerPosition.doubleValueOf(i) - position.doubleValueOf(i)));
        }
        return builder.build();
    }

    /**
     * @return the fdrMaximizerAcceleration
     */
    public ControlParameter getFdrMaximizerAcceleration() {
        return this.fdrMaximizerAcceleration;
    }

    /**
     * @param fdrMaximizerAcceleration
     *            the fdrMaximizerAcceleration to set
     */
    public void setFdrMaximizerAcceleration(ControlParameter fdrMaximizerAcceleration) {
        this.fdrMaximizerAcceleration = fdrMaximizerAcceleration;
    }
    
    /*
     * Not applicable
     */
    @Override
    public void setControlParameters(ParameterizedParticle particle) {
        this.delegate.setControlParameters(particle);
    }
    
    /*
     * not applicable
     */
    @Override
    public HashMap<String, Double> getControlParameterVelocity(ParameterizedParticle particle){
        HashMap<String, Double> result = new HashMap<String, Double>();
        
        double positionInertia = particle.getInertia().getParameter();
        double positionSocialAcceleration = particle.getSocialAcceleration().getParameter();
        double positionCognitiveAcceleration = particle.getCognitiveAcceleration().getParameter();
        double positionVmax = particle.getVmax().getParameter();

        HashMap<String, Double> standardVelocity = this.delegate.getControlParameterVelocity(particle);

        Topology<Particle> topology = ((PSO) AbstractAlgorithm.get()).getTopology();
        Iterator<Particle> swarmIterator = topology.iterator();
        ParameterizedParticle fdrMaximizer = (ParameterizedParticle) swarmIterator.next();
        ParameterizedParticle fdrMaximizerInertia = fdrMaximizer.getClone();
        ParameterizedParticle fdrMaximizerSocial = fdrMaximizer.getClone();
        ParameterizedParticle fdrMaximizerCognitive = fdrMaximizer.getClone();
        ParameterizedParticle fdrMaximizerVmax = fdrMaximizer.getClone();
        double maxFDR = 0.0;

        while (swarmIterator.hasNext()) {
            ParameterizedParticle currentTarget = (ParameterizedParticle) swarmIterator.next();

            if (currentTarget.getId() != particle.getId()) {
                Fitness currentTargetFitness = currentTarget.getBestFitness();
                double currentTargetPositionInertia = currentTarget.getInertia().getBestValue().getParameter();
                double currentTargetPositionSocialAcceleration = currentTarget.getSocialAcceleration().getBestValue().getParameter();
                double currentTargetPositionCognitiveAcceleration = currentTarget.getCognitiveAcceleration().getBestValue().getParameter();
                double currentTargetPositionVmax = currentTarget.getVmax().getBestValue().getParameter();

                double fitnessDifference = (currentTargetFitness.getValue() - particle.getFitness().getValue());
                double testFDRInertia = fitnessDifference / Math.abs(positionInertia - currentTargetPositionInertia);
                double testFDRSocialAcceleration = fitnessDifference / Math.abs(positionInertia - currentTargetPositionSocialAcceleration);
                double testFDRCognitiveAcceleration = fitnessDifference / Math.abs(positionInertia - currentTargetPositionCognitiveAcceleration);
                double testFDRVmax = fitnessDifference / Math.abs(positionInertia - currentTargetPositionVmax);

                if (testFDRInertia > maxFDR) {
                    maxFDR = testFDRInertia;
                    fdrMaximizerInertia = currentTarget;
                }
                
                if (testFDRSocialAcceleration > maxFDR) {
                    maxFDR = testFDRSocialAcceleration;
                    fdrMaximizerSocial = currentTarget;
                }
                
                if (testFDRCognitiveAcceleration > maxFDR) {
                    maxFDR = testFDRCognitiveAcceleration;
                    fdrMaximizerCognitive = currentTarget;
                }
                
                if (testFDRVmax > maxFDR) {
                    maxFDR = testFDRVmax;
                    fdrMaximizerVmax = currentTarget;
                }
            }
        }

        double fdrMaximizerPositionInertia = fdrMaximizerInertia.getInertia().getBestValue().getParameter();
        double fdrMaximizerPositionSocialAcceleration = fdrMaximizerSocial.getSocialAcceleration().getBestValue().getParameter();
        double fdrMaximizerPositionCognitiveAcceleration = fdrMaximizerCognitive.getCognitiveAcceleration().getBestValue().getParameter();
        double fdrMaximizerPositionVmax = fdrMaximizerVmax.getVmax().getBestValue().getParameter();
        
        double inertia = standardVelocity.get("InertiaVelocity") + this.fdrMaximizerAcceleration.getParameter() * this.randomProvider.nextDouble() 
                * (fdrMaximizerPositionInertia - positionInertia);
        
        double socialAcceleration = standardVelocity.get("SocialAccelerationVelocity") + this.fdrMaximizerAcceleration.getParameter() * this.randomProvider.nextDouble() 
                * (fdrMaximizerPositionSocialAcceleration - positionSocialAcceleration);
         
        double cognitiveAcceleration = standardVelocity.get("CognitiveAccelerationVelocity") + this.fdrMaximizerAcceleration.getParameter() * this.randomProvider.nextDouble() 
                * (fdrMaximizerPositionCognitiveAcceleration - positionCognitiveAcceleration);
          
        double vmax = standardVelocity.get("VmaxVelocity") + this.fdrMaximizerAcceleration.getParameter() * this.randomProvider.nextDouble() 
                * (fdrMaximizerPositionVmax - positionVmax);
        
        result.put("InertiaVelocity", inertia);
        result.put("SocialAccelerationVelocity", socialAcceleration);
        result.put("CognitiveAccelerationVelocity", cognitiveAcceleration);
        result.put("VmaxVelocity", vmax);
        
        return result;
    }
}
