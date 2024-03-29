#!/usr/bin/python3

import matplotlib.pyplot as plt
import scipy
import numpy as np

# Plots the impulse response of the Bandstop and its frequency response

def plot_if(name,figtitle,fs = 1000):
    plt.suptitle(figtitle)
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
    plt.ylabel("gain/dB")

    plt.subplot(313)
    p = -np.diff(np.unwrap(np.angle(yf))) / np.diff(fx * 2 * np.pi)
    plt.plot(np.linspace(0,fs,len(yf)-1),p)
    plt.xlim(0,fs/2)
    plt.title("Phase response")
    if fs == 1:
        plt.xlabel("f/1/samples")
        plt.ylabel("delay/samples")
        plt.ylim(-200,200)
    else:
        plt.ylim(-0.075,0.075)
        plt.xlabel("f/Hz")
        plt.ylabel("delay/secs")
    
plot_if("app/elliptic.dat","Elliptic filter")

plt.show()
