import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.FileOutputStream;

import uk.me.berndporr.iirj.Elliptic;


// Various impulse responses written out to files so that you can plot them
public class TestElliptic {
	
	static double ripple = 0.1; // db
	static double rolloff = 2;

	static void bandPassTest() {
		Elliptic butterworth = new Elliptic();
		butterworth.bandPass(2,250,50,5,ripple,rolloff);
		
		FileOutputStream os = null;
		try {
			os = new FileOutputStream("bp.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		PrintStream bp = new PrintStream(os);

		// let's do an impulse response
		for(int i=0;i<500;i++) {
			double v=0;
			if (i == 10) v = 1; 
			v = butterworth.filter(v);
			bp.println(""+v);
		}

		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	static void bandStopTest() {
		Elliptic butterworth = new Elliptic();
		butterworth.bandStop(2,250,50,5,ripple,rolloff);
		
		FileOutputStream os = null;
		try {
			os = new FileOutputStream("bs.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		PrintStream bp = new PrintStream(os);

		// let's do an impulse response
		for(int i=0;i<500;i++) {
			double v=0;
			if (i == 10) v = 1; 
			v = butterworth.filter(v);
			bp.println(""+v);
		}

		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	static void lowPassTest() {
		Elliptic butterworth = new Elliptic();
		butterworth.lowPass(4,250,50,ripple,rolloff);
		
		FileOutputStream os = null;
		try {
			os = new FileOutputStream("lp.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		PrintStream bp = new PrintStream(os);

		// let's do an impulse response
		for(int i=0;i<500;i++) {
			double v=0;
			if (i == 10) v = 1; 
			v = butterworth.filter(v);
			bp.println(""+v);
		}

		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String args[]) {
		lowPassTest();
		bandPassTest();
		bandStopTest();
	}

}
