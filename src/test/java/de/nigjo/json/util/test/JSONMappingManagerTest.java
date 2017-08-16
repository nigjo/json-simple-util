/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  de.nigjo.json.util.JSONMappingManager
 *  org.junit.Assert
 *  org.junit.Test
 */
package de.nigjo.json.util.test;

import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

import de.nigjo.json.util.JSONMappingManager;
import de.nigjo.json.util.test.data.AutoGuiDefinition;
import de.nigjo.json.util.test.data.PropertyContainer;
import de.nigjo.json.util.test.data.TestDataGenerator;

public class JSONMappingManagerTest
{
  public static void setDebugMode() throws Exception
  {
    Field debug = JSONMappingManager.class.getDeclaredField("debug");
    debug.setAccessible(true);
    debug.set(null, true);
  }

  @Test
  public void testParse_Path_Class() throws Exception
  {
    AutoGuiDefinition expResult = TestDataGenerator.createExpectedDemoResult();
    URL resource = JSONMappingManagerTest.class.getResource("demo.autogui");
    Path input = Paths.get(resource.toURI());
    Object result = JSONMappingManager.parse(input, AutoGuiDefinition.class);
    Assert.assertEquals(expResult, result);
  }

  @Test
  public void testParse_Reader_Class() throws Exception
  {
    AutoGuiDefinition expResult = TestDataGenerator.createExpectedDemoResult();
    try(InputStreamReader in = new InputStreamReader(
        JSONMappingManagerTest.class.getResourceAsStream("demo.autogui"),
        StandardCharsets.UTF_8))
    {
      Object result = JSONMappingManager.parse(in, AutoGuiDefinition.class);
      Assert.assertEquals(expResult, result);
    }
  }
  
  @Test
  public void testReadmeDemo() throws Exception
  {
    PropertyContainer expResult = TestDataGenerator.createReadmeDemoResult();
    try(InputStreamReader in = new InputStreamReader(
        JSONMappingManagerTest.class.getResourceAsStream("demoproperties.json"),
        StandardCharsets.UTF_8))
    {
      Object result = JSONMappingManager.parse(in, PropertyContainer.class);
      Assert.assertEquals(expResult, result);
    }
  }

}
