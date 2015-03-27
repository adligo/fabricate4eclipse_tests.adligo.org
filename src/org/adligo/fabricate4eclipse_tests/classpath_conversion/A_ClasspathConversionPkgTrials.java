package org.adligo.fabricate4eclipse_tests.classpath_conversion;

import org.adligo.fabricate4eclipse_tests.etc.Fab4EclipseParamsFactory;
import org.adligo.tests4j.run.api.Tests4J;
import org.adligo.tests4j.system.shared.api.I_Tests4J_TrialList;
import org.adligo.tests4j.system.shared.api.Tests4J_Params;
import org.adligo.tests4j.system.shared.trials.I_Trial;

import java.util.ArrayList;
import java.util.List;

public class A_ClasspathConversionPkgTrials implements I_Tests4J_TrialList {
	
	public static void main(String [] args) {
		try {
			Tests4J_Params params = new Fab4EclipseParamsFactory().create();
			
			A_ClasspathConversionPkgTrials me = new A_ClasspathConversionPkgTrials();
			params.addTrials(me);
			
			Tests4J.run(params);
		} catch (Exception x) {
			x.printStackTrace();
		}
	}


  @Override
  public List<Class<? extends I_Trial>> getTrials() {
    List<Class<? extends I_Trial>> trials = new ArrayList<Class<? extends I_Trial>>();
    trials.add(FabricateClasspathToEclipse4ishConverterTrial.class);
    
    
    return trials;
  }


}
