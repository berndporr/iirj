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
public class ParameterChecksTest {

	static String prefix="target/surefire-reports/parameter_checks/";

	void createDir() throws Exception {
		File dir = new File(prefix);
		dir.mkdirs();
	}		

	@Test
	public void correctFsTest() throws Exception {

		Butterworth butterworth1 = new Butterworth();
		butterworth1.lowPass(4, 250, 50);
		
		Butterworth butterworth2 = new Butterworth();
		butterworth2.bandPass(2, 250, 50, 5);

		Butterworth butterworth3 = new Butterworth();
		butterworth3.bandStop(2, 250, 50, 5);

		Butterworth butterworth4 = new Butterworth();
		butterworth4.highPass(4, 250, 50);
	}

	@Test
	public void wrongFcTestLowpass() throws Exception {
		Butterworth butterworth = new Butterworth();
		try {
			butterworth.lowPass(4, 250, 125);
			Assert.fail("Exception not generated for wrong fc.");
		} catch (Exception e) {
			System.out.println("Lowpass exception for fc = 0.5: "+e.getMessage());
		}
	}

	@Test
	public void wrongFcTestHighpass() throws Exception {
		Butterworth butterworth = new Butterworth();
		try {
			butterworth.highPass(4, 250, 125);
			Assert.fail("Exception not generated for wrong fc.");
		} catch (Exception e) {
			System.out.println("Highpass exception for fc = 0.5: "+e.getMessage());
		}
	}

	@Test
	public void wrongFcTestBandpass() throws Exception {
		Butterworth butterworth = new Butterworth();
		try {
			butterworth.bandPass(2, 250, 125, 5);
			Assert.fail("Exception not generated for wrong fc.");
		} catch (Exception e) {
			System.out.println("Bandpass exception for fc = 0.5: "+e.getMessage());
		}
	}

	@Test
	public void wrongFcTestBandstop() throws Exception {
		Butterworth butterworth = new Butterworth();
		try {
			butterworth.bandStop(2, 250, 125, 5);
			Assert.fail("Exception not generated for wrong fc.");
		} catch (Exception e) {
			System.out.println("Bandstop exception for fc = 0.5: "+e.getMessage());
		}
	}

	@Test
	public void negFcTestLowpass() throws Exception {
		Butterworth butterworth = new Butterworth();
		try {
			butterworth.lowPass(4, 250, -1);
			Assert.fail("Exception not generated for wrong fc.");
		} catch (Exception e) {
			System.out.println("Lowpass exception for fc < 0: "+e.getMessage());
		}
	}

	@Test
	public void negFcTestHighpass() throws Exception {
		Butterworth butterworth = new Butterworth();
		try {
			butterworth.highPass(4, 250, -1);
			Assert.fail("Exception not generated for wrong fc.");
		} catch (Exception e) {
			System.out.println("Highpass exception for fc < 0: "+e.getMessage());
		}
	}

	@Test
	public void negFcTestBandpass() throws Exception {
		Butterworth butterworth = new Butterworth();
		try {
			butterworth.bandPass(2, 250, -1, 5);
			Assert.fail("Exception not generated for wrong fc.");
		} catch (Exception e) {
			System.out.println("Bandpass exception for fc < 0: "+e.getMessage());
		}
	}

	@Test
	public void negFcTestBandstop() throws Exception {
		Butterworth butterworth = new Butterworth();
		try {
			butterworth.bandStop(2, 250, -1, 5);
			Assert.fail("Exception not generated for wrong fc.");
		} catch (Exception e) {
			System.out.println("Bandstop exception for fc < 0: "+e.getMessage());
		}
	}

	public void main(String args[]) {
		try {
			correctFsTest();
			wrongFcTestLowpass();
			wrongFcTestHighpass();
			wrongFcTestBandpass();
			wrongFcTestBandstop();
			negFcTestLowpass();
			negFcTestHighpass();
			negFcTestBandpass();
			negFcTestBandstop();
		} catch (Exception e) {
		}
	}
}
