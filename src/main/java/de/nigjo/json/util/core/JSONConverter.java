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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
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

  private <J> J convert(Object ioResult, Class<J> type)
  {
    JSONMappingManager.log(() -> "--scanning----------");
    mm.scanClass(type);
    JSONMappingManager.log(() -> "--mapping-----------");
    J result = this.mapToData(ioResult, type);
    JSONMappingManager.log(() -> "--done--------------");
    return type.cast(result);
  }

  private <J> J mapToData(Object source, Class<J> resultType)
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

}
