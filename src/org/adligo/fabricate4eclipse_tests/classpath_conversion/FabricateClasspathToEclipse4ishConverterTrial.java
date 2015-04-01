package org.adligo.fabricate4eclipse_tests.classpath_conversion;

import org.adligo.fabricate.common.en.FabricateEnConstants;
import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.files.xml_io.I_FabXmlFileIO;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.FabSystem;
import org.adligo.fabricate.models.common.FabricationMemoryConstants;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_Parameter;
import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.common.I_RoutineFactory;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;
import org.adligo.fabricate.models.common.Parameter;
import org.adligo.fabricate.models.common.ParameterMutant;
import org.adligo.fabricate.models.dependencies.DependencyMutant;
import org.adligo.fabricate.models.dependencies.I_Ide;
import org.adligo.fabricate.models.dependencies.IdeMutant;
import org.adligo.fabricate.models.dependencies.ProjectDependencyMutant;
import org.adligo.fabricate.models.fabricate.FabricateMutant;
import org.adligo.fabricate.models.project.ProjectMutant;
import org.adligo.fabricate.repository.I_RepositoryFactory;
import org.adligo.fabricate.repository.I_RepositoryPathBuilder;
import org.adligo.fabricate.routines.I_FabricateAware;
import org.adligo.fabricate.routines.I_ProjectAware;
import org.adligo.fabricate.routines.I_RepositoryFactoryAware;
import org.adligo.fabricate.routines.implicit.FindSrcTrait;
import org.adligo.fabricate4eclipse.classpath_conversion.FabricateClasspathToEclipse4ishConverter;
import org.adligo.tests4j.shared.asserts.common.ExpectedThrowable;
import org.adligo.tests4j.shared.asserts.common.I_Thrower;
import org.adligo.tests4j.system.shared.trials.SourceFileScope;
import org.adligo.tests4j.system.shared.trials.Test;
import org.adligo.tests4j_4mockito.MockMethod;
import org.adligo.tests4j_4mockito.MockitoSourceFileTrial;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SourceFileScope (sourceClass=FabricateClasspathToEclipse4ishConverter.class, minCoverage=87.0)
public class FabricateClasspathToEclipse4ishConverterTrial extends MockitoSourceFileTrial {
  private FabSystem sysMock_;
  private I_FabFileIO fileMock_;
  private I_FabXmlFileIO xmlIoMock_;
  private I_FabLog mockLog_;
  private MockMethod<Void> logPrintln_;
  
  private FindSrcTrait findSrcMock_;
  private MockMethod<Void> findSrcMockSetFabricate_;
  private MockMethod<Void> findSrcMockSetProject_;
  private MockMethod<Void> findSrcMockSetup_;
  private MockMethod<Boolean> findSrcMockSetupMutants_;
  private I_RoutineFactory traitFactoryMock_;
  private I_RepositoryFactory repoFactoryMock_;
  private I_FabricationMemoryMutant<Object> fabMemoryMutant_;
  private I_RoutineMemoryMutant<Object> routineMemoryMutant_;
  private I_FabricationMemory<Object> fabMemory_;
  private I_RoutineMemory<Object> routineMemory_;
  private List<String> defaultPlatforms_;
  private I_RoutineBrief brief_;
  
  @SuppressWarnings("unchecked")
  public void beforeTests() {
    sysMock_ = mock(FabSystem.class);
    
    when(sysMock_.lineSeparator()).thenReturn("\n");
    when(sysMock_.getConstants()).thenReturn(FabricateEnConstants.INSTANCE);
    fileMock_ = mock(I_FabFileIO.class);
    when(fileMock_.getNameSeparator()).thenReturn("/");
    xmlIoMock_ = mock(I_FabXmlFileIO.class);
    when(sysMock_.getFileIO()).thenReturn(fileMock_);
    when(sysMock_.getXmlFileIO()).thenReturn(xmlIoMock_);
    
    mockLog_ = mock(I_FabLog.class);
    when(sysMock_.getLog()).thenReturn(mockLog_);
    
    
    findSrcMock_ = mock(FindSrcTrait.class);
    try {
      findSrcMockSetFabricate_ = new MockMethod<Void>();
      doAnswer(findSrcMockSetFabricate_).when(findSrcMock_).setFabricate(any());
      
      findSrcMockSetProject_ = new MockMethod<Void>();
      doAnswer(findSrcMockSetProject_).when(findSrcMock_).setProject(any());
      
      findSrcMockSetup_ = new MockMethod<Void>();
      doAnswer(findSrcMockSetup_).when(findSrcMock_).setup(any(), any());
      
      findSrcMockSetupMutants_ = new MockMethod<Boolean>(true, true);
      doAnswer(findSrcMockSetupMutants_).when(findSrcMock_).setupInitial(any(), any());
      
      traitFactoryMock_ = mock(I_RoutineFactory.class);
      when(traitFactoryMock_.createRoutine(
          FindSrcTrait.NAME, FindSrcTrait.IMPLEMENTED_INTERFACES)).thenReturn(findSrcMock_);
    } catch (FabricationRoutineCreationException x) {
      throw new RuntimeException(x);
    }
    
    ArrayList<String> plats = new ArrayList<String>();
    plats.add("jse");
    defaultPlatforms_ = Collections.unmodifiableList(plats);
    
    fabMemoryMutant_ = mock(I_FabricationMemoryMutant.class);
    when(fabMemoryMutant_.get(FabricationMemoryConstants.PLATFORMS)).thenReturn(defaultPlatforms_);
    routineMemoryMutant_ = mock(I_RoutineMemoryMutant.class);
    
    fabMemory_ = mock(I_FabricationMemory.class);
    when(fabMemory_.get(FabricationMemoryConstants.PLATFORMS)).thenReturn(defaultPlatforms_);
    routineMemory_ = mock(I_RoutineMemory.class);
    
    repoFactoryMock_ = mock(I_RepositoryFactory.class);
    
    brief_ = mock(I_RoutineBrief.class);
    when(brief_.getName()).thenReturn("classpath2eclipse");
    
    logPrintln_ = new MockMethod<Void>();
    doAnswer(logPrintln_).when(mockLog_).println(any());
    when(mockLog_.isLogEnabled(any())).thenReturn(false);
  }
  
  @Test
  public void testClassInstanceOf() throws Exception {
    FabricateClasspathToEclipse4ishConverter converter = new FabricateClasspathToEclipse4ishConverter();
    assertTrue(converter instanceof I_FabricationRoutine);
    assertTrue(converter instanceof I_FabricateAware);
    assertTrue(converter instanceof I_RepositoryFactoryAware);
    assertTrue(converter instanceof I_ProjectAware);
  }
  
