package uk.me.berndporr.iirj;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class CompareWithScipyTest
{
	private final FilterType  filterType;
	private final FilterBType filterBType;
	private final int         order;

	public CompareWithScipyTest(FilterType filterType, FilterBType filterBType, int order) {
		this.filterType = filterType;
		this.filterBType = filterBType;
		this.order = order;
	}

	@Parameters(name = "Test:{index}, Filtertype: {0}, FilterBType: {1}, Order:{2}")
	public static Collection<Object[]> data() {
		Collection<Object[]> entries = new ArrayList<>();
		int[] orders = {1, 2, 4, 5, 10};
		for (FilterType filterType : FilterType.values()) {
			for (FilterBType filterBType : FilterBType.values()) {
				for (int order : orders) {
					entries.add(new Object[]{filterType, filterBType, order});
				}
			}
		}
		return entries;
	}

	@Override
	public String toString() {
		return "IIRJScipyCompare{" +
				"filterType=" + filterType +
				", filterBType=" + filterBType +
				", order=" + order +
				'}';
	}

	private enum FilterType
	{
		Butterworth("Butterworth"),
		Chebychev1("Chebychev1"),
		Chebychev2("Chebychev2");

		private final String scipyString;

		FilterType(String scipyString) {
			this.scipyString = scipyString;
		}
	}

	private enum FilterBType
	{
		Lowpass("lowpass"),
		Highpass("highpass"),
		Bandpass("bandpass"),
		Bandstop("bandstop");

		private final String scipyString;

		FilterBType(String scipyString) {
			this.scipyString = scipyString;
		}
	}

	@Test
	public void compareValues() {

		ClassLoader classloader = Thread.currentThread().getContextClassLoader();

		InputStream resource = classloader.getResourceAsStream("scipy/signal.txt");
		DoubleSignal inputSignal = readSignalFromCSV(resource, "Input");

		double[] time = inputSignal.getxValues();
		double sampleRate = 1.0 / (time[1] - time[0]);
		double lc = 200;
		double hc = 300;

		Cascade filter = createIIRJFilter(filterType, filterBType, order, lc, hc, sampleRate);
		String filterFileName = generateFileNameFromConfig(filterType.scipyString, filterBType.scipyString, lc, hc, order);
		DoubleSignal scipyResult = readSignalFromCSV(classloader.getResourceAsStream("scipy/" + filterFileName), "Scipy#" + filterFileName);

		DoubleSignal filteredSignal = new DoubleSignal(inputSignal.getSize(), "IIRJ#" + filterFileName);
		int size = inputSignal.getSize();
		for (int index = 0; index < size; index++) {
			filteredSignal.setValue(index, inputSignal.getXValue(index), filter.filter(inputSignal.getValue(index)));
		}

		compareSignals(scipyResult, filteredSignal);
	}

	private Cascade createIIRJFilter(FilterType filterType, FilterBType filterBType, int order, double lc, double hc, double sampleRate) {
		System.out.println("Scipy test: "+filterType+", "+filterBType+", "+order+", "+lc+", "+hc+", fs="+sampleRate);
		double centerFrequency = (hc + lc) / 2;
		double widthFrequency = hc - lc;
		double rippleInDb = 1;

		switch (filterType) {
			case Butterworth:
				Butterworth butterworth = new Butterworth();
				switch (filterBType) {
					case Lowpass:
						butterworth.lowPass(order, sampleRate, hc);
						break;
					case Highpass:
						butterworth.highPass(order, sampleRate, lc);
						break;
					case Bandpass:
						butterworth.bandPass(order, sampleRate, centerFrequency, widthFrequency);
						break;
					case Bandstop:
						butterworth.bandStop(order, sampleRate, centerFrequency, widthFrequency);
						break;
				}
				return butterworth;
			case Chebychev1:
				ChebyshevI chebyshevI = new ChebyshevI();

				switch (filterBType) {
					case Lowpass:
						chebyshevI.lowPass(order, sampleRate, hc, rippleInDb);
						break;
					case Highpass:
						chebyshevI.highPass(order, sampleRate, lc, rippleInDb);
						break;
					case Bandpass:
						chebyshevI.bandPass(order, sampleRate, centerFrequency, widthFrequency, rippleInDb);
						break;
					case Bandstop:
						chebyshevI.bandStop(order, sampleRate, centerFrequency, widthFrequency, rippleInDb);
						break;
				}
				return chebyshevI;
			case Chebychev2:
				ChebyshevII chebyshevII = new ChebyshevII();
				switch (filterBType) {
					case Lowpass:
						chebyshevII.lowPass(order, sampleRate, hc, rippleInDb);
						break;
					case Highpass:
						chebyshevII.highPass(order, sampleRate, lc, rippleInDb);
						break;
					case Bandpass:
						chebyshevII.bandPass(order, sampleRate, centerFrequency, widthFrequency, rippleInDb);
						break;
					case Bandstop:
						chebyshevII.bandStop(order, sampleRate, centerFrequency, widthFrequency, rippleInDb);
						break;
				}
				return chebyshevII;
		}

		throw new IllegalArgumentException(
				"Unknown filter configuration: "
						+ "Filter Type: " + filterType
						+ ", Filter B Type:  " + filterBType);
	}

	private void compareSignals(DoubleSignal scipySignal, DoubleSignal iirjSignal) {
		int signal1Size = scipySignal.getSize();
		int signal2Size = iirjSignal.getSize();
		Assert.assertEquals("Different signal1Size of signals", signal1Size, signal2Size);

		for (int index = 0; index < signal1Size; index++) {
			double scipySignalValue = scipySignal.getValue(index);
			double iirjSignalValue = iirjSignal.getValue(index);
			Assert.assertEquals("Different values at index " + index, scipySignalValue, iirjSignalValue, 1E-5);
		}
	}

	private DoubleSignal readSignalFromCSV(InputStream inputStream, String signalName) {
		List<String> lines = new BufferedReader(new InputStreamReader(inputStream,
				StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
		DoubleSignal doubleSignal = new DoubleSignal(lines.size() - 1, signalName);
		for (int i = 1; i < lines.size(); i++) {
			String[] parts = lines.get(i).split(";");
			doubleSignal.setValue(i - 1, Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
		}
		return doubleSignal;
	}

	private String generateFileNameFromConfig(String filterType, String filterBTye, double lc, double hc, int order) {
		return filterType.toLowerCase() + "-" + filterBTye + "-LC_" + (int) lc + "-HC_" + (int) hc + "-Order_" + order + ".csv";
	}
}
