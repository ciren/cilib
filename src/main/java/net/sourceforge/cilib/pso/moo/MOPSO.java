/*
 * Copyright (C) 2003 - 2008
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
package net.sourceforge.cilib.pso.moo;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.moo.archive.Archive;
import net.sourceforge.cilib.moo.archive.StandardArchive;
import net.sourceforge.cilib.problem.MOOptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.MultiObjectiveParticle;
import net.sourceforge.cilib.pso.positionupdatestrategies.GaussianPositionUpdateStrategy;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author CIRG
 */
public class MOPSO extends PSO  {
    private static final long serialVersionUID = -4388678573614103683L;

    private MOOptimisationProblem moproblem;
    private Archive archive;
    //private LocalGuideStrategy localGuideStrategy;

    public MOPSO() {
        super();

        this.archive = new StandardArchive();

        Particle particle = new MultiObjectiveParticle();
        particle.setPositionUpdateStrategy(new GaussianPositionUpdateStrategy());
        this.getInitialisationStrategy().setEntityType(particle);
    }

    @Override
    public void performInitialisation() {
        super.performInitialisation();
        Topology<Particle> topology = this.getTopology();

        for (Particle particle : topology) {
            //particle.setFitness(this.moproblem.getFitness(particle.getPosition(), true));
            particle.calculateFitness();
        }

        // Look at Set
        Collection<OptimisationSolution> paretoFront = new ArrayList<OptimisationSolution>();

        // TODO : prevent fitness re-evaluations
        // TODO : Check Jaco's code
        paretoFront.add(new OptimisationSolution(this.getMoproblem(), topology.get(0).getPosition()));
        for (Particle particle : topology) {
            for (OptimisationSolution solution : paretoFront) {
                int result = solution.getFitness().compareTo(particle.getFitness()); // Check this comparing
                if (result > 0) {
                    // Replace
                }
                else if (result == 0) {
                    // Add
                    break;
                }
                else {
                    // Ignore
                }
            }
        }

        this.archive.addAll(paretoFront);

        createHypercubes();
    }

    /*
     * How the heck do we make hypercubes???
     */
    private void createHypercubes() { // Look at creating a strategy to do this

    }

    @Override
    public void algorithmIteration() {
        // TODO Auto-generated method stub

        StandardArchive standardArchive = (StandardArchive) this.archive;
        Topology<Particle> topology = this.getTopology();

        for (Particle particle : topology) {
            MultiObjectiveParticle moParticle = (MultiObjectiveParticle) particle;

            Particle globalGuide = selectGlobalGuide(/*hypercube*/);

            standardArchive.getLocalGuideStrategy().updateLocalGuide(particle);
            Vector localGuide = standardArchive.getLocalGuideStrategy().getLocalGuide();

            moParticle.setNeighbourhoodBest(globalGuide);
            moParticle.setBestPosition(localGuide);

            moParticle.updateVelocity();
            moParticle.updatePosition();
        }

        maintainBoundaries();

        for (Particle particle : topology) {
            //particle.setFitness(this.moproblem.getFitness(particle.getPosition(), true));
            particle.calculateFitness();
        }

        // TODO :: FIX THIS!!!!!!!!!!!!!!!!!!!!
        //         Look at Set
        Collection<OptimisationSolution> paretoFront = new ArrayList<OptimisationSolution>();

        // TODO : prevent fitness re-evaluations
        // TODO : Check Jaco's code
        paretoFront.add(new OptimisationSolution(this.getMoproblem(), topology.get(0).getPosition()));
        for (Particle particle : topology) {
            for (OptimisationSolution solution : paretoFront) {
                int result = solution.getFitness().compareTo(particle.getFitness()); // Check this comparing
                if (result > 0) {
                    // Replace
                }
                else if (result == 0) {
                    // Add
                    break;
                }
                else {
                    // Ignore
                }
            }
        }

        archive.accept(paretoFront);
    }

    private Particle selectGlobalGuide() { // Look at creating a strategy to do this
        return null;
    }

    //private Vector selectLocalGuide() { // Look at creating a strategy to do this
    //    return null;
    //}

    private void maintainBoundaries() {

    }

    public Archive getArchive() {
        return archive;
    }

    public void setArchive(Archive archive) {
        this.archive = archive;
    }

    public MOOptimisationProblem getMoproblem() {
        return moproblem;
    }

    public void setMoproblem(MOOptimisationProblem moproblem) {
        this.moproblem = moproblem;
    }


//    public Topology<? > getTopology() {
//        // TODO Auto-generated method stub
//        return super.getTopology();
//    }


}
