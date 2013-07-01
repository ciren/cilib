/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.nn.architecture.visitors;

import net.cilib.util.Visitor;
import net.cilib.nn.architecture.Architecture;
import net.cilib.util.Cloneable;

/**
 * Interface extends a Visitor that visits a neural network {@link Architecture}
 */
public interface ArchitectureVisitor extends Visitor<Architecture>, Cloneable {

}
