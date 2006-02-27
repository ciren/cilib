/*
 * SUCOOptimisationAlgorithm.java
 *
 * Created on August 4, 2004, 10:01 PM
 */

package net.sourceforge.cilib.Algorithm;

/** Selective Update Co-Operative Optimisation Algorithm.
 *only updates the context with the contribution of the participant if the overall fitness is improved.
 *
 * @author frans
 */
public class SUCOOptimisationAlgorithm extends CoOperativeOptimisationAlgorithm{
    
    /** Creates a new instance of SUCOOptimisationAlgorithm */
    public SUCOOptimisationAlgorithm() {
    }
    
    protected void performIteration() {
        ParticipatingAlgorithm participant = null;

        for (int i = 0; i < participants; ++i) {
            participant = (ParticipatingAlgorithm) optimisers[i];

            CoOperativeOptimisationProblemAdapter adapter = 
                (CoOperativeOptimisationProblemAdapter)
                    ((OptimisationAlgorithm) optimisers[i]).getOptimisationProblem();
            
            //run the participant with the latest data
            adapter.updateContext(context);

            optimisers[i].performIteration();

            //remember the changes if it is an improvement
            if (this.fitness.compareTo(participant.getContributionFitness()) < 0){
                System.arraycopy(participant.getContribution(), 0, context, adapter.getOffset(), adapter.getDimension());
/*	        for (int j = 0; j < adapter.getDimension(); ++j) {
        	  context[adapter.getOffset() + j] = participant.getContribution()[j];
            	}
*/
                this.fitness = participant.getContributionFitness();
            }         
        }
    }    
}
