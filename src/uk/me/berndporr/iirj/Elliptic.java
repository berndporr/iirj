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
 * Elliptic filters. This done in this way: Elliptic elliptic = new Elliptic();
 * Then call one of the methods below to create low-,high-,band-, or stopband
 * filters. For example: butterworth.bandPass(2,250,50,5);
 */
public class Elliptic extends Cascade {

	class AnalogLowPass extends LayoutBase {

		// shit ton of math in here

		int m_numPoles;
		double m_rippleDb;
		double m_rolloff;

		// approximation to complete elliptic integral of the first kind.
		// fast convergence, peak error less than 2e-16.
		double ellipticK(double k) {
			double m = k * k;
			double a = 1;
			double b = Math.sqrt(1 - m);
			double c = a - b;
			double co;
			do {
				co = c;
				c = (a - b) / 2;
				double ao = (a + b) / 2;
				b = Math.sqrt(a * b);
				a = ao;
			} while (c < co);

			return Math.PI / (a + a);
		}

		// ------------------------------------------------------------------------------

		AnalogLowPass(int nPoles) {
			super(nPoles);
			m_numPoles = nPoles;
			setNormal(0, 1);
		}

		void design(int numPoles, double rippleDb, double rolloff) {
			if (m_numPoles != numPoles || m_rippleDb != rippleDb
					|| m_rolloff != rolloff) {
				m_numPoles = numPoles;
				m_rippleDb = rippleDb;
				m_rolloff = rolloff;

				reset();

				int n = numPoles;

				double e2 = Math.pow(10., rippleDb / 10) - 1;
				double xi = 5 * Math.exp(rolloff - 1) + 1;

				m_K = ellipticK(1 / xi);
				m_Kprime = ellipticK(Math.sqrt(1 - 1 / (xi * xi)));

				int ni = ((n & 1) == 1) ? 0 : 1;
				int i;
				double[] f = new double[100]; // HACK!!!
				for (i = 1; i <= n / 2; i++) {
					double u = (2 * i - ni) * m_K / n;
					double sn = calcsn(u);
					sn = sn * 2 * Math.PI / m_K;
					f[i] = m_zeros[i - 1] = 1 / sn;
				}
				m_zeros[n / 2] = Double.POSITIVE_INFINITY;
				double fb = 1 / (2 * Math.PI);
				m_nin = n % 2;
				m_n2 = n / 2;
				for (i = 1; i <= m_n2; i++) {
					double x = f[m_n2 + 1 - i];
					m_z1[i] = Math.sqrt(1 - 1 / (x * x));
				}
				double ee = e2;// pow(10., rippleDb/20)-1;
				m_e = Math.sqrt(ee);
				double fbb = fb * fb;
				m_m = m_nin + 2 * m_n2;
				m_em = 2 * (m_m / 2);
				double tp = 2 * Math.PI;
				calcfz();
				calcqz();
				if (m_m > m_em)
					m_c1[2 * m_m] = 0;
				for (i = 0; i <= 2 * m_m; i += 2)
					m_a1[m_m - i / 2] = m_c1[i] + m_d1[i];
				double a0 = findfact(m_m);
				int r = 0;
				while (r < m_em / 2) {
					r++;
					m_p[r] /= 10;
					m_q1[r] /= 100;
					double d = 1 + m_p[r] + m_q1[r];
					m_b1[r] = (1 + m_p[r] / 2) * fbb / d;
					m_zf1[r] = fb / Math.pow(d, .25);
					m_zq1[r] = 1 / Math.sqrt(Math.abs(2 * (1 - m_b1[r]
							/ (m_zf1[r] * m_zf1[r]))));
					m_zw1[r] = tp * m_zf1[r];

					m_rootR[r] = -.5 * m_zw1[r] / m_zq1[r];
					m_rootR[r + m_em / 2] = m_rootR[r];
					m_rootI[r] = .5 * Math.sqrt(Math.abs(m_zw1[r] * m_zw1[r]
							/ (m_zq1[r] * m_zq1[r]) - 4 * m_zw1[r] * m_zw1[r]));
					m_rootI[r + m_em / 2] = -m_rootI[r];

					Complex pole = new Complex(-.5 * m_zw1[r] / m_zq1[r],
							.5 * Math.sqrt(Math.abs(m_zw1[r] * m_zw1[r]
									/ (m_zq1[r] * m_zq1[r]) - 4 * m_zw1[r]
									* m_zw1[r])));

					Complex zero = new Complex(0, m_zeros[r - 1]);

					addPoleZeroConjugatePairs(pole, zero);
				}

				if (a0 != 0) {
					m_rootR[r + 1 + m_em / 2] = -Math.sqrt(fbb / (.1 * a0 - 1))
							* tp;
					m_rootI[r + 1 + m_em / 2] = 0;

					add(new Complex(-Math.sqrt(fbb / (.1 * a0 - 1)) * tp),
							new Complex(Double.POSITIVE_INFINITY));
				}

				setNormal(
						0,
						((numPoles & 1) == 1) ? 1. : Math.pow(10.,
								-rippleDb / 20.0));
			}
		}

