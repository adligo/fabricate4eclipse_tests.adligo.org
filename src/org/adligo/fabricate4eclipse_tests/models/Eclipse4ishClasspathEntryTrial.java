package org.adligo.fabricate4eclipse_tests.models;

import org.adligo.fabricate.models.common.ParameterMutant;
import org.adligo.fabricate.models.dependencies.DependencyMutant;
import org.adligo.fabricate.models.dependencies.ProjectDependencyMutant;
import org.adligo.fabricate4eclipse.models.Eclipse4ishClasspathEntryMutant;
import org.adligo.tests4j.shared.asserts.common.ExpectedThrowable;
import org.adligo.tests4j.shared.asserts.common.I_Thrower;
import org.adligo.tests4j.system.shared.trials.SourceFileScope;
import org.adligo.tests4j.system.shared.trials.Test;
import org.adligo.tests4j_4mockito.MockitoSourceFileTrial;

@SourceFileScope (sourceClass=Eclipse4ishClasspathEntryMutant.class, minCoverage=99.0)
public class Eclipse4ishClasspathEntryTrial extends MockitoSourceFileTrial {
  
  @Test
  public void testConstructorDependency() {
    DependencyMutant dm = new DependencyMutant();
    dm.setArtifact("artifact");
    
    Eclipse4ishClasspathEntryMutant a = new Eclipse4ishClasspathEntryMutant(dm, "foo");
    assertEquals("var", a.getKind());
    assertEquals("foo", a.getPath());
    assertNull(a.getCombineaccessrules());
  }

  @Test
  public void testConstructorDependencyExceptions() {
    DependencyMutant dm = new DependencyMutant();
    dm.setArtifact("artifact");
    dm.setType("ide");
    assertThrown(new ExpectedThrowable(new IllegalArgumentException("type")),
        new I_Thrower() {
          
          @SuppressWarnings("unused")
          @Override
          public void run() throws Throwable {
            new Eclipse4ishClasspathEntryMutant(dm, "foo");
          }
        });
    //assertEquals(a.get)
  }
  
  @Test
  public void testConstructorParameter() {
    ParameterMutant pm = new ParameterMutant();
    pm.setKey("key");
    pm.setValue("value");
    
    Eclipse4ishClasspathEntryMutant a = new Eclipse4ishClasspathEntryMutant(pm);
    assertEquals("key", a.getKind());
    assertEquals("value", a.getPath());
    assertNull(a.getCombineaccessrules());
  }
  
