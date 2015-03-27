package org.adligo.fabricate4eclipse_tests.classpath_conversion;

import org.adligo.fabricate.common.en.FabricateEnConstants;
import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.files.xml_io.I_FabXmlFileIO;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.FabSystem;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_Parameter;
import org.adligo.fabricate.models.common.I_RoutineFactory;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;
import org.adligo.fabricate.models.common.Parameter;
import org.adligo.fabricate.models.dependencies.DependencyMutant;
import org.adligo.fabricate.models.dependencies.I_Ide;
import org.adligo.fabricate.models.dependencies.IdeMutant;
import org.adligo.fabricate.models.dependencies.ProjectDependencyMutant;
import org.adligo.fabricate.models.fabricate.FabricateMutant;
import org.adligo.fabricate.models.project.ProjectMutant;
import org.adligo.fabricate.repository.I_RepositoryFactory;
import org.adligo.fabricate.repository.I_RepositoryPathBuilder;
import org.adligo.fabricate.routines.implicit.FindSrcTrait;
import org.adligo.fabricate4eclipse.classpath_conversion.FabricateClasspathToEclipse4ishConverter;
import org.adligo.tests4j.system.shared.trials.SourceFileScope;
import org.adligo.tests4j.system.shared.trials.Test;
import org.adligo.tests4j_4mockito.MockMethod;
import org.adligo.tests4j_4mockito.MockitoSourceFileTrial;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

@SourceFileScope (sourceClass=FabricateClasspathToEclipse4ishConverter.class, minCoverage=0.0)
public class FabricateClasspathToEclipse4ishConverterTrial extends MockitoSourceFileTrial {
  private ByteArrayOutputStream baos_;
  private FabSystem sysMock_;
  private I_FabFileIO fileMock_;
  private I_FabXmlFileIO xmlIoMock_;
  private I_FabLog mockLog_;
  private FindSrcTrait findSrcMock_;
  private MockMethod<Void> findSrcMockSetFabricate_;
  private MockMethod<Void> findSrcMockSetProject_;
  private MockMethod<Void> findSrcMockSetup_;
  private MockMethod<Boolean> findSrcMockSetupMutants_;
  private I_RoutineFactory traitFactoryMock_;
  private I_RepositoryFactory repoFactoryMock_;
  
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
    
    baos_ = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(baos_);
    
    findSrcMock_ = mock(FindSrcTrait.class);
    try {
      findSrcMockSetFabricate_ = new MockMethod<Void>();
      doAnswer(findSrcMockSetFabricate_).when(findSrcMock_).setFabricate(any());
      
      findSrcMockSetProject_ = new MockMethod<Void>();
      doAnswer(findSrcMockSetProject_).when(findSrcMock_).setProject(any());
      
      findSrcMockSetup_ = new MockMethod<Void>();
      doAnswer(findSrcMockSetup_).when(findSrcMock_).setup(any(), any());
      
      findSrcMockSetupMutants_ = new MockMethod<Boolean>();
      doAnswer(findSrcMockSetupMutants_).when(findSrcMock_).setupInitial(any(), any());
      
      traitFactoryMock_ = mock(I_RoutineFactory.class);
      when(traitFactoryMock_.createRoutine(
          FindSrcTrait.NAME, FindSrcTrait.IMPLEMENTED_INTERFACES)).thenReturn(findSrcMock_);
    } catch (FabricationRoutineCreationException x) {
      throw new RuntimeException(x);
    }
    
