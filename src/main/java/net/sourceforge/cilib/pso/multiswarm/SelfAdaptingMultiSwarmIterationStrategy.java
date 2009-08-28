/*
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
package net.sourceforge.cilib.pso.multiswarm;

import java.util.ListIterator;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * Implementation of the self-adapting multi-swarm algorithm as described in
 * @article{blackwell51pso,
 *  title={{Particle swarm optimization in dynamic environments}},
 *  author={Blackwell, T.},
 *  journal={Evolutionary Computation in Dynamic and Uncertain Environments},
 *  volume={51},
 *  pages={29--49}
 * }
 *
 * @author Julien Duhain
 *
 */
public class SelfAdaptingMultiSwarmIterationStrategy extends AbstractIterationStrategy<MultiSwarm> {
    private static final long serialVersionUID = -5097047091224569980L;
    private double dynamicConvergenceRadius = 5;//reinitialise swarms that are within each other's exclusion radius
    private int nexcess = 3;//remove the worst non converged swarm if more than nexcess non converged swarms

    public SelfAdaptingMultiSwarmIterationStrategy(){
        super();
    }

    public SelfAdaptingMultiSwarmIterationStrategy(SelfAdaptingMultiSwarmIterationStrategy copy){
        super();
        this.dynamicConvergenceRadius = copy.dynamicConvergenceRadius;
        this.nexcess = copy.nexcess;
    }

    @Override
    public SelfAdaptingMultiSwarmIterationStrategy getClone() {
        return new SelfAdaptingMultiSwarmIterationStrategy(this);
    }

    public int getNexcess() {
        return nexcess;
    }

    public void setNexcess(int nexcess) {
        this.nexcess = nexcess;
    }

    public double getExclusionRadius() {
        return dynamicConvergenceRadius;
    }

    public void setExclusionRadius(double exlusionRadius) {
        this.dynamicConvergenceRadius = exlusionRadius;
    }

    /**
     * Calculates the dynamic convergence radius that is used to determine both
     * exclusion and convergence
     */
    double calculateRadius() {
        double d = AbstractAlgorithm.get().getOptimisationProblem().getDomain().getDimension();
        //double X = ((Vector) Algorithm.get().getOptimisationProblem().getDomain().getBuiltRepresenation()).getNumeric(0).getBounds().getUpperBound()
        //        - ((Vector) Algorithm.get().getOptimisationProblem().getDomain().getBuiltRepresenation()).getNumeric(0).getBounds().getLowerBound();
        double X = ((Vector) AbstractAlgorithm.get().getOptimisationProblem().getDomain().getBuiltRepresenation()).get(0).getBounds().getUpperBound()
        - ((Vector) AbstractAlgorithm.get().getOptimisationProblem().getDomain().getBuiltRepresenation()).get(0).getBounds().getLowerBound();

        double M = ((MultiSwarm) (AbstractAlgorithm.get())).getPopulations().size();
        return X / (2 * Math.pow(M, 1 / d));

    }

    boolean isConverged(PopulationBasedAlgorithm algorithm) {
        dynamicConvergenceRadius = calculateRadius();

        MultiSwarmDiameterVisitor visitor = new MultiSwarmDiameterVisitor();
        double radius = (Double)((PSO) algorithm).accept(visitor);
        return radius <= dynamicConvergenceRadius;
    }

    @Override
    public void performIteration(MultiSwarm ca) {
    //public void performIteration(MultiSwarms ca) {
        int converged = 0;
        for(ListIterator it=ca.getPopulations().listIterator(); it.hasNext();){
            PopulationBasedAlgorithm currentAlgorithm = (PopulationBasedAlgorithm)it.next();
            if (isConverged(currentAlgorithm)){
                converged++;
            }//if
        }
        if (converged == ca.getPopulations().size()) { //all swarms have converged-> must add swarm
            PopulationBasedAlgorithm pba = ca.getPopulations().get(0).getClone();

            pba.setOptimisationProblem(ca.getOptimisationProblem());
            pba.performInitialisation();
            reInitialise((PSO) pba);
        //    pba.setIterations(Algorithm.get().getIterations());
            ca.addPopulationBasedAlgorithm(pba);// add algorithm
        }// if
        else
            if (ca.getPopulations().size() - converged >= nexcess) { //must remove the worst unconverged swarm
            PopulationBasedAlgorithm weakest = null;

            for (ListIterator it = ca.getPopulations().listIterator(); it.hasNext();) {
                PopulationBasedAlgorithm current = (PopulationBasedAlgorithm) it.next();
                if (isConverged(current)){
                    if (weakest == null    || weakest.getBestSolution().compareTo(current.getBestSolution()) > 0){
                        weakest = current;
                    }//if
                }//if
            }
            ca.removePopulationBasedalgorithm(weakest);
        }// else if

        //perform normal iteration
         for(ListIterator it=ca.getPopulations().listIterator(); it.hasNext();){
             PopulationBasedAlgorithm currentAlgorithm = (PopulationBasedAlgorithm)it.next();
             currentAlgorithm.performIteration();
         }

        //check if swarms are withing exclusionRadius and counts converged swarms
        for (ListIterator it = ca.getPopulations().listIterator(); it.hasNext();) {
            PopulationBasedAlgorithm currentAlgorithm = (PopulationBasedAlgorithm) it.next();
            for (ListIterator other = ca.getPopulations().listIterator(); other.hasNext();) {
                PopulationBasedAlgorithm otherAlgorithm = (PopulationBasedAlgorithm) other.next();
                Vector currentPosition, otherPosition;
                if (!currentAlgorithm.equals(otherAlgorithm)) {
                    currentPosition = (Vector) ((PSO) currentAlgorithm).getBestSolution().getPosition();
                    otherPosition = (Vector) ((PSO) otherAlgorithm).getBestSolution().getPosition();
                    DistanceMeasure dm = new EuclideanDistanceMeasure();
                    double distance = dm.distance(currentPosition,otherPosition);
                    if (distance < dynamicConvergenceRadius) {
                        if (((PSO) currentAlgorithm).getBestSolution().getFitness().compareTo(((PSO) otherAlgorithm).getBestSolution().getFitness()) > 0) {
                            reInitialise((PSO) currentAlgorithm);
                        }//if
                        else {
                            reInitialise((PSO) otherAlgorithm);
                        }// else
                    }// if within radius
                }// if different algorithm
            }// for each pop
        }// for each pop
    }

    public void reInitialise(PSO algorithm) {
        algorithm.performInitialisation();
    }
}
