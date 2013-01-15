/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single.diversity;

import java.util.Iterator;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.measurement.single.diversity.centerinitialisationstrategies.CenterInitialisationStrategy;
import net.sourceforge.cilib.measurement.single.diversity.centerinitialisationstrategies.SpatialCenterInitialisationStrategy;
import net.sourceforge.cilib.measurement.single.diversity.normalisation.DiversityNormalisation;
import net.sourceforge.cilib.measurement.single.diversity.normalisation.NormalisationParameter;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.distancemeasure.DistanceMeasure;
import net.sourceforge.cilib.util.distancemeasure.EuclideanDistanceMeasure;

/**
 * TODO: Add JavaDoc.
 *
 */
public class Diversity implements Measurement<Real> {

    private static final long serialVersionUID = 7417526206433000209L;
    protected DistanceMeasure distanceMeasure;
    protected CenterInitialisationStrategy populationCenter;
    protected DiversityNormalisation normalisationParameter;

    public Diversity() {
        distanceMeasure = new EuclideanDistanceMeasure();
        populationCenter = new SpatialCenterInitialisationStrategy();
        normalisationParameter = new NormalisationParameter();
    }

    public Diversity(Diversity other) {
        this.distanceMeasure = other.distanceMeasure;
        this.populationCenter = other.populationCenter;
        this.normalisationParameter = other.normalisationParameter;
    }

    @Override
    public Diversity getClone() {
        return new Diversity(this);
    }

    @Override
    public Real getValue(Algorithm algorithm) {
        PopulationBasedAlgorithm populationBasedAlgorithm = (PopulationBasedAlgorithm) algorithm;
        int numberOfEntities = populationBasedAlgorithm.getTopology().size();

        Vector center = populationCenter.getCenter(populationBasedAlgorithm.getTopology());
        Iterator<? extends Entity> populationIterator = populationBasedAlgorithm.getTopology().iterator();

        double distanceSum = 0.0;

        while (populationIterator.hasNext()) {
            Vector currentEntityPosition = (Vector) (((Entity) populationIterator.next()).getCandidateSolution());
            distanceSum += distanceMeasure.distance(center, currentEntityPosition);
        }

        distanceSum /= numberOfEntities;
        distanceSum /= normalisationParameter.getNormalisationParameter(populationBasedAlgorithm);

        return Real.valueOf(distanceSum);
    }

    /**
     * @return the distanceMeasure
     */
    public DistanceMeasure getDistanceMeasure() {
        return distanceMeasure;
    }

    /**
     * @param distanceMeasure the distanceMeasure to set
     */
    public void setDistanceMeasure(DistanceMeasure distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
    }

    /**
     * @return the normalisationParameter
     */
    public DiversityNormalisation getNormalisationParameter() {
        return normalisationParameter;
    }

    /**
     * @param normalisationParameter the normalisationParameter to set
     */
    public void setNormalisationParameter(DiversityNormalisation normalisationParameter) {
        this.normalisationParameter = normalisationParameter;
    }

    /**
     * @return the populationCenter
     */
    public CenterInitialisationStrategy getPopulationCenter() {
        return populationCenter;
    }

    /**
     * @param populationCenter the populationCenter to set
     */
    public void setPopulationCenter(CenterInitialisationStrategy populationCenter) {
        this.populationCenter = populationCenter;
    }
}
