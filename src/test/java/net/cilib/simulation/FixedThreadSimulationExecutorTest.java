package net.cilib.simulation;

import org.junit.Assert;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;

/**
 * @since 0.8
 * @author gpampara
 */
public class FixedThreadSimulationExecutorTest {

    private Mockery context = new JUnit4Mockery();

    @Test
    public void testExecute() throws InterruptedException, ExecutionException {
//        SimulationExecutor executor = SimulationExecutors.newFixedThreadExecutor();
//
//        Future<Boolean> result = executor.execute(new Simulation(null, null, null));
//        Assert.assertThat(result.get(), is(true));
    }
}
