package net.sourceforge.cilib.algorithm;

import java.util.Stack;

public class AlgorithmStack {
	
	private Stack<Algorithm> algorithmStack;
	
	public AlgorithmStack() {
		this.algorithmStack = new Stack<Algorithm>();
	}
	
	public synchronized void push(Algorithm algorithm) {
		this.algorithmStack.push(algorithm);
	}
	
	public synchronized Algorithm pop() {
		return this.algorithmStack.pop();
	}
	
	public synchronized Algorithm peek() {
		return this.algorithmStack.peek();
	}

}
