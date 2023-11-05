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
 *
 *  Copyright (c) 2009 by Vinnie Falco
 *  Copyright (c) 2016 by Bernd Porr
 */

import org.apache.commons.math3.complex.Complex;

/**
 * User facing class which contains all the methods the user uses to create
 * Bessel filters. This done in this way: Bessel bessel = new Bessel(); Then
 * call one of the methods below to create low-,high-,band-, or stopband
 * filters. For example: bessel.bandPass(2,250,50,5);
 *
 * The implementation is based on fixed poles for order 1 to 24 calculated with Scipy 1.7.3
 */
public class Bessel extends Cascade
{

	class AnalogLowPass extends LayoutBase
	{

		int degree;

		public AnalogLowPass(int _degree) {
			super(_degree);
			degree = _degree;
			setNormal(0, 1);
		}

		public void design() {
			reset();

			Complex[] m_root = getPoles(degree);

			Complex inf = Complex.INF;
			int pairs = degree / 2;
			for (int i = 0; i < pairs; ++i) {
				Complex c = m_root[i];
				addPoleZeroConjugatePairs(c, inf);
			}

			if ((degree & 1) == 1) {
				add(new Complex(m_root[pairs].getReal()), inf);
			}
		}
	}

	private void setupLowPass(int order, double sampleRate,
							  double cutoffFrequency, int directFormType) {

		AnalogLowPass m_analogProto = new AnalogLowPass(order);

		m_analogProto.design();

		LayoutBase m_digitalProto = new LayoutBase(order);

		new LowPassTransform(cutoffFrequency / sampleRate, m_digitalProto,
				m_analogProto);

		setLayout(m_digitalProto, directFormType);
	}

	/**
	 * Bessel Lowpass filter with default topology
	 *
	 * @param order           The order of the filter
	 * @param sampleRate      The sampling rate of the system
	 * @param cutoffFrequency the cutoff frequency
	 */
	public void lowPass(int order, double sampleRate, double cutoffFrequency) {
		setupLowPass(order, sampleRate, cutoffFrequency,
				DirectFormAbstract.DIRECT_FORM_II);
	}

	/**
	 * Bessel Lowpass filter with custom topology
	 *
	 * @param order           The order of the filter
	 * @param sampleRate      The sampling rate of the system
	 * @param cutoffFrequency The cutoff frequency
	 * @param directFormType  The filter topology. This is either
	 *                        DirectFormAbstract.DIRECT_FORM_I or DIRECT_FORM_II
	 */
	public void lowPass(int order, double sampleRate, double cutoffFrequency,
						int directFormType) {
		setupLowPass(order, sampleRate, cutoffFrequency, directFormType);
	}

	private void setupHighPass(int order, double sampleRate,
							   double cutoffFrequency, int directFormType) {

		AnalogLowPass m_analogProto = new AnalogLowPass(order);
		m_analogProto.design();

		LayoutBase m_digitalProto = new LayoutBase(order);

		new HighPassTransform(cutoffFrequency / sampleRate, m_digitalProto,
				m_analogProto);

		setLayout(m_digitalProto, directFormType);
	}

	/**
	 * Highpass filter with custom topology
	 *
	 * @param order           Filter order (ideally only even orders)
	 * @param sampleRate      Sampling rate of the system
	 * @param cutoffFrequency Cutoff of the system
	 * @param directFormType  The filter topology. See DirectFormAbstract.
	 */
	public void highPass(int order, double sampleRate, double cutoffFrequency,
						 int directFormType) {
		setupHighPass(order, sampleRate, cutoffFrequency, directFormType);
	}

	/**
	 * Highpass filter with default filter topology
	 *
	 * @param order           Filter order (ideally only even orders)
	 * @param sampleRate      Sampling rate of the system
	 * @param cutoffFrequency Cutoff of the system
	 */
	public void highPass(int order, double sampleRate, double cutoffFrequency) {
		setupHighPass(order, sampleRate, cutoffFrequency,
				DirectFormAbstract.DIRECT_FORM_II);
	}

