package de.nigjo.json.util.test.data;

import de.nigjo.json.util.JSONParameter;
import java.util.Objects;

public class PropertyData {

  @JSONParameter
  private String key;
  @JSONParameter
  private String value;

  public PropertyData() {
  }

  PropertyData(String key, String value) {
    this.key = key;
    this.value = value;
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
    final PropertyData other = (PropertyData) obj;
    if (!Objects.equals(this.key, other.key)) {
      return false;
    }
    if (!Objects.equals(this.value, other.value)) {
      return false;
    }
    return true;
  }

}