  @SuppressWarnings({"boxing", "unchecked"})
  @Test
  public void testMethodRunSimpleTwoDepsWindows() throws Exception {
    FabricateClasspathToEclipse4ishConverter converter = new FabricateClasspathToEclipse4ishConverter();
    converter.setTraitFactory(traitFactoryMock_);
    converter.setRepositoryFactory(repoFactoryMock_);
    converter.setSystem(sysMock_);
    converter.setBrief(brief_);
    
    when(fileMock_.getNameSeparator()).thenReturn("\\");
    
    FabricateMutant fm = new FabricateMutant();
    fm.setFabricateRepository("C:\\fabRepo\\");
    converter.setFabricate(fm);
    
    ProjectMutant pm = new ProjectMutant();
    DependencyMutant depA = new DependencyMutant();
    depA.setArtifact("depA");
    pm.addNormalizedDependency(depA);
    DependencyMutant depB = new DependencyMutant();
    pm.addNormalizedDependency(depB);
    depB.setArtifact("depB");
    
    pm.setDir("C:\\foo\\pdir\\");
    converter.setProject(pm);
    
    converter.setup(fabMemory_, routineMemory_);
    assertSame(fm, findSrcMockSetFabricate_.getArg(0));
    assertEquals(1, findSrcMockSetFabricate_.count());
    assertSame(fabMemory_, findSrcMockSetup_.getArgs(0)[0]);
    assertSame(routineMemory_, findSrcMockSetup_.getArgs(0)[1]);
    assertEquals(1, findSrcMockSetup_.count());
    assertSame(fm, findSrcMockSetFabricate_.getArg(0));
    assertEquals(1, findSrcMockSetFabricate_.count());
    assertEquals(0, findSrcMockSetProject_.count());
    assertEquals(0, findSrcMockSetupMutants_.count());
    
    List<String> srcDirs = new ArrayList<String>();
    srcDirs.add("C:\\foo\\bar\\srcA");
    
    File aDirMock = mock(File.class);
    when(aDirMock.getName()).thenReturn("srcA");
    when(fileMock_.instance("C:\\foo\\bar\\srcA")).thenReturn(aDirMock);
    
    File aDirMockB = mock(File.class);
    when(aDirMockB.getName()).thenReturn("srcB");
    when(fileMock_.instance("C:\\foo\\bar\\srcB")).thenReturn(aDirMockB);
    
    File aDirMockC = mock(File.class);
    when(aDirMockC.getName()).thenReturn("srcC");
    when(fileMock_.instance("C:\\foo\\bar\\srcC")).thenReturn(aDirMockB);
    
    MockMethod<List<String>> findSrcOutput = new MockMethod<List<String>>(srcDirs);
    doAnswer(findSrcOutput).when(findSrcMock_).getOutput();
    
    MockMethod<Boolean> existsMethod = new MockMethod<Boolean>();
    doAnswer(existsMethod).when(fileMock_).exists(any());
    
    OutputStream out = mock(OutputStream.class);
    when(fileMock_.newFileOutputStream("C:\\foo\\pdir\\.classpath")).thenReturn(out);
    MockMethod<Void> writeFileMethod = new MockMethod<Void>();
    doAnswer(writeFileMethod).when(fileMock_).writeFile(any(), any());
    
    I_RepositoryPathBuilder repo = mock(I_RepositoryPathBuilder.class);
    when(repoFactoryMock_.createRepositoryPathBuilder("C:\\fabRepo\\")).thenReturn(repo);
    when(repo.getArtifactPath(depA)).thenReturn("C:\\fabRepo\\depA");
    when(repo.getArtifactPath(depB)).thenReturn("C:\\fabRepo\\depB");
    
    converter.run();
    ByteArrayInputStream bais = (ByteArrayInputStream) writeFileMethod.getArgs(0)[0];
    int size = bais.available();
    byte [] bytes = new byte[size];
    bais.read(bytes);
    assertUniform(
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<classpath>\n" +
        "\t<classpathentry kind=\"src\" path=\"srcA\"/>\n" +
        "\t<classpathentry kind=\"var\" path=\"FAB_REPO/depA\"/>\n" +
        "\t<classpathentry kind=\"var\" path=\"FAB_REPO/depB\"/>\n" +
        "</classpath>\n" 
            , new String(bytes));
    assertSame(out, writeFileMethod.getArgs(0)[1]);
    
    assertEquals("The following command line argument was NOT provided for command " +
        "classpath2eclipse using default 'FAB_REPO';\n" +
        "eclipseEnvVariable", logPrintln_.getArg(0));
    assertEquals(1, logPrintln_.count());
  }
  
  @SuppressWarnings({"boxing", "unchecked"})
  @Test
  public void testMethodRunSimpleTwoDepsWindowsEnvVar() throws Exception {
    FabricateClasspathToEclipse4ishConverter converter = new FabricateClasspathToEclipse4ishConverter();
    converter.setTraitFactory(traitFactoryMock_);
    converter.setRepositoryFactory(repoFactoryMock_);
    converter.setSystem(sysMock_);
    converter.setBrief(brief_);
    
    when(fileMock_.getNameSeparator()).thenReturn("\\");
    
    when(sysMock_.getArgValue(FabricateClasspathToEclipse4ishConverter.ECLIPSE_ENV_VAR)).thenReturn("FAB_REPO");
    
    
    FabricateMutant fm = new FabricateMutant();
    fm.setFabricateRepository("C:\\fabRepo\\");
    converter.setFabricate(fm);
    
    ProjectMutant pm = new ProjectMutant();
    DependencyMutant depA = new DependencyMutant();
    depA.setArtifact("depA");
    pm.addNormalizedDependency(depA);
    DependencyMutant depB = new DependencyMutant();
    pm.addNormalizedDependency(depB);
    depB.setArtifact("depB");
    
    pm.setDir("C:\\foo\\pdir\\");
    converter.setProject(pm);
    
    converter.setup(fabMemory_, routineMemory_);
    assertSame(fm, findSrcMockSetFabricate_.getArg(0));
    assertEquals(1, findSrcMockSetFabricate_.count());
    assertSame(fabMemory_, findSrcMockSetup_.getArgs(0)[0]);
    assertSame(routineMemory_, findSrcMockSetup_.getArgs(0)[1]);
    assertEquals(1, findSrcMockSetup_.count());
    assertSame(fm, findSrcMockSetFabricate_.getArg(0));
    assertEquals(1, findSrcMockSetFabricate_.count());
    assertEquals(0, findSrcMockSetProject_.count());
    assertEquals(0, findSrcMockSetupMutants_.count());
    
    List<String> srcDirs = new ArrayList<String>();
    srcDirs.add("C:\\foo\\bar\\srcA");
    
    File aDirMock = mock(File.class);
    when(aDirMock.getName()).thenReturn("srcA");
    when(fileMock_.instance("C:\\foo\\bar\\srcA")).thenReturn(aDirMock);
    
    File aDirMockB = mock(File.class);
    when(aDirMockB.getName()).thenReturn("srcB");
    when(fileMock_.instance("C:\\foo\\bar\\srcB")).thenReturn(aDirMockB);
    
    File aDirMockC = mock(File.class);
    when(aDirMockC.getName()).thenReturn("srcC");
    when(fileMock_.instance("C:\\foo\\bar\\srcC")).thenReturn(aDirMockB);
    
    MockMethod<List<String>> findSrcOutput = new MockMethod<List<String>>(srcDirs);
    doAnswer(findSrcOutput).when(findSrcMock_).getOutput();
    
    MockMethod<Boolean> existsMethod = new MockMethod<Boolean>();
    doAnswer(existsMethod).when(fileMock_).exists(any());
    
    OutputStream out = mock(OutputStream.class);
    when(fileMock_.newFileOutputStream("C:\\foo\\pdir\\.classpath")).thenReturn(out);
    MockMethod<Void> writeFileMethod = new MockMethod<Void>();
    doAnswer(writeFileMethod).when(fileMock_).writeFile(any(), any());
    
    I_RepositoryPathBuilder repo = mock(I_RepositoryPathBuilder.class);
    when(repoFactoryMock_.createRepositoryPathBuilder("C:\\fabRepo\\")).thenReturn(repo);
    when(repo.getArtifactPath(depA)).thenReturn("C:\\fabRepo\\depA");
    when(repo.getArtifactPath(depB)).thenReturn("C:\\fabRepo\\depB");
    
    converter.run();
    ByteArrayInputStream bais = (ByteArrayInputStream) writeFileMethod.getArgs(0)[0];
    int size = bais.available();
    byte [] bytes = new byte[size];
    bais.read(bytes);
    assertUniform(
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<classpath>\n" +
        "\t<classpathentry kind=\"src\" path=\"srcA\"/>\n" +
        "\t<classpathentry kind=\"var\" path=\"FAB_REPO/depA\"/>\n" +
        "\t<classpathentry kind=\"var\" path=\"FAB_REPO/depB\"/>\n" +
        "</classpath>\n" 
            , new String(bytes));
    assertSame(out, writeFileMethod.getArgs(0)[1]);
    
    assertEquals("The following command line argument was NOT provided for command " +
        "classpath2eclipse using default 'FAB_REPO';\n" +
        "eclipseEnvVariable", logPrintln_.getArg(0));
    assertEquals(1, logPrintln_.count());
  }
  
