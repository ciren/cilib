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
package net.sourceforge.cilib.entity.operators.crossover;

import static com.google.common.base.Preconditions.checkState;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p>
 * Parent Centric Crossover Strategy
 * </p>
 * 
 * <p>
 * References:
 * </p>
 * 
 * <p>
 * Deb, K.; Joshi, D.; Anand, A.; , "Real-coded evolutionary algorithms with parent-centric recombination,"
 * Evolutionary Computation, 2002. CEC '02. Proceedings of the 2002 Congress on , vol.1, no., pp.61-66, 12-17 May 2002
 * doi: 10.1109/CEC.2002.1006210
 * </p>
 * <p>
 * The code is based on the MOEA Framework under the LGPL license: http://www.moeaframework.org
 * </p>
 * 
 * @author filipe
 */
public class ParentCentricCrossoverStrategy extends CrossoverStrategy {
    
    private int numberOfOffspring;
    private ControlParameter deviation1;
    private ControlParameter deviation2;
    private GaussianDistribution random;
    
    /**
     * Default constructor
     */
    public ParentCentricCrossoverStrategy() {
        this.numberOfOffspring = 1;
        this.deviation1 = new ConstantControlParameter(0.1);
        this.deviation2 = new ConstantControlParameter(0.1);
        this.random = new GaussianDistribution();
    }
    
    /**
     * Copy constructor
     * 
     * @param copy Crossover strategy to copy
     */
    public ParentCentricCrossoverStrategy(ParentCentricCrossoverStrategy copy) {
        this.numberOfOffspring = copy.numberOfOffspring;
        this.deviation1 = copy.deviation1.getClone();
        this.deviation2 = copy.deviation2.getClone();
        this.random = copy.random;
    }

    /**
     * Clones this crossover strategy
     * 
     * @return The clone of this crossover strategy
     */
    @Override
    public CrossoverStrategy getClone() {
        return new ParentCentricCrossoverStrategy(this);
    }

    /**
     * Performs the parent centric crossover strategy<br/>
     * Note:
     * The selected parent is placed at the end of the parent list
     * 
     * @param parentCollection List of parents to use to perform crossover
     * @return List of offspring calculated using this crossover strategy
     */
    @Override
    public List<Entity> crossover(List<Entity> parentCollection) {
        checkState(parentCollection.size() >= 3, "There must be a minimum of three parents to perform parent-centric crossover.");
        checkState(numberOfOffspring > 0, "At least one offspring must be generated. Check 'numberOfOffspring'.");
        
        List<Entity> parents = new ArrayList<Entity>(parentCollection);
        UniformDistribution randomParent = new UniformDistribution();
        List<Entity> offspring = new ArrayList<Entity>();
        int n = parents.size();
        
        //calculate mean of parents
        Vector mean = (Vector) parents.get(0).getCandidateSolution();
        for (int i = 1; i < n; i++) {
            mean = mean.plus((Vector) parents.get(i).getCandidateSolution());
        }
        
        mean.divide(n);

        //get each offspring
        for (int os = 0; os < numberOfOffspring; os++) {
            int parent = (int) randomParent.getRandomNumber(0.0, n);
            Entity temp = parents.get(parent);
            parents.set(parent, parents.get(n - 1));
            parents.set(n - 1, temp);
            
            List<Vector> eList = new ArrayList<Vector>();
            eList.add(((Vector) parents.get(n - 1).getCandidateSolution()).subtract(mean));

            double D = 0.0;

            //basis vectors defined by remainder of parents
            for (int i = 0; i < n - 1; i++) {
                Vector d = ((Vector) parents.get(i).getCandidateSolution()).subtract(mean);

                if (!isZero(d)) {
                    Vector e = Vector.copyOf(d);
                    for (Vector v : eList) {
                        e = e.subtract(v.multiply(e.dot(v) / v.dot(v)));
                    }

                    if (!isZero(e)) {
                        D += e.length();
                        eList.add(e.normalize());
                    }
                }
            }

            D /= n - 1;

            //construct the offspring
            Vector child = ((Vector) parents.get(n - 1).getCandidateSolution())
                            .plus(eList.get(0).multiply(random.getRandomNumber(0.0, deviation1.getParameter())));
        
            for (int i = 1; i < eList.size(); i++) {
                child = child.plus(eList.get(i).multiply(random.getRandomNumber(0.0, deviation2.getParameter()) * D));
            }

            Entity result = parents.get(n - 1).getClone();
            result.setCandidateSolution(child);
            offspring.add(result);
        }

        return offspring;
    }

    /**
     * Sets the deviation for the first Gaussian number
     * 
     * @param dev The deviation to use
     */
    public void setDeviation1(ControlParameter dev) {
        this.deviation1 = dev;
    }

    /**
     * Sets the deviation for the second Gaussian number
     * 
     * @param dev The deviation to use
     */
    public void setDeviation2(ControlParameter dev) {
        this.deviation2 = dev;
    }

    /**
     * Sets the number of offspring to calculate
     * 
     * @param numberOfOffspring The number of offspring required
     */
    public void setNumberOfOffspring(int numberOfOffspring) {
        this.numberOfOffspring = numberOfOffspring;
    }
    
    /**
     * Determines if the given vector is a zero vector
     * 
     * @param v The vector to check
     * @return True if the vector is a zero vector, false otherwise
     */
    private boolean isZero(Vector v) {
        double epsilon = 0.00000001;
        for (Numeric n : v) {
            if (Math.abs(n.doubleValue()) > epsilon) {
                return false;
            }
        }

        return true;
    }
    
}
