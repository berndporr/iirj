# iirj

An IIR filter library written in JAVA.

Filters: Highpass, lowpass, bandpass and bandstop as
Butterworth, Bessel and Chebyshev Type I/II

It's based on the IIR1 library: [https://github.com/berndporr/iir1]
which in turn is based on [https://github.com/vinniefalco/DSPFilters]
by Vinnie Falco.

![alt tag](filtertest.png)

## Usage

import uk.me.berndporr.iirj.*;

For example the Butterworth filter:

### Constructor
  `Butterworth butterworth = new Butterworth();`

### Initialisation
1. Bandstop

   `butterworth.bandStop(order,Samplingfreq,Center freq,Width in frequ);`

2. Bandpass

   `butterworth.bandPass(order,Samplingfreq,Center freq,Width in frequ);`

3. Lowpass

   `butterworth.lowPass(order,Samplingfreq,Cutoff frequ);`

4. Highpass

   `butterworth.highPass(order,Samplingfreq,Cutoff frequ);`

### Filtering
Sample by sample for realtime processing:

```
v = butterworth.filter(v)
```

## Coding examples See the *Test.java files for complete examples. Run
them with `mvn test`. These test programs write the different impulse
responses of the filters to text files which you can then plot with the
octave script `filtertest.m` Also very useful to tweak the filter
parameters and see the effects.

## Installation
Just run: `mvn install` to add it to your local maven respository or
just point your project to Maven Central:

## Maven central
[http://search.maven.org/#artifactdetails%7Cuk.me.berndporr%7Ciirj%7C1.0%7Cjar]

## Android Studio
```
dependencies {
    compile group: 'uk.me.berndporr', name:'iirj', version: '1.0'
}
```

## Documentation
* Run: "mvn javadoc:javadoc" to generate the JavaDoc
* Run: "mvn site" to generate the web pages containing the documentation.

## Testing
Run: `mvn test` which will create impulse responses in subdirectories
for the filters implemented in: `target/surefire-reports`.

To see the frequency responses install octave, copy the script
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
[http://www.berndporr.me.uk]
