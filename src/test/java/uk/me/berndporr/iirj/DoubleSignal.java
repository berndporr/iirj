package uk.me.berndporr.iirj;

class DoubleSignal
{
	private final double[] xValues;
	private final double[] values;
	private final String name;

	DoubleSignal(int length, String name) {
		xValues = new double[length];
		values = new double[length];
		this.name = name;
	}

	void setValue(int index, double time, double value) {
		xValues[index] = time;
		values[index] = value;
	}

	double getValue(int index) {
		return values[index];
	}

	double getXValue(int index) {
		return xValues[index];
	}

	double[] getxValues() {
		return xValues;
	}

	double[] getValues() {
		return values;
	}

	int getSize() {
		return values.length;
	}

	String getName() {
		return this.name;
	}
}
