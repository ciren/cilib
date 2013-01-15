/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.positionprovider;

import java.util.ArrayList;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;

/** Implementation of the DE PSO of Hendtlass.
 * TODO: can the DE strategies be incorporated somehow?
 */
public class DEPositionProvider implements PositionProvider {

    private static final long serialVersionUID = -4052606351661988520L;
    private ProbabilityDistributionFunction differentialEvolutionProbability; //Make a parameter to set via xml
    private ProbabilityDistributionFunction crossoverProbability;
    private ProbabilityDistributionFunction scaleParameter;
    private ProbabilityDistributionFunction rand1;
    private ProbabilityDistributionFunction rand2;
    private ProbabilityDistributionFunction rand3;
    private ProbabilityDistributionFunction rand4;

    public DEPositionProvider() {
        differentialEvolutionProbability = new GaussianDistribution();
        rand1 = new UniformDistribution();
        rand2 = new UniformDistribution();
        rand3 = new UniformDistribution();
        rand4 = new UniformDistribution();
        crossoverProbability = new GaussianDistribution();
        scaleParameter = new GaussianDistribution();
    }

    public DEPositionProvider(DEPositionProvider copy) {
        this();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DEPositionProvider getClone() {
        return new DEPositionProvider(this);
    }

    @Override
    public Vector get(Particle particle) {
        Vector position = (Vector) particle.getPosition();
        Vector velocity = (Vector) particle.getVelocity();

        if (rand1.getRandomNumber() < differentialEvolutionProbability.getRandomNumber(0.8, 0.1)) {
            return Vectors.sumOf(position, velocity);
        } else {
            ArrayList<Vector> positions = new ArrayList<Vector>(3);

            //select three random individuals, all different and different from particle
            PSO pso = (PSO) AbstractAlgorithm.get();

            /*Iterator k = pso.getTopology().iterator();
            int counter = 0;
            String particleId = particle.getId();
            Vector pos;
            while (k.hasNext() && (counter < 3)) {
            Particle p = (Particle) k.next();
            if ((p.getId() != particleId) && (rand2.getUniform(0,1) <= 0.5)) {
            pos = (Vector) p.getPosition();
            positions.add(pos);
            counter++;
            }
            }*/

            int count = 0;

            while (count < 3) {
                int random = Rand.nextInt(pso.getTopology().size());
                Entity parent = pso.getTopology().get(random);
                if (!positions.contains((Vector) parent.getCandidateSolution())) {
                    positions.add((Vector) parent.getCandidateSolution());
                    count++;
                }
            }

            Vector position1 = positions.get(0);
            Vector position2 = positions.get(1);
            Vector position3 = positions.get(2);

            Vector dePosition = Vector.copyOf(position);
            int j = Double.valueOf(rand3.getRandomNumber(0, position.size())).intValue();
            for (int i = 0; i < position.size(); ++i) {
                if ((rand4.getRandomNumber(0, 1) < crossoverProbability.getRandomNumber(0.5, 0.3)) || (j == i)) {
                    double value = position1.doubleValueOf(i);
                    value += scaleParameter.getRandomNumber(0.7, 0.3) * (position2.doubleValueOf(i) - position3.doubleValueOf(i));
                    dePosition.setReal(i, value);
                }
                //else
                //DEposition.setReal(i, )add(new Real(position3.getReal(i)));
            }


            //position should only become the offspring if its fitness is better
            Fitness trialFitness = pso.getOptimisationProblem().getFitness(dePosition);
            Fitness currentFitness = pso.getOptimisationProblem().getFitness(particle.getCandidateSolution());

            if (trialFitness.compareTo(currentFitness) > 0) {
                particle.setCandidateSolution(dePosition);
            }
            return dePosition;
        }
    }
}
