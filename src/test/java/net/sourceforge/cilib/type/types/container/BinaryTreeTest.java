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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import net.sourceforge.cilib.type.types.Real;

import org.junit.Before;
import org.junit.Test;

public class BinaryTreeTest {

    private BinaryTree<Real> doubleTree;

    @Before
    public void setupBinaryTree() {
        doubleTree = new BinaryTree<Real>(new Real(1.0));
        doubleTree.addSubTree(new BinaryTree<Real>(new Real(2.0)));
        doubleTree.addSubTree(new BinaryTree<Real>(new Real(3.0)));
    }

    @Test
    public void getSubtreeWithValidIndex() {
        assertNotNull(doubleTree.getSubTree(0));
        assertNotNull(doubleTree.getSubTree(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getSubtreeWithInvalidIndex() {
        doubleTree.getSubTree(-1);
        doubleTree.getSubTree(2);
    }

    @Test
    public void removalOfSubtreeByKey() {
        Tree<Real> removed = doubleTree.removeSubTree(new Real(2.0));
        assertNotNull(removed);
        assertEquals(2.0, removed.getKey().getReal(), 0);
        assertTrue(doubleTree.getSubTree(new Real(2.0)).isEmpty());
    }

    @Test
    public void removeSubTreeWithElement() {
        assertTrue(doubleTree.remove(new Real(2.0)));
    }

    @Test
    public void removeSubTreeWithIndex() {
        assertNotNull(doubleTree.removeSubTree(0));
    }
}
