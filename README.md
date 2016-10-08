# iirj
An IIR filter library written in JAVA.

It's based on the IIR1 library by Vinnie Falco and myself.

You need the Apache Commons Math libraries:

http://commons.apache.org/proper/commons-math/index.html

Usage (lowPass,highPass,bandpass and bandstop are supported just now):

Init:

Butterworth butterworth = new Butterworth();

butterworth.bandStop(2,250,50,5);

Then filter with sample by sample:

v = butterworth.filter(v)

See also test.java for a complete example.

This is work in progress and I'll be adding more transforms and
filters.

Have fun

/Bernd Porr
