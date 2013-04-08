/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.architecture.visitors;

import net.sourceforge.cilib.util.Visitor;
import net.sourceforge.cilib.nn.architecture.Architecture;
import net.sourceforge.cilib.util.Cloneable;

/**
 * Interface extends a Visitor that visits a neural network {@link Architecture}
 */
public interface ArchitectureVisitor extends Visitor<Architecture>, Cloneable {

}
