/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching.creation;

import fj.*;
import fj.data.List;
import fj.function.Doubles;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.comparator.SocialBestFitnessComparator;
import net.sourceforge.cilib.entity.visitor.RadiusVisitor;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.measurement.generic.Iterations;
import net.sourceforge.cilib.niching.NichingSwarms;
import static net.sourceforge.cilib.niching.VectorBasedFunctions.*;
import net.sourceforge.cilib.niching.utils.JoinedTopologyProvider;
import net.sourceforge.cilib.niching.utils.TopologyProvider;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.particle.ParticleBehavior;
import net.sourceforge.cilib.pso.velocityprovider.StandardVelocityProvider;
import net.sourceforge.cilib.stoppingcondition.Maximum;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.distancemeasure.DistanceMeasure;
import net.sourceforge.cilib.util.distancemeasure.EuclideanDistanceMeasure;
import net.sourceforge.cilib.util.functions.Populations;

public class VectorBasedNicheCreationStrategy extends NicheCreationStrategy {
    private DistanceMeasure distanceMeasure;
    private TopologyProvider topologyProvider;
    private ControlParameter minSwarmSize;

    public VectorBasedNicheCreationStrategy() {
        distanceMeasure = new EuclideanDistanceMeasure();
        topologyProvider = new JoinedTopologyProvider();
        minSwarmSize = ConstantControlParameter.of(3.0);
        swarmBehavior = new ParticleBehavior();
        swarmBehavior.setVelocityProvider(new StandardVelocityProvider(ConstantControlParameter.of(0.8), ConstantControlParameter.of(1.0), ConstantControlParameter.of(1.0)));
        swarmType = new PSO();
        swarmType.addStoppingCondition(new MeasuredStoppingCondition(new Iterations(), new Maximum(), 500));
    }

    @Override
    public NichingSwarms f(NichingSwarms swarms, Entity b) {
        Particle gBest = (Particle) Topologies.getBestEntity(swarms.getMainSwarm().getTopology(), new SocialBestFitnessComparator());
        List<Particle> newTopology = List.list(gBest);
        List<Particle> swarm = ((List<Particle>) topologyProvider.f(swarms)).delete(gBest, Equal.equal(equalParticle.curry()));

        RadiusVisitor visitor = new RadiusVisitor();
        visitor.visit(swarms.getMainSwarm().getTopology());
        double nRadius = visitor.getResult();

        // get closest particle with dot < 0
        List<Particle> filteredSwarm = swarm.filter(dot(gBest).andThen(Doubles.ltZero));
        if(!filteredSwarm.isEmpty()) {
            Particle closest = filteredSwarm.minimum(Ord.ord(sortByDistance(gBest, distanceMeasure)));
            nRadius = distanceMeasure.distance(closest.getCandidateSolution(), gBest.getCandidateSolution());
            newTopology = newTopology.append(swarm.filter(filter(distanceMeasure, gBest, nRadius)));
        }

        // to prevent new particles from having the same position as the gBest
        if (nRadius == 0) {
            nRadius = ((Vector) gBest.getCandidateSolution()).get(0).getBounds().getUpperBound();
        }

        // Add particles until the swarm has at least 3 particles
        final double nicheRadius = nRadius;
        final UniformDistribution uniform = new UniformDistribution();
        int extras = (int) minSwarmSize.getParameter() - newTopology.length();

        for (int i = 0; i < extras; i++) {
            Particle newP = gBest.getClone();

            // new position within the niche
            Vector solution = (Vector) newP.getCandidateSolution();
            solution = solution.multiply(new P1<Number>() {
                @Override
                public Number _1() {
                    return uniform.getRandomNumber(-nicheRadius, nicheRadius);
                }
            }).plus((Vector) gBest.getCandidateSolution());

            newP.setCandidateSolution(solution);
            newP.getProperties().put(EntityType.Coevolution.POPULATION_ID, Int.valueOf(swarms.getSubswarms().length() + 1));
            newTopology = newTopology.cons(newP);
        }

        // Create the new subswarm, set its optimisation problem, add the particles to it
        PopulationBasedAlgorithm newSubswarm = swarmType.getClone();
        newSubswarm.setOptimisationProblem(swarms.getMainSwarm().getOptimisationProblem());
        newSubswarm.getTopology().clear();
        ((Topology<Particle>) newSubswarm.getTopology()).addAll(newTopology.toCollection());

        // Remove the subswarms particles from the main swarm
        PopulationBasedAlgorithm newMainSwarm = swarms.getMainSwarm().getClone();
        newMainSwarm.getTopology().clear();
        for(Entity e : swarms.getMainSwarm().getTopology()) {
            Particle p = (Particle) e;

            if (!newTopology.exists(equalParticle.f(p))) {
                ((Topology<Entity>) newMainSwarm.getTopology()).add(e.getClone());
            }
        }

        // Set the subswarm's behavior and return the new swarms
        return NichingSwarms.of(newMainSwarm, swarms.getSubswarms().cons(Populations.enforceTopology(swarmBehavior).f(newSubswarm.getClone())));
   }

    public void setDistanceMeasure(DistanceMeasure distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
    }

    public DistanceMeasure getDistanceMeasure() {
        return distanceMeasure;
    }

    public ControlParameter getMinSwarmSize() {
        return minSwarmSize;
    }

    public void setMinSwarmSize(ControlParameter minSwarmSize) {
        this.minSwarmSize = minSwarmSize;
    }

    public void setTopologyProvider(TopologyProvider topologyProvider) {
        this.topologyProvider = topologyProvider;
    }

    public TopologyProvider getTopologyProvider() {
        return topologyProvider;
    }
}
