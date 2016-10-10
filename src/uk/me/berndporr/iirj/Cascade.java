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
import org.apache.commons.math3.complex.ComplexUtils;

/**
 * 
 * The mother of all filters. It contains the coefficients of all
 * filter stages as a sequence of 2nd order filters and the states
 * of the 2nd order filters which also imply if it's direct form I or II
 *
 */
public class Cascade {

	// coefficients
	public Biquad[] m_biquads;

	// the states of the filters
	public DirectFormAbstract[] m_states;

	// number of biquads in the system
	public int m_numBiquads;

	public int numPoles;

	public int getNumBiquads() {
		return m_numBiquads;
	}

	public Biquad getBiquad(int index) {
		return m_biquads[index];
	}

	Cascade() {
		m_numBiquads = 0;
		m_biquads = null;
		m_states = null;
	}

	public void reset() {
		for (int i = 0; i < m_numBiquads; i++)
			m_states[i].reset();
	}

	public double filter(double in) {
		double out = in;
		for (int i = 0; i < m_numBiquads; i++) {
			if (m_states[i] != null) {
			out = m_states[i].process1(out, m_biquads[i]);
			}
		}
		return out;
	}

	public Complex response(double normalizedFrequency) {
		double w = 2 * Math.PI * normalizedFrequency;
		Complex czn1 = ComplexUtils.polar2Complex(1., -w);
		Complex czn2 = ComplexUtils.polar2Complex(1., -2 * w);
		Complex ch = new Complex(1);
		Complex cbot = new Complex(1);

		for (int i = 0; i < m_numBiquads; i++) {
			Biquad stage = m_biquads[i];
			Complex cb = new Complex(1);
			Complex ct = new Complex(stage.getB0() / stage.getA0());
			ct = MathSupplement.addmul(ct, stage.getB1() / stage.getA0(), czn1);
			ct = MathSupplement.addmul(ct, stage.getB2() / stage.getA0(), czn2);
			cb = MathSupplement.addmul(cb, stage.getA1() / stage.getA0(), czn1);
			cb = MathSupplement.addmul(cb, stage.getA2() / stage.getA0(), czn2);
			ch = ch.multiply(ct);
			cbot = cbot.multiply(cb);
		}

		return ch.divide(cbot);
	}

	void applyScale(double scale) {
		// For higher order filters it might be helpful
		// to spread this factor between all the stages.
		m_biquads[0].applyScale(scale);
	}

	void setLayout(LayoutBase proto, int filterTypes) {
		numPoles = proto.getNumPoles();
		m_numBiquads = (numPoles + 1) / 2;
		m_biquads = new Biquad[m_numBiquads];
		switch (filterTypes) {
		case DirectFormAbstract.DIRECT_FORM_I:
			m_states = new DirectFormI[m_numBiquads];
			for (int i = 0; i < m_numBiquads; i++) {
				m_states[i] = new DirectFormI();
			}
			break;
		case DirectFormAbstract.DIRECT_FORM_II:
		default:
			m_states = new DirectFormII[m_numBiquads];
			for (int i = 0; i < m_numBiquads; i++) {
				m_states[i] = new DirectFormII();
			}
			break;
		}
		for (int i = 0; i < m_numBiquads; ++i) {
			PoleZeroPair p = proto.getPair(i);
			m_biquads[i] = new Biquad();
			m_biquads[i].setPoleZeroPair(p);
		}
		applyScale(proto.getNormalGain()
				/ ((response(proto.getNormalW() / (2 * Math.PI)))).abs());
	}

};
