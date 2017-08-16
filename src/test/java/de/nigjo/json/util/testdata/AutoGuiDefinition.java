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

public class AutoGuiDefinition
{
  @JSONParameter
  private String title;
  @JSONParameter
  private String icon;
  @JSONParameter
  private List<Group> groups;

  public String getTitle()
  {
    return this.title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public String getIcon()
  {
    return this.icon;
  }

  public void setIcon(String icon)
  {
    this.icon = icon;
  }

  public List<Group> getGroups()
  {
    return this.groups;
  }

  public void setGroups(List<Group> groups)
  {
    this.groups = groups;
  }

  public int hashCode()
  {
    int hash = 7;
    hash = 67 * hash + Objects.hashCode(this.title);
    hash = 67 * hash + Objects.hashCode(this.icon);
    hash = 67 * hash + Objects.hashCode(this.groups);
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
    AutoGuiDefinition other = (AutoGuiDefinition)obj;
    if(!Objects.equals(this.title, other.title))
    {
      return false;
    }
    if(!Objects.equals(this.icon, other.icon))
    {
      return false;
    }
    if(!Objects.equals(this.groups, other.groups))
    {
      return false;
    }
    return true;
  }

  public String toString()
  {
    return "{\"title\":\"" + this.getTitle()
        + "\",\"icon\":\"" + this.getIcon()
        + "\",\"groups\": " + AutoGuiDefinition.printArray(this.getGroups()) + "}";
  }

  public static String printArray(List<? extends Object> groups)
  {
    return "["
        + groups.stream()
            .map(Object::toString)
            .reduce("", (o, e) -> o.isEmpty() ? e : o + ',' + e)
        + "]";
  }

}
