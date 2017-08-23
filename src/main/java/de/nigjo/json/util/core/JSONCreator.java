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

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import static de.nigjo.json.util.core.JSONMappingManager.log;

/**
 *
 * @author Jens Hofschröer
 */
public class JSONCreator
{

  private final JSONMappingManager mm;

  private JSONCreator()
  {
    this.mm = new JSONMappingManager();
  }

  public static JSONObject createObject(Object sourcedata)
  {
    JSONCreator creator = new JSONCreator();
    return creator.convert(sourcedata);
  }

  private JSONObject convert(Object dataSource)
  {
    JSONMappingManager.log(() -> "--scanning----------");
    mm.scanClass(dataSource.getClass());
    JSONMappingManager.log(() -> "--mapping-----------");
    JSONObject result = mapToJSON(dataSource);
    JSONMappingManager.log(() -> "--done--------------");
    return result;
  }

  @SuppressWarnings("unchecked")
  private JSONObject mapToJSON(Object source)
  {
    Map<String, MappingInfo> infos = mm.getInfos(source.getClass());
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

}
