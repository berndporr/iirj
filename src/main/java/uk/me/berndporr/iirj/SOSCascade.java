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
 * 
 *  Copyright (c) 2009 by Vinnie Falco
 *  Copyright (c) 2016 by Bernd Porr
 */


package uk.me.berndporr.iirj;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexUtils;

/**
 *         User facing class which contains all the methods the user uses
 *         to create Custom filters. This done in this way:
 *         Custom butterworth = new SOSCascade(); 
 *         Then call one of the methods below to 
 *         provide the coefficients.
 */
public class SOSCascade extends Cascade {

	public void setup(int order,
			   double[][] sosCoefficients,
			   int directFormType) {
		setSOScoeff(order,sosCoefficients,directFormType);
	}

	public void setup(int order,
			  double[][] sosCoefficients) {
		setSOScoeff(order,sosCoefficients,DirectFormAbstract.DIRECT_FORM_II);
	}
}
