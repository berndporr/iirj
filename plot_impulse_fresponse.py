#!/usr/bin/python3

import matplotlib.pyplot as plt
import numpy as np
import sys

# Plots the impulse response of the Bandstop and its frequency response

def plot_if(figno,name,figtitle):
    plt.figure(figno)
    plt.suptitle(figtitle)
    fs = 250
    y = np.loadtxt(name);
    plt.subplot(311)
    plt.title("Impulse response")
    plt.plot(y);
    #
    # Fourier Transform
    yf = np.fft.fft(y)
    plt.subplot(312)
    fx = np.linspace(0,fs,len(yf))
    plt.plot(fx,20*np.log10(abs(yf)))
    plt.xlim(0,fs/2)
    plt.title("Frequency response")
    plt.xlabel("f/Hz")
    plt.ylabel("gain/dB")

    plt.subplot(313)
    p = -np.diff(np.unwrap(np.angle(yf))) / np.diff(fx * 2 * np.pi)
    plt.plot(np.linspace(0,fs,len(yf)-1),p)
    plt.xlim(0,fs/2)
    plt.ylim(-0.075,0.075)
    plt.title("Phase response")
    plt.xlabel("f/Hz")
    plt.ylabel("delay/secs")

if len(sys.argv) < 2:
    print("Specify which filter shall be plotted: bessel, butterworth, chebyshevI, chebyshevII.")
    quit()
    
prefix = "target/surefire-reports/"+sys.argv[1]+"/"
    
plot_if(1,prefix+"lp.txt","Lowpass")

plot_if(2,prefix+"hp.txt","Highpass")

plot_if(3,prefix+"bs.txt","Bandstop")

plot_if(4,prefix+"bp.txt","Bandpass")

plt.show()
