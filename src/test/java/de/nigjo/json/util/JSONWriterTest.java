package de.nigjo.json.util;

import java.io.Writer;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;

import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;

import de.nigjo.json.util.testdata.PropertyContainer;
import de.nigjo.json.util.testdata.TestDataGenerator;

/**
 * Eine neue Klasse von hof. Erstellt Aug 16, 2017, 11:03:39 AM.
 *
 * @todo Hier fehlt die Beschreibung der Klasse.
 *
 * @author hof
 */
public class JSONWriterTest
{

  @BeforeClass
  public static void setDebugMode() throws Exception
  {
    if(Boolean.getBoolean("test.debug") || Boolean.getBoolean("test.debug.writer"))
    {
      Field debug = JSONMappingManager.class.getDeclaredField("debug");
      debug.setAccessible(true);
      debug.set(null, true);
    }
  }

  @AfterClass
  public static void resetDebugMode() throws Exception
  {
    if(Boolean.getBoolean("test.debug") || Boolean.getBoolean("test.debug.writer"))
    {
      Field debug = JSONMappingManager.class.getDeclaredField("debug");
      debug.setAccessible(true);
      debug.set(null, false);
    }
  }

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
