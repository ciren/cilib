/*
 * GBestMergeStrategy.java
 *
 * Created on 13 May 2006
 *
 * Copyright (C) 2003 - 2006 
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
 *
 */
package net.sourceforge.cilib.pso.niching;

import java.util.List;
import java.util.ListIterator;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.visitor.RadiusVisitor;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * 
 * @author Edrich van Loggerenberg
 * @author Segun
 * @author Gary Pampara
 */
public class GBestMergeStrategy<E extends PopulationBasedAlgorithm> implements
	MergeStrategy<E>
{

    private ControlParameter threshold;

    public GBestMergeStrategy()
    {
	this.threshold = new ConstantControlParameter(0.001);
    }

    public void merge(PSO mainSwarm, List<PopulationBasedAlgorithm> subSwarms)
    {
	for (ListIterator<PopulationBasedAlgorithm> i = subSwarms.listIterator(); i.hasNext();)
	{
	    PSO subSwarm1 = (PSO) i.next();

	    if (subSwarm1 != null)
	    {
		Particle gBestParticle1 = subSwarm1.getBestParticle();

		for (ListIterator<PopulationBasedAlgorithm> j = subSwarms.listIterator(); j.hasNext();)
		{
		    PSO subSwarm2 = (PSO) j.next();

		    if (subSwarm2 != null)
		    {
			if (subSwarm1 != subSwarm2)
			{
			    Particle gBestParticle2 = subSwarm2.getBestParticle();

			    if (TestNearZero(getRadius(subSwarm1)) && TestNearZero(getRadius(subSwarm2)))
			    {
			    if (normalise(mainSwarm, gBestParticle1, gBestParticle2) < threshold.getParameter())
				{
				    subSwarm1.getTopology().addAll(subSwarm2.getTopology());
				    // the two swarms are now merged, so delete the one
				    j.remove();
				    i = subSwarms.listIterator();

				    subSwarm1.getInitialisationStrategy().setEntityNumber(subSwarm1.getTopology().size());
				    subSwarm2.getInitialisationStrategy().setEntityNumber(subSwarm2.getTopology().size());
				    //System.out.println("merged (normalised): S1R: " + subSwarm1.getRadius() + " S2R: " + subSwarm2.getRadius());
				    break;
				}
			    }
			    else
			    {
				DistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();
				
				//double distance = distanceMeasure.distance(subSwarm1.getAveragePosition(), subSwarm2.getAveragePosition());
				//changed from average particle position to best particle position...
				double distance = distanceMeasure.distance(((Vector)subSwarm1.getBestParticle().getPosition()), ((Vector)subSwarm2.getBestParticle().getPosition()));

				if ((distance < (getRadius(subSwarm1) + getRadius(subSwarm2))) && (distance < 0.001)) // if ((distance < 0.001))
				{
				    subSwarm1.getTopology().addAll(subSwarm2.getTopology());
				    // the two swarms are now merged, so delete the one
				    j.remove();
				    i = subSwarms.listIterator();

				    subSwarm1.getInitialisationStrategy().setEntityNumber(subSwarm1.getTopology().size());
				    subSwarm2.getInitialisationStrategy().setEntityNumber(subSwarm2.getTopology().size());
				    //System.out.println("merged: " + distance + " S1R: " + subSwarm1.getRadius() + " S2R: " + subSwarm2.getRadius());
				    break;
				}
			    }
			}
		    }
		}
	    }
	}
    }

    private double getRadius(PSO subSwarm) {
    	RadiusVisitor radiusVisitor = new RadiusVisitor();
    	subSwarm.accept(radiusVisitor);
    	return radiusVisitor.getResult();
	}

	public boolean TestNearZero(double value)
    {
	if (value < 0.0001)
	    return true;
	else
	    return false;
    }
    
    public double normalise(PSO mainSwarm, Particle gBest1, Particle gBest2)
    {
	Real range = (Real) ((Vector) mainSwarm.getOptimisationProblem().getDomain().getBuiltRepresenation().clone()).get(0);

	Vector gBestPos1 = (Vector) gBest1.getPosition();
	Vector gBestPos2 = (Vector) gBest2.getPosition();

	Vector v1 = new Vector();
	Vector v2 = new Vector();

	for (int i = 0; i < gBest1.getPosition().getDimension(); i++) {
		v1.append(new Real((gBestPos1.getReal(i) - range.getLowerBound()) / (range.getUpperBound() - range.getLowerBound())));
		v2.append(new Real((gBestPos2.getReal(i) - range.getLowerBound()) / (range.getUpperBound() - range.getLowerBound())));
	}

	double sum = 0.0;
	for (int i = 0; i < gBest1.getPosition().getDimension(); i++)
	{
	    sum += (v1.getReal(i) - v2.getReal(i)) * (v1.getReal(i) - v2.getReal(i));
	    sum = Math.sqrt(sum);
	}
	return sum;
}

    public ControlParameter getThreshold()
    {
	return threshold;
    }

    public void setThreshold(ControlParameter threshold)
    {
	this.threshold = threshold;
    }

}
