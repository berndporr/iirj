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
 * 
 * Digital/analogue filter coefficient storage space organising the
 * storage as PoleZeroPairs so that we have as always a 2nd order filter
 *
 */
public class LayoutBase {

	private int m_numPoles;
	private PoleZeroPair[] m_pair;
	private double m_normalW;
	private double m_normalGain;

	LayoutBase(PoleZeroPair[] pairs) {
		m_numPoles = pairs.length * 2;
		m_pair = pairs;
	}

	LayoutBase(int numPoles) {
		m_numPoles = 0;
		m_pair = new PoleZeroPair[numPoles / 2];
	}

	void reset() {
		m_numPoles = 0;
	}

	int getNumPoles() {
		return m_numPoles;
	}

	void add(Complex pole, Complex zero) {
		m_pair[m_numPoles / 2] = new PoleZeroPair(pole, zero);
		++m_numPoles;
	}

	void addPoleZeroConjugatePairs(Complex pole, Complex zero) {
		if (pole == null) System.out.println("LayoutBase addConj() pole == null");
		if (zero == null) System.out.println("LayoutBase addConj() zero == null");
		if (m_pair == null) System.out.println("LayoutBase addConj() m_pair == null");
		m_pair[m_numPoles / 2] = new PoleZeroPair(pole, zero, pole.conjugate(),
				zero.conjugate());
		m_numPoles += 2;
	}

	void add(ComplexPair poles, ComplexPair zeros) {
		System.out.println("LayoutBase add() numPoles="+m_numPoles);
		m_pair[m_numPoles / 2] = new PoleZeroPair(poles.first, zeros.first,
				poles.second, zeros.second);
		m_numPoles += 2;
	}

	PoleZeroPair getPair(int pairIndex) {
		return m_pair[pairIndex];
	}

	double getNormalW() {
		return m_normalW;
	}

	double getNormalGain() {
		return m_normalGain;
	}

	void setNormal(double w, double g) {
		m_normalW = w;
		m_normalGain = g;
	}
};
