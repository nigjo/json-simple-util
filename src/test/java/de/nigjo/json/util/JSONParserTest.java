/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  de.nigjo.json.util.JSONMappingManager
 *  org.junit.Assert
 *  org.junit.Test
 */
package de.nigjo.json.util;

import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.nigjo.json.util.testdata.AutoGuiDefinition;
import de.nigjo.json.util.testdata.PropertyContainer;
import de.nigjo.json.util.testdata.TestDataGenerator;

public class JSONParserTest
{
  @BeforeClass
  public static void setDebugMode() throws Exception
  {
    if(Boolean.getBoolean("test.debug") || Boolean.getBoolean("test.debug.parser"))
    {
      Field debug = JSONMappingManager.class.getDeclaredField("debug");
      debug.setAccessible(true);
      debug.set(null, true);
    }
  }

  @AfterClass
  public static void resetDebugMode() throws Exception
  {
    if(Boolean.getBoolean("test.debug") || Boolean.getBoolean("test.debug.parser"))
    {
      Field debug = JSONMappingManager.class.getDeclaredField("debug");
      debug.setAccessible(true);
      debug.set(null, false);
    }
  }

  @Test
  public void testParse_Path_Class() throws Exception
  {
    AutoGuiDefinition expResult = TestDataGenerator.createExpectedDemoResult();
    URL resource = JSONParserTest.class.getResource("testdata/demo.autogui");
    Path input = Paths.get(resource.toURI());
    Object result = JSONUtilities.parse(input, AutoGuiDefinition.class);
    Assert.assertEquals(expResult, result);
  }

  @Test
  public void testParse_Reader_Class() throws Exception
  {
    AutoGuiDefinition expResult = TestDataGenerator.createExpectedDemoResult();
    try(InputStreamReader in = new InputStreamReader(
        JSONParserTest.class.getResourceAsStream("testdata/demo.autogui"),
        StandardCharsets.UTF_8))
    {
      Object result = JSONUtilities.parse(in, AutoGuiDefinition.class);
      Assert.assertEquals(expResult, result);
    }
  }

  @Test
  public void testReadmeDemo() throws Exception
  {
    PropertyContainer expResult = TestDataGenerator.createReadmeDemoResult();
    try(InputStreamReader in = new InputStreamReader(
        JSONParserTest.class.getResourceAsStream("testdata/demoproperties.json"),
        StandardCharsets.UTF_8))
    {
      Object result = JSONUtilities.parse(in, PropertyContainer.class);
      Assert.assertEquals(expResult, result);
    }
  }

}
