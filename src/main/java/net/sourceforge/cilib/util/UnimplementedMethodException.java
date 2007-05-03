package net.sourceforge.cilib.util;

/**
 * This exception should be used to "flag" a method that has not been implemented, but needs to be implemented.
 * Usually clone methods and/or copy constructors.
 * @author Theuns Cloete
 */
public class UnimplementedMethodException extends RuntimeException {
	private static final long serialVersionUID = 9006590422435019942L;

	public UnimplementedMethodException() {
	}

	public UnimplementedMethodException(String message) {
		super("This method is not implemented: " + message);
	}
}
