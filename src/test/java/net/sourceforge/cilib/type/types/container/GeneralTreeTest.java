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
package net.sourceforge.cilib.type.types.container;

import static org.junit.Assert.assertEquals;
import net.sourceforge.cilib.container.visitor.PrePostVisitor;
import net.sourceforge.cilib.type.types.Real;

import org.junit.Test;

public class GeneralTreeTest {

    @Test
    public void creation() {
        Tree<Real> tree = new GeneralTree<Real>(new Real(3.0));

        assertEquals(0, tree.getDegree());

        tree.add(new Real(1.0));
        tree.add(new Real(2.0));

        assertEquals(2, tree.getDegree());

        Tree<Real> child = tree.getSubTree(new Real(2.0));
        assertEquals(0, child.getDegree());
    }

    @Test
    public void preOrderVisitorTraversal() {
        Tree<Real> tree = new GeneralTree<Real>(new Real(0.0));
        tree.add(new Real(1.0));
        tree.add(new Real(2.0));
        tree.add(new Real(3.0));

        assertEquals(3, tree.size());

        Tree<Real> child1 = tree.getSubTree(new Real(1.0));
        child1.add(new Real(4.0));

        assertEquals(1, child1.size());

        StringBuilder buffer = new StringBuilder();
        PrintingVisitor<Real> visitor = new PrintingVisitor<Real>(buffer);

        tree.accept(visitor);

        assertEquals("0.0,1.0,4.0,2.0,3.0", buffer.toString());
    }

    private class PrintingVisitor<E> extends PrePostVisitor<E> {
        private StringBuilder buffer;

        public PrintingVisitor(StringBuilder buffer) {
            this.buffer = buffer;
        }

        @Override
        public void visit(E o) {
            if (buffer.length() != 0)
                buffer.append(",");

            buffer.append(o.toString());
        }
    }
}
