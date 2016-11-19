package uk.me.berndporr.iirj;
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import java.io.File;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.util.Scanner;

import uk.me.berndporr.iirj.Butterworth;

import org.junit.Assert;
import org.junit.Test;

// Detect the heartbeat in an ECG
// The idea is to create a matched filter for ECG which in turn is
// a bandpass and which in turn could be understood as a wavelet.
// The center and bandwidth here are experimental results so that the
// impulse response resembles the timing of an R peak.
public class DetectorTest {

	static String prefix="target/surefire-reports/detector/";

	void createDir() {
		File dir = new File(prefix);
		dir.mkdirs();
	}		


	@Test
	public void detTest() throws Exception {
		createDir();
		
		Butterworth butterworth = new Butterworth();
		// this fakes an R peak so we have a matched filter!
		butterworth.bandPass(2,250,20,15);
		
		FileOutputStream os = new FileOutputStream(prefix+"det.txt");
		PrintStream bp = new PrintStream(os);
		
		FileOutputStream heartrate = new FileOutputStream(prefix+"hr.txt");
		PrintStream hr = new PrintStream(heartrate);
		
		Scanner is = new Scanner(new File("src/test/resources/ecg.dat"));
		
		double max = 0;
		double t1=0,t2=0;
		int notDet = 0;
		double time = 0;
		int ignore = 100;
		int sampleno = 0;

		// let's do an impulse response
		while(is.hasNextLine()) {
			double v=0;
			time = time + 1.0/125.0;
			String data = is.nextLine();
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
					int r = (int)Math.round((t1-t2)*60);
					if (r>30) {
						hr.println(String.format("%d %d",sampleno,r));
						Assert.assertTrue((r>60)&&(r<160));
					}
					t2 = t1;
				}
			}
			sampleno++;
		}

		os.close();
		heartrate.close();
		is.close();

	}
	
	
	public void main(String args[]) {
		try {
		detTest();
		} catch (Exception e) {};
	}

}
