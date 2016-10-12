package uk.me.berndporr.iirj;

/*******************************************************************************

 "A Collection of Useful C++ Classes for Digital Signal Processing"
 By Vinnie Falco and adjusted for Java by Bernd Porr

 Official project location:
 https://github.com/vinniefalco/DSPFilters

 See Documentation.cpp for contact information, notes, and bibliography.

 --------------------------------------------------------------------------------

 License: MIT License (http://www.opensource.org/licenses/mit-license.php)
 Copyright (c) 2009 by Vinnie Falco
 Copyright (c) 2016 by Bernd Porr

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.

 *******************************************************************************/

import org.apache.commons.math3.complex.Complex;

/**
 * User facing class which contains all the methods the user uses to create
 * ChebyshevI filters. This done in this way: ChebyshevI elliptic = new ChebyshevI();
 * Then call one of the methods below to create low-,high-,band-, or stopband
 * filters. For example: butterworth.bandPass(2,250,50,5);
 */
public class ChebyshevI extends Cascade {

	class AnalogLowPass extends LayoutBase {

		// shit ton of math in here

		int m_numPoles;
		double m_rippleDb;

		// ------------------------------------------------------------------------------

		AnalogLowPass(int nPoles) {
			super(nPoles);
			m_numPoles = nPoles;
		}

		void design (int numPoles,
                double rippleDb) {
			m_numPoles = numPoles;
			m_rippleDb = rippleDb;

			reset ();

			double eps = Math.sqrt (1. / Math.exp (-rippleDb * 0.1 * MathSupplement.doubleLn10) - 1);
			double v0 = MathSupplement.asinh (1 / eps) / numPoles;
			double sinh_v0 = -Math.sinh (v0);
			double cosh_v0 = Math.cosh (v0);

			double n2 = 2 * numPoles;
			int pairs = numPoles / 2;
			for (int i = 0; i < pairs; ++i) {
				int k = 2 * i + 1 - numPoles;
				double a = sinh_v0 * Math.cos (k * Math.PI / n2);
				double b = cosh_v0 * Math.sin (k * Math.PI / n2);

				addPoleZeroConjugatePairs (new Complex (a, b), new Complex(Double.POSITIVE_INFINITY));
			}

			if ((numPoles & 1)==1) {
				add (new Complex(sinh_v0, 0), new Complex(Double.POSITIVE_INFINITY));
				setNormal (0, 1);
			} else {
				setNormal (0, Math.pow (10, -rippleDb/20.));
			}
		}
	}


	private void setupLowPass(int order, double sampleRate,
			double cutoffFrequency, double rippleDb,
			int directFormType) {

		AnalogLowPass m_analogProto = new AnalogLowPass(order);
		m_analogProto.design(order, rippleDb);

		LayoutBase m_digitalProto = new LayoutBase(order);

		new LowPassTransform(cutoffFrequency / sampleRate, m_digitalProto,
				m_analogProto);

		setLayout(m_digitalProto, directFormType);
	}

	/**
	 * ChebyshevI Lowpass filter
	 * 
	 * @param order
	 *            The order of the filter
	 * @param sampleRate
	 *            The sampling rate of the system
	 * @param cutoffFrequency
	 *            the cutoff frequency
	 * @param rippleDb
	 *            passband ripple in decibel
	 *            sensible value: 1dB
	 */
	public void lowPass(int order, double sampleRate, double cutoffFrequency,
			double rippleDb) {
		setupLowPass(order, sampleRate, cutoffFrequency, rippleDb, 
				DirectFormAbstract.DIRECT_FORM_II);
	}

	/**
	 * ChebyshevI Lowpass filter and defining the filter topology
	 * 
	 * @param order
	 *            The order of the filter
	 * @param sampleRate
	 *            The sampling rate of the system
	 * @param cutoffFrequency
	 *            The cutoff frequency
	 * @param rippleDb
	 *            passband ripple in decibel
	 *            sensible value: 1dB
	 * @param directFormType
	 *            The filter topology. This is either
	 *            DirectFormAbstract.DIRECT_FORM_I or DIRECT_FORM_II
	 */
	public void lowPass(int order, double sampleRate, double cutoffFrequency,
			double rippleDb, int directFormType) {
		setupLowPass(order, sampleRate, cutoffFrequency, rippleDb, 
				directFormType);
	}

	private void setupHighPass(int order, double sampleRate,
			double cutoffFrequency, double rippleDb, 
			int directFormType) {

		AnalogLowPass m_analogProto = new AnalogLowPass(order);
		m_analogProto.design(order, rippleDb);

		LayoutBase m_digitalProto = new LayoutBase(order);

		new HighPassTransform(cutoffFrequency / sampleRate, m_digitalProto,
				m_analogProto);

		setLayout(m_digitalProto, directFormType);
	}

