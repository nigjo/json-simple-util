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

import java.util.Map;
import java.util.Objects;

import de.nigjo.json.util.JSONParameter;

/**
 *
 * @author Jens Hofschröer
 */
public class UndefinedValues
{
  @JSONParameter
  String caption;
  @JSONParameter
  Map<String, Value> values;

  @Override
  public int hashCode()
  {
    int hash = 5;
    hash = 67 * hash + Objects.hashCode(this.caption);
    hash = 67 * hash + Objects.hashCode(this.values);
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
    final UndefinedValues other = (UndefinedValues)obj;
    if(!Objects.equals(this.caption, other.caption))
    {
      return false;
    }
    if(!Objects.equals(this.values, other.values))
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "UndefinedValues{" + "caption=" + caption + ", values=" + values + '}';
  }

}
