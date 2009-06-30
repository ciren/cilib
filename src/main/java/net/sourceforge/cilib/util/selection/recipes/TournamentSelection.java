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
package net.sourceforge.cilib.util.selection.recipes;

import java.util.Comparator;
import java.util.List;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ProportionalControlParameter;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.Random;
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.Selection.Entry;
import net.sourceforge.cilib.util.selection.ordering.DefaultComparator;
import net.sourceforge.cilib.util.selection.ordering.RandomOrdering;
import net.sourceforge.cilib.util.selection.ordering.SortedOrdering;

/**
 * A recipe for Tournament selection.
 * <p>
 * Tournament selection is performed by:
 * <ol>
 *   <li>Randomly ordering a list of elements.</li>
 *   <li>Selecting a sublist of {@code tournamentSize}.</li>
 *   <li>Sorting the created sublist.</li>
 *   <li>Selecting the best perfroming element.</li>
 *   <li>Return the result.</li>
 * </ol>
 *
 * @param <E> The selection type.
 * @author Wiehann Matthysen
 */
public class TournamentSelection<E extends Comparable> implements SelectionRecipe<E> {
    private static final long serialVersionUID = -6689673224380247931L;

    private ControlParameter tournamentProportion;
    private Comparator<Entry<E>> comparator;
    private Random random;

    /**
     * Create a new instance.
     */
    public TournamentSelection() {
        this.tournamentProportion = new ProportionalControlParameter();
        this.comparator = new DefaultComparator<E>();
        this.random = new MersenneTwister();
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public TournamentSelection(TournamentSelection copy) {
        this.tournamentProportion = copy.tournamentProportion.getClone();
        this.comparator = copy.comparator;
        this.random = copy.random.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TournamentSelection getClone() {
        return new TournamentSelection(this);
    }

    /**
     * Get the size of the tournament.
     * @return The sizeof the tournament.
     */
    public ControlParameter getTournamentSize() {
        return this.tournamentProportion;
    }

    /**
     * Set the size of the tournament.
     * @param tournamanetSize The value to set.
     */
    public void setTournamentSize(ControlParameter tournamanetSize) {
        this.tournamentProportion = tournamanetSize;
    }

    /**
     * Set the comparator for the selection.
     * @param comparator The value to set.
     */
    public void setComparator(Comparator<Entry<E>> comparator) {
        this.comparator = comparator;
    }

    /**
     * Get the comparator for the selection.
     * @return The current comparator.
     */
    public Comparator<Entry<E>> getComparator() {
        return this.comparator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E select(List<? extends E> elements) {
        int tournamentSize = Double.valueOf(this.tournamentProportion.getParameter() * elements.size()).intValue();
        return Selection.from(elements).orderBy(new RandomOrdering<E>(this.random)).
                last(tournamentSize).orderBy(new SortedOrdering<E>(this.comparator)).last().singleSelect();
    }
}
