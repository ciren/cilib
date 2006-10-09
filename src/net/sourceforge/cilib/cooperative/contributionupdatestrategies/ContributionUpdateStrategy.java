package net.sourceforge.cilib.cooperative.contributionupdatestrategies;

import net.sourceforge.cilib.cooperative.CooperativeEntity;
import net.sourceforge.cilib.entity.Entity;

public interface ContributionUpdateStrategy {
	//TODO this class will need the following things:
	//1. The entity (which stores it's own fitness)
	//2. The participants
	public void updateContribution(Entity src, int srcPos, CooperativeEntity dst, int dstPos, int length);
}
