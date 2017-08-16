/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  de.nigjo.json.util.JSONParameter
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
