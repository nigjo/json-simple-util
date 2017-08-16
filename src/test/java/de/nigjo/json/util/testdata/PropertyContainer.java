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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.nigjo.json.util.JSONParameter;

public class PropertyContainer {

  @JSONParameter
  private String name;
  @JSONParameter(name = "properties")
  private List<PropertyData> data;

  public PropertyContainer() {
  }

  PropertyContainer(String name) {
    this.name = name;
  }

  void addData(PropertyData item) {
    if (data == null) {
      data = new ArrayList<>();
    }
    data.add(item);
  }

  @Override
  public int hashCode() {
    int hash = 7;
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final PropertyContainer other = (PropertyContainer) obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.data, other.data)) {
      return false;
    }
    return true;
  }

}
