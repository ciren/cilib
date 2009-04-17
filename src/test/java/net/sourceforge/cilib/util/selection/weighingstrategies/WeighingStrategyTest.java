/**
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

package net.sourceforge.cilib.util.selection.weighingstrategies;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.List;

import net.sourceforge.cilib.container.Pair;

import org.junit.Test;

public class WeighingStrategyTest {
	
	@Test
	public void testInverseWeighingStrategy() {
		UniformWeighingStrategy<Double, Object> uniformWeighingStrategy = new UniformWeighingStrategy<Double, Object>();
		double weight = 2.0;
		uniformWeighingStrategy.setWeight(weight);
		InverseWeighingStrategyDecorator<Object> inverseWeighingStrategyDecorator = 
			new InverseWeighingStrategyDecorator<Object>();
		inverseWeighingStrategyDecorator.setWeighingStrategy(uniformWeighingStrategy);
		List<Object> objects = Collections.nCopies(10, new Object());
		List<Pair<Double, Object>> weighedObjects = inverseWeighingStrategyDecorator.weigh(objects);
		for (Pair<Double, Object> weighedObject : weighedObjects) {
			assertThat(weighedObject.getKey(), is(1.0 / weight));
		}
	}
	
	@Test
	public void testLinearWeighingStrategy() {
		LinearWeighingStrategy<Object> linearWeighingStrategy = new LinearWeighingStrategy<Object>();
		List<Object> objects = Collections.nCopies(10, new Object());
		linearWeighingStrategy.setMinWeight(1.0);
		linearWeighingStrategy.setMaxWeight(objects.size());
		List<Pair<Double, Object>> weighedObjects = linearWeighingStrategy.weigh(objects);
		for (int i = 0; i < objects.size(); ++i) {
			assertThat(weighedObjects.get(i).getKey(), is(Integer.valueOf(i + 1).doubleValue()));
		}
	}
	
	@Test
	public void testUniformWeighingStrategy() {
		UniformWeighingStrategy<Double, Object> uniformWeighingStrategy = new UniformWeighingStrategy<Double, Object>();
		double weight = 1.0;
		uniformWeighingStrategy.setWeight(weight);
		List<Object> objects = Collections.nCopies(10, new Object());
		List<Pair<Double, Object>> weighedObjects = uniformWeighingStrategy.weigh(objects);
		for (Pair<Double, Object> weighedObject : weighedObjects) {
			assertThat(weighedObject.getKey(), is(weight));
		}
	}
}