    repoFactoryMock_ = mock(I_RepositoryFactory.class);
  }
  
  @SuppressWarnings({"boxing", "unchecked", "rawtypes"})
  @Test
  public void testMethodRunTwoSrcDirs() throws Exception {
    FabricateClasspathToEclipse4ishConverter converter = new FabricateClasspathToEclipse4ishConverter();
    converter.setTraitFactory(traitFactoryMock_);
    converter.setRepositoryFactory(repoFactoryMock_);
    converter.setSystem(sysMock_);
    
    FabricateMutant fm = new FabricateMutant();
    converter.setFabricate(fm);
    
    ProjectMutant pm = new ProjectMutant();
    pm.setDir("/foo/pdir/");
    converter.setProject(pm);
    
    I_FabricationMemoryMutant fabMemory = mock(I_FabricationMemoryMutant.class);
    I_RoutineMemoryMutant routineMemory = mock(I_RoutineMemoryMutant.class);
    converter.setupInitial(fabMemory, routineMemory);
    assertSame(fm, findSrcMockSetFabricate_.getArg(0));
    assertEquals(1, findSrcMockSetFabricate_.count());
    assertSame(fabMemory, findSrcMockSetupMutants_.getArgs(0)[0]);
    assertSame(routineMemory, findSrcMockSetupMutants_.getArgs(0)[1]);
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
  }
  
  @SuppressWarnings({"boxing", "unchecked", "rawtypes"})
  @Test
  public void testMethodRunTwoSrcDirsThreeDepsOneIdeTwoProject() throws Exception {
    FabricateClasspathToEclipse4ishConverter converter = new FabricateClasspathToEclipse4ishConverter();
    converter.setTraitFactory(traitFactoryMock_);
    converter.setRepositoryFactory(repoFactoryMock_);
    converter.setSystem(sysMock_);
    
    FabricateMutant fm = new FabricateMutant();
    fm.setFabricateRepository("fabRepo");
    converter.setFabricate(fm);
    
    ProjectMutant pm = new ProjectMutant();
    DependencyMutant depA = new DependencyMutant();
    pm.addNormalizedDependency(depA);
    DependencyMutant depB = new DependencyMutant();
    pm.addNormalizedDependency(depB);
    DependencyMutant depC = new DependencyMutant();
    List<I_Ide> cchildren = new ArrayList<I_Ide>();
    IdeMutant im = new IdeMutant();
    im.setName("eclipse");
    List<I_Parameter> ideChild = new ArrayList<I_Parameter>();
    ideChild.add(new Parameter("ideKey", "ideValue"));
    im.setChildren(ideChild);
    depC.setChildren(cchildren);
    
    pm.addNormalizedDependency(depC);
    
    pm.addProjectDependency(new ProjectDependencyMutant("project_j"));
    pm.addProjectDependency(new ProjectDependencyMutant("project_k"));
    pm.setDir("/foo/pdir/");
    converter.setProject(pm);
    
    I_FabricationMemoryMutant fabMemory = mock(I_FabricationMemoryMutant.class);
    I_RoutineMemoryMutant routineMemory = mock(I_RoutineMemoryMutant.class);
    converter.setupInitial(fabMemory, routineMemory);
    assertSame(fm, findSrcMockSetFabricate_.getArg(0));
    assertEquals(1, findSrcMockSetFabricate_.count());
    assertSame(fabMemory, findSrcMockSetupMutants_.getArgs(0)[0]);
    assertSame(routineMemory, findSrcMockSetupMutants_.getArgs(0)[1]);
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
    when(repoFactoryMock_.createRepositoryPathBuilder("fabRepo")).thenReturn(repo);
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
        "\t<classpathentry kind=\"var\" path=\"/foo/bar/depB\"/>\n" +
        "\t<classpathentry combineaccessrules=\"false\" kind=\"src\" path=\"/project_j\"/>\n" +
        "\t<classpathentry combineaccessrules=\"false\" kind=\"src\" path=\"/project_k\"/>\n" +
        "</classpath>\n" 
            , new String(bytes));
    assertSame(out, writeFileMethod.getArgs(0)[1]);
  }
  
  
  @SuppressWarnings({"boxing", "unchecked", "rawtypes"})
  @Test
  public void testMethodRunTwoSrcDirsThreeDepsOneIdeTwoProject_EnvVar() throws Exception {
    FabricateClasspathToEclipse4ishConverter converter = new FabricateClasspathToEclipse4ishConverter();
    converter.setTraitFactory(traitFactoryMock_);
    converter.setRepositoryFactory(repoFactoryMock_);
    converter.setSystem(sysMock_);
    
    when(sysMock_.getArgValue(FabricateClasspathToEclipse4ishConverter.ECLIPSE_ENV_VAR)).thenReturn("FAB_REPO");
    
    FabricateMutant fm = new FabricateMutant();
    fm.setFabricateRepository("/fabRepo");
    converter.setFabricate(fm);
    
    ProjectMutant pm = new ProjectMutant();
    DependencyMutant depA = new DependencyMutant();
    pm.addNormalizedDependency(depA);
    DependencyMutant depB = new DependencyMutant();
    pm.addNormalizedDependency(depB);
    
    pm.setDir("/foo/pdir/");
    converter.setProject(pm);
    
    I_FabricationMemory fabMemory = mock(I_FabricationMemory.class);
    I_RoutineMemory routineMemory = mock(I_RoutineMemory.class);
    converter.setup(fabMemory, routineMemory);
    assertSame(fm, findSrcMockSetFabricate_.getArg(0));
    assertEquals(1, findSrcMockSetFabricate_.count());
    assertSame(fabMemory, findSrcMockSetup_.getArgs(0)[0]);
    assertSame(routineMemory, findSrcMockSetup_.getArgs(0)[1]);
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
    
    MockMethod<List<String>> findSrcOutput = new MockMethod<List<String>>(srcDirs);
    doAnswer(findSrcOutput).when(findSrcMock_).getOutput();
    
    MockMethod<Boolean> existsMethod = new MockMethod<Boolean>();
    doAnswer(existsMethod).when(fileMock_).exists(any());
    
    OutputStream out = mock(OutputStream.class);
    when(fileMock_.newFileOutputStream("/foo/pdir/.classpath")).thenReturn(out);
    MockMethod<Void> writeFileMethod = new MockMethod<Void>();
    doAnswer(writeFileMethod).when(fileMock_).writeFile(any(), any());
    
    I_RepositoryPathBuilder repo = mock(I_RepositoryPathBuilder.class);
    when(repoFactoryMock_.createRepositoryPathBuilder("/fabRepo")).thenReturn(repo);
    when(repo.getArtifactPath(depA)).thenReturn("/fabRepo/depA");
    when(repo.getArtifactPath(depB)).thenReturn("/fabRepo/depB");
    
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
        "\t<classpathentry kind=\"var\" path=\"FAB_REPO/depB\"/>\n" +
        "</classpath>\n" 
            , new String(bytes));
    assertSame(out, writeFileMethod.getArgs(0)[1]);
  }
}
