package org.adligo.fabricate4eclipse_tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.adligo.tests4j.models.shared.coverage.I_PackageCoverageBrief;
import org.adligo.tests4j.models.shared.metadata.I_TrialMetadata;
import org.adligo.tests4j.models.shared.metadata.I_TrialRunMetadata;
import org.adligo.tests4j.models.shared.results.I_TrialRunResult;
import org.adligo.tests4j.run.discovery.ClassesWithSourceFileTrialsCalculator;
import org.adligo.tests4j.shared.common.TrialType;
import org.adligo.tests4j.shared.output.I_Tests4J_Log;
import org.adligo.tests4j.system.shared.trials.AbstractTrial;
import org.adligo.tests4j.system.shared.trials.I_MetaTrial;
import org.adligo.tests4j.system.shared.trials.TrialTypeAnnotation;

@TrialTypeAnnotation (type=TrialType.META_TRIAL_TYPE)
public class Fab4EclipseMetaTrial  extends AbstractTrial implements I_MetaTrial {
	private static final long TESTS = 28;
	private static final int TRIALS = 3;
	
	private ClassesWithSourceFileTrialsCalculator calculator_;
	private I_TrialRunResult results_;
	private I_Tests4J_Log log_;
	//hmm package comparison data to include;
	// passing tests
	// relevant classes with trials %
	// minimum source file coverage %
	// minimum package coverage %
	
	@SuppressWarnings("boxing")
  @Override
	public void afterMetadataCalculated(I_TrialRunMetadata metadata) throws Exception {
		calculator_ = new ClassesWithSourceFileTrialsCalculator(metadata);
		
		log_ = super.getLog();
		double minPctWithTrials = 45.0;
		if (calculator_.getPctWithTrialsDouble() <= minPctWithTrials) {
			Set<String> classes = calculator_.getClassesWithOutTrials();
			log_.log("The following source files do NOT have a associated SourceFileTrial.");
			for (String className: classes) {
				log_.log(className);
			}
		}
		assertGreaterThanOrEquals(minPctWithTrials, calculator_.getPctWithTrialsDouble());
		// includes this
		List<? extends I_TrialMetadata> trialMetadata = metadata.getAllTrialMetadata();
		
		List<String> mdNames = new ArrayList<String>();
		
		for (I_TrialMetadata md: trialMetadata) {
			mdNames.add(md.getTrialName());
		}
		Collections.sort(mdNames);
		
		StringBuilder sb = new StringBuilder();
		for (String mdName: mdNames) {
			sb.append("'");
			sb.append(mdName);
			sb.append("'");
			sb.append(System.getProperty("line.seperator"));
		}
		assertEquals(sb.toString(), TRIALS, metadata.getAllTrialsCount());
		
		assertEquals(TESTS,  metadata.getAllTestsCount());
	}

	public void assertCoverageMatrix() {
	  assertCoverageMatrix("org.adligo.fabricate4eclipse.classpath_conversion",
        100.0, 81.0);
	  assertCoverageMatrix("org.adligo.fabricate4eclipse.models",
        100.0, 95.0);
	}

	private void assertCoverageMatrix(String pkgName, double minPctSourceTrials, double minPctCoverage) {
		double pctWithTrials = calculator_.getPctWithTrialsDouble(pkgName);
		if (pctWithTrials < minPctSourceTrials) {
			
			StringBuilder sb = new StringBuilder("The following package;" + System.lineSeparator() + 
					pkgName + System.lineSeparator() + 
					" is missing the following source file trials " + 
					System.lineSeparator());
			Set<String> sfns = calculator_.getClassesWithOutTrials(pkgName);
			for (String sfn: sfns) {
				sb.append(sfn);
				sb.append(System.lineSeparator());
			}
			assertGreaterThanOrEquals(sb.toString(), minPctSourceTrials, 
					pctWithTrials);
				
		}
		
		if (results_.hasCoverage()) {
			I_PackageCoverageBrief cover =  getCoverage(pkgName, results_.getCoverage());
			assertNotNull("The following package didn't have code coverage and should;" +
					System.lineSeparator() + pkgName,
					cover);
			assertGreaterThanOrEquals("The following package didn't have enough code coverage;" +
				System.lineSeparator() + pkgName, minPctCoverage, cover.getPercentageCoveredDouble());
			
			
		}
	}
	
	
	private I_PackageCoverageBrief getCoverage(String packageName, List<I_PackageCoverageBrief> coverages) {
		for (I_PackageCoverageBrief pc: coverages) {
			if (packageName.equals(pc.getPackageName())) {
				return pc;
			}
		}
		for (I_PackageCoverageBrief pc: coverages) {
			if (packageName.contains(pc.getPackageName())) {
				return getCoverage(packageName, pc.getChildPackageCoverage());
			} 
		}
		return null;
	}
	
  @Override
	public void afterNonMetaTrialsRun(I_TrialRunResult results) throws Exception {
		//this assert is also for the child-packages;
		results_ = results;
		assertCoverageMatrix();
		
		//allow to run with out coverage plugin,
		//you may want to require this for your project.
		if (results.hasCoverage()) {
			double actual = results.getCoveragePercentage();
			assertGreaterThanOrEquals(55.0, actual);
		}	
		//TODO
		//assertEquals(1,results.getTrialsIgnored());
		//assertEquals(1,results.getTestsIgnored());
		assertGreaterThanOrEquals(TRIALS - 2, results.getTrialsPassed());
		//usually -2, ignored several tests
		assertGreaterThanOrEquals(TESTS - 10, results.getTestsPassed());
		
		//does not include assertions from this class yet
		//I think the single threaded count is off somewhere
		assertGreaterThanOrEquals(445L,results.getAsserts().longValue());
		assertGreaterThanOrEquals(200L,results.getUniqueAsserts().longValue());
	}



}
