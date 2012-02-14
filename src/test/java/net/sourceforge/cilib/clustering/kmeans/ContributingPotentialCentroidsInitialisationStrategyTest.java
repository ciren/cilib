package net.sourceforge.cilib.clustering.kmeans;

import java.util.List;

import net.sourceforge.cilib.functions.clustering.ClusteringFunctionTests;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.collection.IsCollectionContaining.hasItem;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsSame.sameInstance;

/**
 * @author Theuns Cloete
 */
public class ContributingPotentialCentroidsInitialisationStrategyTest {

    @Test
    public void testInitialise() {
        CentroidsInitialisationStrategy init = new ContributingPotentialCentroidsInitialisationStrategy();
        DataTable<StandardPattern, TypeList> dataTable = ClusteringFunctionTests.getDataTable();
        DomainRegistry domainRegistry = ClusteringFunctionTests.getDomainRegistry();
        DistanceMeasure distanceMeasure = ClusteringFunctionTests.getDistanceMeasure();
        List<Vector> centroids = init.initialise(dataTable, domainRegistry, distanceMeasure, 4);

        assertThat(centroids.size(), is(4));

        for (Vector centroid : centroids) {
            assertThat(centroid.size(), is(2));
        }
    }

    @Test
    public void testReinitialise() {
        CentroidsInitialisationStrategy init = new ContributingPotentialCentroidsInitialisationStrategy();
        DataTable<StandardPattern, TypeList> dataTable = ClusteringFunctionTests.getDataTable();
        DomainRegistry domainRegistry = ClusteringFunctionTests.getDomainRegistry();
        DistanceMeasure distanceMeasure = ClusteringFunctionTests.getDistanceMeasure();
        List<Vector> centroids = ClusteringFunctionTests.getSeparateCentroids();
        Vector centroidBefore = centroids.get(2);
        Vector centroidAfter = init.reinitialise(centroids, dataTable, domainRegistry, distanceMeasure, 2);

        assertThat(centroids.size(), is(4));
        assertThat(centroids, not(hasItem(centroidBefore)));
        assertThat(centroids, hasItem(centroidAfter));
        assertThat(centroidBefore, not(sameInstance(centroidAfter)));

        for (Vector centroid : centroids) {
            assertThat(centroid.size(), is(2));
        }
    }
}
