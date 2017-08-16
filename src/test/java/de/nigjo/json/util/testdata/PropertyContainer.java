package de.nigjo.json.util.testdata;

import de.nigjo.json.util.JSONParameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
