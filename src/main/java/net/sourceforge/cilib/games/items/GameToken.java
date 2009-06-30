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
package net.sourceforge.cilib.games.items;
/**
 * This enum defines descriptors for any game items.
 * @author leo
 *
 */
public enum GameToken implements GameEnum {
    DEFAULT;
    /**
     * {@inheritDoc}
     */
    public String getDescription(){
        switch(this){
            case DEFAULT:
                return "Default";
        }
        return "Unkown";
    }
    /**
     * Predator Prey tokens
     * @author leo
     */
    public enum PredatorPrey implements GameEnum{
        PREDATOR,
        PREY;
        /**
         * {@inheritDoc}
         */
        public String getDescription(){
            switch(this){
                case PREDATOR:
                    return "P";
                case PREY:
                    return "Y";
            }
            return "Unkown";
        }
    }
    /**
     * Tick Tack Toe tokens
     * @author leo
     *
     */
    public enum TicTacToe implements GameEnum{
        NOUGHT,
        CROSS;
        /**
         * {@inheritDoc}
         */
        public String getDescription(){
            switch(this){
                case NOUGHT:
                    return "O";
                case CROSS:
                    return "X";
            }
            return "UNKOWN";
        }
    }
    /**
     * Tetris tokens
     * @author leo
     */
    public enum Tetris implements GameEnum{
        BOX,
        LSHAPE,
        RLSHAPE,
        LINE,
        T,
        ZIGZAG,
        RZIGZAG;
        /**
         * {@inheritDoc}
         */
        public String getDescription(){ //finish?!
            switch(this){
                case BOX:
                    return "B";
                case LSHAPE:
                    return "[";
                case RLSHAPE:
                    return "]";
                case LINE:
                    return "|";
                case T:
                    return "T";
                case ZIGZAG:
                    return "/";
                case RZIGZAG:
                    return "\\";
            }
            return "UNKOWN";
        }
    }
}