  @SuppressWarnings({"boxing", "unchecked"})
  @Test
  public void testMethodRunSimpleTwoSrcDirs() throws Exception {
    FabricateClasspathToEclipse4ishConverter converter = new FabricateClasspathToEclipse4ishConverter();
    converter.setTraitFactory(traitFactoryMock_);
    converter.setRepositoryFactory(repoFactoryMock_);
    converter.setSystem(sysMock_);
    converter.setBrief(brief_);
    
    FabricateMutant fm = new FabricateMutant();
    converter.setFabricate(fm);
    
    ProjectMutant pm = new ProjectMutant();
    pm.setDir("/foo/pdir/");
    converter.setProject(pm);
    

    converter.setupInitial(fabMemoryMutant_, routineMemoryMutant_);
    assertSame(fm, findSrcMockSetFabricate_.getArg(0));
    assertEquals(1, findSrcMockSetFabricate_.count());
    assertSame(fabMemoryMutant_, findSrcMockSetupMutants_.getArgs(0)[0]);
    assertSame(routineMemoryMutant_, findSrcMockSetupMutants_.getArgs(0)[1]);
    assertEquals(1, findSrcMockSetupMutants_.count());
    assertSame(fm, findSrcMockSetFabricate_.getArg(0));
    assertEquals(1, findSrcMockSetFabricate_.count());
    assertEquals(0, findSrcMockSetProject_.count());
    assertEquals(0, findSrcMockSetup_.count());
    
    List<String> srcDirs = new ArrayList<String>();
    srcDirs.add("/foo/bar/srcA");
    srcDirs.add("/foo/bar/srcB");
    //check removal of duplicate
    srcDirs.add("/foo/bar/srcA");
    
    File aDirMock = mock(File.class);
    when(aDirMock.getName()).thenReturn("srcA");
    when(fileMock_.instance("/foo/bar/srcA")).thenReturn(aDirMock);
    
    File aDirMockB = mock(File.class);
    when(aDirMockB.getName()).thenReturn("srcB");
    when(fileMock_.instance("/foo/bar/srcB")).thenReturn(aDirMockB);
    
    MockMethod<List<String>> findSrcOutput = new MockMethod<List<String>>(srcDirs);
    doAnswer(findSrcOutput).when(findSrcMock_).getOutput();
    
    MockMethod<Boolean> existsMethod = new MockMethod<Boolean>();
    doAnswer(existsMethod).when(fileMock_).exists(any());
    
    OutputStream out = mock(OutputStream.class);
    when(fileMock_.newFileOutputStream("/foo/pdir/.classpath")).thenReturn(out);
    MockMethod<Void> writeFileMethod = new MockMethod<Void>();
    doAnswer(writeFileMethod).when(fileMock_).writeFile(any(), any());
    
    converter.run();
    ByteArrayInputStream bais = (ByteArrayInputStream) writeFileMethod.getArgs(0)[0];
    int size = bais.available();
    byte [] bytes = new byte[size];
    bais.read(bytes);
    assertUniform(
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<classpath>\n" +
        "\t<classpathentry kind=\"src\" path=\"srcA\"/>\n" +
        "\t<classpathentry kind=\"src\" path=\"srcB\"/>\n" +
        "</classpath>\n" 
            , new String(bytes));
    assertSame(out, writeFileMethod.getArgs(0)[1]);
    
    assertEquals("The following command line argument was NOT provided for command " +
        "classpath2eclipse using default 'FAB_REPO';\n" +
        "eclipseEnvVariable", logPrintln_.getArg(0));
    assertEquals(1, logPrintln_.count());
  }
  
