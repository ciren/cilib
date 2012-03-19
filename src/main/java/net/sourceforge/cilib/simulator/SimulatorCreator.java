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
package net.sourceforge.cilib.simulator;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.util.concurrent.ExecutorService;

/**
 *
 */
class SimulatorCreator implements Provider<Simulator> {

    private final Provider<ExecutorService> service;
    private XMLObjectFactory algorithmFactory;
    private XMLObjectFactory problemFactory;
    private XMLObjectFactory measurementFactory;
    private int samples;
    private MeasurementCombiner combiner;

    @Inject
    SimulatorCreator(Provider<ExecutorService> service) {
        this.service = service;
    }

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

    SimulatorCreator combiner(MeasurementCombiner combiner) {
        this.combiner = combiner;
        return this;
    }

    @Override
    public Simulator get() {
        Simulator simulator = new Simulator(service.get(), algorithmFactory, problemFactory, measurementFactory, combiner, samples);
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
