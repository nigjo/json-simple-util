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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

class JSONMappingManager
{
  private static boolean debug = false;
  private Map<Class<?>, Map<String, MappingInfo>> mappings;

  private JSONMappingManager()
  {
  }

  static JSONObject convertToJSON(Object source)
  {
    JSONMappingManager mm = new JSONMappingManager();
    return mm.convert(source);
  }

  private JSONObject convert(Object dataSource)
  {
    this.mappings = new HashMap<>();
    JSONMappingManager.log(() -> "--scanning----------");
    scanClass(dataSource.getClass());
    JSONMappingManager.log(() -> "--mapping-----------");
    JSONObject result = mapToJSON(dataSource);
    JSONMappingManager.log(() -> "--done--------------");
    return result;
  }

  static <J> J convertResult(Object result, Class<J> type)
  {
    JSONMappingManager mm = new JSONMappingManager();
    return mm.convert(result, type);
  }

  private <J> J convert(Object ioResult, Class<J> type)
  {
    this.mappings = new HashMap<>();
    JSONMappingManager.log(() -> "--scanning----------");
    this.scanClass(type);
    JSONMappingManager.log(() -> "--mapping-----------");
    J result = this.mapToData(ioResult, type);
    JSONMappingManager.log(() -> "--done--------------");
    return type.cast(result);
  }

  private <J> J mapToData(Object source, Class<J> resultType)
  {
    Map<String, MappingInfo> infos = this.mappings.get(resultType);
    if(infos != null)
    {
      try
      {
        Constructor<J> resultCreator = resultType.getConstructor();
        J result = resultCreator.newInstance();
        for(MappingInfo info : infos.values())
        {
          this.mapToData((JSONObject)source, result, info);
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
    JSONMappingManager.log(
        () -> "ergebnistyp fuer " + resultType.getName() + " nicht bekannt");
    return null;
  }

  private void mapToData(JSONObject source, Object result, MappingInfo info)
      throws ReflectiveOperationException
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
        JSONMappingManager.log(() -> "no mapping for " + info.kind.name());
        break;
      case STRING:
        this.set(info, result, parameterValue);
        break;
      case ARRAY:
        JSONArray array = (JSONArray)parameterValue;
        if(info.data.getType().isArray())
        {
          JSONMappingManager.log(() -> "no mapping for arrays");
          break;
        }
        if(info.mapping != null)
        {
          try
          {
            //Class<?> elementClass = Class.forName(elementName);
            @SuppressWarnings("unchecked")
            List<?> mapped =
                (List)array.stream()
                    .map(e -> this.mapToData(e, info.mapping))
                    .collect(Collectors.toList());
            this.set(info, result, mapped);
          }
          catch(ClassNotFoundException ex)
          {
            JSONMappingManager.log(() -> ex.toString());
          }
        }
        break;
      case OBJECT:
        if(parameterValue instanceof Long)
        {
          this.set(info, result, ((Long)parameterValue).intValue());
          break;
        }
        this.set(info, result, parameterValue);
        break;
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

  @SuppressWarnings("unchecked")
  private JSONObject mapToJSON(Object source)
  {
    Map<String, MappingInfo> infos = mappings.get(source.getClass());
    if(infos != null)
    {
      JSONObject jo = new JSONObject();
      try
      {
        for(MappingInfo info : infos.values())
        {
          switch(info.kind)
          {
            case BOOLEAN:
            case NUMBER:
            case STRING:
              put(jo, info, source);
              break;
            case OBJECT:
              //TODO: mapping
              break;
            case ARRAY:
              Object data = get(info, source);
              if(data instanceof List)
              {
                JSONArray result = (JSONArray)((List)data).stream()
                    .map(i -> mapToJSON(i))
                    .collect(Collectors.toCollection(JSONArray::new));
                jo.put(info.parameterName, result);
              }
              else
              {
                JSONMappingManager.log(() -> "no mapping for arrays");
              }
              break;
            default:
              JSONMappingManager.log(() -> "no mapping for " + info.kind.name());
              break;
          }
        }
        return jo;
      }
      catch(ReflectiveOperationException ex)
      {
        throw new IllegalStateException(ex);
      }
    }
    log(() -> "unkown mapping for " + source.getClass());
    return null;
  }

  @SuppressWarnings("unchecked")
  private void put(JSONObject jo, MappingInfo info,
      Object source)
      throws ReflectiveOperationException
  {
    Object result = get(info, source);
    if(result == null)
    {
      if(info.nullable)
      {
        jo.put(info.parameterName, null);
      }
    }
    else
    {
      jo.put(info.parameterName, result);
    }
  }

  private Object get(MappingInfo info, Object source) throws IllegalAccessException,
      IllegalArgumentException, SecurityException, InvocationTargetException
  {
    Object result;
    if(info.getter != null)
    {
      result = info.getter.invoke(source);
    }
    else
    {
      info.data.setAccessible(true);
      result = info.data.get(source);
    }
    return result;
  }

  private static void log(Supplier<String> message)
  {
    if(debug)
    {
      System.out.println(message.get());
    }
  }

  private void scanClass(Class<?> type)
  {
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
      infos = new LinkedHashMap<>();
      this.mappings.put(type, infos);
    }
    for(Field field : type.getDeclaredFields())
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
      Class<?> fieldType = field.getType();
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
      try
      {
        info.getter = type.getDeclaredMethod("get" + Character.toUpperCase(
            info.parameterName.charAt(0)) + info.parameterName.substring(1));
        Class<?> returnType = info.getter.getReturnType();
        if(fieldType.isAssignableFrom(returnType))
        {
          JSONMappingManager.log(() -> "  - getter found: " + info.getter.getName());
        }
        else
        {
          info.getter = null;
        }
      }
      catch(NoSuchMethodException | SecurityException ex)
      {
        info.getter = null;
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
          info.mapping = Class.forName(elementName);
          this.scanClass(info.mapping);
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
    private String parameterName;
    private MappingType kind;
    private Class<?> mapping;
    private boolean requiered;
    private boolean nullable;
    private Field data;
    private Method setter;
    private Method getter;
  }

  private static enum MappingType
  {
    OBJECT,
    ARRAY,
    STRING,
    NUMBER,
    BOOLEAN,
    NULL;
  }

}
