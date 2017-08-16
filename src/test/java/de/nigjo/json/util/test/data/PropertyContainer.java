package de.nigjo.json.util.test.data;

import java.util.List;
import java.util.Objects;

import de.nigjo.json.util.JSONParameter;

public class PropertyContainer
{
  @JSONParameter
  String name;
  @JSONParameter(name = "properties")
  List<PropertyData> data;

  @Override
  public int hashCode()
  {
    int hash = 7;
    return hash;
  }

  @Override
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
    if(getClass() != obj.getClass())
    {
      return false;
    }
    final PropertyContainer other = (PropertyContainer)obj;
    if(!Objects.equals(this.name, other.name))
    {
      return false;
    }
    if(!Objects.equals(this.data, other.data))
    {
      return false;
    }
    return true;
  }
  
}
