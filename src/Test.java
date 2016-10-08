import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.FileOutputStream;

import uk.me.berndporr.iirj.Butterworth;


// impulse response of a lowpass filter
public class Test {

	static void bandPassTest() {
		Butterworth butterworth = new Butterworth();
		butterworth.bandPass(2,250,50,5);
		
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
		Butterworth butterworth = new Butterworth();
		butterworth.bandStop(2,250,50,5);
		
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
		Butterworth butterworth = new Butterworth();
		butterworth.lowPass(4,250,50);
		
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
	
	
	static void highPassTest() {
		Butterworth butterworth = new Butterworth();
		butterworth.highPass(4,250,50);
		
		FileOutputStream os = null;
		try {
			os = new FileOutputStream("hp.txt");
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
		highPassTest();
		bandPassTest();
		bandStopTest();
	}

}
