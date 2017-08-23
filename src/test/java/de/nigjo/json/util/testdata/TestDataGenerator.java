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

import java.util.Arrays;
import java.util.LinkedHashMap;

public class TestDataGenerator
{
  public static AutoGuiDefinition createExpectedDemoResult()
  {
    AutoGuiDefinition definition = new AutoGuiDefinition();
    definition.setTitle("Demonstration JSON AutoGUI Prototyp");
    definition.setIcon("selfmade.punkt.blau");

    Group gruppe1 = new Group();
    gruppe1.setName("Gruppe 1");
    gruppe1.setColumn("links");
    gruppe1.setRow("oben");

    Value aktiv = new Value();
    aktiv.setName("Aktiv");
    aktiv.setType("boolean");
    aktiv.setValue(true);

    Value wert1 = new Value();
    wert1.setName("Wert 1");
    wert1.setType("int");
    wert1.setValue(42);

    Value wert2 = new Value();
    wert2.setName("Wert 2");
    wert2.setType("components");

    RowComponent mainlabel = new RowComponent();
    mainlabel.setColumn("mainlabel");
    mainlabel.setType("label");
    mainlabel.setValue("Wert 2");

    RowComponent shortsign = new RowComponent();
    shortsign.setColumn("shortsign");
    shortsign.setType("label");
    shortsign.setValue("\u00b5");

    RowComponent input = new RowComponent();
    input.setColumn("input");
    input.setType("float");
    input.setValue(23.45);

    RowComponent unit = new RowComponent();
    unit.setColumn("unit");
    unit.setType("label");
    unit.setValue("[\u00b0]");

    wert2.setComponents(Arrays.asList(mainlabel, shortsign, input, unit));

    gruppe1.setValues(Arrays.asList(aktiv, wert1, wert2));

    Group gruppe2 = new Group();
    gruppe2.setName("Gruppe 2");
    gruppe2.setColumn("links");
    gruppe2.setRow("unten");

    Group vornamen = new Group();
    vornamen.setName("Vornamen");
    vornamen.setColumn("unten_links");
    vornamen.setRow("unten_1");

    Value klaus = new Value();
    klaus.setName("Name A");
    klaus.setType("string");
    klaus.setValue("Klaus");

    Value peter = new Value();
    peter.setName("Name B");
    peter.setType("string");
    peter.setValue("Peter");

    vornamen.setValues(Arrays.asList(klaus, peter));

    Group nachnamen = new Group();
    nachnamen.setName("Nachnamen");
    nachnamen.setColumn("unten_rechts");
    nachnamen.setRow("unten_1");

    Value mueller = new Value();
    mueller.setName("deutsch");
    mueller.setType("string");
    mueller.setValue("M\u00fcller");

    Value miller = new Value();
    miller.setName("english");
    miller.setType("string");
    miller.setValue("Miller");

    nachnamen.setValues(Arrays.asList(mueller, miller));

    gruppe2.setGroups(Arrays.asList(vornamen, nachnamen));

    definition.setGroups(Arrays.asList(gruppe1, gruppe2));

    return definition;
  }

  public static PropertyContainer createReadmeDemoResult()
  {
    PropertyContainer c = new PropertyContainer("Demodata");
    c.addData(new PropertyData("key1", "value1"));
    c.addData(new PropertyData("key2", "value2"));
    return c;
  }

  private TestDataGenerator()
  {
  }

  public static UndefinedValues createMappedDemoResult()
  {
    UndefinedValues values = new UndefinedValues();

    values.caption = "testdata";
    Value wert1 = new Value();
    wert1.setName("Wert 1");
    wert1.setValue(42);
    wert1.setType("int");
    Value aktiv = new Value();
    aktiv.setName("Aktiv");
    aktiv.setValue(true);
    aktiv.setType("boolean");
    Value unsinn = new Value();

    values.values = new LinkedHashMap<>();
    values.values.put("wert1", wert1);
    values.values.put("Aktiv", aktiv);
    values.values.put("Unsinn", unsinn);

    return values;
  }

}
