/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.crossover.velocityprovider;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;

/**
 * This OffspringVelocityProvider calculates an offspring's velocity according to
 * Lovbjerg et al's hybrid PSO:
 * <p>
 * @INPROCEEDINGS{Løvbjerg01hybridparticle,
 *   author = {Morten Løvbjerg and Thomas Kiel Rasmussen and Thiemo Krink},
 *   title = {Hybrid Particle Swarm Optimiser with Breeding and Subpopulations},
 *   booktitle = {Proceedings of the Genetic and Evolutionary Computation Conference (GECCO-2001},
 *   year = {2001},
 *   pages = {469--476},
 *   publisher = {Morgan Kaufmann}
 * }
 * </p>
 */
public class LovbjergOffspringVelocityProvider extends OffspringVelocityProvider {
    @Override
    public StructuredType f(List<Particle> parent, Particle offspring) {
        Vector velocity = (Vector) offspring.getVelocity();

        return Vectors.sumOf(Lists.transform(parent, new Function<Particle, Vector>() {
            @Override
            public Vector apply(Particle f) {
                return (Vector) f.getVelocity();
            }
        })).normalize().multiply(velocity.length());
    }
}
