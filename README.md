# json-simple-util

Utilities to map JSON Data to POJOs with [JSON.simple][jsonsimple].

The only annotation needed is `@JSONParameter` on your fields you want to be mapped
to the JSON data. If there are propper setters in the class, these will be used.
Otherwise the data is written to the field directly.

## Sample

`demoproperties.json`:

    {
      "name": "Demodata",
      "properties": [
        {
          "key": "key1",
          "value": "value1"
        },
        {
          "key": "key2",
          "value": "value2"
        }
      ]
    }

`PropertyContainer.class`:

    class PropertyContainer {
      @JSONParameter
      String name;
      @JSONParameter(name = "properties")
      List<PropertyData> data;
    }

`PropertyData.class`:
    
    class PropertyData {
      @JSONParameter
      String key;
      @JSONParameter
      String value;
    }

`parse()`

    PropertyContainer parse(Path infile){
      return JSONMappingManager.parse(infile, PropertyContainer.class);
    }

[jsonsimple]: https://github.com/fangyidong/json-simple
