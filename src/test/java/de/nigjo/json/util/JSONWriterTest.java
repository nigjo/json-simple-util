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

import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import de.nigjo.json.util.testdata.PropertyContainer;
import de.nigjo.json.util.testdata.TestDataGenerator;

/**
 * Test class to check the creation of JSON files from POJOs.
 * 
 * @author Jens Hofschröer
 */
public class JSONWriterTest
{

  @Test
  public void testReadmeDemo() throws Exception
  {
    PropertyContainer demodata = TestDataGenerator.createReadmeDemoResult();
    Path tempFile = Files.createTempFile("~jsonutil", ".json");
    tempFile.toFile().deleteOnExit();
    try(Writer output =
        Files.newBufferedWriter(tempFile, StandardCharsets.UTF_8,
            StandardOpenOption.TRUNCATE_EXISTING))
    {
      JSONUtilities.write(output, demodata);
    }
    List<String> result = Files.readAllLines(tempFile, StandardCharsets.UTF_8);
    List<String> expected = Collections.singletonList("{"
        + "\"name\":\"Demodata\","
        + "\"properties\":[{"
        + "\"value\":\"value1\","
        + "\"key\":\"key1\""
        + "},{"
        + "\"value\":\"value2\","
        + "\"key\":\"key2\""
        + "}]}");

    assertEquals(expected, result);
  }

}