  @SuppressWarnings({"boxing", "unchecked"})
  @Test
  public void testMethodRunSimpleTwoSrcDirsThreeDepsOneIdeTwoProject() throws Exception {
    FabricateClasspathToEclipse4ishConverter converter = new FabricateClasspathToEclipse4ishConverter();
    converter.setTraitFactory(traitFactoryMock_);
    converter.setRepositoryFactory(repoFactoryMock_);
    converter.setSystem(sysMock_);
    converter.setBrief(brief_);
    
    FabricateMutant fm = new FabricateMutant();
    fm.setFabricateRepository("/foo/bar/");
    converter.setFabricate(fm);
    
    ProjectMutant pm = new ProjectMutant();
    DependencyMutant depA = new DependencyMutant();
    depA.setArtifact("depA");
    pm.addNormalizedDependency(depA);
    DependencyMutant depB = new DependencyMutant();
    pm.addNormalizedDependency(depB);
    depB.setArtifact("depB");
    DependencyMutant depC = new DependencyMutant();
    depC.setArtifact("depC");
    List<I_Ide> cchildren = new ArrayList<I_Ide>();
    IdeMutant im = new IdeMutant();
    im.setName("eclipse");
    List<I_Parameter> ideChild = new ArrayList<I_Parameter>();
    ideChild.add(new Parameter("ideKey", "ideValue"));
    im.setChildren(ideChild);
    cchildren.add(im);
    depC.setChildren(cchildren);
    
    pm.addNormalizedDependency(depC);
    
    pm.addProjectDependency(new ProjectDependencyMutant("project_j"));
    pm.addProjectDependency(new ProjectDependencyMutant("project_k"));
    pm.setDir("/foo/pdir/");
    converter.setProject(pm);
    
    assertTrue(converter.setupInitial(fabMemoryMutant_, routineMemoryMutant_));
    assertSame(fm, findSrcMockSetFabricate_.getArg(0));
    assertEquals(1, findSrcMockSetFabricate_.count());
    assertSame(fabMemoryMutant_, findSrcMockSetupMutants_.getArgs(0)[0]);
    assertSame(routineMemoryMutant_, findSrcMockSetupMutants_.getArgs(0)[1]);
    assertEquals(1, findSrcMockSetupMutants_.count());
    assertSame(fm, findSrcMockSetFabricate_.getArg(0));
    assertEquals(1, findSrcMockSetFabricate_.count());
    assertEquals(0, findSrcMockSetProject_.count());
    assertEquals(0, findSrcMockSetup_.count());
    
    List<String> srcDirs = new ArrayList<String>();
    srcDirs.add("/foo/bar/srcA");
    srcDirs.add("/foo/bar/srcB");
    
    File aDirMock = mock(File.class);
    when(aDirMock.getName()).thenReturn("srcA");
    when(fileMock_.instance("/foo/bar/srcA")).thenReturn(aDirMock);
    
    File aDirMockB = mock(File.class);
    when(aDirMockB.getName()).thenReturn("srcB");
    when(fileMock_.instance("/foo/bar/srcB")).thenReturn(aDirMockB);
    
    MockMethod<List<String>> findSrcOutput = new MockMethod<List<String>>(srcDirs);
    doAnswer(findSrcOutput).when(findSrcMock_).getOutput();
    
    MockMethod<Boolean> existsMethod = new MockMethod<Boolean>();
    doAnswer(existsMethod).when(fileMock_).exists(any());
    
    OutputStream out = mock(OutputStream.class);
    when(fileMock_.newFileOutputStream("/foo/pdir/.classpath")).thenReturn(out);
    MockMethod<Void> writeFileMethod = new MockMethod<Void>();
    doAnswer(writeFileMethod).when(fileMock_).writeFile(any(), any());
    
    I_RepositoryPathBuilder repo = mock(I_RepositoryPathBuilder.class);
    when(repoFactoryMock_.createRepositoryPathBuilder("/foo/bar/")).thenReturn(repo);
    when(repo.getArtifactPath(depA)).thenReturn("/foo/bar/depA");
    when(repo.getArtifactPath(depB)).thenReturn("/foo/bar/depB");
    
    converter.run();
    ByteArrayInputStream bais = (ByteArrayInputStream) writeFileMethod.getArgs(0)[0];
    int size = bais.available();
    byte [] bytes = new byte[size];
    bais.read(bytes);
    assertUniform(
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<classpath>\n" +
        "\t<classpathentry kind=\"src\" path=\"srcA\"/>\n" +
        "\t<classpathentry kind=\"src\" path=\"srcB\"/>\n" +
        "\t<classpathentry kind=\"var\" path=\"FAB_REPO/depA\"/>\n" +
        "\t<classpathentry kind=\"var\" path=\"FAB_REPO/depB\"/>\n" +
        "\t<classpathentry kind=\"ideKey\" path=\"ideValue\"/>\n" +
        "\t<classpathentry combineaccessrules=\"false\" kind=\"src\" path=\"/project_j\"/>\n" +
        "\t<classpathentry combineaccessrules=\"false\" kind=\"src\" path=\"/project_k\"/>\n" +
        "</classpath>\n" 
            , new String(bytes));
    assertSame(out, writeFileMethod.getArgs(0)[1]);
    
    assertEquals("The following command line argument was NOT provided for command " +
        "classpath2eclipse using default 'FAB_REPO';\n" +
        "eclipseEnvVariable", logPrintln_.getArg(0));
    assertEquals(1, logPrintln_.count());
  }
  
  
  @SuppressWarnings({"boxing", "unchecked"})
  @Test
  public void testMethodRunSimpleTwoSrcDirsThreeDepsTwoProject_EnvVar() throws Exception {
    FabricateClasspathToEclipse4ishConverter converter = new FabricateClasspathToEclipse4ishConverter();
    converter.setTraitFactory(traitFactoryMock_);
    converter.setRepositoryFactory(repoFactoryMock_);
    converter.setSystem(sysMock_);
    converter.setBrief(brief_);
    
    when(sysMock_.hasArg(FabricateClasspathToEclipse4ishConverter.ECLIPSE_ENV_VAR)).thenReturn(true);
    when(sysMock_.getArgValue(FabricateClasspathToEclipse4ishConverter.ECLIPSE_ENV_VAR)).thenReturn("FAB_REPO2");
    
    FabricateMutant fm = new FabricateMutant();
    fm.setFabricateRepository("/fabRepo/");
    converter.setFabricate(fm);
    
    ProjectMutant pm = new ProjectMutant();
    DependencyMutant depA = new DependencyMutant();
    depA.setArtifact("depA");
    pm.addNormalizedDependency(depA);
    DependencyMutant depB = new DependencyMutant();
    pm.addNormalizedDependency(depB);
    depB.setArtifact("depB");
    DependencyMutant depC = new DependencyMutant();
    depC.setArtifact("depC");
    pm.addNormalizedDependency(depC);
    
    pm.addProjectDependency(new ProjectDependencyMutant("project_j"));
    pm.addProjectDependency(new ProjectDependencyMutant("project_k"));
    pm.setDir("/foo/pdir/");
    converter.setProject(pm);
    
    converter.setup(fabMemory_, routineMemory_);
    assertSame(fm, findSrcMockSetFabricate_.getArg(0));
    assertEquals(1, findSrcMockSetFabricate_.count());
    assertSame(fabMemory_, findSrcMockSetup_.getArgs(0)[0]);
    assertSame(routineMemory_, findSrcMockSetup_.getArgs(0)[1]);
    assertEquals(1, findSrcMockSetup_.count());
    assertSame(fm, findSrcMockSetFabricate_.getArg(0));
    assertEquals(1, findSrcMockSetFabricate_.count());
    assertEquals(0, findSrcMockSetProject_.count());
    assertEquals(0, findSrcMockSetupMutants_.count());
    
    List<String> srcDirs = new ArrayList<String>();
    srcDirs.add("/foo/bar/srcA");
    srcDirs.add("/foo/bar/srcB");
    
    File aDirMock = mock(File.class);
    when(aDirMock.getName()).thenReturn("srcA");
    when(fileMock_.instance("/foo/bar/srcA")).thenReturn(aDirMock);
    
    File aDirMockB = mock(File.class);
    when(aDirMockB.getName()).thenReturn("srcB");
    when(fileMock_.instance("/foo/bar/srcB")).thenReturn(aDirMockB);
    
    File aDirMockC = mock(File.class);
    when(aDirMockC.getName()).thenReturn("srcC");
    when(fileMock_.instance("/foo/bar/srcC")).thenReturn(aDirMockB);
    
    MockMethod<List<String>> findSrcOutput = new MockMethod<List<String>>(srcDirs);
    doAnswer(findSrcOutput).when(findSrcMock_).getOutput();
    
    MockMethod<Boolean> existsMethod = new MockMethod<Boolean>();
    doAnswer(existsMethod).when(fileMock_).exists(any());
    
    OutputStream out = mock(OutputStream.class);
    when(fileMock_.newFileOutputStream("/foo/pdir/.classpath")).thenReturn(out);
    MockMethod<Void> writeFileMethod = new MockMethod<Void>();
    doAnswer(writeFileMethod).when(fileMock_).writeFile(any(), any());
    
    I_RepositoryPathBuilder repo = mock(I_RepositoryPathBuilder.class);
    when(repoFactoryMock_.createRepositoryPathBuilder("/fabRepo/")).thenReturn(repo);
    when(repo.getArtifactPath(depA)).thenReturn("/fabRepo/depA");
    when(repo.getArtifactPath(depB)).thenReturn("/fabRepo/depB");
    when(repo.getArtifactPath(depC)).thenReturn("/fabRepo/depC");
    
    converter.run();
    ByteArrayInputStream bais = (ByteArrayInputStream) writeFileMethod.getArgs(0)[0];
    int size = bais.available();
    byte [] bytes = new byte[size];
    bais.read(bytes);
    assertUniform(
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<classpath>\n" +
        "\t<classpathentry kind=\"src\" path=\"srcA\"/>\n" +
        "\t<classpathentry kind=\"src\" path=\"srcB\"/>\n" +
        "\t<classpathentry kind=\"var\" path=\"FAB_REPO2/depA\"/>\n" +
        "\t<classpathentry kind=\"var\" path=\"FAB_REPO2/depB\"/>\n" +
        "\t<classpathentry kind=\"var\" path=\"FAB_REPO2/depC\"/>\n" +
        "\t<classpathentry combineaccessrules=\"false\" kind=\"src\" path=\"/project_j\"/>\n" +
        "\t<classpathentry combineaccessrules=\"false\" kind=\"src\" path=\"/project_k\"/>\n" +
        "</classpath>\n" 
            , new String(bytes));
    assertSame(out, writeFileMethod.getArgs(0)[1]);
  }
  
  @SuppressWarnings({"boxing", "unchecked"})
  @Test
  public void testMethodRunSimpleTwoSrcDirsThreeDepsTwoProject_EnvVarException() throws Exception {
    FabricateClasspathToEclipse4ishConverter converter = new FabricateClasspathToEclipse4ishConverter();
    converter.setTraitFactory(traitFactoryMock_);
    converter.setRepositoryFactory(repoFactoryMock_);
    converter.setSystem(sysMock_);
    converter.setBrief(brief_);
    
    when(sysMock_.hasArg(FabricateClasspathToEclipse4ishConverter.ECLIPSE_ENV_VAR)).thenReturn(true);
    when(sysMock_.getArgValue(FabricateClasspathToEclipse4ishConverter.ECLIPSE_ENV_VAR)).thenReturn("");
    
    FabricateMutant fm = new FabricateMutant();
    fm.setFabricateRepository("/fabRepo/");
    converter.setFabricate(fm);
    
    ProjectMutant pm = new ProjectMutant();
    DependencyMutant depA = new DependencyMutant();
    depA.setArtifact("depA");
    pm.addNormalizedDependency(depA);
    DependencyMutant depB = new DependencyMutant();
    pm.addNormalizedDependency(depB);
    depB.setArtifact("depB");
    DependencyMutant depC = new DependencyMutant();
    depC.setArtifact("depC");
    pm.addNormalizedDependency(depC);
    
    pm.addProjectDependency(new ProjectDependencyMutant("project_j"));
    pm.addProjectDependency(new ProjectDependencyMutant("project_k"));
    pm.setDir("/foo/pdir/");
    converter.setProject(pm);
    
    
    assertThrown(new ExpectedThrowable(new IllegalStateException(
        "The following command line argument is required for command classpath2eclipse;\n" +
        "eclipseEnvVariable")),
        new I_Thrower() {
          
          @Override
          public void run() throws Throwable {
            converter.setup(fabMemory_, routineMemory_);
          }
        });
    
  }
  
