package de.nigjo.json.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONUtilities
{

  public static <J> J parse(Path infile, Class<J> type) throws IOException
  {
    try(BufferedReader in = Files.newBufferedReader(infile, StandardCharsets.UTF_8))
    {
      return parse(in, type);
    }
  }

  public static <J> J parse(Reader input, Class<J> type) throws IOException
  {
    JSONParser p = new JSONParser();
    try
    {
      Object result = p.parse(input);
      return JSONMappingManager.convertResult(result, type);
    }
    catch(ParseException ex)
    {
      throw new IOException((Throwable)ex);
    }
  }
  
  
  public static void write(Writer output, Object sourcedata) throws IOException
  {
    JSONObject jsondata = JSONMappingManager.convertToJSON(sourcedata);

    JSONObject.writeJSONString(jsondata, output);
  }

  private JSONUtilities()
  {
  }

}
