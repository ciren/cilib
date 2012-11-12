/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection.weighting;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import fj.F;
import fj.F2;
import fj.Ord;
import fj.Ordering;
import fj.P;
import fj.P2;
import java.util.List;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.util.selection.WeightedObject;

public class EntityWeighting implements Weighting {

    private EntityFitness<Entity> entityFitness;

    public EntityWeighting() {
        this.entityFitness = new CurrentFitness();
    }

    public EntityWeighting(EntityFitness<Entity> entityFitness) {
        this.entityFitness = entityFitness;
    }

    @Override
    public <T> Iterable<WeightedObject> weigh(Iterable<T> iterable) {
        Preconditions.checkArgument(Iterables.get(iterable, 0) instanceof Entity);

        P2<Double, Double> minMaxFitness = getMinMaxFitness(Lists.newArrayList(iterable));
        List<WeightedObject> result = Lists.newArrayList();

        double minMaxDifference = minMaxFitness._2() - minMaxFitness._1();

        for (T t : iterable) {
            double weight;

            if (Double.isNaN(minMaxDifference)) { // all NaNs
                weight = 1.0;
            } else { // some NaNs
                if (minMaxDifference != 0.0) {
                    weight = (this.entityFitness.getFitness((Entity) t).getValue() - minMaxFitness._1()) / minMaxDifference;
                } else { // some NaNs or all the same
                    // if minMaxDifference is zero it means that all entities have the same fitness
                    // which will make it the same as a constant weighting, so as long as the values
                    // are the same it works out the same
                    weight = 1.0;
                }

                if (Double.isNaN(this.entityFitness.getFitness((Entity) t).getValue())) {
                    weight = 0.0;
                }
            }

            result.add(new WeightedObject(t, weight));
        }

        return result;
    }

    public void setEntityFitness(EntityFitness<Entity> entityFitness) {
        this.entityFitness = entityFitness;
    }

    public EntityFitness<Entity> getEntityFitness() {
        return entityFitness;
    }

    private static Ord<Entity> entityOrdering = Ord.ord(new F2<Entity, Entity, Ordering>() {
        @Override
        public Ordering f(Entity a, Entity b) {
            return Ordering.values()[a.compareTo(b) + 1];
        };
    }.curry());

    private static F<Entity, Boolean> inferiorFilter(final EntityFitness<Entity> entityFitness) {
        return new F<Entity, Boolean>() {
            @Override
            public Boolean f(Entity a) {
                return entityFitness.getFitness(a) != InferiorFitness.instance();
            }
        };
    }

    private <T> P2<Double, Double> getMinMaxFitness(List<T> entities) {
        fj.data.List<Entity> e = ((fj.data.List<Entity>) fj.data.List.iterableList(entities))
            .filter(inferiorFilter(entityFitness));

        if (e.isEmpty()) {
            return P.p(Double.NaN, Double.NaN);
        }

        Fitness min = e.minimum(entityOrdering).getFitness();
        Fitness max = e.maximum(entityOrdering).getFitness();

        return P.p(min.getValue(), max.getValue());
    }
}
