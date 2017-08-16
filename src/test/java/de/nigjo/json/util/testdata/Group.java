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

public class Group
{
  @JSONParameter
  private String name;
  @JSONParameter
  private String title;
  @JSONParameter
  private String column;
  @JSONParameter
  private String row;
  @JSONParameter
  private List<Group> groups;
  @JSONParameter
  private List<Value> values;

  public String toString()
  {
    return "{"
        + "\"name\":\"" + this.name
        + "\",\"title\":\"" + this.title
        + "\",\"column\":\"" + this.column
        + "\",\"row\":\"" + this.row + "\""
        + (this.groups != null ? (",\"groups\":" + printArray(this.groups)) : "")
        + (this.values != null ? (",\"values\":" + printArray(this.values)) : "")
        + '}';
  }

  public String getName()
  {
    return this.name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getTitle()
  {
    return this.title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public String getColumn()
  {
    return this.column;
  }

  public void setColumn(String column)
  {
    this.column = column;
  }

  public String getRow()
  {
    return this.row;
  }

  public void setRow(String row)
  {
    this.row = row;
  }

  public List<Group> getGroups()
  {
    return this.groups;
  }

  public void setGroups(List<Group> groups)
  {
    this.groups = groups;
  }

  public List<Value> getValues()
  {
    return this.values;
  }

  public void setValues(List<Value> values)
  {
    this.values = values;
  }

  public int hashCode()
  {
    int hash = 3;
    hash = 31 * hash + Objects.hashCode(this.name);
    hash = 31 * hash + Objects.hashCode(this.title);
    hash = 31 * hash + Objects.hashCode(this.column);
    hash = 31 * hash + Objects.hashCode(this.row);
    hash = 31 * hash + Objects.hashCode(this.groups);
    hash = 31 * hash + Objects.hashCode(this.values);
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
    Group other = (Group)obj;
    if(!Objects.equals(this.name, other.name))
    {
      return false;
    }
    if(!Objects.equals(this.title, other.title))
    {
      return false;
    }
    if(!Objects.equals(this.column, other.column))
    {
      return false;
    }
    if(!Objects.equals(this.row, other.row))
    {
      return false;
    }
    if(!Objects.equals(this.groups, other.groups))
    {
      return false;
    }
    if(!Objects.equals(this.values, other.values))
    {
      return false;
    }
    return true;
  }

}
