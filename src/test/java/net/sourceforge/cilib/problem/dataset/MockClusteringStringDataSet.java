package net.sourceforge.cilib.problem.dataset;

public class MockClusteringStringDataSet extends MockStringDataSet {
	private static final long serialVersionUID = 5346632651777290824L;

	public MockClusteringStringDataSet() {
		patternExpression = ",\\s*Class[\\d+]|,\\s*";
		data = "7.5, 1.5,Class0\n";
		data += "7.0, 3.5, Class0\n";
		data += "6.0, 2.0, Class0\n";
		data += "5.0, 4.0, Class0\n";
		data += "5.0, 2.5, Class1\n";
		data += "1.5, 2.0, Class1\n";
		data += "2.0, 4.0, Class1\n";
		data += "2.0, 6.0, Class1\n";
		data += "2.5, 0.5, Class2\n";
		data += "3.0, 5.0, Class2\n";
		data += "3.0, 7.0, Class2\n";
		data += "3.5, 6.5, Class2\n";
		data += "3.5, 5.0, Class3\n";
		data += "4.0, 3.0, Class3\n";
		data += "3.5, 1.0, Class3\n";
		data += "6.0, 1.0, Class3\n";
	}

}
