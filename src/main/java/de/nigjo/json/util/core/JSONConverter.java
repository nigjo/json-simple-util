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
package de.nigjo.json.util.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Helperclass to convert JSON Items to Java Classes.
 *
 * @author Jens Hofschröer
 */
public class JSONConverter
{
  private final JSONMappingManager mm;

  private JSONConverter()
  {
    this.mm = new JSONMappingManager();
  }

  public static <J> J readObject(JSONObject jsonObject, Class<J> type)
  {
    JSONConverter converter = new JSONConverter();
    return converter.convert(jsonObject, type);
  }

  public static <J> List<J> readArray(JSONArray source, Class<J> elementType)
  {
    JSONConverter reader = new JSONConverter();
    return reader.convertArray(source, elementType);
  }

  private <J> List<J> convertArray(JSONArray ioResult, Class<J> elementType)
  {
    JSONMappingManager.log(() -> "--scanning----------");
    mm.scanClass(elementType);
    JSONMappingManager.log(() -> "--mapping-----------");
    @SuppressWarnings(value = "unchecked")
    List<J> mapped =
        (List)ioResult.stream().
            map((e) -> this.mapUnknown(e, elementType)).collect(Collectors.toList());
    JSONMappingManager.log(() -> "--done--------------");
    return mapped;
  }

  private <J> J convert(Object ioResult, Class<J> type)
  {
    JSONMappingManager.log(() -> "--scanning----------");
    mm.scanClass(type);
    JSONMappingManager.log(() -> "--mapping-----------");
    J result = this.mapUnknown(ioResult, type);
    JSONMappingManager.log(() -> "--done--------------");
    return type.cast(result);
  }

  private <J> J mapUnknown(Object source, Class<J> resultType)
  {
    Map<String, MappingInfo> infos = this.mm.getInfos(resultType);
    if(infos != null)
    {
      try
      {
        Constructor<J> resultCreator = resultType.getConstructor();
        J result = resultCreator.newInstance();
        for(MappingInfo info : infos.values())
        {
          this.mapObject((JSONObject)source, result, info);
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

  private void mapObject(JSONObject source, Object result, MappingInfo info)
      throws ReflectiveOperationException
  {
    JSONMappingManager.log(() ->
        "mapping " + result.getClass().getSimpleName() + '.' + info.parameterName);
    Object parameterValue = source.get(info.parameterName);
    if(parameterValue == null)
    {
      boolean nullSet = source.containsKey(info.parameterName);
      if(info.requiered && !nullSet)
      {
        throw new IllegalArgumentException("missing requiered parameter "
            + info.parameterName + " for " + result.getClass().getName());
      }
      if(nullSet)
      {
        if(info.requiered && !info.nullable)
        {
          throw new IllegalArgumentException("requiered parameter " + info.parameterName
              + " for " + result.getClass().getName() + " can not be null");
        }
        if(info.nullable)
        {
          set(info, result, null);
        }
      }
      return;
    }
    switch(info.kind)
    {
      case NUMBER:
        if(parameterValue instanceof Number)
        {
          switch(info.data.getType().getName())
          {
            case "byte":
            case "java.lang.Byte":
              this.set(info, result, ((Number)parameterValue).byteValue());
              break;
            case "short":
            case "java.lang.Short":
              this.set(info, result, ((Number)parameterValue).byteValue());
              break;
            case "int":
            case "java.lang.Integer":
              this.set(info, result, ((Number)parameterValue).intValue());
              break;
            case "long":
            case "java.lang.Long":
              this.set(info, result, ((Number)parameterValue).longValue());
              break;
            case "float":
            case "java.lang.Float":
              this.set(info, result, ((Number)parameterValue).floatValue());
              break;
            case "double":
            case "java.lang.Double":
              this.set(info, result, ((Number)parameterValue).doubleValue());
              break;
      default:
              JSONMappingManager.log(
                  () -> "unknown number format " + info.data.getType());
              this.set(info, result, parameterValue);
        break;
          }
        }
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
            @SuppressWarnings("unchecked")
            List<?> mapped =
                (List)array.stream()
                    .map(e -> this.mapUnknown(e, info.mapping))
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
        }
        else if(parameterValue instanceof JSONObject)
        {
          // simple Map?
          Map<String, Object> mappedResult = new LinkedHashMap<>();
          @SuppressWarnings("unchecked")
          Set<Map.Entry<?, ?>> entrys = ((JSONObject)parameterValue).entrySet();
          for(Map.Entry<?, ?> entry : entrys)
          {
            Object mapped = mapUnknown(entry.getValue(), info.mapping);
            mappedResult.put((String)entry.getKey(), mapped);
          }
          this.set(info, result, mappedResult);
        }
        else
        {
        this.set(info, result, parameterValue);
        }
        break;
      default:
        JSONMappingManager.log(() -> "no mapping for " + info.kind.name());
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

}
