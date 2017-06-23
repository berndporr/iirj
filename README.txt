iirj
====

An IIR filter library written in JAVA.

Filters: Butterworth, Bessel and Chebyshev Type I/II

It's based on the IIR1 library: https://github.com/berndporr/iir1
which in turn is based on https://github.com/vinniefalco/DSPFilters
by Vinnie Falco.

Dependencies
------------
You need the Apache Commons Math libraries from here:
http://commons.apache.org/proper/commons-math/index.html

maven is used as a built system

Usage
-----
import uk.me.berndporr.iirj.*;

For example the Butterworth filter:

1) Init
Butterworth butterworth = new Butterworth();

1a) Bandstop
butterworth.bandStop(order,Samplingfreq,Center freq,Width in frequ);

1b) Bandpass
butterworth.bandPass(order,Samplingfreq,Center freq,Width in frequ);

1c) Lowpass
butterworth.lowPass(order,Samplingfreq,Cutoff frequ);

1d) Highpass
butterworth.highPass(order,Samplingfreq,Cutoff frequ);

2) Filtering
sample by sample for realtime processing:

v = butterworth.filter(v)

Coding examples
---------------
See the *Test.java files for complete examples. Run them with
"mvn test"
These test programs write different impulse responses of
filters to text files which you can then plot with
the octave script filtertest.m Also very useful to tweak the
filter parameters and see the effects.

Installation
------------
Just run: "mvn install" to add it to your local maven respository.

Android Studio
--------------
dependencies {

    repositories {
        mavenLocal()
    }

    compile group: 'uk.me.berndporr', name:'iirj', version: '1.0'
    compile group: 'org.apache.commons', name: 'commons-math3', version: '3.6.1'
}

Documentation
-------------
Run: "mvn javadoc:javadoc" to generate the JavaDoc
Run: "mvn site" to generate the web pages containing the documentation.

Testing
-------
Run: "mvn test"
this will create impulse responses in subdirectories
for the filters implemented in: 'target/surefire-reports'.
Then install octave, copy the script
'src/test/resources/filtertest.m'
in these subdirectories and run it from there. You should see the
different frequency reponses of these filters. You can try
out different filter parameters by modifiing the test
scripts and re-run 'mvn test'.
The script DetectorTest uses a bandpass filter to detect the
heartbeats of an ECG recording faking a matched filter which could
be also seen as a 1st approximation of a wavelet. The heartrate is
stored in hr.txt.

Have fun

/Bernd Porr
mail@berndporr.me.uk