  @SuppressWarnings({"boxing", "unchecked"})
  @Test
  public void testMethodRunStrenuousDuplicateDependency() throws Exception {
    FabricateClasspathToEclipse4ishConverter converter = new FabricateClasspathToEclipse4ishConverter();
    converter.setTraitFactory(traitFactoryMock_);
    converter.setRepositoryFactory(repoFactoryMock_);
    converter.setSystem(sysMock_);
    converter.setBrief(brief_);
    
    FabricateMutant fm = new FabricateMutant();
    fm.setFabricateRepository("/foo/bar/");
    converter.setFabricate(fm);
    
    ProjectMutant pm = new ProjectMutant();
    DependencyMutant depA = new DependencyMutant();
    depA.setArtifact("depA");
    depA.setFileName("1");
    pm.addNormalizedDependency(depA);
    DependencyMutant depA2 = new DependencyMutant();
    depA2.setArtifact("depA");
    depA2.setFileName("2");
    pm.addNormalizedDependency(depA2);

    
    pm.setDir("/foo/pdir/");
    converter.setProject(pm);
    
    assertTrue(converter.setupInitial(fabMemoryMutant_, routineMemoryMutant_));
    assertSame(fm, findSrcMockSetFabricate_.getArg(0));
    assertEquals(1, findSrcMockSetFabricate_.count());
    assertSame(fabMemoryMutant_, findSrcMockSetupMutants_.getArgs(0)[0]);
    assertSame(routineMemoryMutant_, findSrcMockSetupMutants_.getArgs(0)[1]);
    assertEquals(1, findSrcMockSetupMutants_.count());
    assertSame(fm, findSrcMockSetFabricate_.getArg(0));
    assertEquals(1, findSrcMockSetFabricate_.count());
    assertEquals(0, findSrcMockSetProject_.count());
    assertEquals(0, findSrcMockSetup_.count());
    
    List<String> srcDirs = new ArrayList<String>();
    srcDirs.add("/foo/bar/srcA");
    
    File aDirMock = mock(File.class);
    when(aDirMock.getName()).thenReturn("srcA");
    when(fileMock_.instance("/foo/bar/srcA")).thenReturn(aDirMock);
    
    MockMethod<List<String>> findSrcOutput = new MockMethod<List<String>>(srcDirs);
    doAnswer(findSrcOutput).when(findSrcMock_).getOutput();
    
    MockMethod<Boolean> existsMethod = new MockMethod<Boolean>();
    doAnswer(existsMethod).when(fileMock_).exists(any());
    
    OutputStream out = mock(OutputStream.class);
    when(fileMock_.newFileOutputStream("/foo/pdir/.classpath")).thenReturn(out);
    MockMethod<Void> writeFileMethod = new MockMethod<Void>();
    doAnswer(writeFileMethod).when(fileMock_).writeFile(any(), any());
    
    I_RepositoryPathBuilder repo = mock(I_RepositoryPathBuilder.class);
    when(repoFactoryMock_.createRepositoryPathBuilder("/foo/bar/")).thenReturn(repo);
    when(repo.getArtifactPath(depA)).thenReturn("/foo/bar/depA");
    when(repo.getArtifactPath(depA2)).thenReturn("/foo/bar/depA");
    
    converter.run();
    ByteArrayInputStream bais = (ByteArrayInputStream) writeFileMethod.getArgs(0)[0];
    int size = bais.available();
    byte [] bytes = new byte[size];
    bais.read(bytes);
    assertUniform(
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<classpath>\n" +
        "\t<classpathentry kind=\"src\" path=\"srcA\"/>\n" +
        "\t<classpathentry kind=\"var\" path=\"FAB_REPO/depA\"/>\n" +
        "</classpath>\n" 
            , new String(bytes));
    assertSame(out, writeFileMethod.getArgs(0)[1]);
    
    assertEquals("The following command line argument was NOT provided for command " +
        "classpath2eclipse using default 'FAB_REPO';\n" +
        "eclipseEnvVariable", logPrintln_.getArg(0));
    assertEquals(1, logPrintln_.count());
  }

  @SuppressWarnings({"boxing", "unchecked"})
  @Test
  public void testMethodRunStrenuousDuplicateDependenciesWithEnvVar() throws Exception {
    FabricateClasspathToEclipse4ishConverter converter = new FabricateClasspathToEclipse4ishConverter();
    converter.setTraitFactory(traitFactoryMock_);
    converter.setRepositoryFactory(repoFactoryMock_);
    converter.setSystem(sysMock_);
    converter.setBrief(brief_);
    
    when(sysMock_.getArgValue(FabricateClasspathToEclipse4ishConverter.ECLIPSE_ENV_VAR)).thenReturn("FAB_REPO");
    
    FabricateMutant fm = new FabricateMutant();
    fm.setFabricateRepository("/fabRepo/");
    converter.setFabricate(fm);
    
    ProjectMutant pm = new ProjectMutant();
    DependencyMutant depA = new DependencyMutant();
    depA.setArtifact("depA");
    depA.setVersion("snapshot");
    pm.addNormalizedDependency(depA);
    DependencyMutant depA1 = new DependencyMutant();
    pm.addNormalizedDependency(depA1);
    depA1.setArtifact("depA");
    
    pm.setDir("/foo/pdir/");
    converter.setProject(pm);
    
    converter.setup(fabMemory_, routineMemory_);
    assertSame(fm, findSrcMockSetFabricate_.getArg(0));
    assertEquals(1, findSrcMockSetFabricate_.count());
    assertSame(fabMemory_, findSrcMockSetup_.getArgs(0)[0]);
    assertSame(routineMemory_, findSrcMockSetup_.getArgs(0)[1]);
    assertEquals(1, findSrcMockSetup_.count());
    assertSame(fm, findSrcMockSetFabricate_.getArg(0));
    assertEquals(1, findSrcMockSetFabricate_.count());
    assertEquals(0, findSrcMockSetProject_.count());
    assertEquals(0, findSrcMockSetupMutants_.count());
    
    List<String> srcDirs = new ArrayList<String>();
    srcDirs.add("/foo/bar/srcA");
    
    File aDirMock = mock(File.class);
    when(aDirMock.getName()).thenReturn("srcA");
    when(fileMock_.instance("/foo/bar/srcA")).thenReturn(aDirMock);
    
    MockMethod<List<String>> findSrcOutput = new MockMethod<List<String>>(srcDirs);
    doAnswer(findSrcOutput).when(findSrcMock_).getOutput();
    
    MockMethod<Boolean> existsMethod = new MockMethod<Boolean>();
    doAnswer(existsMethod).when(fileMock_).exists(any());
    
    OutputStream out = mock(OutputStream.class);
    when(fileMock_.newFileOutputStream("/foo/pdir/.classpath")).thenReturn(out);
    MockMethod<Void> writeFileMethod = new MockMethod<Void>();
    doAnswer(writeFileMethod).when(fileMock_).writeFile(any(), any());
    
    I_RepositoryPathBuilder repo = mock(I_RepositoryPathBuilder.class);
    when(repoFactoryMock_.createRepositoryPathBuilder("/fabRepo/")).thenReturn(repo);
    when(repo.getArtifactPath(depA)).thenReturn("/fabRepo/depA");
    when(repo.getArtifactPath(depA1)).thenReturn("/fabRepo/depA");
    
    converter.run();
    ByteArrayInputStream bais = (ByteArrayInputStream) writeFileMethod.getArgs(0)[0];
    int size = bais.available();
    byte [] bytes = new byte[size];
    bais.read(bytes);
    assertUniform(
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<classpath>\n" +
        "\t<classpathentry kind=\"src\" path=\"srcA\"/>\n" +
        "\t<classpathentry kind=\"var\" path=\"FAB_REPO/depA\"/>\n" +
        "</classpath>\n" 
            , new String(bytes));
    assertSame(out, writeFileMethod.getArgs(0)[1]);
  }
  