	private void setupBandStop(int order, double sampleRate,
							   double centerFrequency, double widthFrequency, int directFormType) {

		AnalogLowPass m_analogProto = new AnalogLowPass(order);
		m_analogProto.design();

		LayoutBase m_digitalProto = new LayoutBase(order * 2);

		new BandStopTransform(centerFrequency / sampleRate, widthFrequency
				/ sampleRate, m_digitalProto, m_analogProto);

		setLayout(m_digitalProto, directFormType);
	}

	/**
	 * Bandstop filter with default topology
	 *
	 * @param order           Filter order (actual order is twice)
	 * @param sampleRate      Samping rate of the system
	 * @param centerFrequency Center frequency
	 * @param widthFrequency  Width of the notch
	 */
	public void bandStop(int order, double sampleRate, double centerFrequency,
						 double widthFrequency) {
		setupBandStop(order, sampleRate, centerFrequency, widthFrequency,
				DirectFormAbstract.DIRECT_FORM_II);
	}

	/**
	 * Bandstop filter with custom topology
	 *
	 * @param order           Filter order (actual order is twice)
	 * @param sampleRate      Samping rate of the system
	 * @param centerFrequency Center frequency
	 * @param widthFrequency  Width of the notch
	 * @param directFormType  The filter topology
	 */
	public void bandStop(int order, double sampleRate, double centerFrequency,
						 double widthFrequency, int directFormType) {
		setupBandStop(order, sampleRate, centerFrequency, widthFrequency,
				directFormType);
	}

	private void setupBandPass(int order, double sampleRate,
							   double centerFrequency, double widthFrequency, int directFormType) {

		AnalogLowPass m_analogProto = new AnalogLowPass(order);
		m_analogProto.design();

		LayoutBase m_digitalProto = new LayoutBase(order * 2);

		new BandPassTransform(centerFrequency / sampleRate, widthFrequency
				/ sampleRate, m_digitalProto, m_analogProto);

		setLayout(m_digitalProto, directFormType);
	}

	/**
	 * Bandpass filter with default topology
	 *
	 * @param order           Filter order
	 * @param sampleRate      Sampling rate
	 * @param centerFrequency Center frequency
	 * @param widthFrequency  Width of the notch
	 */
	public void bandPass(int order, double sampleRate, double centerFrequency,
						 double widthFrequency) {
		setupBandPass(order, sampleRate, centerFrequency, widthFrequency,
				DirectFormAbstract.DIRECT_FORM_II);
	}

	/**
	 * Bandpass filter with custom topology
	 *
	 * @param order           Filter order
	 * @param sampleRate      Sampling rate
	 * @param centerFrequency Center frequency
	 * @param widthFrequency  Width of the notch
	 * @param directFormType  The filter topology (see DirectFormAbstract)
	 */
	public void bandPass(int order, double sampleRate, double centerFrequency,
						 double widthFrequency, int directFormType) {
		setupBandPass(order, sampleRate, centerFrequency, widthFrequency,
				directFormType);
	}