		// generate the product of (z+s1[i]) for i = 1 .. sn and store it in
		// b1[]
		// (i.e. f[z] = b1[0] + b1[1] z + b1[2] z^2 + ... b1[sn] z^sn)
		void prodpoly(int sn) {
			m_b1[0] = m_s1[1];
			m_b1[1] = 1;
			int i, j;
			for (j = 2; j <= sn; j++) {
				m_a1[0] = m_s1[j] * m_b1[0];
				for (i = 1; i <= j - 1; i++)
					m_a1[i] = m_b1[i - 1] + m_s1[j] * m_b1[i];
				for (i = 0; i != j; i++)
					m_b1[i] = m_a1[i];
				m_b1[j] = 1;
			}
		}

		// determine f(z)^2
		void calcfz2(int i) {
			int ji = 0;
			int jf = 0;
			if (i < m_em + 2) {
				ji = 0;
				jf = i;
			}
			if (i > m_em) {
				ji = i - m_em;
				jf = m_em;
			}
			m_c1[i] = 0;
			int j;
			for (j = ji; j <= jf; j += 2)
				m_c1[i] += m_a1[j] * (m_a1[i - j] * Math.pow(10., m_m - i / 2));
		}

		// calculate f(z)
		void calcfz() {
			int i = 1;
			if (m_nin == 1)
				m_s1[i++] = 1;
			for (; i <= m_nin + m_n2; i++)
				m_s1[i] = m_s1[i + m_n2] = m_z1[i - m_nin];
			prodpoly(m_nin + 2 * m_n2);
			for (i = 0; i <= m_em; i += 2)
				m_a1[i] = m_e * m_b1[i];
			for (i = 0; i <= 2 * m_em; i += 2)
				calcfz2(i);
		}

		// determine q(z)
		void calcqz() {
			int i;
			for (i = 1; i <= m_nin; i++)
				m_s1[i] = -10;
			for (; i <= m_nin + m_n2; i++)
				m_s1[i] = -10 * m_z1[i - m_nin] * m_z1[i - m_nin];
			for (; i <= m_nin + 2 * m_n2; i++)
				m_s1[i] = m_s1[i - m_n2];
			prodpoly(m_m);
			int dd = ((m_nin & 1) == 1) ? -1 : 1;
			for (i = 0; i <= 2 * m_m; i += 2)
				m_d1[i] = dd * m_b1[i / 2];
		}

		// compute factors
		double findfact(int t) {
			int i;
			double a = 0;
			for (i = 1; i <= t; i++)
				m_a1[i] /= m_a1[0];
			m_a1[0] = m_b1[0] = m_c1[0] = 1;
			int i1 = 0;
			for (;;) {
				if (t <= 2)
					break;
				double p0 = 0, q0 = 0;
				i1++;
				for (;;) {
					m_b1[1] = m_a1[1] - p0;
					m_c1[1] = m_b1[1] - p0;
					for (i = 2; i <= t; i++)
						m_b1[i] = m_a1[i] - p0 * m_b1[i - 1] - q0 * m_b1[i - 2];
					for (i = 2; i < t; i++)
						m_c1[i] = m_b1[i] - p0 * m_c1[i - 1] - q0 * m_c1[i - 2];
					int x1 = t - 1;
					int x2 = t - 2;
					int x3 = t - 3;
					double x4 = m_c1[x2] * m_c1[x2] + m_c1[x3]
							* (m_b1[x1] - m_c1[x1]);
					if (x4 == 0)
						x4 = 1e-3;
					double ddp = (m_b1[x1] * m_c1[x2] - m_b1[t] * m_c1[x3])
							/ x4;
					p0 += ddp;
					double dq = (m_b1[t] * m_c1[x2] - m_b1[x1]
							* (m_c1[x1] - m_b1[x1]))
							/ x4;
					q0 += dq;
					if (Math.abs(ddp + dq) < 1e-6)
						break;
				}
				m_p[i1] = p0;
				m_q1[i1] = q0;
				m_a1[1] = m_a1[1] - p0;
				t -= 2;
				for (i = 2; i <= t; i++)
					m_a1[i] -= p0 * m_a1[i - 1] + q0 * m_a1[i - 2];
				if (t <= 2)
					break;
			}

			if (t == 2) {
				i1++;
				m_p[i1] = m_a1[1];
				m_q1[i1] = m_a1[2];
			}
			if (t == 1)
				a = -m_a1[1];

			return a;
		}

