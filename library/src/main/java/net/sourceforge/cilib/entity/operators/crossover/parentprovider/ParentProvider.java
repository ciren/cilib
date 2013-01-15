/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.operators.crossover.parentprovider;

import fj.F;
import java.util.List;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.util.Cloneable;

/**
 * Selects a parent to be used as the main parent for Crossover strategies that require
 * a variable number of parents.
 */
public abstract class ParentProvider extends F<List<Entity>, Entity> implements Cloneable {

	@Override
	public abstract ParentProvider getClone();
}