  @SuppressWarnings({"boxing", "unchecked"})
  @Test
  public void testMethodRunStrenuousDuplicateDependencyIde() throws Exception {
    FabricateClasspathToEclipse4ishConverter converter = new FabricateClasspathToEclipse4ishConverter();
    converter.setTraitFactory(traitFactoryMock_);
    converter.setRepositoryFactory(repoFactoryMock_);
    converter.setSystem(sysMock_);
    converter.setBrief(brief_);
    
    FabricateMutant fm = new FabricateMutant();
    fm.setFabricateRepository("fabRepo");
    converter.setFabricate(fm);
    
    ProjectMutant pm = new ProjectMutant();
    DependencyMutant depA = new DependencyMutant();
    depA.setArtifact("depA");
    depA.setFileName("1");
    
    List<I_Ide> cchildren = new ArrayList<I_Ide>();
    IdeMutant im = new IdeMutant();
    im.setName("eclipse");
    List<I_Parameter> ideChild = new ArrayList<I_Parameter>();
    ideChild.add(new Parameter("ideKey", "ideValue"));
    ideChild.add(new Parameter("ideKey", "ideValue"));
    im.setChildren(ideChild);
    cchildren.add(im);
    depA.setChildren(cchildren);
    
    pm.addNormalizedDependency(depA);

    pm.setDir("/foo/pdir/");
    converter.setProject(pm);
    
    converter.setupInitial(fabMemoryMutant_, routineMemoryMutant_);
    assertSame(fm, findSrcMockSetFabricate_.getArg(0));
    assertEquals(1, findSrcMockSetFabricate_.count());
    assertSame(fabMemoryMutant_, findSrcMockSetupMutants_.getArgs(0)[0]);
    assertSame(routineMemoryMutant_, findSrcMockSetupMutants_.getArgs(0)[1]);
    assertEquals(1, findSrcMockSetupMutants_.count());
    assertSame(fm, findSrcMockSetFabricate_.getArg(0));
    assertEquals(1, findSrcMockSetFabricate_.count());
    assertEquals(0, findSrcMockSetProject_.count());
    assertEquals(0, findSrcMockSetup_.count());
    
    List<String> srcDirs = new ArrayList<String>();
    srcDirs.add("/foo/bar/srcA");
    
    File aDirMock = mock(File.class);
    when(aDirMock.getName()).thenReturn("srcA");
    when(fileMock_.instance("/foo/bar/srcA")).thenReturn(aDirMock);
    
    MockMethod<List<String>> findSrcOutput = new MockMethod<List<String>>(srcDirs);
    doAnswer(findSrcOutput).when(findSrcMock_).getOutput();
    
    MockMethod<Boolean> existsMethod = new MockMethod<Boolean>();
    doAnswer(existsMethod).when(fileMock_).exists(any());
    
    OutputStream out = mock(OutputStream.class);
    when(fileMock_.newFileOutputStream("/foo/pdir/.classpath")).thenReturn(out);
    MockMethod<Void> writeFileMethod = new MockMethod<Void>();
    doAnswer(writeFileMethod).when(fileMock_).writeFile(any(), any());
    
    I_RepositoryPathBuilder repo = mock(I_RepositoryPathBuilder.class);
    when(repoFactoryMock_.createRepositoryPathBuilder("fabRepo")).thenReturn(repo);
    when(repo.getArtifactPath(depA)).thenReturn("/foo/bar/depA");
    
    converter.run();
    ByteArrayInputStream bais = (ByteArrayInputStream) writeFileMethod.getArgs(0)[0];
    int size = bais.available();
    byte [] bytes = new byte[size];
    bais.read(bytes);
    assertUniform(
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<classpath>\n" +
        "\t<classpathentry kind=\"src\" path=\"srcA\"/>\n" +
        "\t<classpathentry kind=\"ideKey\" path=\"ideValue\"/>\n" +
        "</classpath>\n" 
            , new String(bytes));
    assertSame(out, writeFileMethod.getArgs(0)[1]);
    
    assertEquals("The following command line argument was NOT provided for command " +
        "classpath2eclipse using default 'FAB_REPO';\n" +
        "eclipseEnvVariable", logPrintln_.getArg(0));
    assertEquals(1, logPrintln_.count());
  }
  
  @SuppressWarnings({"boxing", "unchecked"})
  @Test
  public void testMethodRunStrenuousDependencyIdeNotOnPlatform() throws Exception {
    FabricateClasspathToEclipse4ishConverter converter = new FabricateClasspathToEclipse4ishConverter();
    converter.setTraitFactory(traitFactoryMock_);
    converter.setRepositoryFactory(repoFactoryMock_);
    converter.setSystem(sysMock_);
    converter.setBrief(brief_);
    
    FabricateMutant fm = new FabricateMutant();
    fm.setFabricateRepository("fabRepo");
    converter.setFabricate(fm);
    
    ProjectMutant pm = new ProjectMutant();
    DependencyMutant depA = new DependencyMutant();
    depA.setArtifact("depA");
    depA.setFileName("1");
    depA.setPlatform("gwt");
    
    List<I_Ide> cchildren = new ArrayList<I_Ide>();
    IdeMutant im = new IdeMutant();
    im.setName("eclipse");
    List<I_Parameter> ideChild = new ArrayList<I_Parameter>();
    ideChild.add(new Parameter("ideKey", "ideValue"));
    ideChild.add(new Parameter("ideKey", "ideValue"));
    im.setChildren(ideChild);
    cchildren.add(im);
    depA.setChildren(cchildren);
    
    pm.addNormalizedDependency(depA);

    pm.setDir("/foo/pdir/");
    converter.setProject(pm);
    
    converter.setupInitial(fabMemoryMutant_, routineMemoryMutant_);
    assertSame(fm, findSrcMockSetFabricate_.getArg(0));
    assertEquals(1, findSrcMockSetFabricate_.count());
    assertSame(fabMemoryMutant_, findSrcMockSetupMutants_.getArgs(0)[0]);
    assertSame(routineMemoryMutant_, findSrcMockSetupMutants_.getArgs(0)[1]);
    assertEquals(1, findSrcMockSetupMutants_.count());
    assertSame(fm, findSrcMockSetFabricate_.getArg(0));
    assertEquals(1, findSrcMockSetFabricate_.count());
    assertEquals(0, findSrcMockSetProject_.count());
    assertEquals(0, findSrcMockSetup_.count());
    
    List<String> srcDirs = new ArrayList<String>();
    srcDirs.add("/foo/bar/srcA");
    
    File aDirMock = mock(File.class);
    when(aDirMock.getName()).thenReturn("srcA");
    when(fileMock_.instance("/foo/bar/srcA")).thenReturn(aDirMock);
    
    MockMethod<List<String>> findSrcOutput = new MockMethod<List<String>>(srcDirs);
    doAnswer(findSrcOutput).when(findSrcMock_).getOutput();
    
    MockMethod<Boolean> existsMethod = new MockMethod<Boolean>();
    doAnswer(existsMethod).when(fileMock_).exists(any());
    
    OutputStream out = mock(OutputStream.class);
    when(fileMock_.newFileOutputStream("/foo/pdir/.classpath")).thenReturn(out);
    MockMethod<Void> writeFileMethod = new MockMethod<Void>();
    doAnswer(writeFileMethod).when(fileMock_).writeFile(any(), any());
    
    I_RepositoryPathBuilder repo = mock(I_RepositoryPathBuilder.class);
    when(repoFactoryMock_.createRepositoryPathBuilder("fabRepo")).thenReturn(repo);
    when(repo.getArtifactPath(depA)).thenReturn("/foo/bar/depA");
    
    converter.run();
    ByteArrayInputStream bais = (ByteArrayInputStream) writeFileMethod.getArgs(0)[0];
    int size = bais.available();
    byte [] bytes = new byte[size];
    bais.read(bytes);
    assertUniform(
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<classpath>\n" +
        "\t<classpathentry kind=\"src\" path=\"srcA\"/>\n" +
        "</classpath>\n" 
            , new String(bytes));
    assertSame(out, writeFileMethod.getArgs(0)[1]);
    
    assertEquals("The following command line argument was NOT provided for command " +
        "classpath2eclipse using default 'FAB_REPO';\n" +
        "eclipseEnvVariable", logPrintln_.getArg(0));
    assertEquals(1, logPrintln_.count());
  }
  
  
  @SuppressWarnings({"boxing", "unchecked"})
  @Test
  public void testMethodRunStrenuousDuplicateProjectDependencies() throws Exception {
    FabricateClasspathToEclipse4ishConverter converter = new FabricateClasspathToEclipse4ishConverter();
    converter.setTraitFactory(traitFactoryMock_);
    converter.setRepositoryFactory(repoFactoryMock_);
    converter.setSystem(sysMock_);
    converter.setBrief(brief_);
    
    FabricateMutant fm = new FabricateMutant();
    fm.setFabricateRepository("/fabRepo/");
    converter.setFabricate(fm);
    
    ProjectMutant pm = new ProjectMutant();
    
    pm.addProjectDependency(new ProjectDependencyMutant("project_j"));
    
    ProjectDependencyMutant pdmB = new ProjectDependencyMutant("project_j");
    pdmB.setPlatform("GWT");
    pm.addProjectDependency(pdmB);
    
    pm.setDir("/foo/pdir/");
    converter.setProject(pm);
    
    converter.setup(fabMemory_, routineMemory_);
    assertSame(fm, findSrcMockSetFabricate_.getArg(0));
    assertEquals(1, findSrcMockSetFabricate_.count());
    assertSame(fabMemory_, findSrcMockSetup_.getArgs(0)[0]);
    assertSame(routineMemory_, findSrcMockSetup_.getArgs(0)[1]);
    assertEquals(1, findSrcMockSetup_.count());
    assertSame(fm, findSrcMockSetFabricate_.getArg(0));
    assertEquals(1, findSrcMockSetFabricate_.count());
    assertEquals(0, findSrcMockSetProject_.count());
    assertEquals(0, findSrcMockSetupMutants_.count());
    
    List<String> srcDirs = new ArrayList<String>();
    srcDirs.add("/foo/bar/srcA");
    
    File aDirMock = mock(File.class);
    when(aDirMock.getName()).thenReturn("srcA");
    when(fileMock_.instance("/foo/bar/srcA")).thenReturn(aDirMock);
    
    MockMethod<List<String>> findSrcOutput = new MockMethod<List<String>>(srcDirs);
    doAnswer(findSrcOutput).when(findSrcMock_).getOutput();
    
    MockMethod<Boolean> existsMethod = new MockMethod<Boolean>();
    doAnswer(existsMethod).when(fileMock_).exists(any());
    
    OutputStream out = mock(OutputStream.class);
    when(fileMock_.newFileOutputStream("/foo/pdir/.classpath")).thenReturn(out);
    MockMethod<Void> writeFileMethod = new MockMethod<Void>();
    doAnswer(writeFileMethod).when(fileMock_).writeFile(any(), any());
    
    I_RepositoryPathBuilder repo = mock(I_RepositoryPathBuilder.class);
    when(repoFactoryMock_.createRepositoryPathBuilder("/fabRepo/")).thenReturn(repo);
    
    converter.run();
    ByteArrayInputStream bais = (ByteArrayInputStream) writeFileMethod.getArgs(0)[0];
    int size = bais.available();
    byte [] bytes = new byte[size];
    bais.read(bytes);
    assertUniform(
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<classpath>\n" +
        "\t<classpathentry kind=\"src\" path=\"srcA\"/>\n" +
        "\t<classpathentry combineaccessrules=\"false\" kind=\"src\" path=\"/project_j\"/>\n" +
        "</classpath>\n" 
            , new String(bytes));
    assertSame(out, writeFileMethod.getArgs(0)[1]);
    
    assertEquals("The following command line argument was NOT provided for command " +
        "classpath2eclipse using default 'FAB_REPO';\n" +
        "eclipseEnvVariable", logPrintln_.getArg(0));
    assertEquals(1, logPrintln_.count());
  }
  
