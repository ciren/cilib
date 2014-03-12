/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.moo.wfg;

import net.sourceforge.cilib.problem.MOOptimisationProblem;

public abstract class WFGProblem extends MOOptimisationProblem {
    
    protected int m;
    protected int l;
    
    WFGProblem() {
       this.m = 3;
       this.l = 20;
       initialize();
    }
    
    WFGProblem(WFGProblem copy) {
        super(copy);
        this.m = copy.m;
        this.l = copy.l;
        initialize();
    }
    
    protected abstract void initialize();

    public void setM(int m) {
        this.m = m;
        initialize();
    }
    
    public int getM() {
        return this.m;
    }
    
    public void setL(int l) {
        this.l = l;
        initialize();
    }
    
    public int getL() {
        return this.l;
    }
}
