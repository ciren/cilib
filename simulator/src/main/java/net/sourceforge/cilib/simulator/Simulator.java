/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.simulator;

import com.google.common.collect.Lists;
import java.io.File;
import java.util.Map;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.ProgressEvent;
import net.sourceforge.cilib.algorithm.ProgressListener;
import net.sourceforge.cilib.math.random.generator.seeder.SeedSelectionStrategy;
import net.sourceforge.cilib.math.random.generator.seeder.Seeder;
import net.sourceforge.cilib.problem.Problem;

/**
 * <p>
 * This class represents an instance of a "simulator" that may or may not contain multiple
 * individual simulations.
 * </p>
 * <p>
 * Each simulation experiment is repeated based on the
 * number of samples that the measurement suite requires (although this is subject to change
 * in a future version of the library). In this implementation each experiment is
 * execute in its own thread. Thus, each experiment is execute in parallel for a given simulation.
 * </p>
 * <p>
 * The primary purpose of running simulations is to measure the performance of the given algorithm
 * on a given problem. For that reason, a simulation accepts a measurement suite which it uses to
 * record the performance.
 * </p>
 */
public class Simulator {

    private static final long serialVersionUID = 8987667794610802908L;
    private final Simulation[] simulations;
    private final CopyOnWriteArrayList<ProgressListener> progressListeners;
    private final Map<Simulation, Double> progress;
    private final XMLObjectFactory algorithmFactory;
    private final XMLObjectFactory problemFactory;
    private final XMLObjectFactory measurementFactory;
    private final MeasurementCombiner combiner;
    private final int samples;
    private final SeedSelectionStrategy seeder;

    /**
     * Creates a new instance of Simulator given an algorithm factory, a problem factory and a
     * measurement suite.
     * @see net.sourceforge.cilib.XMLObjectFactory
     * @param algorithmFactory The algorithm factory.
     * @param problemFactory The problem factory.
     * @param measurementFactory The measurement suite.
     */
    public Simulator(XMLObjectFactory algorithmFactory,
            XMLObjectFactory problemFactory,
            XMLObjectFactory measurementFactory,
            MeasurementCombiner combiner, int samples, SeedSelectionStrategy seeder) {
        this.algorithmFactory = algorithmFactory;
        this.problemFactory = problemFactory;
        this.measurementFactory = measurementFactory;
        this.combiner = combiner;
        this.samples = samples;
        this.progressListeners = new CopyOnWriteArrayList<ProgressListener>();
        this.progress = new ConcurrentHashMap<Simulation, Double>();
        this.simulations = new Simulation[samples];
        this.seeder = seeder;
    }

    /**
     * Perform the initialisation of the {@code Simulator} by creating the required
     * {@code Simulation} instances and executing the threads.
     */
    public void init() {
        Seeder.setSeederStrategy(seeder);
        for (int i = 0; i < samples; ++i) {
            simulations[i] = createSimulation();
            progress.put(simulations[i], 0.0);
        }
    }

    public Simulation createSimulation() {
        return new Simulation(this, (Algorithm) algorithmFactory.newObject(),
                (Problem) problemFactory.newObject(),
                (MeasurementSuite) measurementFactory.newObject());
    }

    /**
     * Executes all the experiments for this simulation. The measurement suite will
     * be closed once this method completes.
     */
    public void execute() {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        CompletionService<Simulation> completionService = new ExecutorCompletionService<Simulation>(executor);
        for (int i = 0; i < samples; ++i) {
            completionService.submit(simulations[i], simulations[i]); // The return value is explicitly null.
        }

        final Simulation[] completedSimulations = new Simulation[samples];
        try {
            for (int i = 0; i < samples; i++) {
                Future<Simulation> simulation = completionService.take();
                completedSimulations[i] = simulation.get();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            executor.shutdownNow(); // Shutdown now - time to explode
            throw new RuntimeException(ex);
        }

        // Get the names of the measurements.
        List<String> descriptions = simulations[0].getMeasurementSuite().getDescriptions(); // Law of demeter!

        List<File> fileList = Lists.newArrayList();
        for (Simulation simulation : completedSimulations) {
            fileList.add(simulation.getMeasurementSuite().getFile());
        }
        executor.shutdown();

        combiner.combine(descriptions, fileList);
    }

    /**
     * Terminates all the experiments.
     */
    public void terminate() {
        for (int i = 0; i < samples; ++i) {
            simulations[i].terminate();
        }
    }

    /**
     * Adds a listener for progress events. A progress is fired periodically based on the resolution
     * of the measurements. {@see ProgressEvent} {@see ProgressListener}
     * @param listener The event listener
     */
    public void addProgressListener(ProgressListener listener) {
        progressListeners.add(listener);
    }

    /**
     * Removes a listener for progress events.
     * @param listener The event listener
     */
    public void removeProgressListener(ProgressListener listener) {
        progressListeners.remove(listener);
    }

    private void notifyProgress() {
        double ave = 0;
        for (Double tmp : progress.values()) {
            ave += tmp.doubleValue();
        }

        ave /= progress.size();

        for (ProgressListener listener : progressListeners) {
            listener.handleProgressEvent(new ProgressEvent(ave));
        }
    }

    /**
     * Update the progress of the current simulation with the provided
     * percentage.
     * @param simulation to be updated.
     * @param percentageComplete updated percentage value.
     */
    void updateProgress(Simulation simulation, double percentageComplete) {
        progress.put(simulation, percentageComplete);
        notifyProgress();
    }

    public int getSamples() {
        return samples;
    }
}
