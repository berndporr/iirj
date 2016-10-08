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

public class MathSupplement {

	static Complex solve_quadratic_1(double a, double b, double c) {
		return (new Complex(-b).add(new Complex(b * b - 4 * a * c, 0)).sqrt())
				.divide(2. * a);
	}

	static Complex solve_quadratic_2(double a, double b, double c) {
		return (new Complex(-b).subtract(new Complex(b * b - 4 * a * c, 0))
				.sqrt()).divide(2. * a);
	}

	static Complex adjust_imag(Complex c) {
		if (Math.abs(c.getImaginary()) < 1e-30)
			return new Complex(c.getReal(), 0);
		else
			return c;
	}

	static Complex addmul(Complex c, double v, Complex c1) {
		return new Complex(c.getReal() + v * c1.getReal(), c.getImaginary() + v
				* c1.getImaginary());
	}

	static Complex recip(Complex c) {
		double n = 1.0 / (c.abs() * c.abs());

		return new Complex(n * c.getReal(), n * c.getImaginary());
	}

	static double asinh(double x) {
		return Math.log(x + Math.sqrt(x * x + 1));
	}

	static double acosh(double x) {
		return Math.log(x + Math.sqrt(x * x - 1));
	}

}