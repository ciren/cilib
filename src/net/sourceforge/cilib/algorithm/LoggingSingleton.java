package net.sourceforge.cilib.algorithm;

import org.apache.log4j.PropertyConfigurator;

public class LoggingSingleton {
	
	private static LoggingSingleton instance;
	
	private LoggingSingleton() {
		System.out.println("here only once");
		PropertyConfigurator.configure("log4j.properties");
	}
	
	public static LoggingSingleton initialise() {
		if (instance == null)
			instance = new LoggingSingleton();
		
		return instance;
	}

}
