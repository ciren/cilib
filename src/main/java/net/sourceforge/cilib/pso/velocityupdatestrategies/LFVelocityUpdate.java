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
package net.sourceforge.cilib.pso.velocityupdatestrategies;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.problem.GradientOptimisationProblem;
import net.sourceforge.cilib.pso.particle.LFDecorator;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author  barlad
 */
public class LFVelocityUpdate implements VelocityUpdateStrategy {
    private static final long serialVersionUID = 2786412133881308328L;

    private StandardVelocityUpdate standard;
    private GradientOptimisationProblem problem;

    /** Creates a new instance of LFVelocityUpdate. */
    public LFVelocityUpdate() {
        standard = new StandardVelocityUpdate();
    }

    public LFVelocityUpdate(LFVelocityUpdate copy) {
        standard = copy.standard.getClone();
    }

    public LFVelocityUpdate getClone() {
        return new LFVelocityUpdate(this);
    }

    public void setStandardVelocityUpdate(StandardVelocityUpdate standard) {
        this.standard = standard;
    }

    public StandardVelocityUpdate getStandardVelocityUpdate() {
        return standard;
    }

    public void updateVelocity(Particle particle) {
        LFDecorator lfParticle = LFDecorator.extract(particle);
        if (particle.getNeighbourhoodBest().getId() == particle.getId()) {
            double p = lfParticle.getP();
            double delta = lfParticle.getDelta();
            double delta1 = lfParticle.getDelta1();
            double deltaT = lfParticle.getDeltaT();
            double epsilon = lfParticle.getEpsilon();
            int s = lfParticle.getS();
            int m = lfParticle.getM();
            int i = lfParticle.getI();
            int j = lfParticle.getJ();

            //double[] velocity = particle.getVelocity();
            //double[] position = particle.getPosition();
            //double[] previousPosition = lfParticle.getPreviousPosition();
            //double[] previousVelocity = lfParticle.getPreviousVelocity();
            Vector velocity = (Vector) particle.getVelocity();
            Vector position = (Vector) particle.getPosition();
            Vector previousPosition = (Vector) lfParticle.getPreviousPosition();
            Vector previousVelocity = (Vector) lfParticle.getPreviousVelocity();

            double[] nextGradient = lfParticle.getNextGradient();

            double[] gradient = lfParticle.getGradient();

            // If the particle was not the neighbourhood best in the previous
            // epoch, reinitialise the leapfrog variables.
            if (!lfParticle.getWasNeighbourhoodBest()) {
                deltaT = lfParticle.getDefaultDeltaT();

                // Reset state variables
                i = 0;
                j = 2;
                s = 0;
                p = 1;

                // LeapFrog Algorithm Step 2
                // Calculate initial gradient
                //double [] functionGradient = getGradient(position);
                Vector functionGradient = getGradient(position);
                for (int l = 0; l < particle.getDimension(); ++l) {
                    //gradient[l] = -functionGradient[l];
                    gradient[l] = -functionGradient.getReal(l);
                }

                // Calculate initial velocity
                for (int l = 0; l < particle.getDimension(); ++l) {

                    //velocity[l] = 0.5 * gradient[l] * deltaT;
                    velocity.setReal(l, 0.5 * gradient[l] * deltaT);
                }

                lfParticle.setWasNeighbourhoodBest(true);
            }

            // LeapFrog Algorithm Step 3
            double lenDeltaX = calculateEuclidianLength(velocity) * deltaT;

            // LeapFrog Algorithm Step 4
            if (lenDeltaX < delta) {
                // LeapFrog Algorithm Step 5a: Update p and deltaT
                p = p + delta1;
                deltaT = deltaT * p;
            }
            else {
                for (int l = 0; l < particle.getDimension(); ++l) {
                    //velocity[l] = delta * velocity[l] / (lenDeltaX);
                    velocity.setReal(l, delta * velocity.getReal(l) / (lenDeltaX));
                }

                // LeapFrog Algorithm Step 5b
                if (s < m) {
                    deltaT = deltaT / 2.0;
                    double tmp;
                    for (int l = 0; l < particle.getDimension(); ++l) {
                        /*tmp = position[l] + previousPosition[l];
                        previousPosition[l] = position[l];
                        position[l] = tmp / 2.0;

                        tmp = velocity[l] + previousVelocity[l];
                        previousVelocity[l] = velocity[l];
                        velocity[l] = tmp / 2.0;
                        s = 0;*/

                        tmp = position.getReal(l) + previousPosition.getReal(l);
                        previousPosition.setReal(l, position.getReal(l));
                        position.setReal(l, tmp / 2.0);

                        tmp = velocity.getReal(l) + previousVelocity.getReal(l);
                        previousVelocity.setReal(l, velocity.getReal(l));
                        velocity.setReal(l, tmp / 2.0);
                        s = 0;
                    }
                }
            }

            // Step 5: Update position;
            for (int l = 0; l < particle.getDimension(); ++l) {
                //previousPosition[l] = position[l];
                //position[l] += velocity[l] * deltaT;
                previousPosition.setReal(l, position.getReal(l));
                position.setReal(l, position.getReal(l)+velocity.getReal(l)*deltaT);
            }

            boolean repeatEndLoop = true;

            while (repeatEndLoop) {
                // LeapFrog Algorithm Step 6
                //double [] functionGradient = getGradient(position);
                Vector functionGradient = getGradient(position);
                for (int l = 0; l < particle.getDimension(); ++l) {
                    //nextGradient[l] = -functionGradient[l];
                    nextGradient[l] = -functionGradient.getReal(l);
                }

                for (int l = 0; l < particle.getDimension(); ++l) {
                    //previousVelocity[l] = velocity[l];
                    //velocity[l] += nextGradient[l] * deltaT;
                    previousVelocity.setReal(l, velocity.getReal(l));
                    velocity.setReal(l, velocity.getReal(i)+nextGradient[l]*deltaT);
                }

                // LeapFrog Algorithm Step 7a
                if (calculateDotProduct(nextGradient, gradient) > 0) {
                    s = 0;
                }
                else {
                    ++s;
                    p = 1;
                }

                for (int l = 0; l < particle.getDimension(); ++l) {
                    gradient[l] = nextGradient[l];
                }

                // LeapFrog Algorithm Step 7
                if (calculateEuclidianLength(nextGradient) > epsilon) {
                    // LeapFrog Algorithm Step 8
                    if (calculateEuclidianLength(velocity) > calculateEuclidianLength(previousVelocity)) {
                        i = 0;
                        repeatEndLoop = false;
                        }
                    else {
                        double tmp;
                        for (int l = 0; l < particle.getDimension(); ++l) {
                            /*tmp = position[l] + previousPosition[l];
                            previousPosition[l] = position[l];
                            position[l] = tmp / 2.0;*/
                            tmp = position.getReal(l) + previousPosition.getReal(l);
                            previousPosition.setReal(l, position.getReal(l));
                            position.setReal(l, tmp/2.0);
                        }
                        ++i;

                        // LeapFrog Algorithm  Step 9
                        if (i <= j) {
                            for (int l = 0; l < particle.getDimension(); ++l) {
                                //tmp = velocity[l] + previousVelocity[l];
                                //previousVelocity[l] = velocity[l];
                                //velocity[l] = tmp / 4.0;
                                tmp = velocity.getReal(l) + previousVelocity.getReal(l);
                                previousVelocity.setReal(l, velocity.getReal(l));
                                velocity.setReal(l, tmp/4.0);
                            }
                        }
                        else {
                            for (int l = 0; l < particle.getDimension(); ++l) {
                               // previousVelocity[l] = velocity[l];
                               // velocity[l] = 0;
                                previousVelocity.setReal(l, velocity.getReal(l));
                                velocity.setReal(l, 0.0);
                                j = 1;
                            }
                        }
                    }
                }
                else {
                    repeatEndLoop = false;
                }
            }

            // Set the state values in the particle
            lfParticle.setP(p);
            lfParticle.setDeltaT(deltaT);
            lfParticle.setS(s);
            lfParticle.setI(i);
            lfParticle.setJ(j);
        }
        else {
            lfParticle.setWasNeighbourhoodBest(false);
            standard.updateVelocity(particle);
        }
    }

