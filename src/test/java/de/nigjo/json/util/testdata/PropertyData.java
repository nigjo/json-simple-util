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

public class PropertyData
{

  @JSONParameter
  private String key;
  @JSONParameter
  private String value;

  public PropertyData()
  {
  }

  PropertyData(String key, String value)
  {
    this.key = key;
    this.value = value;
  }

  public void setValue(String value)
  {
    this.value = value;
  }

  public String getValue()
  {
    return value;
  }

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
    final PropertyData other = (PropertyData)obj;
    if(!Objects.equals(this.key, other.key))
    {
      return false;
    }
    if(!Objects.equals(this.value, other.value))
    {
      return false;
    }
    return true;
  }

}
