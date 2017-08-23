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

import de.nigjo.json.util.core.JSONConverter;
import de.nigjo.json.util.core.JSONCreator;

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
      if(result instanceof JSONObject)
      {
        return JSONConverter.readObject((JSONObject)result, type);
      }
      else if(type.isInstance(result))
      {
        return type.cast(result);
      }
      else
      {
        throw new IOException("data does not contain a JSONObject.");
      }
    }
    catch(ParseException ex)
    {
      throw new IOException((Throwable)ex);
    }
  }

  public static void write(Writer output, Object sourcedata) throws IOException
  {
    JSONObject jsondata = JSONCreator.createObject(sourcedata);

    JSONObject.writeJSONString(jsondata, output);
  }

  private JSONUtilities()
  {
  }

}
