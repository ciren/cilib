/*
 * Diversity.java
 * 
 * Copyright (C) 2003, 2004 - CIRG@UP 
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
package net.sourceforge.cilib.measurement.single.diversity;

import java.util.Iterator;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.measurement.single.diversity.centerinitialisationstrategies.CenterInitialisationStrategy;
import net.sourceforge.cilib.measurement.single.diversity.centerinitialisationstrategies.SpatialCenterInitialisationStrategy;
import net.sourceforge.cilib.measurement.single.diversity.normalisation.NormalisationParameter;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * TODO: Add JavaDoc
 * @author gpampara
 *
 */
public class Diversity implements Measurement {
	private static final long serialVersionUID = 7417526206433000209L;
	private DistanceMeasure distanceMeasure;
	private CenterInitialisationStrategy populationCenter;
	private NormalisationParameter normalisationParameter;
	
	public Diversity() {
		distanceMeasure = new EuclideanDistanceMeasure();
		populationCenter = new SpatialCenterInitialisationStrategy();
		normalisationParameter = new NormalisationParameter();
	}
	
	public Diversity(Diversity other) {
		this.distanceMeasure = other.distanceMeasure;
		this.populationCenter = other.populationCenter;
		this.normalisationParameter = other.normalisationParameter;
	}
	
	public Diversity clone() {
		return new Diversity(this);
	}
	
	public String getDomain() {
		return "R";
	}
	
	public Type getValue() {
		PopulationBasedAlgorithm algorithm = (PopulationBasedAlgorithm) Algorithm.get();
		int numberOfEntities = algorithm.getPopulationSize();
		
		Vector center = (Vector) populationCenter.getCenter();
		Iterator<? extends Entity> populationIterator = algorithm.getTopology().iterator();
		
		double entitySum = 0.0;
		
		while (populationIterator.hasNext()) {
			Vector currentEntityPosition = (Vector) (((Entity) populationIterator.next()).getContents());
			entitySum += distanceMeasure.distance(center, currentEntityPosition);
		}
		
		entitySum /= numberOfEntities;
		
		normalisationParameter.setDistanceMeasure(distanceMeasure);
		entitySum /= normalisationParameter.getValue();
		
		return new Real(entitySum);
	}

	/**
	 * @return the distanceMeasure
	 */
	public DistanceMeasure getDistanceMeasure() {
		return distanceMeasure;
	}

	/**
	 * @param distanceMeasure the distanceMeasure to set
	 */
	public void setDistanceMeasure(DistanceMeasure distanceMeasure) {
		this.distanceMeasure = distanceMeasure;
	}

	/**
	 * @return the normalisationParameter
	 */
	public NormalisationParameter getNormalisationParameter() {
		return normalisationParameter;
	}

	/**
	 * @param normalisationParameter the normalisationParameter to set
	 */
	public void setNormalisationParameter(NormalisationParameter normalizationParameter) {
		this.normalisationParameter = normalizationParameter;
	}

	/**
	 * @return the populationCenter
	 */
	public CenterInitialisationStrategy getPopulationCenter() {
		return populationCenter;
	}

	/**
	 * @param populationCenter the populationCenter to set
	 */
	public void setPopulationCenter(CenterInitialisationStrategy populationCenter) {
		this.populationCenter = populationCenter;
	}

}
