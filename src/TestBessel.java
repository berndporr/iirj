import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.FileOutputStream;

import uk.me.berndporr.iirj.Bessel;


// Various impulse responses written out to files so that you can plot them
public class TestBessel {

	static void bandPassTest() {
		Bessel bessel = new Bessel();
		bessel.bandPass(2,250,50,5);
		
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
			v = bessel.filter(v);
			bp.println(""+v);
		}

		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	static void bandStopTest() {
		Bessel bessel = new Bessel();
		bessel.bandStop(2,250,50,5);
		
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
			v = bessel.filter(v);
			bp.println(""+v);
		}

		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	static void lowPassTest() {
		Bessel bessel = new Bessel();
		bessel.lowPass(4,250,50);
		
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
			v = bessel.filter(v);
			bp.println(""+v);
		}

		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	static void highPassTest() {
		Bessel bessel = new Bessel();
		bessel.highPass(4,250,50);
		
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
			v = bessel.filter(v);
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
