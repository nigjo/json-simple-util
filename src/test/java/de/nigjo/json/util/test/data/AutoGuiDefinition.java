package de.nigjo.json.util.test.data;

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
