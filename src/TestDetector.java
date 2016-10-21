import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.util.Scanner;

import uk.me.berndporr.iirj.Butterworth;


// Detect the heartbeat in an ECG
// The idea is to create a matched filter for ECG which in turn is
// a bandpass and which in turn could be understood as a wavelet.
// The center and bandwidth here are experimental results so that the
// impulse response resembles the timing of an R peak.
public class TestDetector {

	static void bandPassTest() {
		Butterworth butterworth = new Butterworth();
		// this fakes an R peak so we have a matched filter!
		butterworth.bandPass(2,250,20,15);
		
		FileOutputStream os = null;
		try {
			os = new FileOutputStream("det.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		PrintStream bp = new PrintStream(os);
		
		Scanner is = null;
		try {
			is = new Scanner(new File("ecg.dat"));
		} catch (Exception e) {}
		
		double max = 0;
		double t1=0,t2=0;
		int notDet = 0;
		double time = 0;
		int ignore = 100;

		// let's do an impulse response
		while(is.hasNextLine()) {
			double v=0;
			time = time + 1.0/125.0;
			String data = is.nextLine();
			//System.out.println(data);
			v = Double.parseDouble(data);
			v = butterworth.filter(v);
			v = v*v;
			bp.println(""+v);
			if (ignore>0) {
				ignore--;
			} else {
				if (v > max) {
					max = v;
				}
			}
			if (notDet>0) {
				notDet--;
			} else {
				if (v > 0.5*max) {
					t1 = time;
					notDet = 50;
					System.out.println(""+(t1-t2)*60);
					t2 = t1;
				}
			}
		}

		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String args[]) {
		bandPassTest();
	}

}
