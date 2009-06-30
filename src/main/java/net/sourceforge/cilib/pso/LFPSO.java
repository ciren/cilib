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
package net.sourceforge.cilib.pso;

import net.sourceforge.cilib.algorithm.GradientOptimisationAlgorithm;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.problem.GradientOptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationProblemAdapter;
import net.sourceforge.cilib.pso.particle.LFDecorator;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.pso.velocityupdatestrategies.LFVelocityUpdate;
import net.sourceforge.cilib.pso.velocityupdatestrategies.VelocityUpdateStrategy;

/**
 * <p>
 * An implementation of the LeapFrog PSO algorithm, a modified Particle Swarm Optimiser
 * based upon the improved leapfrog dynamic method for unconstrained minimization.
 * </p><p>
 * References:
 * </p><p><ul><li>
 * J.A. Snyman, "A new and dynamic method for unconstrained minimization", Applied
 * Mathematical Modelling, 1982, Vol. 6, December.
 * </li><li>
 * J.A. Snyman, "An improved version of the original leapfrog dynamic method for unconstained
 * minimization", Applied Mathematical Modelling, 1983, Vol. 7, June.
 * </li></ul></p>
 *
 * @author  barlad
 *
 * TODO:: Need Global best update strategies
 */
public class LFPSO extends PSO implements GradientOptimisationAlgorithm {
    private static final long serialVersionUID = -1385469595182738903L;
    /**
     * Creates a new instance of <code>LFPSO</code>. All fields are initialised to
     * reasonable defaults. Note that the {@link net.sourceforge.cilib.problem.GradientOptimisationProblem}
     * is initially <code>null</code> and must be set before {@link #initialise()} is called.
     *
     */
    public LFPSO() {
        super();

        velocityUpdate = new LFVelocityUpdate();
        //super.setVelocityUpdate(velocityUpdate);
        //super.setPrototypeParticle(new LFDecorator(new StandardParticle()));
        this.getInitialisationStrategy().setEntityType(new LFDecorator(new StandardParticle()));
    }

    public void setPrototypeParticle(Particle particle) {
        //super.setPrototypeParticle(new LFDecorator(particle));
        this.getInitialisationStrategy().setEntityType(new LFDecorator(particle));
    }

    public void setVelocityUpdate(VelocityUpdateStrategy vu) {
        if (vu instanceof LFVelocityUpdate) {
            throw new UnsupportedOperationException("This operation is not valid on the algorithm level");
            //super.setVelocityUpdate(vu);
        }
        else {
            throw new RuntimeException("Vecocity update must be a LFVelocityUpdate");
        }
    }

    /**
     *  Returns the number of times the gradient of the function was evaluated.
     *  Not implemented yet.
     */
    public int getGradientEvaluations() {
        return 0;
    }

    public GradientOptimisationProblem getGradientOptimisationProblem() {
        return problem;
    }

    public void setGradientOptimisationProblem(GradientOptimisationProblem problem) {
        this.problem = problem;
        super.setOptimisationProblem((OptimisationProblemAdapter) problem);

        velocityUpdate.setGradientOptimisationProblem(problem);
    }

    private GradientOptimisationProblem problem;
    private LFVelocityUpdate velocityUpdate;
}