	/**
	 * ChebyshevI Highpass filter
	 * 
	 * @param order
	 *            The order of the filter
	 * @param sampleRate
	 *            The sampling rate of the system
	 * @param cutoffFrequency
	 *            the cutoff frequency
	 * @param rippleDb
	 *            passband ripple in decibel
	 *            sensible value: 1dB
	 */
	public void highPass(int order, double sampleRate, double cutoffFrequency,
			double rippleDb) {
		setupHighPass(order, sampleRate, cutoffFrequency, rippleDb, 
				DirectFormAbstract.DIRECT_FORM_II);
	}

	/**
	 * ChebyshevI Lowpass filter and defining the filter topology
	 * 
	 * @param order
	 *            The order of the filter
	 * @param sampleRate
	 *            The sampling rate of the system
	 * @param cutoffFrequency
	 *            The cutoff frequency
	 * @param rippleDb
	 *            passband ripple in decibel
	 *            sensible value: 1dB
	 * @param directFormType
	 *            The filter topology. This is either
	 *            DirectFormAbstract.DIRECT_FORM_I or DIRECT_FORM_II
	 */
	public void highPass(int order, double sampleRate, double cutoffFrequency,
			double rippleDb, int directFormType) {
		setupHighPass(order, sampleRate, cutoffFrequency, rippleDb, 
				directFormType);
	}

	private void setupBandStop(int order, double sampleRate,
			double centerFrequency, double widthFrequency, double rippleDb,
			int directFormType) {

		AnalogLowPass m_analogProto = new AnalogLowPass(order);
		m_analogProto.design(order, rippleDb);

		LayoutBase m_digitalProto = new LayoutBase(order * 2);

		new BandStopTransform(centerFrequency / sampleRate, widthFrequency
				/ sampleRate, m_digitalProto, m_analogProto);

		setLayout(m_digitalProto, directFormType);
	}

	/**
	 * Bandstop filter with default topology
	 * 
	 * @param order
	 *            Filter order (actual order is twice)
	 * @param sampleRate
	 *            Samping rate of the system
	 * @param centerFrequency
	 *            Center frequency
	 * @param widthFrequency
	 *            Width of the notch
	 * @param rippleDb
	 *            passband ripple in decibel
	 *            sensible value: 1dB
	 */
	public void bandStop(int order, double sampleRate, double centerFrequency,
			double widthFrequency, double rippleDb) {
		setupBandStop(order, sampleRate, centerFrequency, widthFrequency,
				rippleDb,  DirectFormAbstract.DIRECT_FORM_II);
	}

	/**
	 * Bandstop filter with default topology
	 * 
	 * @param order
	 *            Filter order (actual order is twice)
	 * @param sampleRate
	 *            Samping rate of the system
	 * @param centerFrequency
	 *            Center frequency
	 * @param widthFrequency
	 *            Width of the notch
	 * @param directFormType
	 *            The filter topology
	 * @param rippleDb
	 *            passband ripple in decibel
	 *            sensible value: 1dB
	 */
	public void bandStop(int order, double sampleRate, double centerFrequency,
			double widthFrequency, double rippleDb, int directFormType) {
		setupBandStop(order, sampleRate, centerFrequency, widthFrequency,
				rippleDb,  directFormType);
	}

	private void setupBandPass(int order, double sampleRate,
			double centerFrequency, double widthFrequency, double rippleDb,
			int directFormType) {

		AnalogLowPass m_analogProto = new AnalogLowPass(order);
		m_analogProto.design(order, rippleDb);

		LayoutBase m_digitalProto = new LayoutBase(order * 2);

		new BandPassTransform(centerFrequency / sampleRate, widthFrequency
				/ sampleRate, m_digitalProto, m_analogProto);

		setLayout(m_digitalProto, directFormType);

	}

	public void bandPass(int order, double sampleRate, double centerFrequency,
			double widthFrequency, double rippleDb) {
		setupBandPass(order, sampleRate, centerFrequency, widthFrequency,
				rippleDb,  DirectFormAbstract.DIRECT_FORM_II);
	}

	/**
	 * Bandpass filter with custom topology
	 * 
	 * @param order
	 *            Filter order
	 * @param sampleRate
	 *            Sampling rate
	 * @param centerFrequency
	 *            Center frequency
	 * @param widthFrequency
	 *            Width of the notch
	 * @param rippleDb
	 *            passband ripple in decibel
	 *            sensible value: 1dB
	 * @param directFormType
	 *            The filter topology (see DirectFormAbstract)
	 */
	public void bandPass(int order, double sampleRate, double centerFrequency,
			double widthFrequency, double rippleDb, int directFormType) {
		setupBandPass(order, sampleRate, centerFrequency, widthFrequency,
				rippleDb,  directFormType);
	}

}
