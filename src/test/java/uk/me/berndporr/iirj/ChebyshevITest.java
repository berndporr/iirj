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


import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.File;

import uk.me.berndporr.iirj.ChebyshevI;

import org.junit.Assert;
import org.junit.Test;

// Various impulse responses written out to files so that you can plot them
public class ChebyshevITest {

	static double ripple = 0.1; // db

	static String prefix="target/surefire-reports/chebyshevI/";

	void createDir() throws Exception {
		File dir = new File(prefix);
		dir.mkdirs();
	}		

	@Test
	public void lowPassTest() throws Exception {
		ChebyshevI chebyshevI = new ChebyshevI();
		chebyshevI.lowPass(4, 250, 50, ripple);

		createDir();

		FileOutputStream os = new FileOutputStream(prefix+"lp.txt");
		PrintStream bp = new PrintStream(os);

		// let's do an impulse response
		for (int i = 0; i < 500; i++) {
			double v = 0;
			if (i == 10)
				v = 1;
			v = chebyshevI.filter(v);
			bp.println("" + v);
		}
		Assert.assertTrue(Math.abs(chebyshevI.filter(0))<1E-49);
		Assert.assertTrue(Math.abs(chebyshevI.filter(0))!=0.0);
		Assert.assertTrue(Math.abs(chebyshevI.filter(0))!=Double.NaN);

		os.close();
	}

	@Test
	public void bandPassTest() throws Exception {
		ChebyshevI chebyshevI = new ChebyshevI();
		chebyshevI.bandPass(2, 250, 50, 5, ripple);

		createDir();

		FileOutputStream os = new FileOutputStream(prefix+"bp.txt");
		PrintStream bp = new PrintStream(os);

		// let's do an impulse response
		for (int i = 0; i < 500; i++) {
			double v = 0;
			if (i == 10)
				v = 1;
			v = chebyshevI.filter(v);
			bp.println("" + v);
		}
		Assert.assertTrue(Math.abs(chebyshevI.filter(0))<1E-15);
		Assert.assertTrue(Math.abs(chebyshevI.filter(0))!=0.0);
		Assert.assertTrue(Math.abs(chebyshevI.filter(0))!=Double.NaN);

		os.close();
	}

	@Test
	public void bandStopTest() throws Exception {
		ChebyshevI chebyshevI = new ChebyshevI();
		chebyshevI.bandStop(2, 250, 50, 5, ripple);

		createDir();

		FileOutputStream os = new FileOutputStream(prefix+"bs.txt");
		PrintStream bp = new PrintStream(os);

		// let's do an impulse response
		for (int i = 0; i < 500; i++) {
			double v = 0;
			if (i == 10)
				v = 1;
			v = chebyshevI.filter(v);
			bp.println("" + v);
		}
		Assert.assertTrue(Math.abs(chebyshevI.filter(0))<1E-5);
		Assert.assertTrue(Math.abs(chebyshevI.filter(0))!=0.0);
		Assert.assertTrue(Math.abs(chebyshevI.filter(0))!=Double.NaN);

		os.close();
	}

	@Test
	public void highPassTest() throws Exception {
		ChebyshevI chebyshevI = new ChebyshevI();
		chebyshevI.highPass(4, 250, 50, ripple);

		createDir();
		FileOutputStream os = new FileOutputStream(prefix+"hp.txt");
		PrintStream bp = new PrintStream(os);

		// let's do an impulse response
		for (int i = 0; i < 500; i++) {
			double v = 0;
			if (i == 10)
				v = 1;
			v = chebyshevI.filter(v);
			bp.println("" + v);
		}
		Assert.assertTrue(Math.abs(chebyshevI.filter(0))<1E-44);
		Assert.assertTrue(Math.abs(chebyshevI.filter(0))!=0.0);
		Assert.assertTrue(Math.abs(chebyshevI.filter(0))!=Double.NaN);

		os.close();
	}

	public void main(String args[]) {
		try {
		lowPassTest();
		highPassTest();
		bandPassTest();
		bandStopTest();
		} catch (Exception e) {
		}
	}
}
