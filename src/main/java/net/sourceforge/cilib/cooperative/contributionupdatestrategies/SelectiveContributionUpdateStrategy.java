package net.sourceforge.cilib.cooperative.contributionupdatestrategies;

import net.sourceforge.cilib.cooperative.CooperativeEntity;
import net.sourceforge.cilib.entity.Entity;

public class SelectiveContributionUpdateStrategy implements ContributionUpdateStrategy {
	public void updateContribution(Entity src, int srcPos, CooperativeEntity dst, int dstPos, int length) {
		//copy participant contribution to context only when the participant's fitness is better than the context's fitness
		if(src.compareTo(dst) > 0)
			dst.update(src, srcPos, dstPos, length);
	}
}
