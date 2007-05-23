package net.sourceforge.cilib.algorithm.proxy;

import net.sourceforge.cilib.algorithm.Algorithm;

public class LocalAlgorithmProxy implements AlgorithmProxy {
	
	private static ThreadLocal<Algorithm> localInstance = new ThreadLocal<Algorithm>();

	public Algorithm get() {	
		Algorithm current = localInstance.get();
		if (current != null)
			current = current.getCurrentAlgorithm();
		return current;
	}

	public void set(Algorithm algorithm) {
		localInstance.set(algorithm);
	}

}
