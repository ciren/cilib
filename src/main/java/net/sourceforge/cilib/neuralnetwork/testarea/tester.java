package net.sourceforge.cilib.neuralnetwork.testarea;

import java.util.Random;

public class tester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Random ran1 = new Random(System.currentTimeMillis());
		Random ran2 = new Random(System.currentTimeMillis());

		ran1.setSeed(100);
		ran2.setSeed(100);
		
		for (int i = 0; i < 20; i++){
			System.out.println("Ran1 = " + ran1.nextDouble() + ", ran2 = " + ran2.nextDouble());
		}
	}

}
