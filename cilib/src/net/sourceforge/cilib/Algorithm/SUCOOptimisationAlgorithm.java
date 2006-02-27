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
        double testContext[] = new double[context.length];
        
        ParticipatingAlgorithm participant = null;
        for (int i = 0; i < participants; ++i) {
            participant = (ParticipatingAlgorithm) optimisers[i];

            CoOperativeOptimisationProblemAdapter adapter = 
                (CoOperativeOptimisationProblemAdapter)
                    ((OptimisationAlgorithm) optimisers[i]).getOptimisationProblem();
            
            adapter.updateContext(context);

            optimisers[i].performIteration();

            //test the contribution before updating context
            System.arraycopy(context,  0, testContext, 0, testContext.length);
            
            for (int j = 0; j < adapter.getDimension(); ++j) {
               testContext[adapter.getOffset() + j] = participant.getContribution()[j];
            }

            //update the current context only if the fitness is improved.
            if (problem.getFitness(testContext, false).compareTo(problem.getFitness(context,false)) >= 0)
                for (int j = 0; j < adapter.getDimension(); ++j) {
                   context[adapter.getOffset() + j] = participant.getContribution()[j];
                }  
        }
    }
}
