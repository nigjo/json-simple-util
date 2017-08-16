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

import java.util.Objects;

import de.nigjo.json.util.JSONParameter;

public class RowComponent
{
  @JSONParameter
  private String column;
  @JSONParameter
  private String type;
  @JSONParameter
  private Object value;

  public String getColumn()
  {
    return this.column;
  }

  public void setColumn(String column)
  {
    this.column = column;
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

  public int hashCode()
  {
    int hash = 5;
    hash = 23 * hash + Objects.hashCode(this.column);
    hash = 23 * hash + Objects.hashCode(this.type);
    hash = 23 * hash + Objects.hashCode(this.value);
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
    RowComponent other = (RowComponent)obj;
    if(!Objects.equals(this.column, other.column))
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
    return true;
  }

  public String toString()
  {
    return "{"
        + "\"column\":\"" + this.column
        + "\",\"type\":\"" + this.type
        + "\",\"value\":" + (this.value instanceof String
            ? ("\"" + this.value + '\"')
            : this.value)
        + '}';
  }

}