		double calcsn(double u) {
			double sn = 0;
			int j;
			// q = modular constant
			double q = Math.exp(-Math.PI * m_Kprime / m_K);
			double v = Math.PI * .5 * u / m_K;
			for (j = 0;; j++) {
				double w = Math.pow(q, j + .5);
				sn += w * Math.sin((2 * j + 1) * v) / (1 - w * w);
				if (w < 1e-7)
					break;
			}
			return sn;
		}

		// ------------------------------------------------------------------------------

		double m_p0;
		double m_q;
		double m_K;
		double m_Kprime;
		double m_e;
		int m_nin;
		int m_m;
		int m_n2;
		int m_em;
		double[] m_zeros = new double[100];
		double[] m_c1 = new double[100];
		double[] m_b1 = new double[100];
		double[] m_a1 = new double[100];
		double[] m_d1 = new double[100];
		double[] m_q1 = new double[100];
		double[] m_z1 = new double[100];
		double[] m_f1 = new double[100];
		double[] m_s1 = new double[100];
		double[] m_p = new double[100];
		double[] m_zw1 = new double[100];
		double[] m_zf1 = new double[100];
		double[] m_zq1 = new double[100];
		double[] m_rootR = new double[100];
		double[] m_rootI = new double[100];

	}

	private void setupLowPass(int order, double sampleRate,
			double cutoffFrequency, double rippleDb, double rolloff,
			int directFormType) {

		AnalogLowPass m_analogProto = new AnalogLowPass(order);
		m_analogProto.design(order, rippleDb, rolloff);

		LayoutBase m_digitalProto = new LayoutBase(order);

		new LowPassTransform(cutoffFrequency / sampleRate, m_digitalProto,
				m_analogProto);

		setLayout(m_digitalProto, directFormType);
	}

	/**
	 * Elliptic Lowpass filter
	 * 
	 * @param order
	 *            The order of the filter
	 * @param sampleRate
	 *            The sampling rate of the system
	 * @param cutoffFrequency
	 *            the cutoff frequency
	 */
	public void lowPass(int order, double sampleRate, double cutoffFrequency,
			double rippleDb, double rolloff) {
		setupLowPass(order, sampleRate, cutoffFrequency, rippleDb, rolloff,
				DirectFormAbstract.DIRECT_FORM_II);
	}

	/**
	 * Elliptic Lowpass filter and defining the filter topology
	 * 
	 * @param order
	 *            The order of the filter
	 * @param sampleRate
	 *            The sampling rate of the system
	 * @param cutoffFrequency
	 *            The cutoff frequency
	 * @param directFormType
	 *            The filter topology. This is either
	 *            DirectFormAbstract.DIRECT_FORM_I or DIRECT_FORM_II
	 */
	public void lowPass(int order, double sampleRate, double cutoffFrequency,
			double rippleDb, double rolloff, int directFormType) {
		setupLowPass(order, sampleRate, cutoffFrequency, rippleDb, rolloff,
				directFormType);
	}

	private void setupBandStop(int order, double sampleRate,
			double centerFrequency, double widthFrequency, double rippleDb,
			double rolloff, int directFormType) {

		AnalogLowPass m_analogProto = new AnalogLowPass(order);
		m_analogProto.design(order, rippleDb, rolloff);

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
	 */
	public void bandStop(int order, double sampleRate, double centerFrequency,
			double widthFrequency, double rippleDb, double rolloff) {
		setupBandStop(order, sampleRate, centerFrequency, widthFrequency,
				rippleDb, rolloff, DirectFormAbstract.DIRECT_FORM_II);
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
	 */
	public void bandStop(int order, double sampleRate, double centerFrequency,
			double widthFrequency, int directFormType, double rippleDb,
			double rolloff) {
		setupBandStop(order, sampleRate, centerFrequency, widthFrequency,
				rippleDb, rolloff, directFormType);
	}

	private void setupBandPass(int order, double sampleRate,
			double centerFrequency, double widthFrequency, double rippleDb,
			double rolloff, int directFormType) {

		AnalogLowPass m_analogProto = new AnalogLowPass(order);
		m_analogProto.design(order, rippleDb, rolloff);

		LayoutBase m_digitalProto = new LayoutBase(order * 2);

		new BandPassTransform(centerFrequency / sampleRate, widthFrequency
				/ sampleRate, m_digitalProto, m_analogProto);

		setLayout(m_digitalProto, directFormType);

	}

	public void bandPass(int order, double sampleRate, double centerFrequency,
			double widthFrequency, double rippleDb, double rolloff) {
		setupBandPass(order, sampleRate, centerFrequency, widthFrequency,
				rippleDb, rolloff, DirectFormAbstract.DIRECT_FORM_II);
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
	 * @param directFormType
	 *            The filter topology (see DirectFormAbstract)
	 */
	public void bandPass(int order, double sampleRate, double centerFrequency,
			double widthFrequency, int directFormType, double rippleDb,
			double rolloff) {
		setupBandPass(order, sampleRate, centerFrequency, widthFrequency,
				rippleDb, rolloff, directFormType);
	}

}
