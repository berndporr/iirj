# iirj
An IIR filter library written in JAVA.

It's based on the IIR1 library by Vinnie Falco and myself.

## Dependencies
You need the Apache Commons Math libraries from here:

http://commons.apache.org/proper/commons-math/index.html

## Usage

### Init

Butterworth butterworth = new Butterworth();

### Bandstop
butterworth.bandStop(order,Samplingfreq,Center freq,Width in frequ);

### Bandpass
butterworth.bandPass(order,Samplingfreq,Center freq,Width in frequ);

### Lowpass
butterworth.lowPass(order,Samplingfreq,Cutoff frequ);

### Highpass
butterworth.highPass(order,Samplingfreq,Cutoff frequ);

### Filtering
sample by sample for realtime processing:

v = butterworth.filter(v)

See also Test.java for a complete example.

Have fun

/Bernd Porr