  @Test
  public void testConstructorProject() {
    ProjectDependencyMutant pm = new ProjectDependencyMutant();
    pm.setProjectName("projectName");
    
    Eclipse4ishClasspathEntryMutant a = new Eclipse4ishClasspathEntryMutant(pm);
    assertEquals("src", a.getKind());
    assertEquals("/projectName", a.getPath());
    assertEquals("false", a.getCombineaccessrules());
  }
  
  
  @Test
  public void testMethodsGetsAndSets() {
    Eclipse4ishClasspathEntryMutant a = new Eclipse4ishClasspathEntryMutant();
    assertNull(a.getCombineaccessrules());
    assertNull(a.getKind());
    assertNull(a.getPath());
    
    a.setCombineaccessrules("true");
    a.setKind("kind");
    a.setPath("path");
    
    assertEquals("true", a.getCombineaccessrules());
    assertEquals("kind", a.getKind());
    assertEquals("path", a.getPath());
    
    a.setCombineaccessrules(null);
    a.setKind(null);
    a.setPath(null);
    
    assertNull(a.getCombineaccessrules());
    assertNull(a.getKind());
    assertNull(a.getPath());
  }
  @SuppressWarnings("boxing")
  @Test
  public void testMethodsEqualsHashCodeAndToString() {
    Eclipse4ishClasspathEntryMutant a = new Eclipse4ishClasspathEntryMutant();
    Eclipse4ishClasspathEntryMutant b = new Eclipse4ishClasspathEntryMutant();
    b.setCombineaccessrules("true");
    Eclipse4ishClasspathEntryMutant c = new Eclipse4ishClasspathEntryMutant();
    c.setKind("kind");
    Eclipse4ishClasspathEntryMutant d = new Eclipse4ishClasspathEntryMutant();
    d.setPath("path");
    Eclipse4ishClasspathEntryMutant e = new Eclipse4ishClasspathEntryMutant();
    e.setCombineaccessrules("true");
    e.setKind("E");
    Eclipse4ishClasspathEntryMutant f = new Eclipse4ishClasspathEntryMutant();
    f.setKind("F");
    f.setPath("pathF");
    Eclipse4ishClasspathEntryMutant g = new Eclipse4ishClasspathEntryMutant();
    g.setCombineaccessrules("true");
    g.setKind("kind");
    g.setPath("path");
    Eclipse4ishClasspathEntryMutant h = new Eclipse4ishClasspathEntryMutant();
    h.setCombineaccessrules("true");
    h.setKind("kind");
    h.setPath("path");
    Eclipse4ishClasspathEntryMutant i = new Eclipse4ishClasspathEntryMutant();
    i.setCombineaccessrules("true1");
    i.setKind("kind");
    i.setPath("path");
    Eclipse4ishClasspathEntryMutant j = new Eclipse4ishClasspathEntryMutant();
    j.setCombineaccessrules("true");
    j.setKind("kind1");
    j.setPath("path");
    Eclipse4ishClasspathEntryMutant k = new Eclipse4ishClasspathEntryMutant();
    k.setCombineaccessrules("true");
    k.setKind("kind");
    k.setPath("path1");
    
    assertEquals(a.hashCode(), a.hashCode());
    assertEquals(a, a);
    assertNotEquals(a, null);
    assertNotEquals(a, new Object());
    
    assertNotEquals(a.hashCode(), b.hashCode());
    assertNotEquals(a.hashCode(), c.hashCode());
    assertNotEquals(a.hashCode(), d.hashCode());
    assertNotEquals(a.hashCode(), e.hashCode());
    assertNotEquals(a.hashCode(), f.hashCode());
    assertNotEquals(a.hashCode(), g.hashCode());
    assertNotEquals(a.hashCode(), h.hashCode());
    assertNotEquals(a.hashCode(), i.hashCode());
    assertNotEquals(a.hashCode(), j.hashCode());
    assertNotEquals(a.hashCode(), k.hashCode());
    
    assertNotEquals(a, b);
    assertNotEquals(a, c);
    assertNotEquals(a, d);
    assertNotEquals(a, e);
    assertNotEquals(a, f);
    assertNotEquals(a, g);
    assertNotEquals(a, h);
    assertNotEquals(a, i);
    assertNotEquals(a, j);
    assertNotEquals(a, k);
    assertEquals("Eclipse4ishClasspathEntryMutant [kind=null, path=null, combineaccessrules=null]", a.toString());
    
    assertEquals(b.hashCode(), b.hashCode());
    assertEquals(b, b);
    assertNotEquals(b.hashCode(), a.hashCode());
    assertNotEquals(b.hashCode(), c.hashCode());
    assertNotEquals(b.hashCode(), d.hashCode());
    assertNotEquals(b.hashCode(), e.hashCode());
    assertNotEquals(b.hashCode(), f.hashCode());
    assertNotEquals(b.hashCode(), g.hashCode());
    assertNotEquals(b.hashCode(), h.hashCode());
    assertNotEquals(b.hashCode(), i.hashCode());
    assertNotEquals(b.hashCode(), j.hashCode());
    assertNotEquals(b.hashCode(), k.hashCode());
    
    assertNotEquals(b, a);
    assertNotEquals(b, c);
    assertNotEquals(b, d);
    assertNotEquals(b, e);
    assertNotEquals(b, f);
    assertNotEquals(b, g);
    assertNotEquals(b, h);
    assertNotEquals(b, i);
    assertNotEquals(b, j);
    assertNotEquals(b, k);
    assertEquals("Eclipse4ishClasspathEntryMutant [kind=null, path=null, combineaccessrules=true]", b.toString());
    
    assertEquals(c.hashCode(), c.hashCode());
    assertEquals(c, c);
    assertNotEquals(c.hashCode(), a.hashCode());
    assertNotEquals(c.hashCode(), b.hashCode());
    assertNotEquals(c.hashCode(), d.hashCode());
    assertNotEquals(c.hashCode(), e.hashCode());
    assertNotEquals(c.hashCode(), f.hashCode());
    assertNotEquals(c.hashCode(), g.hashCode());
    assertNotEquals(c.hashCode(), h.hashCode());
    assertNotEquals(c.hashCode(), i.hashCode());
    assertNotEquals(c.hashCode(), j.hashCode());
    assertNotEquals(c.hashCode(), k.hashCode());
    
    assertNotEquals(c, a);
    assertNotEquals(c, b);
    assertNotEquals(c, d);
    assertNotEquals(c, e);
    assertNotEquals(c, f);
    assertNotEquals(c, g);
    assertNotEquals(c, h);
    assertNotEquals(c, i);
    assertNotEquals(c, j);
    assertNotEquals(c, k);
    assertEquals("Eclipse4ishClasspathEntryMutant [kind=kind, path=null, combineaccessrules=null]",c.toString());

    assertEquals(d.hashCode(), d.hashCode());
    assertEquals(d, d);
    assertNotEquals(d.hashCode(), a.hashCode());
    assertNotEquals(d.hashCode(), b.hashCode());
    assertNotEquals(c.hashCode(), d.hashCode());
    assertNotEquals(c.hashCode(), e.hashCode());
    assertNotEquals(c.hashCode(), f.hashCode());
    assertNotEquals(c.hashCode(), g.hashCode());
    assertNotEquals(c.hashCode(), h.hashCode());
    assertNotEquals(c.hashCode(), i.hashCode());
    assertNotEquals(c.hashCode(), j.hashCode());
    assertNotEquals(c.hashCode(), k.hashCode());
    
    assertNotEquals(c, a);
    assertNotEquals(c, b);
    assertNotEquals(c, d);
    assertNotEquals(c, e);
    assertNotEquals(c, f);
    assertNotEquals(c, g);
    assertNotEquals(c, h);
    assertNotEquals(c, i);
    assertNotEquals(c, j);
    assertNotEquals(c, k);
    assertEquals("Eclipse4ishClasspathEntryMutant [kind=kind, path=null, combineaccessrules=null]", c.toString());
    
    assertEquals(d.hashCode(), d.hashCode());
    assertEquals(d, d);
    assertNotEquals(d.hashCode(), a.hashCode());
    assertNotEquals(d.hashCode(), b.hashCode());
    assertNotEquals(d.hashCode(), c.hashCode());
    assertNotEquals(d.hashCode(), e.hashCode());
    assertNotEquals(d.hashCode(), f.hashCode());
    assertNotEquals(d.hashCode(), g.hashCode());
    assertNotEquals(d.hashCode(), h.hashCode());
    assertNotEquals(d.hashCode(), i.hashCode());
    assertNotEquals(d.hashCode(), j.hashCode());
    assertNotEquals(d.hashCode(), k.hashCode());
   
    assertNotEquals(d, a);
    assertNotEquals(d, b);
    assertNotEquals(d, c);
    assertNotEquals(d, e);
    assertNotEquals(d, f);
    assertNotEquals(d, g);
    assertNotEquals(d, h);
    assertNotEquals(d, i);
    assertNotEquals(d, j);
    assertNotEquals(d, k);
    assertEquals("Eclipse4ishClasspathEntryMutant [kind=null, path=path, combineaccessrules=null]", d.toString());
    
    assertEquals(e.hashCode(), e.hashCode());
    assertEquals(e, e);
    assertNotEquals(e.hashCode(), a.hashCode());
    assertNotEquals(e.hashCode(), b.hashCode());
    assertNotEquals(e.hashCode(), c.hashCode());
    assertNotEquals(e.hashCode(), d.hashCode());
    assertNotEquals(e.hashCode(), f.hashCode());
    assertNotEquals(e.hashCode(), g.hashCode());
    assertNotEquals(e.hashCode(), h.hashCode());
    assertNotEquals(e.hashCode(), i.hashCode());
    assertNotEquals(e.hashCode(), j.hashCode());
    assertNotEquals(e.hashCode(), k.hashCode());
    
    assertNotEquals(e, a);
    assertNotEquals(e, b);
    assertNotEquals(e, c);
    assertNotEquals(e, d);
    assertNotEquals(e, f);
    assertNotEquals(e, g);
    assertNotEquals(e, h);
    assertNotEquals(e, i);
    assertNotEquals(e, j);
    assertNotEquals(e, k);
    assertEquals("Eclipse4ishClasspathEntryMutant [kind=E, path=null, combineaccessrules=true]", e.toString());
  
    assertEquals(f.hashCode(), f.hashCode());
    assertEquals(f, f);
    assertNotEquals(f.hashCode(), a.hashCode());
    assertNotEquals(f.hashCode(), b.hashCode());
    assertNotEquals(f.hashCode(), c.hashCode());
    assertNotEquals(f.hashCode(), d.hashCode());
    assertNotEquals(f.hashCode(), e.hashCode());
    assertNotEquals(f.hashCode(), g.hashCode());
    assertNotEquals(f.hashCode(), h.hashCode());
    assertNotEquals(f.hashCode(), i.hashCode());
    assertNotEquals(f.hashCode(), j.hashCode());
    assertNotEquals(f.hashCode(), k.hashCode());
    
    assertNotEquals(f, a);
    assertNotEquals(f, b);
    assertNotEquals(f, c);
    assertNotEquals(f, d);
    assertNotEquals(f, e);
    assertNotEquals(f, g);
    assertNotEquals(f, h);
    assertNotEquals(f, i);
    assertNotEquals(f, j);
    assertNotEquals(f, k);
    assertEquals("Eclipse4ishClasspathEntryMutant [kind=F, path=pathF, combineaccessrules=null]", f.toString());
    
    assertEquals(g.hashCode(), g.hashCode());
    assertEquals(g.hashCode(), h.hashCode());
    assertEquals(g, g);
    assertEquals(g.hashCode(), g.hashCode());
    
    assertNotEquals(g.hashCode(), a.hashCode());
    assertNotEquals(g.hashCode(), b.hashCode());
    assertNotEquals(g.hashCode(), c.hashCode());
    assertNotEquals(g.hashCode(), d.hashCode());
    assertNotEquals(g.hashCode(), e.hashCode());
    assertNotEquals(g.hashCode(), f.hashCode());
    
    assertNotEquals(g.hashCode(), i.hashCode());
    assertNotEquals(g.hashCode(), j.hashCode());
    assertNotEquals(g.hashCode(), k.hashCode());
    
    assertNotEquals(g, a);
    assertNotEquals(g, b);
    assertNotEquals(g, c);
    assertNotEquals(g, d);
    assertNotEquals(g, e);
    assertNotEquals(g, f);
    assertNotEquals(g, i);
    assertNotEquals(g, j);
    assertNotEquals(g, k);
    assertEquals("Eclipse4ishClasspathEntryMutant [kind=kind, path=path, combineaccessrules=true]", g.toString());
    
    assertEquals(h.hashCode(), g.hashCode());
    assertEquals(h.hashCode(), h.hashCode());
    assertEquals(h, g);
    assertEquals(h.hashCode(), g.hashCode());
    
    assertNotEquals(h.hashCode(), a.hashCode());
    assertNotEquals(h.hashCode(), b.hashCode());
    assertNotEquals(h.hashCode(), c.hashCode());
    assertNotEquals(h.hashCode(), d.hashCode());
    assertNotEquals(h.hashCode(), e.hashCode());
    assertNotEquals(h.hashCode(), f.hashCode());
    
    assertNotEquals(h.hashCode(), i.hashCode());
    assertNotEquals(h.hashCode(), j.hashCode());
    assertNotEquals(h.hashCode(), k.hashCode());
    
    assertNotEquals(h, a);
    assertNotEquals(h, b);
    assertNotEquals(h, c);
    assertNotEquals(h, d);
    assertNotEquals(h, e);
    assertNotEquals(h, f);
    assertNotEquals(h, i);
    assertNotEquals(h, j);
    assertNotEquals(h, k);
    assertEquals("Eclipse4ishClasspathEntryMutant [kind=kind, path=path, combineaccessrules=true]", h.toString());
    
    assertEquals(i.hashCode(), i.hashCode());
    assertEquals(i, i);
    assertNotEquals(i.hashCode(), a.hashCode());
    assertNotEquals(i.hashCode(), b.hashCode());
    assertNotEquals(i.hashCode(), c.hashCode());
    assertNotEquals(i.hashCode(), d.hashCode());
    assertNotEquals(i.hashCode(), f.hashCode());
    assertNotEquals(i.hashCode(), g.hashCode());
    assertNotEquals(i.hashCode(), h.hashCode());
    assertNotEquals(i.hashCode(), j.hashCode());
    assertNotEquals(i.hashCode(), k.hashCode());
    
    assertNotEquals(i, a);
    assertNotEquals(i, b);
    assertNotEquals(i, c);
    assertNotEquals(i, d);
    assertNotEquals(i, f);
    assertNotEquals(i, g);
    assertNotEquals(i, h);
    assertNotEquals(i, j);
    assertNotEquals(i, k);
    assertEquals("Eclipse4ishClasspathEntryMutant [kind=kind, path=path, combineaccessrules=true1]", i.toString());
    
    assertEquals(j.hashCode(), j.hashCode());
    assertEquals(j, j);
    assertNotEquals(j.hashCode(), a.hashCode());
    assertNotEquals(j.hashCode(), b.hashCode());
    assertNotEquals(j.hashCode(), c.hashCode());
    assertNotEquals(j.hashCode(), d.hashCode());
    assertNotEquals(j.hashCode(), f.hashCode());
    assertNotEquals(j.hashCode(), g.hashCode());
    assertNotEquals(j.hashCode(), h.hashCode());
    assertNotEquals(j.hashCode(), i.hashCode());
    assertNotEquals(j.hashCode(), k.hashCode());
    
    assertNotEquals(j, a);
    assertNotEquals(j, b);
    assertNotEquals(j, c);
    assertNotEquals(j, d);
    assertNotEquals(j, f);
    assertNotEquals(j, g);
    assertNotEquals(j, h);
    assertNotEquals(j, i);
    assertNotEquals(j, k);
    assertEquals("Eclipse4ishClasspathEntryMutant [kind=kind1, path=path, combineaccessrules=true]", j.toString());
    
    assertEquals(k.hashCode(), k.hashCode());
    assertEquals(k, k);
    assertNotEquals(k.hashCode(), a.hashCode());
    assertNotEquals(k.hashCode(), b.hashCode());
    assertNotEquals(k.hashCode(), c.hashCode());
    assertNotEquals(k.hashCode(), d.hashCode());
    assertNotEquals(k.hashCode(), f.hashCode());
    assertNotEquals(k.hashCode(), g.hashCode());
    assertNotEquals(k.hashCode(), h.hashCode());
    assertNotEquals(k.hashCode(), i.hashCode());
    assertNotEquals(k.hashCode(), j.hashCode());
    
    assertNotEquals(k, a);
    assertNotEquals(k, b);
    assertNotEquals(k, c);
    assertNotEquals(k, d);
    assertNotEquals(k, f);
    assertNotEquals(k, g);
    assertNotEquals(k, h);
    assertNotEquals(k, i);
    assertNotEquals(k, j);
    assertEquals("Eclipse4ishClasspathEntryMutant [kind=kind, path=path1, combineaccessrules=true]", k.toString());
  }
  
  @Test
  public void testStaticMethodIs4Eclipse() {
    DependencyMutant dm = new DependencyMutant();
    assertTrue(Eclipse4ishClasspathEntryMutant.is4Eclipse(dm));
    dm.setType("ide");
    assertFalse(Eclipse4ishClasspathEntryMutant.is4Eclipse(dm));
    dm.setType("jar");
    assertTrue(Eclipse4ishClasspathEntryMutant.is4Eclipse(dm));
  }
}
