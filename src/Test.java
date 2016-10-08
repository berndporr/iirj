import uk.me.berndporr.iirj.Butterworth;


// impulse response of a lowpass filter
public class Test {

	public static void main(String args[]) {
		Butterworth butterworth = new Butterworth();
		butterworth.bandPass(2,250,50,5);

		// let's do an impulse response
		for(int i=0;i<500;i++) {
			double v=0;
			if (i == 10) v = 1; 
			v = butterworth.filter(v);
			System.out.println(""+v);
		}
	}

}
