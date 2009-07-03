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
package net.sourceforge.cilib.cooperative.algorithmiterators;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import net.sourceforge.cilib.pso.PSO;

import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/*
 * This is not a good unit test. All unit tests are supposed to test atomic actions, not aggregations.
 * This will need to be refactored.
 */
public class SequentialAlgorithmIteratorTest {
    private AlgorithmIterator<PSO> testIterator = null;
    private ArrayList<PSO> listOfPSOs = null;

    public SequentialAlgorithmIteratorTest() {
        testIterator = new SequentialAlgorithmIterator<PSO>();
        listOfPSOs = new ArrayList<PSO>();
        for (int i = 0; i < 10; i++) {
            listOfPSOs.add(new PSO());
        }
    }

    @Test
    public void testNextAndPrevious() {
        // reset the testIterator
        testIterator.setAlgorithms(listOfPSOs);

        ListIterator<PSO> javaIterator = listOfPSOs.listIterator();

        assertEquals(testIterator.hasNext(), javaIterator.hasNext());
        assertEquals(testIterator.hasPrevious(), javaIterator.hasPrevious());

        while (testIterator.hasNext()) {
            assertSame(testIterator.next(), javaIterator.next());
            assertSame(testIterator.previous(), javaIterator.previous());
            assertSame(testIterator.next(), javaIterator.next());
            assertEquals(testIterator.nextIndex(), javaIterator.nextIndex());
            assertEquals(testIterator.previousIndex(), javaIterator.previousIndex());
            assertEquals(testIterator.hasNext(), javaIterator.hasNext());
            assertEquals(testIterator.hasPrevious(), testIterator.hasPrevious());
        }

        try {
            testIterator.next();
        }
        catch (NoSuchElementException nsee) {
        }

        assertEquals(testIterator.hasNext(), javaIterator.hasNext());
        assertEquals(testIterator.hasPrevious(), javaIterator.hasPrevious());

        while (testIterator.hasPrevious()) {
            assertSame(testIterator.previous(), javaIterator.previous());
            assertSame(testIterator.next(), javaIterator.next());
            assertSame(testIterator.previous(), javaIterator.previous());
            assertEquals(testIterator.nextIndex(), javaIterator.nextIndex());
            assertEquals(testIterator.previousIndex(), javaIterator.previousIndex());
            assertEquals(testIterator.hasNext(), javaIterator.hasNext());
            assertEquals(testIterator.hasPrevious(), testIterator.hasPrevious());
        }

        try {
            testIterator.previous();
        }
        catch (NoSuchElementException nsee) {
        }

        assertEquals(testIterator.hasNext(), javaIterator.hasNext());
        assertEquals(testIterator.hasPrevious(), javaIterator.hasPrevious());
    }

    @Test
    public void testAddAndRemoveAndSet() {
        // reset the testIterator
        testIterator.setAlgorithms(listOfPSOs);

        for (int i = 0; i < listOfPSOs.size(); i++) {
            if (i == 0) {
                try {
                    testIterator.remove();
                }
                catch (IndexOutOfBoundsException iobe) {
                }

                try {
                    testIterator.set(new PSO());
                }
                catch (IndexOutOfBoundsException iobe) {
                }
            }

            testIterator.next();
            if (i > 4 && i < 7)
                testIterator.remove();
            if (i > 7 && i < 10) {
                PSO tmp = new PSO();
                testIterator.set(tmp);
                assertSame(tmp, testIterator.current());
            }
        }

        //    set last element
        PSO tmp = new PSO();
        testIterator.set(tmp);
        assertSame(tmp, testIterator.current());

        testNextAndPrevious();

        // reset the testIterator
        testIterator.setAlgorithms(listOfPSOs);

        // clear the list
        while (testIterator.hasNext()) {
            testIterator.next();
            testIterator.remove();
        }

        assertTrue(listOfPSOs.isEmpty());
        assertFalse(testIterator.hasNext());
        assertFalse(testIterator.hasPrevious());
    }
}
