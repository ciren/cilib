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
package net.sourceforge.cilib.simulator;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.util.concurrent.ExecutorService;

/**
 *
 * @author gpampara
 */
class SimulatorCreator implements Provider<Simulator> {

    @Inject
    private Provider<ExecutorService> service;

    private XMLObjectFactory algorithmFactory;
    private XMLObjectFactory problemFactory;
    private XMLObjectFactory measurementFactory;
    private int samples;

    SimulatorCreator algorithm(XMLObjectFactory algorithmFactory) {
        this.algorithmFactory = algorithmFactory;
        return this;
    }

    SimulatorCreator problem(XMLObjectFactory problemFactory) {
        this.problemFactory = problemFactory;
        return this;
    }

    SimulatorCreator measurement(XMLObjectFactory measurementFactory) {
        this.measurementFactory = measurementFactory;
        return this;
    }

    @Override
    public Simulator get() {
        Simulator simulator = new Simulator(service.get(), algorithmFactory, problemFactory, measurementFactory, samples);
        this.algorithmFactory = null;
        this.problemFactory = null;
        this.measurementFactory = null;
        return simulator;
    }

    SimulatorCreator samples(int samples) {
        this.samples = samples;
        return this;
    }
}
