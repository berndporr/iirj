import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.FileOutputStream;

import uk.me.berndporr.iirj.ChebyshevI;

// Various impulse responses written out to files so that you can plot them
public class TestChebyshevI {

	static double ripple = 0.1; // db

	static void bandPassTest() {
		ChebyshevI chebyshevI = new ChebyshevI();
		chebyshevI.bandPass(2, 250, 50, 5, ripple);

		FileOutputStream os = null;
		try {
			os = new FileOutputStream("bp.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		PrintStream bp = new PrintStream(os);

		// let's do an impulse response
		for (int i = 0; i < 500; i++) {
			double v = 0;
			if (i == 10)
				v = 1;
			v = chebyshevI.filter(v);
			bp.println("" + v);
		}

		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void bandStopTest() {
		ChebyshevI chebyshevI = new ChebyshevI();
		chebyshevI.bandStop(2, 250, 50, 5, ripple);

		FileOutputStream os = null;
		try {
			os = new FileOutputStream("bs.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		PrintStream bp = new PrintStream(os);

		// let's do an impulse response
		for (int i = 0; i < 500; i++) {
			double v = 0;
			if (i == 10)
				v = 1;
			v = chebyshevI.filter(v);
			bp.println("" + v);
		}

		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void lowPassTest() {
		ChebyshevI chebyshevI = new ChebyshevI();
		chebyshevI.lowPass(4, 250, 50, ripple);

		FileOutputStream os = null;
		try {
			os = new FileOutputStream("lp.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		PrintStream bp = new PrintStream(os);

		// let's do an impulse response
		for (int i = 0; i < 500; i++) {
			double v = 0;
			if (i == 10)
				v = 1;
			v = chebyshevI.filter(v);
			bp.println("" + v);
		}

		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void highPassTest() {
		ChebyshevI chebyshevI = new ChebyshevI();
		chebyshevI.highPass(4, 250, 50, ripple);

		FileOutputStream os = null;
		try {
			os = new FileOutputStream("hp.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		PrintStream bp = new PrintStream(os);

		// let's do an impulse response
		for (int i = 0; i < 500; i++) {
			double v = 0;
			if (i == 10)
				v = 1;
			v = chebyshevI.filter(v);
			bp.println("" + v);
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
