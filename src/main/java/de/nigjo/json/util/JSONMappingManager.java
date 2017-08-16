/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.json.simple.JSONArray
 *  org.json.simple.JSONObject
 *  org.json.simple.parser.JSONParser
 *  org.json.simple.parser.ParseException
 */
package de.nigjo.json.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONMappingManager
{
  private static boolean debug = false;
  private Map<Class<?>, Map<String, MappingInfo>> mappings;

  private JSONMappingManager()
  {
  }

  public static <J> J parse(Path infile, Class<J> type) throws IOException
  {
    BufferedReader in = Files.newBufferedReader(infile, StandardCharsets.UTF_8);
    Throwable throwable = null;
    try
    {
      J j = JSONMappingManager.parse(in, type);
      return j;
    }
    catch(IOException ex)
    {
      throwable = ex;
      throw ex;
    }
    finally
    {
      if(in != null)
      {
        if(throwable != null)
        {
          try
          {
            in.close();
          }
          catch(Throwable throwable3)
          {
            throwable.addSuppressed(throwable3);
          }
        }
        else
        {
          in.close();
        }
      }
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

  private static <J> J convertResult(Object result, Class<J> type)
  {
    JSONMappingManager mm = new JSONMappingManager();
    return mm.convert(result, type);
  }

  private <J> J convert(Object ioResult, Class<J> type)
  {
    this.mappings = new HashMap();
    JSONMappingManager.log(() -> "--scanning----------");
    this.scanClass(type);
    JSONMappingManager.log(() -> "--mapping-----------");
    J result = this.map(ioResult, type);
    JSONMappingManager.log(() -> "--done--------------");
    return type.cast(result);
  }

  private <J> J map(Object source, Class<J> resultType)
  {
    Map<String, MappingInfo> infos = this.mappings.get(resultType);
    if(infos != null)
    {
      try
      {
        Constructor<J> resultCreator = resultType.getConstructor(new Class[0]);
        J result = resultCreator.newInstance(new Object[0]);
        for(MappingInfo info : infos.values())
        {
          this.map((JSONObject)source, result, info);
        }
        return resultType.cast(result);
      }
      catch(InvocationTargetException ex)
      {
        throw new IllegalArgumentException(ex.getCause());
      }
      catch(ReflectiveOperationException ex)
      {
        throw new IllegalArgumentException(ex);
      }
    }
    JSONMappingManager.log(() -> "ergebnistyp fuer " + resultType.getName()
        + " nicht bekannt");
    return null;
  }

  private void map(JSONObject source, Object result, MappingInfo info) throws
      ReflectiveOperationException
  {
    Object parameterValue = source.get((Object)info.parameterName);
    if(parameterValue == null)
    {
      if(!info.nullable && info.requiered)
      {
        throw new IllegalArgumentException("missing parameter " + info.parameterName);
      }
      return;
    }
    switch(info.kind)
    {
      default:
      {
        JSONMappingManager.log(() -> "no mapping for " + info.kind.name());
        break;
      }
      case STRING:
      {
        this.set(info, result, parameterValue);
        break;
      }
      case ARRAY:
      {
        JSONArray array = (JSONArray)parameterValue;
        if(info.data.getType().isArray())
        {
          JSONMappingManager.log(() -> "no mapping for arrays");
          break;
        }
        String typeName = info.data.getGenericType().getTypeName();
        String elementName = typeName.substring(typeName.indexOf(60) + 1, typeName.
            lastIndexOf(62));
        info.kind = MappingType.ARRAY;
        try
        {
          //Class<?> elementClass = Class.forName(elementName);
          final Class<? extends Object> mappingClass = Class.forName(elementName);
          List mapped =
              (List)array.stream()
                  .map(e -> this.map(e, mappingClass))
                  .collect(Collectors.toList());
          this.set(info, result, mapped);
        }
        catch(ClassNotFoundException ex)
        {
          JSONMappingManager.log(() -> ex.toString());
        }
        break;
      }
      case OBJECT:
      {
        if(parameterValue instanceof Long)
        {
          this.set(info, result, ((Long)parameterValue).intValue());
          break;
        }
        this.set(info, result, parameterValue);
      }
    }
  }

  private void set(MappingInfo info, Object result, Object parameterValue) throws
      ReflectiveOperationException
  {
    if(info.setter != null)
    {
      info.setter.invoke(result, parameterValue);
    }
    else
    {
      info.data.setAccessible(true);
      info.data.set(result, parameterValue);
    }
  }

  private static void log(Supplier<String> message)
  {
    if(debug)
    {
      System.out.println(message.get());
    }
  }

  /*
     * WARNING - Removed try catching itself - possible behaviour change.
   */
  private void scanClass(Class<?> type)
  {
    Field[] declaredFields;
    LinkedHashMap<String, MappingInfo> infos;
    JSONMappingManager.log(() -> "scanning " + type.getName());
    JSONMappingManager jSONMappingManager = this;
    synchronized(jSONMappingManager)
    {
      if(this.mappings.containsKey(type))
      {
        JSONMappingManager.log(() -> "- scanning skipped. already scanned.");
        return;
      }
      infos = new LinkedHashMap<String, MappingInfo>();
      this.mappings.put(type, infos);
    }
    for(Field field : declaredFields = type.getDeclaredFields())
    {
      JSONParameter annotation = field.getAnnotation(JSONParameter.class);
      if(annotation == null)
      {
        continue;
      }
      MappingInfo info = new MappingInfo();
      info.data = field;
      info.parameterName = annotation.name();
      if(info.parameterName.isEmpty())
      {
        info.parameterName = field.getName();
      }
      JSONMappingManager.log(() -> "- parameterName: " + info.parameterName);
      info.requiered = annotation.requiered();
      JSONMappingManager.log(() -> "  - requiered: " + info.requiered);
      info.nullable = annotation.nullable();
      JSONMappingManager.log(() -> "  - nullable: " + info.nullable);
      Class fieldType = field.getType();
      try
      {
        info.setter = type.getDeclaredMethod("set" + Character.toUpperCase(
            info.parameterName.charAt(0)) + info.parameterName.substring(1), fieldType);
        JSONMappingManager.log(() -> "  - setter found: " + info.setter.getName());
      }
      catch(NoSuchMethodException | SecurityException ex)
      {
        info.setter = null;
      }
      if(String.class.isAssignableFrom(fieldType))
      {
        info.kind = MappingType.STRING;
      }
      else if(Number.class.isAssignableFrom(fieldType))
      {
        info.kind = MappingType.NUMBER;
      }
      else if(Boolean.class.isAssignableFrom(fieldType) || Boolean.TYPE == fieldType)
      {
        info.kind = MappingType.BOOLEAN;
      }
      else if(fieldType.isArray())
      {
        info.kind = MappingType.ARRAY;
        this.scanClass(fieldType.getComponentType());
      }
      else if(List.class.equals(fieldType))
      {
        String typeName = field.getGenericType().getTypeName();
        String elementName = typeName.substring(typeName.indexOf(60) + 1, typeName.
            lastIndexOf(62));
        JSONMappingManager.log(() -> "  - listType: " + elementName);
        info.kind = MappingType.ARRAY;
        try
        {
          Class elementClass = Class.forName(elementName);
          this.scanClass(elementClass);
        }
        catch(ClassNotFoundException ex)
        {
          JSONMappingManager.log(() -> ex.toString());
        }
      }
      else
      {
        info.kind = MappingType.OBJECT;
        this.scanClass(fieldType);
      }
      JSONMappingManager.log(() -> "  - kind: " + info.kind.name());
      infos.put(info.parameterName, info);
    }
    JSONMappingManager.log(() -> "scanning of " + type.getName() + " done.");
  }

  private static class MappingInfo
  {
    String parameterName;
    MappingType kind;
    Class<?> mapping;
    boolean requiered;
    boolean nullable;
    Field data;
    Method setter;

    private MappingInfo()
    {
    }

  }

  private static enum MappingType
  {
    OBJECT,
    ARRAY,
    STRING,
    NUMBER,
    BOOLEAN,
    NULL;

    private MappingType()
    {
    }

  }

}
