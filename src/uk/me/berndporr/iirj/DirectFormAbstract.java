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


/**
 * Abstract form of the a filter which can have different state variables
 *
 * Direct form I or II is derived from it
 */
public abstract class DirectFormAbstract {

    public void DirectFormAbstract () {
        reset();
    }

    public abstract void reset();

    public abstract double process1 (double in, Biquad s);

    public static final int DIRECT_FORM_I = 0;
    public static final int DIRECT_FORM_II = 1;

};