	/**
	 * These poles are calculated with Scipy version 1.7.3
	 **/
	public Complex[] getPoles(int N) {
		switch (N) {
			case 1:
				return new Complex[]{new Complex(-0.9999999999999998, -0.0)};
			case 2:
				return new Complex[]{
						new Complex(-0.8660254037844386, 0.5000000000000001),
						new Complex(-0.8660254037844386, -0.5000000000000001)};
			case 3:
				return new Complex[]{
						new Complex(-0.7456403858480766, 0.711366624972835),
						new Complex(-0.9416000265332067, -0.0),
						new Complex(-0.7456403858480766, -0.711366624972835)};
			case 4:
				return new Complex[]{
						new Complex(-0.6572111716718827, 0.830161435004873),
						new Complex(-0.9047587967882448, 0.27091873300387465),
						new Complex(-0.9047587967882448, -0.27091873300387465),
						new Complex(-0.6572111716718827, -0.830161435004873)};
			case 5:
				return new Complex[]{
						new Complex(-0.5905759446119191, 0.9072067564574549),
						new Complex(-0.8515536193688397, 0.4427174639443327),
						new Complex(-0.92644207738776, -0.0),
						new Complex(-0.8515536193688397, -0.4427174639443327),
						new Complex(-0.5905759446119191, -0.9072067564574549)};
			case 6:
				return new Complex[]{
						new Complex(-0.5385526816693109, 0.9616876881954276),
						new Complex(-0.799654185832829, 0.5621717346937318),
						new Complex(-0.909390683047227, 0.18569643967930463),
						new Complex(-0.909390683047227, -0.18569643967930463),
						new Complex(-0.799654185832829, -0.5621717346937318),
						new Complex(-0.5385526816693109, -0.9616876881954276)};
			case 7:
				return new Complex[]{
						new Complex(-0.4966917256672317, 1.0025085084544205),
						new Complex(-0.7527355434093216, 0.6504696305522552),
						new Complex(-0.8800029341523379, 0.32166527623077407),
						new Complex(-0.919487155649029, -0.0),
						new Complex(-0.8800029341523379, -0.32166527623077407),
						new Complex(-0.7527355434093216, -0.6504696305522552),
						new Complex(-0.4966917256672317, -1.0025085084544205)};
			case 8:
				return new Complex[]{
						new Complex(-0.4621740412532123, 1.0343886811269012),
						new Complex(-0.7111381808485397, 0.7186517314108402),
						new Complex(-0.8473250802359334, 0.42590175382729345),
						new Complex(-0.9096831546652911, 0.1412437976671423),
						new Complex(-0.9096831546652911, -0.1412437976671423),
						new Complex(-0.8473250802359334, -0.42590175382729345),
						new Complex(-0.7111381808485397, -0.7186517314108402),
						new Complex(-0.4621740412532123, -1.0343886811269012)};
			case 9:
				return new Complex[]{
						new Complex(-0.4331415561553623, 1.0600736701359301),
						new Complex(-0.6743622686854763, 0.7730546212691185),
						new Complex(-0.8148021112269013, 0.50858156896315),
						new Complex(-0.8911217017079759, 0.25265809345821644),
						new Complex(-0.9154957797499037, -0.0),
						new Complex(-0.8911217017079759, -0.25265809345821644),
						new Complex(-0.8148021112269013, -0.50858156896315),
						new Complex(-0.6743622686854763, -0.7730546212691185),
						new Complex(-0.4331415561553623, -1.0600736701359301)};
			case 10:
				return new Complex[]{
						new Complex(-0.408322073286886, 1.0812748428191246),
						new Complex(-0.641751386698832, 0.8175836167191021),
						new Complex(-0.7837694413101444, 0.5759147538499949),
						new Complex(-0.8688459641284763, 0.34300082337663096),
						new Complex(-0.9091347320900505, 0.11395831373355113),
						new Complex(-0.9091347320900505, -0.11395831373355113),
						new Complex(-0.8688459641284763, -0.34300082337663096),
						new Complex(-0.7837694413101444, -0.5759147538499949),
						new Complex(-0.641751386698832, -0.8175836167191021),
						new Complex(-0.408322073286886, -1.0812748428191246)};
			case 11:
				return new Complex[]{
						new Complex(-0.3868149510055095, 1.0991174667631216),
						new Complex(-0.6126871554915195, 0.8547813893314768),
						new Complex(-0.7546938934722308, 0.6319150050721851),
						new Complex(-0.8453044014712964, 0.41786969178012484),
						new Complex(-0.8963656705721169, 0.20804803750710324),
						new Complex(-0.9129067244518985, -0.0),
						new Complex(-0.8963656705721169, -0.20804803750710324),
						new Complex(-0.8453044014712964, -0.41786969178012484),
						new Complex(-0.7546938934722308, -0.6319150050721851),
						new Complex(-0.6126871554915195, -0.8547813893314768),
						new Complex(-0.3868149510055095, -1.0991174667631216)};
			case 12:
				return new Complex[]{
						new Complex(-0.3679640085526314, 1.1143735756415463),
						new Complex(-0.5866369321861475, 0.8863772751320727),
						new Complex(-0.7276681615395162, 0.6792961178764695),
						new Complex(-0.8217296939939076, 0.48102121151006766),
						new Complex(-0.8802534342016832, 0.2871779503524228),
						new Complex(-0.9084478234140686, 0.0955063652134504),
						new Complex(-0.9084478234140686, -0.0955063652134504),
						new Complex(-0.8802534342016832, -0.2871779503524228),
						new Complex(-0.8217296939939076, -0.48102121151006766),
						new Complex(-0.7276681615395162, -0.6792961178764695),
						new Complex(-0.5866369321861475, -0.8863772751320727),
						new Complex(-0.3679640085526314, -1.1143735756415463)};
			case 13:
				return new Complex[]{
						new Complex(-0.3512792323389817, 1.1275915483177048),
						new Complex(-0.5631559842430192, 0.9135900338325103),
						new Complex(-0.7026234675721276, 0.7199611890171304),
						new Complex(-0.7987460692470972, 0.5350752120696801),
						new Complex(-0.8625094198260551, 0.3547413731172991),
						new Complex(-0.8991314665475196, 0.17683429561610436),
						new Complex(-0.9110914665984182, -0.0),
						new Complex(-0.8991314665475196, -0.17683429561610436),
						new Complex(-0.8625094198260551, -0.3547413731172991),
						new Complex(-0.7987460692470972, -0.5350752120696801),
						new Complex(-0.7026234675721276, -0.7199611890171304),
						new Complex(-0.5631559842430192, -0.9135900338325103),
						new Complex(-0.3512792323389817, -1.1275915483177048)};
			case 14:
				return new Complex[]{
						new Complex(-0.3363868224902031, 1.1391722978398595),
						new Complex(-0.5418766775112306, 0.9373043683516926),
						new Complex(-0.6794256425119225, 0.7552857305042031),
						new Complex(-0.7766591387063627, 0.581917067737761),
						new Complex(-0.8441199160909851, 0.41316538251026935),
						new Complex(-0.8869506674916446, 0.24700791787653337),
						new Complex(-0.9077932138396491, 0.08219639941940153),
						new Complex(-0.9077932138396491, -0.08219639941940153),
						new Complex(-0.8869506674916446, -0.24700791787653337),
						new Complex(-0.8441199160909851, -0.41316538251026935),
						new Complex(-0.7766591387063627, -0.581917067737761),
						new Complex(-0.6794256425119225, -0.7552857305042031),
						new Complex(-0.5418766775112306, -0.9373043683516926),
						new Complex(-0.3363868224902031, -1.1391722978398595)};
			case 15:
				return new Complex[]{
						new Complex(-0.32299630597664436, 1.14941615458363),
						new Complex(-0.5224954069658329, 0.9581787261092528),
						new Complex(-0.6579196593111004, 0.7862895503722519),
						new Complex(-0.7556027168970723, 0.6229396358758263),
						new Complex(-0.8256631452587149, 0.46423487527343266),
						new Complex(-0.8731264620834984, 0.3082352470564267),
						new Complex(-0.9006981694176978, 0.1537681197278439),
						new Complex(-0.9097482363849062, -0.0),
						new Complex(-0.9006981694176978, -0.1537681197278439),
						new Complex(-0.8731264620834984, -0.3082352470564267),
						new Complex(-0.8256631452587149, -0.46423487527343266),
						new Complex(-0.7556027168970723, -0.6229396358758263),
						new Complex(-0.6579196593111004, -0.7862895503722519),
						new Complex(-0.5224954069658329, -0.9581787261092528),
						new Complex(-0.32299630597664436, -1.14941615458363)};
			case 16:
				return new Complex[]{
						new Complex(-0.3108782755645388, 1.1585528411993304),
						new Complex(-0.504760644442476, 0.9767137477799086),
						new Complex(-0.6379502514039066, 0.8137453537108762),
						new Complex(-0.7356166304713119, 0.6591950877860395),
						new Complex(-0.8074790293236005, 0.5092933751171801),
						new Complex(-0.8584264231521322, 0.3621697271802063),
						new Complex(-0.8911723070323643, 0.2167089659900576),
						new Complex(-0.9072099595087002, 0.07214211304111731),
						new Complex(-0.9072099595087002, -0.07214211304111731),
						new Complex(-0.8911723070323643, -0.2167089659900576),
						new Complex(-0.8584264231521322, -0.3621697271802063),
						new Complex(-0.8074790293236005, -0.5092933751171801),
						new Complex(-0.7356166304713119, -0.6591950877860395),
						new Complex(-0.6379502514039066, -0.8137453537108762),
						new Complex(-0.504760644442476, -0.9767137477799086),
						new Complex(-0.3108782755645388, -1.1585528411993304)};
			case 17:
				return new Complex[]{
						new Complex(-0.2998489459990074, 1.1667612729256676),
						new Complex(-0.4884629337672707, 0.9932971956316782),
						new Complex(-0.6193710717342137, 0.8382497252826986),
						new Complex(-0.7166893842372346, 0.6914936286393607),
						new Complex(-0.7897644147799701, 0.5493724405281085),
						new Complex(-0.8433414495836128, 0.41007592829100215),
						new Complex(-0.8801100704438625, 0.2725347156478803),
						new Complex(-0.9016273850787279, 0.13602679951730237),
						new Complex(-0.9087141161336388, -0.0),
						new Complex(-0.9016273850787279, -0.13602679951730237),
						new Complex(-0.8801100704438625, -0.2725347156478803),
						new Complex(-0.8433414495836128, -0.41007592829100215),
						new Complex(-0.7897644147799701, -0.5493724405281085),
						new Complex(-0.7166893842372346, -0.6914936286393607),
						new Complex(-0.6193710717342137, -0.8382497252826986),
						new Complex(-0.4884629337672707, -0.9932971956316782),
						new Complex(-0.2998489459990074, -1.1667612729256676)};
			case 18:
				return new Complex[]{
						new Complex(-0.28975920298804836, 1.1741830106000584),
						new Complex(-0.47342680699161527, 1.0082343003148009),
						new Complex(-0.6020482668090644, 0.8602708961893664),
						new Complex(-0.698782144500527, 0.7204696509726628),
						new Complex(-0.7726285030739557, 0.5852778162086639),
						new Complex(-0.8281885016242831, 0.45293856978159136),
						new Complex(-0.8681095503628832, 0.32242049251632576),
						new Complex(-0.8939764278132456, 0.19303746408947578),
						new Complex(-0.9067004324162776, 0.06427924106393068),
						new Complex(-0.9067004324162776, -0.06427924106393068),
						new Complex(-0.8939764278132456, -0.19303746408947578),
						new Complex(-0.8681095503628832, -0.32242049251632576),
						new Complex(-0.8281885016242831, -0.45293856978159136),
						new Complex(-0.7726285030739557, -0.5852778162086639),
						new Complex(-0.698782144500527, -0.7204696509726628),
						new Complex(-0.6020482668090644, -0.8602708961893664),
						new Complex(-0.47342680699161527, -1.0082343003148009),
						new Complex(-0.28975920298804836, -1.1741830106000584)};
			case 19:
				return new Complex[]{
						new Complex(-0.28048668514393643, 1.1809316284532905),
						new Complex(-0.4595043449730982, 1.0217687769126707),
						new Complex(-0.5858613321217828, 0.8801817131014564),
						new Complex(-0.6818424412912439, 0.7466272357947761),
						new Complex(-0.7561260971541627, 0.6176483917970176),
						new Complex(-0.81317255515782, 0.49153650355624595),
						new Complex(-0.8555768765618422, 0.3672925896399872),
						new Complex(-0.8849290585034383, 0.2442590757549817),
						new Complex(-0.9021937639390656, 0.1219568381872026),
						new Complex(-0.9078934217899399, -0.0),
						new Complex(-0.9021937639390656, -0.1219568381872026),
						new Complex(-0.8849290585034383, -0.2442590757549817),
						new Complex(-0.8555768765618422, -0.3672925896399872),
						new Complex(-0.81317255515782, -0.49153650355624595),
						new Complex(-0.7561260971541627, -0.6176483917970176),
						new Complex(-0.6818424412912439, -0.7466272357947761),
						new Complex(-0.5858613321217828, -0.8801817131014564),
						new Complex(-0.4595043449730982, -1.0217687769126707),
						new Complex(-0.28048668514393643, -1.1809316284532905)};
			case 20:
				return new Complex[]{
						new Complex(-0.2719299580251656, 1.187099379810886),
						new Complex(-0.44657006982051484, 1.0340977025608422),
						new Complex(-0.5707026806915716, 0.8982829066468254),
						new Complex(-0.6658120544829929, 0.7703721701100757),
						new Complex(-0.7402780309646765, 0.6469975237605224),
						new Complex(-0.7984251191290602, 0.526494238881713),
						new Complex(-0.8427907479956664, 0.4078917326291931),
						new Complex(-0.8749560316673335, 0.2905559296567909),
						new Complex(-0.8959150941925766, 0.17403171759187044),
						new Complex(-0.9062570115576768, 0.0579617802778495),
						new Complex(-0.9062570115576768, -0.0579617802778495),
						new Complex(-0.8959150941925766, -0.17403171759187044),
						new Complex(-0.8749560316673335, -0.2905559296567909),
						new Complex(-0.8427907479956664, -0.4078917326291931),
						new Complex(-0.7984251191290602, -0.526494238881713),
						new Complex(-0.7402780309646765, -0.6469975237605224),
						new Complex(-0.6658120544829929, -0.7703721701100757),
						new Complex(-0.5707026806915716, -0.8982829066468254),
						new Complex(-0.44657006982051484, -1.0340977025608422),
						new Complex(-0.2719299580251656, -1.187099379810886)};
			case 21:
				return new Complex[]{
						new Complex(-0.2640041595834026, 1.192762031948052),
						new Complex(-0.4345168906815267, 1.045382255856986),
						new Complex(-0.5564766488918568, 0.9148198405846728),
						new Complex(-0.6506315378609469, 0.7920349342629497),
						new Complex(-0.7250839687106612, 0.6737426063024383),
						new Complex(-0.7840287980408347, 0.5583186348022857),
						new Complex(-0.8299435470674447, 0.4448177739407957),
						new Complex(-0.8643915813643203, 0.33262585125221866),
						new Complex(-0.8883808106664449, 0.221306921508435),
						new Complex(-0.9025428073192694, 0.11052525727898564),
						new Complex(-0.9072262653142963, -0.0),
						new Complex(-0.9025428073192694, -0.11052525727898564),
						new Complex(-0.8883808106664449, -0.221306921508435),
						new Complex(-0.8643915813643203, -0.33262585125221866),
						new Complex(-0.8299435470674447, -0.4448177739407957),
						new Complex(-0.7840287980408347, -0.5583186348022857),
						new Complex(-0.7250839687106612, -0.6737426063024383),
						new Complex(-0.6506315378609469, -0.7920349342629497),
						new Complex(-0.5564766488918568, -0.9148198405846728),
						new Complex(-0.4345168906815267, -1.045382255856986),
						new Complex(-0.2640041595834026, -1.192762031948052)};
			case 22:
				return new Complex[]{
						new Complex(-0.2566376987939318, 1.1979824335552132),
						new Complex(-0.4232528745642628, 1.055755605227546),
						new Complex(-0.5430983056306306, 0.9299947824439877),
						new Complex(-0.6362427683267832, 0.8118875040246348),
						new Complex(-0.710530545641879, 0.6982266265924525),
						new Complex(-0.7700332930556816, 0.5874255426351151),
						new Complex(-0.8171682088462721, 0.47856194922027806),
						new Complex(-0.8534754036851689, 0.37103893194823206),
						new Complex(-0.8799661455640174, 0.2644363039201535),
						new Complex(-0.8972983138153532, 0.15843519122898653),
						new Complex(-0.9058702269930871, 0.05277490828999903),
						new Complex(-0.9058702269930871, -0.05277490828999903),
						new Complex(-0.8972983138153532, -0.15843519122898653),
						new Complex(-0.8799661455640174, -0.2644363039201535),
						new Complex(-0.8534754036851689, -0.37103893194823206),
						new Complex(-0.8171682088462721, -0.47856194922027806),
						new Complex(-0.7700332930556816, -0.5874255426351151),
						new Complex(-0.710530545641879, -0.6982266265924525),
						new Complex(-0.6362427683267832, -0.8118875040246348),
						new Complex(-0.5430983056306306, -0.9299947824439877),
						new Complex(-0.4232528745642628, -1.055755605227546),
						new Complex(-0.2566376987939318, -1.1979824335552132)};
			case 23:
				return new Complex[]{
						new Complex(-0.24976972022089555, 1.2028131878706976),
						new Complex(-0.4126986617510148, 1.0653287944755134),
						new Complex(-0.5304922463810195, 0.9439760364018306),
						new Complex(-0.6225903228771341, 0.830155830281298),
						new Complex(-0.6965966033912709, 0.7207341374753049),
						new Complex(-0.7564660146829884, 0.6141594859476034),
						new Complex(-0.8045561642053178, 0.5095305912227259),
						new Complex(-0.8423805948021127, 0.40626579482376024),
						new Complex(-0.8709469395587415, 0.3039581993950041),
						new Complex(-0.8909283242471254, 0.20230246993812245),
						new Complex(-0.9027564979912508, 0.10105343353140452),
						new Complex(-0.9066732476324991, -0.0),
						new Complex(-0.9027564979912508, -0.10105343353140452),
						new Complex(-0.8909283242471254, -0.20230246993812245),
						new Complex(-0.8709469395587415, -0.3039581993950041),
						new Complex(-0.8423805948021127, -0.40626579482376024),
						new Complex(-0.8045561642053178, -0.5095305912227259),
						new Complex(-0.7564660146829884, -0.6141594859476034),
						new Complex(-0.6965966033912709, -0.7207341374753049),
						new Complex(-0.6225903228771341, -0.830155830281298),
						new Complex(-0.5304922463810195, -0.9439760364018306),
						new Complex(-0.4126986617510148, -1.0653287944755134),
						new Complex(-0.24976972022089555, -1.2028131878706976)};
			case 24:
				return new Complex[]{
						new Complex(-0.2433481337524877, 1.2072986837319726),
						new Complex(-0.4027853855197517, 1.074195196518675),
						new Complex(-0.518591457482032, 0.9569048385259057),
						new Complex(-0.609622156737833, 0.8470292433077197),
						new Complex(-0.6832565803536519, 0.7415032695091649),
						new Complex(-0.7433392285088533, 0.6388084216222569),
						new Complex(-0.792169546234349, 0.5380628490968015),
						new Complex(-0.8312326466813242, 0.43869859335973066),
						new Complex(-0.8615278304016356, 0.34032021126186257),
						new Complex(-0.8837358034555707, 0.24263352344013842),
						new Complex(-0.8983105104397869, 0.145405613387361),
						new Complex(-0.9055312363372773, 0.04844006654047869),
						new Complex(-0.9055312363372773, -0.04844006654047869),
						new Complex(-0.8983105104397869, -0.145405613387361),
						new Complex(-0.8837358034555707, -0.24263352344013842),
						new Complex(-0.8615278304016356, -0.34032021126186257),
						new Complex(-0.8312326466813242, -0.43869859335973066),
						new Complex(-0.792169546234349, -0.5380628490968015),
						new Complex(-0.7433392285088533, -0.6388084216222569),
						new Complex(-0.6832565803536519, -0.7415032695091649),
						new Complex(-0.609622156737833, -0.8470292433077197),
						new Complex(-0.518591457482032, -0.9569048385259057),
						new Complex(-0.4027853855197517, -1.074195196518675),
						new Complex(-0.2433481337524877, -1.2072986837319726)};

			default:
				throw new IllegalArgumentException("Unsupported order: " + N);
		}
	}
}