    /**
     *  Returns the euclidian length of a vector x
     */
    private double calculateEuclidianLength(Vector x) {
        double l = 0;
        /*for (int i = 0; i < x.length; i++) {
            l += x[i] * x[i];
        } */
        for (int i = 0; i < x.getDimension(); i++) {
            l += x.getReal(i) * x.getReal(i);
        }
        return (Math.sqrt(l));
    }

    private double calculateEuclidianLength(double [] x) {
        double l = 0;
        for (int i = 0; i < x.length; i++) {
            l += x[i] * x[i];
        }
        return Math.sqrt(l);
    }

    /**
     *  Returns the dot product of two vectors.
     */
    private double calculateDotProduct(double [] x, double [] y) {
        if (x.length != y.length) {
            throw new RuntimeException("Cannot calculate dot product because vectors are of different sizes.");
        }
        else {
            double t = 0;
            for (int i = 0; i < x.length; i++) {
            t += x[i] * y[i];
            }
            return t;
        }
    }

    public void setGradientOptimisationProblem(GradientOptimisationProblem problem) {
        this.problem = problem;
    }

    /**
     * Returns the gradient of the problem function at the given position.
     */
    public Vector getGradient(Vector position) {
        return problem.getGradient(position);
    }


    public void updateControlParameters(Particle particle) {
        // TODO Auto-generated method stub

    }
}
