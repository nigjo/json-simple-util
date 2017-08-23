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

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import de.nigjo.json.util.JSONParameter;
import de.nigjo.json.util.core.MappingInfo.MappingType;

class JSONMappingManager
{
  private static boolean debug =
      Boolean.getBoolean(JSONMappingManager.class.getName() + ".debug");
  private Map<Class<?>, Map<String, MappingInfo>> mappings;

  JSONMappingManager()
  {
  }

  public static void log(Supplier<String> message)
  {
    if(debug)
    {
      System.out.println(message.get());
    }
  }

  public void scanClass(Class<?> type)
  {
    if(mappings == null)
    {
      mappings = new LinkedHashMap<>();
    }
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
          info.mapping = type.getClassLoader().loadClass(elementName);
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

  public Map<String, MappingInfo> getInfos(Class<?> resultType)
  {
    if(mappings != null)
    {
      return mappings.get(resultType);
    }
    else
    {
      return null;
    }
  }

}
