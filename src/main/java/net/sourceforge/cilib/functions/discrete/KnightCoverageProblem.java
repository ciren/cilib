/*
 * Copyright (C) 2003 - 2008
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
package net.sourceforge.cilib.functions.discrete;

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.problem.OptimisationProblemAdapter;
import net.sourceforge.cilib.type.DomainParser;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;


/**
 * @author otter
 * Problem description : place the minimun amount of knights on a standard 8 by 8 square board, such that every block
 * is covered by a valid move.
 *
 * This problem is an OptimisationProblem and thus based on the <code>OptimisationProblem</code> interface
 * @deprecated
 */
@Deprecated
public class KnightCoverageProblem extends OptimisationProblemAdapter {
	private static final long serialVersionUID = 3070562330891331356L;

	public KnightCoverageProblem(KnightCoverageProblem copy) {

	}

	public KnightCoverageProblem getClone() {
		return new KnightCoverageProblem(this);
	}

    /**
     * Returns the fitness of a potential solution to this problem. The solution object is described
     * by the domain of this problem, see {@link #getDomain()}. An instance of {@link InferiorFitness}
     * should be returned if the solution falls outside the search space of this problem.
     *
     *
     * @param solution The potential solution found by the optimisation algorithm.
     * @param count True if this call should contribute to the fitness evaluation count, see {@link #getFitnessEvaluations()}.
     * @return The fitness of the solution.
     */
	protected Fitness calculateFitness(Type solution) {
/*        //solution, is supposed to be the Genes, which is of Type - Vector.
    	double fftest = 0.0;
        for(int r =  0; r < ((Vector)solution).getDimension(); r++) {
                if( ((Vector)solution).getBit(r))
                	fftest += 1.0;
            }
*/


        double fit = 0.0;
        char[][] board = new char[8][8];
        //construct a coverage board out of the gene representation...,
        //0 - uncovered open block
        //k - knight
        //n - covered n times.
        for(int r =  0; r < 8; r++) {
            for(int k =  0; k < 8; k++) {
                if (((Vector) solution).getBit(8*r+k))
                    board[r][k] = 'k';
                else
                    board[r][k] = '0';
            }
        }
 /*
        System.out.println("GENE REP:");
        System.out.println(solution);
        System.out.println("BOARD:");
        for(int r =  0; r < 8; r++) {
            for(int k =  0; k < 8; k++) {
                System.out.print(" "+board[r][k]);
            }
            System.out.println();
        }
   */
        this.crawler(board);
     /*   System.out.println("COVERAGE:");
        for(int r =  0; r < 8; r++) {
            for(int k =  0; k < 8; k++) {
                System.out.print(" "+board[r][k]);
            }
            System.out.println();
        }
*/

        //WORK OUT FITNESS, BY PENALIZING CERTAIN PROPERTIES>>>
        for(int r =  0; r < 8; r++) {
            for(int k =  0; k < 8; k++) {
                if(board[r][k] == 'k')
                    fit += 1.0;
                else if(board[r][k] == '0')
                    fit += 1.9;
            }
        }

/*
        if(true)
            throw new RuntimeException("KC PROBLEM");
  */

        return new MinimisationFitness(fit);
    }

    /**
     * Returns the domain component that describes the search space for this problem.
     *
     * @return A {@link net.sourceforge.cilib.Domain.Component} object representing the search space.
     */
    public DomainRegistry getDomain() {
        return null;
    }

    /**
     * Sets the domain of the function.
     *
     * See {@link net.sourceforge.cilib.Domain.Component}.
     *
     * @param representation the string representation for the function domain.
     */
    public void setDomain(String representation) {
    	System.out.println("BINNE");
    	DomainParser.getInstance().parse(representation);
    }

    /**
     * Little helper function in order to work out the coverage
     */
    private void crawler(char[][] board) {

        for(int r =  0; r < 8; r++) {
            for(int k =  0; k < 8; k++) {
                if(board[r][k] == 'k') {   //IDENTIFY KNGIHT POSITIONS
                    //A : Left up
                    if ((r-2 >= 0) && (k-1>= 0) && (board[r-2][k-1] != 'k'))
                        board[r-2][k-1]++;
                    //B
                    if ((r-2 >= 0) && (k+1 <= (7)) && (board[r-2][k+1] != 'k'))
                        board[r-2][k+1]++;
                    //C
                    if ((r-1 >= 0) && (k+2 <= (7)) && (board[r-1][k+2] != 'k'))
                        board[r-1][k+2]++;
                    //D
                    if ((r+1 <= (7)) && (k+2 <= (7)) && (board[r+1][k+2] != 'k'))
                        board[r+1][k+2]++;
                    //E
                    if ((r+2 <= (7)) && (k+1 <= (7)) && (board[r+2][k+1] != 'k'))
                        board[r+2][k+1]++;
                    //F
                    if ((r+2 <= (7)) && (k-1 >= 0) && (board[r+2][k-1] != 'k'))
                        board[r+2][k-1]++;
                    //G
                    if ((r+1 <= (7)) && (k-2 >= 0) && (board[r+1][k-2] != 'k'))
                        board[r+1][k-2]++;
                    //H
                    if ((r-1 >= 0) && (k-2 >= 0) && (board[r-1][k-2] != 'k'))
                        board[r-1][k-2]++;
                }
            }
        }
    }

	public DomainRegistry getBehaviouralDomain() {
		throw new UnsupportedOperationException("Method not implemented.");
	}

}
