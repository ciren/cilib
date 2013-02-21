/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.io.transform;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract DataOperator that applies the data operation to a selected
 * number of elements only. Selection of elements occurs numerically as a list
 * integers. The concrete extension of the class processes the list to determine
 * selected items.
 */
public abstract class SelectiveDataOperator implements DataOperator {

    protected List<Integer> selectedItems;

    /**
     * Default constructor. Initialises selectedItems.
     */
    public SelectiveDataOperator() {
        selectedItems = new ArrayList<Integer>();
    }

    public SelectiveDataOperator(SelectiveDataOperator rhs) {
        selectedItems = new ArrayList<Integer>();
        for (Integer curInt : rhs.selectedItems)
            selectedItems.add(new Integer(curInt.intValue()));
    }

    @Override
    public abstract SelectiveDataOperator getClone();

    /**
     * Add number to selected items.
     * @param selected a selected item.
     */
    public void addSelection(int selected) {
        selectedItems.add(selected);
    }

    /**
     * Add all numbers to selected items.
     * @param selection a number of selected items.
     */
    public void addSelection(List<Integer> selection) {
        selectedItems.addAll(selection);
    }

    /**
     * Parses the string and adds the selection to the selected items.
     * @param selection
     */
    public void addSelection(String selection) {
        List<Integer> selectionList = parseSelectionString(selection);
        for (Integer item : selectionList) {
            this.addSelection(item);
        }
    }

    /**
     * Parses the string for a range representing a selection in the form:
     * lower_bound : upper_bound
     * @param selection string in the form: "int colon int".
     * @return list of integers representing selection.
     */
    public static List<Integer> parseSelectionString(String selection) {
        String[] tokens = selection.split("[:]");
        if (tokens.length != 2)
            throw new UnsupportedOperationException("Expected string: \"lower_bound : upper_bound\"");
        int lower = -1;
        try {
            lower = Integer.parseInt(tokens[0].trim());
        }
        catch (NumberFormatException ex) {
            throw new UnsupportedOperationException("Lower bound not a valid integer: "+tokens[0]);
        }

        int upper = -1;
        try {
            upper = Integer.parseInt(tokens[1].trim());
        }
        catch (NumberFormatException ex) {
            throw new UnsupportedOperationException("Upper bound not a valid integer: "+tokens[1]);
        }

        if (lower > upper) {
            throw new UnsupportedOperationException("Lower bound is larger than upper bound.");
        }

        List<Integer> selectionList = new ArrayList<Integer>();
        for (int i = lower; i <= upper; i++) {
            selectionList.add(i);
        }
        return selectionList;
    }

    /**
     * Gets the selected items.
     * @return the selected items.
     */
    public List<Integer> getSelectedItems() {
        return selectedItems;
    }

    /**
     * Sets the selected items.
     * @param selectedItems the new selected items.
     */
    public void setSelectedItems(List<Integer> selectedItems) {
        this.selectedItems = selectedItems;
    }
}
