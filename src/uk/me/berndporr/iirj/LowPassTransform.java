package uk.me.berndporr.iirj;
import org.apache.commons.math3.complex.Complex;

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

public class LowPassTransform {


    double f;

    private Complex transform(Complex c) {
        if (c.isInfinite())
            return new Complex(-1, 0);

        // frequency transform
        c = c.multiply(f);

        Complex one = new Complex(1, 0);

        // bilinear low pass transform
        return (one.add(c)).divide(one.subtract(c));
    }

    public LowPassTransform(double fc,
                     LayoutBase digital,
                     LayoutBase analog) {
        digital.reset();

        // prewarp
        f = Math.tan(Math.PI * fc);

        int numPoles = analog.getNumPoles();
        int pairs = numPoles / 2;
        for (int i = 0; i < pairs; ++i) {
            PoleZeroPair pair = analog.getPair(i);
            digital.addPoleZeroConjugatePairs(transform(pair.poles.first),
                    transform(pair.zeros.first));
        }

        if ((numPoles & 1) == 1) {
            PoleZeroPair pair = analog.getPair(pairs);
            digital.add(transform(pair.poles.first),
                    transform(pair.zeros.first));
        }

        digital.setNormal(analog.getNormalW(),
                analog.getNormalGain());
    }

}
