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
package de.nigjo.json.util.testdata;

import java.util.List;
import java.util.Objects;

import de.nigjo.json.util.JSONParameter;
import static de.nigjo.json.util.testdata.AutoGuiDefinition.printArray;

public class Value
{
  @JSONParameter
  private String name;
  @JSONParameter
  private String type;
  @JSONParameter
  private Object value;
  @JSONParameter
  private List<RowComponent> components;

  public String getName()
  {
    return this.name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getType()
  {
    return this.type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public Object getValue()
  {
    return this.value;
  }

  public void setValue(Object value)
  {
    this.value = value;
  }

  public List<RowComponent> getComponents()
  {
    return this.components;
  }

  public void setComponents(List<RowComponent> components)
  {
    this.components = components;
  }

  public int hashCode()
  {
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.name);
    hash = 97 * hash + Objects.hashCode(this.type);
    hash = 97 * hash + Objects.hashCode(this.value);
    hash = 97 * hash + Objects.hashCode(this.components);
    return hash;
  }

  public boolean equals(Object obj)
  {
    if(this == obj)
    {
      return true;
    }
    if(obj == null)
    {
      return false;
    }
    if(this.getClass() != obj.getClass())
    {
      return false;
    }
    Value other = (Value)obj;
    if(!Objects.equals(this.name, other.name))
    {
      return false;
    }
    if(!Objects.equals(this.type, other.type))
    {
      return false;
    }
    if(!Objects.equals(this.value, other.value))
    {
      return false;
    }
    if(!Objects.equals(this.components, other.components))
    {
      return false;
    }
    return true;
  }

  public String toString()
  {
    return "{"
        + "\"name\":\"" + this.name
        + "\",\"type\":\"" + this.type
        + "\",\"value\":" + (this.value instanceof String
            ? ("\"" + this.value + '\"')
            : this.value)
        + (this.components != null
            ? (",\"components\":" + printArray(this.components))
            : "")
        + '}';
  }

}
