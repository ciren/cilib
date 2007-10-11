package net.sourceforge.cilib.pso.niching;

import java.util.Comparator;

import net.sourceforge.cilib.container.Pair;
import net.sourceforge.cilib.entity.Entity;

public class PairDistanceAndParticleComparator implements Comparator<Pair<Double, Entity>>
{
	public int compare(Pair<Double, Entity> o1, Pair<Double, Entity> o2)
	{
		return o1.getKey().compareTo(o2.getKey());
	}
}
