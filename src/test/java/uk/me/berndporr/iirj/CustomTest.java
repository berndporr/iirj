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

// Tests if the Cascade works, the filter stages and
// if the custom parameters are properly transmitted.
public class CustomTest {

	static String prefix="target/surefire-reports/butterworth/";

	final double[][] coeff1 =
	{{ 0.02008337,  0.04016673,  0.02008337,
	   1,         -1.56101808,  0.64135154}};

	final double[] input1 = {
		-1.0,
		0.5,
		1.0};

	final double[] result1 = {
		-2.00833656e-02,
		-6.14755450e-02,
		-6.30005740e-02};

	final double[][] coeff2 =
	{{1.78260999e-03,  3.56521998e-03,  1.78260999e-03,
	  1.00000000e+00,  -1.25544047e+00, 4.09013783e-01},
	 {1.00000000e+00,  2.00000000e+00,  1.00000000e+00,
	  1.00000000e+00,  -1.51824184e+00, 7.03962657e-01}};

	final double[] input2 = {
		-1,0.5,-1,0.5,-0.3,3,-1E-5
	};

	final double[] result2 = {
		-0.00178261, -0.01118353, -0.03455084, -0.07277369, -0.11973872, -0.158864,
		-0.15873629
	};

	final double delta = 1E-5;

	@Test
	public void test1() throws Exception {

		SOSCascade cust = new SOSCascade();
		cust.setup(coeff1);
		int i = 0;
		for(double v:input1) {
			Assert.assertEquals(cust.filter(v),result1[i++],delta);
		}
		
	}

	@Test
	public void test2() throws Exception {

		SOSCascade cust = new SOSCascade();
		cust.setup(coeff1,DirectFormAbstract.DIRECT_FORM_I);
		int i = 0;
		for(double v:input1) {
			Assert.assertEquals(cust.filter(v),result1[i++],delta);
		}
		
	}

	@Test
	public void test3() throws Exception {

		SOSCascade cust = new SOSCascade();
		cust.setup(coeff2);
		int i = 0;
		for(double v:input2) {
			Assert.assertEquals(cust.filter(v),result2[i++],delta);
		}
		
	}

	@Test
	public void test4() throws Exception {

		SOSCascade cust = new SOSCascade();
		cust.setup(coeff2,DirectFormAbstract.DIRECT_FORM_I);
		int i = 0;
		for(double v:input2) {
			Assert.assertEquals(cust.filter(v),result2[i++],delta);
		}
		
	}

}
