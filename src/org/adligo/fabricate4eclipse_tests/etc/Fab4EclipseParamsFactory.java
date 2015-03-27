package org.adligo.fabricate4eclipse_tests.etc;

import org.adligo.tests4j.system.shared.api.AbstractParamsFactory;
import org.adligo.tests4j.system.shared.api.Tests4J_CoveragePluginParams;
import org.adligo.tests4j.system.shared.api.Tests4J_Params;
import org.adligo.tests4j_4jacoco.plugin.factories.Tests4J_4MockitoPluginFactory;
import org.adligo.tests4j_4jacoco.plugin.instrumentation.TrialInstrumenter;

import java.util.ArrayList;
import java.util.List;

public class Fab4EclipseParamsFactory extends AbstractParamsFactory {

  @Override
  public Tests4J_Params create() {
    Tests4J_Params params = new Tests4J_Params();
    List<String> nonInstrumentedPackages = new ArrayList<String>();
    nonInstrumentedPackages.add("org.apache.");
    nonInstrumentedPackages.add("com.sun.");
    //somewhere in here I am using a System class loader
    //in stead of a current class loader, not sure where
    
    params.setAdditionalNonInstrumentedPackages(nonInstrumentedPackages);
    
    //params.setCoveragePluginFactoryClass(MockitoPluginFactory.class);
    
    params.setCoveragePluginFactoryClass(Tests4J_4MockitoPluginFactory.class);
    Tests4J_CoveragePluginParams plugParams = new Tests4J_CoveragePluginParams();
    plugParams.setInstrumentedClassOutputFolder("classes_instrumented");
    plugParams.setWriteOutInstrumentedClasses(true);
    params.setCoverageParams(plugParams);
    //params.setLogState(Tests4J_NotificationManager.class, true);
    //params.setLogState(ClassAndDependenciesInstrumenter.class, true);
    //params.setLogState(TrialInstrumenter.class, true);
    //params.setLogState(Recorder.class, true);
    //params.setLogState(CallJacocoInit.class, true);
    params.setLogState(TrialInstrumenter.class, true);
    return params;
  }

}
