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
package net.sourceforge.cilib.games.states;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.games.items.GameItem;
import net.sourceforge.cilib.games.items.ItemLocation;

/**
 * This is a {@linkplain GameState} where {@linkplain GameItem}'s are stored in a list of items. This implies that any items position
 * in the list has not correlation to the items location in the game. This is usefull when you have items in large 2 or 3 dimensional spaces
 * and you would more likely look for an item by searching through a list than quering a location in a known structure.
 * @author leo
 */
public class ListGameState extends GameState {
    private static final long serialVersionUID = -3783796952913336520L;
    protected List<GameItem> currentState;
    public ListGameState() {
        currentState = new ArrayList<GameItem>();
    }
    /**
     * Copy constructor
     * @param other
     */
    public ListGameState(ListGameState other){
        currentState = new ArrayList<GameItem>();
        for(int i = 0; i < other.currentState.size(); ++i)
            currentState.add(other.currentState.get(i).getClone());
    }
    public int getSize(){
        return currentState.size();
    }
    /**
     * Add an item to the game state at the end of the list.
     * @param item the item to add
     */
    public void addGameItem(GameItem item){
        currentState.add(item);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void clearState(){
        currentState.clear();
    }
    /**
     * Get an item with the specified index in the list.
     * @param index the index in the list of items
     * @return the item to retrieve
     */
    public GameItem getItem(int index) {
        if(index >= currentState.size())
            throw new RuntimeException("Index greater than vector size");
        return currentState.get(index);
    }
    /**
     * Get an item that matches a given {@linkplain ItemLocation}
     * @param location the location
     * @return the requested item, if the item is not found {@code null} is returned
     */
    public GameItem getItem(ItemLocation location) {
        for(int i = 0; i < currentState.size(); ++i){
            if(currentState.get(i).getLocation().equals(location))
                return currentState.get(i);
        }
        return null;
    }
    /**
     * Returns first item that contains specified token
     * @param itemToken the target token
     * @return the game item
     */
    public GameItem getItem(Enum itemToken){
        for(int i = 0; i < currentState.size(); ++i){
            if(currentState.get(i).getToken() == itemToken)
                return currentState.get(i);
        }
        return null;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ListGameState getClone() {
        return new ListGameState(this);
    }
}
