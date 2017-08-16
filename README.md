# JSON.simple Utils

Utilities to map JSON Data to POJOs with [JSON.simple][jsonsimple].

The only annotation needed is `@JSONParameter` on your fields you want to be mapped
to the JSON data. If there are propper setters in the class, these will be used.
Otherwise the data is written to the field directly.

> The current version is not fully JSON complient and in alpha phase.

> This is a lightweight library to process JSON files. If you wish to do more fancy
> stuff refere to the [Jackson][jackson] Suite.

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
      private String name;
      @JSONParameter(name = "properties")
      private List<PropertyData> data;
    }

`PropertyData.class`:
    
    class PropertyData {
      @JSONParameter
      private String key;
      @JSONParameter
      private String value;
    }

`parse()`:

    PropertyContainer parse(Path infile){
      return JSONUtilities.parse(infile, PropertyContainer.class);
    }

[simpleutils]: https://github.com/nigjo/json-simple-util
[jsonsimple]: https://github.com/fangyidong/json-simple
[jackson]: https://github.com/FasterXML
