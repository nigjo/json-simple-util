/*
 * Copyright 2017 Jens Hofschröer.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.nigjo.json.util;

import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

import de.nigjo.json.util.testdata.AutoGuiDefinition;
import de.nigjo.json.util.testdata.PropertyContainer;
import de.nigjo.json.util.testdata.TestDataGenerator;
import de.nigjo.json.util.testdata.UndefinedValues;

/**
 * test class to check the conversion from JSON data to POJOs.
 *
 * @author Jens Hofschröer
 */
public class JSONParserTest
{
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

  @Test
  public void testMappedDemo() throws Exception
  {
    UndefinedValues expResult = TestDataGenerator.createMappedDemoResult();
    try(InputStreamReader in = new InputStreamReader(
        JSONParserTest.class.getResourceAsStream("testdata/values.json"),
        StandardCharsets.UTF_8))
    {
      Object result = JSONUtilities.parse(in, UndefinedValues.class);
      Assert.assertEquals(expResult, result);
    }
  }

}
