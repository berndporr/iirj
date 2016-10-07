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
 * Created by bp1 on 06/10/16.
 */

public class Butterworth extends Cascade {

  class AnalogLowPass extends LayoutBase {

    int m_numPoles;

    AnalogLowPass(int nPoles) {
    	super(nPoles);
      m_numPoles = nPoles;
      setNormal(0, 1);
    }

    void design(int numPoles) {
        m_numPoles = numPoles;

        reset();

        double n2 = 2 * numPoles;
        int pairs = numPoles / 2;
        for (int i = 0; i < pairs; ++i) {
          Complex c = ComplexUtils.polar2Complex(1F, Math.PI + (2 * i + 1) * Math.PI / n2);
          addPoleZeroConjugatePairs(c, Complex.INF);
        }

        if ((numPoles & 1) == 1)
          add(new Complex(-1), Complex.INF);
    }
  }


    public  void lowPass(int order,
			     double sampleRate,
			     double cutoffFrequency) {

      AnalogLowPass m_analogProto = new AnalogLowPass(order);
      m_analogProto.design(order);

      LayoutBase m_digitalProto = new LayoutBase(order);

      new LowPassTransform(
              cutoffFrequency / sampleRate,
              m_digitalProto,
              m_analogProto);

      setLayout(m_digitalProto, DirectFormAbstract.DIRECT_FORM_II);
    }



    public void highPass(int order,
                  double sampleRate,
                  double cutoffFrequency) {

      AnalogLowPass m_analogProto = new AnalogLowPass(order);
      m_analogProto.design(order);

      LayoutBase m_digitalProto = new LayoutBase(order);

      new HighPassTransform(cutoffFrequency / sampleRate,
              m_digitalProto,
              m_analogProto);

      setLayout(m_digitalProto, DirectFormAbstract.DIRECT_FORM_II);
    }


    public void bandStop(int order,
                  double sampleRate,
                  double centerFrequency,
                  double widthFrequency) {

      AnalogLowPass m_analogProto = new AnalogLowPass(order);
      m_analogProto.design(order);

      LayoutBase m_digitalProto = new LayoutBase(order);

      new BandStopTransform(centerFrequency / sampleRate,
              widthFrequency / sampleRate,
              m_digitalProto,
              m_analogProto);

      setLayout(m_digitalProto, DirectFormAbstract.DIRECT_FORM_II);
    }

}
