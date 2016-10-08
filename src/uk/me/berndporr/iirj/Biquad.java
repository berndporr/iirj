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
 * Contains the coefficients of a 2nd order digital filter with two poles and two zeros
 */
public class Biquad {

    double m_a0;
    double m_a1;
    double m_a2;
    double m_b1;
    double m_b2;
    double m_b0;

    double getA0() {
        return m_a0;
    }

    double getA1() {
        return m_a1 * m_a0;
    }

    double getA2() {
        return m_a2 * m_a0;
    }

    double getB0() {
        return m_b0 * m_a0;
    }

    double getB1() {
        return m_b1 * m_a0;
    }

    double getB2() {
        return m_b2 * m_a0;
    }

    Complex response(double normalizedFrequency) {
        double a0 = getA0();
        double a1 = getA1();
        double a2 = getA2();
        double b0 = getB0();
        double b1 = getB1();
        double b2 = getB2();

        double w = 2 * Math.PI * normalizedFrequency;
        Complex czn1 = ComplexUtils.polar2Complex(1., -w);
        Complex czn2 = ComplexUtils.polar2Complex(1., -2 * w);
        Complex ch = new Complex(1);
        Complex cbot = new Complex(1);

        Complex ct = new Complex(b0 / a0);
        Complex cb = new Complex(1);
        ct = MathSupplement.addmul(ct, b1 / a0, czn1);
        ct = MathSupplement.addmul(ct, b2 / a0, czn2);
        cb = MathSupplement.addmul(cb, a1 / a0, czn1);
        cb = MathSupplement.addmul(cb, a2 / a0, czn2);
        ch = ch.multiply(ct);
        cbot = cbot.multiply(cb);

        return ch.divide(cbot);
    }

    void setCoefficients(double a0, double a1, double a2,
                         double b0, double b1, double b2) {
        m_a0 = a0;
        m_a1 = a1 / a0;
        m_a2 = a2 / a0;
        m_b0 = b0 / a0;
        m_b1 = b1 / a0;
        m_b2 = b2 / a0;
    }

    void setOnePole(Complex pole, Complex zero) {
        double a0 = 1;
        double a1 = -pole.getReal();
        double a2 = 0;
        double b0 = -zero.getReal();
        double b1 = 1;
        double b2 = 0;
        setCoefficients(a0, a1, a2, b0, b1, b2);
    }

    void setTwoPole(Complex pole1, Complex zero1,
                    Complex pole2, Complex zero2) {
        double a0 = 1;
        double a1;
        double a2;

        if (pole1.getImaginary() != 0) {

            a1 = -2 * pole1.getReal();
            a2 = pole1.abs() * pole1.abs();
        } else {

            a1 = -(pole1.getReal() + pole2.getReal());
            a2 = pole1.getReal() * pole2.getReal();
        }

        double b0 = 1;
        double b1;
        double b2;

        if (zero1.getImaginary() != 0) {

            b1 = -2 * zero1.getReal();
            b2 = zero1.abs() * zero1.abs();
        } else {

            b1 = -(zero1.getReal() + zero2.getReal());
            b2 = zero1.getReal() * zero2.getReal();
        }

        setCoefficients(a0, a1, a2, b0, b1, b2);
    }

    void setPoleZeroForm(BiquadPoleState bps) {
        setPoleZeroPair(bps);
        applyScale(bps.gain);
    }

    void setIdentity() {
        setCoefficients(1, 0, 0, 1, 0, 0);
    }

    void applyScale(double scale) {
        m_b0 *= scale;
        m_b1 *= scale;
        m_b2 *= scale;
    }


    void setPoleZeroPair(PoleZeroPair pair) {
        if (pair.isSinglePole()) {
            setOnePole(pair.poles.first, pair.zeros.first);
        } else {
            setTwoPole(pair.poles.first, pair.zeros.first,
                    pair.poles.second, pair.zeros.second);
        }
    }
}