  @SuppressWarnings({"boxing", "unchecked"})
  @Test
  public void testMethodRunStrenuousIdeAttributesWithDuplicates() throws Exception {
    FabricateClasspathToEclipse4ishConverter converter = new FabricateClasspathToEclipse4ishConverter();
    converter.setTraitFactory(traitFactoryMock_);
    converter.setRepositoryFactory(repoFactoryMock_);
    converter.setSystem(sysMock_);
    converter.setBrief(brief_);
    
    FabricateMutant fm = new FabricateMutant();
    ParameterMutant fabIde = new ParameterMutant();
    fabIde.setKey("ide");
    fabIde.setValue("eclipse");
    fabIde.addChild(new ParameterMutant("ideKeyA", "fabIdeValueA"));
    fabIde.addChild(new ParameterMutant("ideKeyB", "fabIdeValueB"));
    fm.addAttribute(fabIde);
    converter.setFabricate(fm);
    
    ProjectMutant pm = new ProjectMutant();
    ParameterMutant projIde = new ParameterMutant();
    projIde.setKey("ide");
    projIde.setValue("eclipse");
    projIde.addChild(new ParameterMutant("ideKeyB", "projIdeValueB"));
    projIde.addChild(new ParameterMutant("ideKeyC", "projIdeValueC"));
    projIde.addChild(new ParameterMutant("ideKeyC", "projIdeValueC"));
    pm.addAttribute(projIde);
    pm.setDir("/foo/pdir/");
    converter.setProject(pm);
    
    assertTrue(converter.setupInitial(fabMemoryMutant_, routineMemoryMutant_));
    
    assertSame(fm, findSrcMockSetFabricate_.getArg(0));
    assertEquals(1, findSrcMockSetFabricate_.count());
    assertSame(fabMemoryMutant_, findSrcMockSetupMutants_.getArgs(0)[0]);
    assertSame(routineMemoryMutant_, findSrcMockSetupMutants_.getArgs(0)[1]);
    assertEquals(1, findSrcMockSetupMutants_.count());
    assertSame(fm, findSrcMockSetFabricate_.getArg(0));
    assertEquals(1, findSrcMockSetFabricate_.count());
    assertEquals(0, findSrcMockSetProject_.count());
    assertEquals(0, findSrcMockSetup_.count());
    
    List<String> srcDirs = new ArrayList<String>();
    srcDirs.add("/foo/bar/srcA");
    
    File aDirMock = mock(File.class);
    when(aDirMock.getName()).thenReturn("srcA");
    when(fileMock_.instance("/foo/bar/srcA")).thenReturn(aDirMock);
    
    
    MockMethod<List<String>> findSrcOutput = new MockMethod<List<String>>(srcDirs);
    doAnswer(findSrcOutput).when(findSrcMock_).getOutput();
    
    MockMethod<Boolean> existsMethod = new MockMethod<Boolean>();
    doAnswer(existsMethod).when(fileMock_).exists(any());
    
    OutputStream out = mock(OutputStream.class);
    when(fileMock_.newFileOutputStream("/foo/pdir/.classpath")).thenReturn(out);
    MockMethod<Void> writeFileMethod = new MockMethod<Void>();
    doAnswer(writeFileMethod).when(fileMock_).writeFile(any(), any());
    
    converter.run();
    ByteArrayInputStream bais = (ByteArrayInputStream) writeFileMethod.getArgs(0)[0];
    int size = bais.available();
    byte [] bytes = new byte[size];
    bais.read(bytes);
    assertUniform(
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<classpath>\n" +
        "\t<classpathentry kind=\"src\" path=\"srcA\"/>\n" +
        "\t<classpathentry kind=\"ideKeyA\" path=\"fabIdeValueA\"/>\n" +
        "\t<classpathentry kind=\"ideKeyB\" path=\"projIdeValueB\"/>\n" +
        "\t<classpathentry kind=\"ideKeyC\" path=\"projIdeValueC\"/>\n" +
        "</classpath>\n" 
            , new String(bytes));
    assertSame(out, writeFileMethod.getArgs(0)[1]);
    
    assertEquals("The following command line argument was NOT provided for command " +
        "classpath2eclipse using default 'FAB_REPO';\n" +
        "eclipseEnvVariable", logPrintln_.getArg(0));
    assertEquals(1, logPrintln_.count());
  }
  
