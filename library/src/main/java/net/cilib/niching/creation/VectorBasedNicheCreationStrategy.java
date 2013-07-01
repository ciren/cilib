/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.niching.creation;

import static net.cilib.niching.VectorBasedFunctions.dot;
import static net.cilib.niching.VectorBasedFunctions.equalParticle;
import static net.cilib.niching.VectorBasedFunctions.filter;
import static net.cilib.niching.VectorBasedFunctions.sortByDistance;
import net.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.cilib.controlparameter.ConstantControlParameter;
import net.cilib.controlparameter.ControlParameter;
import net.cilib.entity.Entity;
import net.cilib.entity.EntityType;
import net.cilib.entity.Topologies;
import net.cilib.entity.comparator.SocialBestFitnessComparator;
import net.cilib.entity.visitor.RadiusVisitor;
import net.cilib.math.random.UniformDistribution;
import net.cilib.measurement.generic.Iterations;
import net.cilib.niching.NichingSwarms;
import net.cilib.niching.utils.JoinedTopologyProvider;
import net.cilib.niching.utils.TopologyProvider;
import net.cilib.pso.PSO;
import net.cilib.pso.particle.Particle;
import net.cilib.pso.particle.ParticleBehavior;
import net.cilib.pso.velocityprovider.StandardVelocityProvider;
import net.cilib.stoppingcondition.Maximum;
import net.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.cilib.type.types.Int;
import net.cilib.type.types.container.Vector;
import net.cilib.util.distancemeasure.DistanceMeasure;
import net.cilib.util.distancemeasure.EuclideanDistanceMeasure;
import net.cilib.util.functions.Populations;
import fj.Equal;
import fj.Ord;
import fj.P1;
import fj.data.List;
import fj.function.Doubles;

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
        double nRadius = visitor.f(swarms.getMainSwarm().getTopology());

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
        SinglePopulationBasedAlgorithm newSubswarm = swarmType.getClone();
        newSubswarm.setOptimisationProblem(swarms.getMainSwarm().getOptimisationProblem());
        ((SinglePopulationBasedAlgorithm<Particle>) newSubswarm).setTopology(newSubswarm.getTopology().append(newTopology));

        // Remove the subswarms particles from the main swarm
        SinglePopulationBasedAlgorithm newMainSwarm = swarms.getMainSwarm().getClone();
        newMainSwarm.setTopology(List.nil());
        fj.data.List<Entity> local = swarms.getMainSwarm().getTopology();

        for(Entity e : local) {
            Particle p = (Particle) e;

            if (!newTopology.exists(equalParticle.f(p))) {
            	newMainSwarm.setTopology(newMainSwarm.getTopology().snoc(e.getClone()));
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
