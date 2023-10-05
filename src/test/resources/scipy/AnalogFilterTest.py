import numpy as np
from scipy.signal import butter, lfilter, bessel, cheby1, cheby2, sosfilt
import matplotlib.pyplot as plt


def showSignals(time, signal, filtered_signal):
    plt.plot(time, signal, label='Original Signal')
    plt.plot(time, filtered_signal, label='Filtered Signal (Butterworth, 350 Hz lowpass)')
    plt.xlabel('Time (s)')
    plt.ylabel('Amplitude')
    plt.legend()
    plt.show()


def show_fft(time, signal, filtered_signal, sample_rate):
    N = len(signal)
    fft_original = np.fft.fft(signal)
    fft_filtered = np.fft.fft(filtered_signal)
    freqs = np.fft.fftfreq(N, 1 / sample_rate)

    # Only take the positive frequencies (up to the Nyquist frequency)
    positive_freqs = freqs[:N // 2]
    fft_original_positive = np.abs(fft_original[:N // 2])
    fft_filtered_positive = np.abs(fft_filtered[:N // 2])

    plt.figure()
    plt.plot(positive_freqs, fft_original_positive, label='Original Signal')
    plt.plot(positive_freqs, fft_filtered_positive, label='Filtered Signal')
    plt.xlabel('Frequency (Hz)')
    plt.ylabel('Amplitude')
    plt.title('FFT of Original and Filtered Signals')
    plt.legend()
    plt.show()


def export_to_csv(time, filtered_signal, path):
    with open(path, 'w') as file:
        # Write the header row
        file.write('time;signal\n')
        # Write the data rows
        for t, s in zip(time, filtered_signal):
            file.write(f'{t:.4f};{s:.5f}\n')


def readData(filename):
    # Read the data
    data = np.loadtxt(fname=filename, delimiter=';')
    time = data[:, 0]
    signal = data[:, 1]
    return time, signal


def filterAndExport(sos, signal, exportName):
    # Apply the filter to the signal (using lfilter for one-pass filtering)
    filtered_signal = sosfilt(sos, signal)
    # Export the result to a CSV file
    export_to_csv(time, filtered_signal, exportName)
    print(f"Filtered signal has been saved to {exportName}")

def createFileNameFromFilter(filtertype, btype, lc, hc, order):
    return str(filtertype + "-" + btype + "-LC_" + str(lc) + "-HC_" + str(hc) + "-Order_" + str(order) + ".csv")

time, signal = readData('signal.txt')

# Calculate the sample rate
sample_rate = 1 / (time[1] - time[0])
lc = 200
hc = 300

filtertypes = ['Butterworth', 'Bessel', 'Chebychev1', 'Chebychev2']
btypes = ['lowpass', 'highpass', 'bandpass', 'bandstop']
orders = [1,2,4,5,10]

###########################################################################

for filtertype in filtertypes:
    for btype in btypes:
        for order in orders:
            # Handle different critical frequencies for lowpass/highpass vs bandpass/bandstop
            if btype in ['lowpass', 'highpass']:
                Wn = hc if btype == 'lowpass' else lc
            else:
                Wn = [lc, hc]

            if filtertype == "Butterworth":
                sos = butter(N=order, Wn=Wn, fs=sample_rate, btype=btype, output='sos')
            elif filtertype == "Bessel":
                sos = bessel(N=order, Wn=Wn, fs=sample_rate, btype=btype, output='sos')
            elif filtertype == "Chebychev1":
                sos = cheby1(N=order, rp=1, Wn=Wn, fs=sample_rate, btype=btype, output='sos')
            elif filtertype == "Chebychev2":
                sos = cheby2(N=order, rs=1, Wn=Wn, fs=sample_rate, btype=btype, output='sos')

            filename = createFileNameFromFilter(filtertype.lower(), btype, lc, hc, order)
            filterAndExport(sos, signal, filename)


############################################################################


#b, a = butter(N=2, Wn=lc, fs=sample_rate, btype='lowpass')
#filterAndExport(b, a, signal, "Butterworth-Lowcut-350Hz-2.Order.txt")

#b, a = butter(N=1, Wn=hc, fs=sample_rate, btype='highpass')#
#filterAndExport(b, a, signal, "Butterworth-Highcut-450Hz-1.Order.txt")

#b, a = butter(N=2, Wn=hc, fs=sample_rate, btype='highpass')
#filterAndExport(b, a, signal, "Butterworth-Highcut-450Hz-2.Order.txt")


# Plot the original and filtered signals
# showSignals(time, signal, filtered_signal)

# Show the FFT of both signals
# show_fft(time, signal, filtered_signal, sample_rate)
