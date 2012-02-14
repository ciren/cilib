package net.sourceforge.cilib.clustering.kmeans;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import java.util.Collection;

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
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.hamcrest.collection.IsCollectionContaining.hasItem;
import static org.hamcrest.collection.IsCollectionContaining.hasItems;

/**
 * @author Theuns Cloete
 */
public class DataSetBasedCentroidsInitialisationStrategyTest {

    @Test
    public void testInitialise() {
        CentroidsInitialisationStrategy init = new DataSetBasedCentroidsInitialisationStrategy();
        DataTable<StandardPattern, TypeList> dataTable = ClusteringFunctionTests.getDataTable();
        DomainRegistry domainRegistry = ClusteringFunctionTests.getDomainRegistry();
        DistanceMeasure distanceMeasure = ClusteringFunctionTests.getDistanceMeasure();
        List<Vector> centroids = init.initialise(dataTable, domainRegistry, distanceMeasure, 4);

        assertThat(centroids.size(), is(4));

        for (Vector centroid : centroids) {
            assertThat(centroid.size(), is(2));
        }

        Iterable<Vector> vectorsInDataTable = Iterables.transform(dataTable, new Function<StandardPattern, Vector>() {
            @Override
            public Vector apply(StandardPattern from) {
                return from.getVector();
            }
        });

        assertThat(vectorsInDataTable, hasItems(centroids.toArray(new Vector[] {})));
    }

    @Test
    public void testReinitialise() {
        CentroidsInitialisationStrategy init = new DataSetBasedCentroidsInitialisationStrategy();
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

        Iterable<Vector> vectorsInDataTable = Iterables.transform(dataTable, new Function<StandardPattern, Vector>() {
            @Override
            public Vector apply(StandardPattern from) {
                return from.getVector();
            }
        });

        assertThat(vectorsInDataTable, hasItem(centroidAfter));
    }
}
