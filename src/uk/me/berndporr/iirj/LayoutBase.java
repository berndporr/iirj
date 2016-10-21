/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 *  Copyright (c) 2009 by Vinnie Falco
 *  Copyright (c) 2016 by Bernd Porr
 */

package uk.me.berndporr.iirj;

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
