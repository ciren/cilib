/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.entity.operators.crossover.parentprovider;

import fj.F;
import java.util.List;
import net.cilib.entity.Entity;
import net.cilib.util.Cloneable;

/**
 * Selects a parent to be used as the main parent for Crossover strategies that require
 * a variable number of parents.
 */
public abstract class ParentProvider extends F<List<Entity>, Entity> implements Cloneable {

	@Override
	public abstract ParentProvider getClone();
}
