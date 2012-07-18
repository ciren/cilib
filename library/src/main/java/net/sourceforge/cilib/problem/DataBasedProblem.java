/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.problem;

import net.sourceforge.cilib.problem.dataset.DataProvider;

public class DataBasedProblem implements Problem {

    private static final long serialVersionUID = 1L;

    private Problem problem;
    private DataProvider dataProvider;

    public DataBasedProblem() {
        problem = null;
        dataProvider = null;
    }

    public DataBasedProblem(DataBasedProblem copy) {
        this.problem = copy.problem.getClone();
        this.dataProvider = copy.dataProvider;
    }

    @Override
    public DataBasedProblem getClone() {
        return new DataBasedProblem(this);
    }

    @Override
    public void changeEnvironment() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public Problem getProblem() {
        return this.problem;
    }

    public DataProvider getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }
}
