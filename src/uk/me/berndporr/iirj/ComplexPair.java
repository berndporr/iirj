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
 * @author Bernd Porr
 *
 */
public class ComplexPair {

        public Complex first;
        public Complex second;

        ComplexPair (Complex c1,
                     Complex c2) {
            first = c1;
            second = c2;
        }

        ComplexPair (Complex c1) {
            first = c1;
            second = new Complex(0,0);
        }

        boolean isConjugate () {
            return second.equals(first.conjugate());
        }

        boolean isReal () {
            return first.getImaginary() == 0 && second.getImaginary() == 0;
        }

        // Returns true if this is either a conjugate pair,
        // or a pair of reals where neither is zero.
        boolean isMatchedPair () {
            if (first.getImaginary() != 0)
                return second.equals(first.conjugate());
            else
            return second.getImaginary() == 0 &&
                    second.getReal() != 0 &&
                    first.getReal() != 0;
        }

        boolean is_nan()
        {
            return first.isNaN() || second.isNaN();
        }
    };