  @SuppressWarnings({"boxing", "unchecked"})
  @Test
  public void testMethodRunStrenuousMultiplePlatform() throws Exception {
    FabricateClasspathToEclipse4ishConverter converter = new FabricateClasspathToEclipse4ishConverter();
    converter.setTraitFactory(traitFactoryMock_);
    converter.setRepositoryFactory(repoFactoryMock_);
    converter.setSystem(sysMock_);
    converter.setBrief(brief_);
    
    FabricateMutant fm = new FabricateMutant();
    fm.setFabricateRepository("/foo/bar/");
    converter.setFabricate(fm);
    
    ProjectMutant pm = new ProjectMutant();
    DependencyMutant depA = new DependencyMutant();
    depA.setArtifact("depA");
    depA.setPlatform("GWT");
    depA.setFileName("1");
    pm.addNormalizedDependency(depA);
    DependencyMutant depA2 = new DependencyMutant();
    depA2.setArtifact("depB");
    depA2.setFileName("2");
    pm.addNormalizedDependency(depA2);

    
    pm.setDir("/foo/pdir/");
    converter.setProject(pm);
    
    List<String> platforms = new ArrayList<String>();
    platforms.add("gwt");
    platforms.add("jse");
    reset(fabMemoryMutant_);
    when(fabMemoryMutant_.get(FabricationMemoryConstants.PLATFORMS)).thenReturn(platforms);
    
    converter.setupInitial(fabMemoryMutant_, routineMemoryMutant_);
    assertSame(fm, findSrcMockSetFabricate_.getArg(0));
    assertEquals(1, findSrcMockSetFabricate_.count());
    assertSame(fabMemoryMutant_, findSrcMockSetupMutants_.getArgs(0)[0]);
    assertSame(routineMemoryMutant_, findSrcMockSetupMutants_.getArgs(0)[1]);
    assertEquals(1, findSrcMockSetupMutants_.count());
    assertSame(fm, findSrcMockSetFabricate_.getArg(0));
    assertEquals(1, findSrcMockSetFabricate_.count());
    assertEquals(0, findSrcMockSetProject_.count());
    assertEquals(0, findSrcMockSetup_.count());
    
    List<String> srcDirs = new ArrayList<String>();
    srcDirs.add("/foo/bar/srcA");
    
    File aDirMock = mock(File.class);
    when(aDirMock.getName()).thenReturn("srcA");
    when(fileMock_.instance("/foo/bar/srcA")).thenReturn(aDirMock);
    
    MockMethod<List<String>> findSrcOutput = new MockMethod<List<String>>(srcDirs);
    doAnswer(findSrcOutput).when(findSrcMock_).getOutput();
    
    MockMethod<Boolean> existsMethod = new MockMethod<Boolean>();
    doAnswer(existsMethod).when(fileMock_).exists(any());
    
    OutputStream out = mock(OutputStream.class);
    when(fileMock_.newFileOutputStream("/foo/pdir/.classpath")).thenReturn(out);
    MockMethod<Void> writeFileMethod = new MockMethod<Void>();
    doAnswer(writeFileMethod).when(fileMock_).writeFile(any(), any());
    
    I_RepositoryPathBuilder repo = mock(I_RepositoryPathBuilder.class);
    when(repoFactoryMock_.createRepositoryPathBuilder("/foo/bar/")).thenReturn(repo);
    when(repo.getArtifactPath(depA)).thenReturn("/foo/bar/depA");
    when(repo.getArtifactPath(depA2)).thenReturn("/foo/bar/depA");
    
    converter.run();
    ByteArrayInputStream bais = (ByteArrayInputStream) writeFileMethod.getArgs(0)[0];
    int size = bais.available();
    byte [] bytes = new byte[size];
    bais.read(bytes);
    assertUniform(
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<classpath>\n" +
        "\t<classpathentry kind=\"src\" path=\"srcA\"/>\n" +
        "\t<classpathentry kind=\"var\" path=\"FAB_REPO/depA\"/>\n" +
        "</classpath>\n" 
            , new String(bytes));
    assertSame(out, writeFileMethod.getArgs(0)[1]);
    
    assertEquals("The following command line argument was NOT provided for command " +
        "classpath2eclipse using default 'FAB_REPO';\n" +
        "eclipseEnvVariable", logPrintln_.getArg(0));
    assertEquals(1, logPrintln_.count());
  }
  
  @SuppressWarnings({"boxing", "unchecked"})
  @Test
  public void testMethodRunStrenuousMultiplePlatformEnvVar() throws Exception {
    FabricateClasspathToEclipse4ishConverter converter = new FabricateClasspathToEclipse4ishConverter();
    converter.setTraitFactory(traitFactoryMock_);
    converter.setRepositoryFactory(repoFactoryMock_);
    converter.setSystem(sysMock_);
    converter.setBrief(brief_);
    
    when(sysMock_.getArgValue(FabricateClasspathToEclipse4ishConverter.ECLIPSE_ENV_VAR)).thenReturn("FAB_REPO");
    
    
    FabricateMutant fm = new FabricateMutant();
    fm.setFabricateRepository("/fabRepo/");
    converter.setFabricate(fm);
    
    ProjectMutant pm = new ProjectMutant();
    DependencyMutant depA = new DependencyMutant();
    depA.setArtifact("depA");
    depA.setPlatform("GWT");
    depA.setFileName("1");
    pm.addNormalizedDependency(depA);
    DependencyMutant depA2 = new DependencyMutant();
    depA2.setArtifact("depB");
    depA2.setFileName("2");
    pm.addNormalizedDependency(depA2);

    pm.setDir("/foo/pdir/");
    converter.setProject(pm);
    
    List<String> platforms = new ArrayList<String>();
    platforms.add("gwt");
    platforms.add("jse");
    reset(fabMemory_);
    when(fabMemory_.get(FabricationMemoryConstants.PLATFORMS)).thenReturn(platforms);
    
    converter.setup(fabMemory_, routineMemory_);
    assertSame(fm, findSrcMockSetFabricate_.getArg(0));
    assertEquals(1, findSrcMockSetFabricate_.count());
    assertSame(fabMemory_, findSrcMockSetup_.getArgs(0)[0]);
    assertSame(routineMemory_, findSrcMockSetup_.getArgs(0)[1]);
    assertEquals(1, findSrcMockSetup_.count());
    assertSame(fm, findSrcMockSetFabricate_.getArg(0));
    assertEquals(1, findSrcMockSetFabricate_.count());
    assertEquals(0, findSrcMockSetProject_.count());
    assertEquals(0, findSrcMockSetupMutants_.count());
    
    List<String> srcDirs = new ArrayList<String>();
    srcDirs.add("/foo/bar/srcA");
    
    File aDirMock = mock(File.class);
    when(aDirMock.getName()).thenReturn("srcA");
    when(fileMock_.instance("/foo/bar/srcA")).thenReturn(aDirMock);
    
    MockMethod<List<String>> findSrcOutput = new MockMethod<List<String>>(srcDirs);
    doAnswer(findSrcOutput).when(findSrcMock_).getOutput();
    
    MockMethod<Boolean> existsMethod = new MockMethod<Boolean>();
    doAnswer(existsMethod).when(fileMock_).exists(any());
    
    OutputStream out = mock(OutputStream.class);
    when(fileMock_.newFileOutputStream("/foo/pdir/.classpath")).thenReturn(out);
    MockMethod<Void> writeFileMethod = new MockMethod<Void>();
    doAnswer(writeFileMethod).when(fileMock_).writeFile(any(), any());
    
    I_RepositoryPathBuilder repo = mock(I_RepositoryPathBuilder.class);
    when(repoFactoryMock_.createRepositoryPathBuilder("/fabRepo/")).thenReturn(repo);
    when(repo.getArtifactPath(depA)).thenReturn("/foo/bar/depA");
    when(repo.getArtifactPath(depA2)).thenReturn("/foo/bar/depA");
    
    converter.run();
    ByteArrayInputStream bais = (ByteArrayInputStream) writeFileMethod.getArgs(0)[0];
    int size = bais.available();
    byte [] bytes = new byte[size];
    bais.read(bytes);
    assertUniform(
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<classpath>\n" +
        "\t<classpathentry kind=\"src\" path=\"srcA\"/>\n" +
        "\t<classpathentry kind=\"var\" path=\"FAB_REPO/depA\"/>\n" +
        "</classpath>\n" 
            , new String(bytes));
    assertSame(out, writeFileMethod.getArgs(0)[1]);
  }
}
