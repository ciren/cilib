/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.cooperative.algorithmiterators;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import net.sourceforge.cilib.algorithm.iterator.AlgorithmIterator;
import net.sourceforge.cilib.algorithm.iterator.SequentialAlgorithmIterator;
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
