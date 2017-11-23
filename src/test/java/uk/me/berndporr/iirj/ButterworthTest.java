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

import uk.me.berndporr.iirj.Butterworth;

import org.junit.Assert;
import org.junit.Test;

// Various impulse responses written out to files so that you can plot them
public class ButterworthTest {

	static String prefix="target/surefire-reports/butterworth/";

	void createDir() throws Exception {
		File dir = new File(prefix);
		dir.mkdirs();
	}		

	@Test
	public void lowPassTest() throws Exception {

		Butterworth butterworth = new Butterworth();
		butterworth.lowPass(4, 250, 50);

		createDir();
		FileOutputStream os = new FileOutputStream(prefix+"lp.txt");
		PrintStream bp = new PrintStream(os);

		// let's do an impulse response
		for (int i = 0; i < 500; i++) {
			double v = 0;
			if (i == 10)
				v = 1;
			v = butterworth.filter(v);
			bp.println("" + v);
		}
		Assert.assertTrue(Math.abs(butterworth.filter(0))<1E-80);
		Assert.assertTrue(Math.abs(butterworth.filter(0))!=0.0);
		Assert.assertTrue(Math.abs(butterworth.filter(0))!=Double.NaN);

		os.close();
	}

	@Test
	public void bandPassTest() throws Exception {
		Butterworth butterworth = new Butterworth();
		butterworth.bandPass(2, 250, 50, 5);

		createDir();
		FileOutputStream os = new FileOutputStream(prefix+"bp.txt");
		PrintStream bp = new PrintStream(os);

		// let's do an impulse response
		for (int i = 0; i < 500; i++) {
			double v = 0;
			if (i == 10)
				v = 1;
			v = butterworth.filter(v);
			bp.println("" + v);
		}
		Assert.assertTrue(Math.abs(butterworth.filter(0))<1E-10);
		Assert.assertTrue(Math.abs(butterworth.filter(0))!=0.0);
		Assert.assertTrue(Math.abs(butterworth.filter(0))!=Double.NaN);

		os.close();
	}

	@Test
	public void bandStopTest() throws Exception {
		Butterworth butterworth = new Butterworth();
		butterworth.bandStop(2, 250, 50, 5);

		createDir();
		FileOutputStream os = new FileOutputStream(prefix+"bs.txt");
		PrintStream bp = new PrintStream(os);

		// let's do an impulse response
		for (int i = 0; i < 500; i++) {
			double v = 0;
			if (i == 10)
				v = 1;
			v = butterworth.filter(v);
			bp.println("" + v);
		}
		Assert.assertTrue(Math.abs(butterworth.filter(0))<1E-10);
		Assert.assertTrue(Math.abs(butterworth.filter(0))!=0.0);
		Assert.assertTrue(Math.abs(butterworth.filter(0))!=Double.NaN);

		os.close();
	}

	@Test
	public void bandStopDCTest() throws Exception {
		Butterworth butterworth = new Butterworth();
		butterworth.bandStop(2, 250, 50, 5);

		createDir();
		FileOutputStream os = new FileOutputStream(prefix+"bsdc.txt");
		PrintStream bp = new PrintStream(os);

		// let's do an impulse response
		for (int i = 0; i < 500; i++) {
			double v = 0;
			if (i>10) v = 1;
			v = butterworth.filter(v);
			bp.println("" + v);
		}
		Assert.assertTrue(Math.abs(butterworth.filter(1))>0.99999999);
		Assert.assertTrue(Math.abs(butterworth.filter(1))<1.00000001);
		Assert.assertTrue(Math.abs(butterworth.filter(1))!=Double.NaN);

		os.close();
	}

	@Test
	public void highPassTest() throws Exception {
		Butterworth butterworth = new Butterworth();
		butterworth.highPass(4, 250, 50);

		createDir();
		FileOutputStream os = new FileOutputStream(prefix+"hp.txt");
		PrintStream bp = new PrintStream(os);

		// let's do an impulse response
		for (int i = 0; i < 500; i++) {
			double v = 0;
			if (i == 10)
				v = 1;
			v = butterworth.filter(v);
			bp.println("" + v);
		}
		Assert.assertTrue(Math.abs(butterworth.filter(0))<1E-80);
		Assert.assertTrue(Math.abs(butterworth.filter(0))!=0.0);
		Assert.assertTrue(Math.abs(butterworth.filter(0))!=Double.NaN);

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
