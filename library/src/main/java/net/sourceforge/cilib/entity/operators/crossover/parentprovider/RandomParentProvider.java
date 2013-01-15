/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.operators.crossover.parentprovider;

import java.util.List;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;

public class RandomParentProvider extends ParentProvider {

	@Override
	public ParentProvider getClone() {
		return this;
	}

	public Entity f(List<Entity> a) {
		return new RandomSelector<Entity>().on(a).select();
	}
}
