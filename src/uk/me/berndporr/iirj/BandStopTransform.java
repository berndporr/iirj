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
 * Created by bp1 on 05/10/16.
 */
public class BandStopTransform {


  double wc;
  double wc2;
  double a;
  double b;
  double a2;
  double b2;


    BandStopTransform(double fc,
                      double fw,
                      LayoutBase digital,
                      LayoutBase analog) {
        digital.reset();

        double ww = 2 * Math.PI * fw;

        wc2 = 2 * Math.PI * fc - (ww / 2);
        wc = wc2 + ww;

        // this is crap
        if (wc2 < 1e-8)
            wc2 = 1e-8;
        if (wc > Math.PI - 1e-8)
            wc = Math.PI - 1e-8;

        a = Math.cos((wc + wc2) * .5) /
                Math.cos((wc - wc2) * .5);
        b = Math.tan((wc - wc2) * .5);
        a2 = a * a;
        b2 = b * b;

        int numPoles = analog.getNumPoles();
        int pairs = numPoles / 2;
        for (int i = 0; i < pairs; ++i) {
            PoleZeroPair pair = analog.getPair(i);
            ComplexPair p = transform(pair.poles.first);
            ComplexPair z = transform(pair.zeros.first);

            digital.addPoleZeroConjugatePairs(p.first, z.first);
            digital.addPoleZeroConjugatePairs(p.second, z.second);
        }

        if ((numPoles & 1) == 1) {
            ComplexPair poles = transform(analog.getPair(pairs).poles.first);
            ComplexPair zeros = transform(analog.getPair(pairs).zeros.first);

            digital.add(poles, zeros);
        }

        if (fc < 0.25)
            digital.setNormal(Math.PI, analog.getNormalGain());
        else
            digital.setNormal(0, analog.getNormalGain());
    }

    ComplexPair transform(Complex c) {
        if (c.isInfinite())
            c = new Complex(-1);
        else
            c = ((new Complex(1)).add(c)).divide((new Complex(1)).subtract(c)); // bilinear

        Complex u = new Complex(0);
        u = MathSupplement.addmul(u, 4 * (b2 + a2 - 1), c);
        u = u.add(8 * (b2 - a2 + 1));
        u = u.multiply(c);
        u = u.add(4 * (a2 + b2 - 1));
        u = u.sqrt();

        Complex v = u.multiply(-.5);
        v = v.add(a);
        v = MathSupplement.addmul(v, -a, c);

        u = u.multiply(.5);
        u = u.add(a);
        u = MathSupplement.addmul(u, -a, c);

        Complex d = new Complex(b + 1);
        d = MathSupplement.addmul(d, b - 1, c);

        return new ComplexPair(u.divide(d), v.divide(d));
    }

}
