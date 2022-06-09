package com.encocns.test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class JsonUtil {

    public static Gson gson = new GsonBuilder()
//                .setLongSerializationPolicy(LongSerializationPolicy.STRING)
                .registerTypeAdapterFactory(new ObjectAdapterFactory())
                .create();
    
    public static JsonParser parser = new JsonParser();

    
    public static class ObjectAdapterFactory<T> implements TypeAdapterFactory {

        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            // 
            
            Class<T> rawType = (Class<T>) type.getRawType();

            //수정
            if( rawType != String.class && rawType != BigDecimal.class  ){
                return null;
            }

            //수정
            if( rawType == BigDecimal.class  ){
                return (TypeAdapter<T>) new BigDecimalAdapter();
            }
            
            return (TypeAdapter<T>) new StringAdapter();
            
        }
        
    }
    
    public static class StringAdapter extends TypeAdapter<String> {

        @Override
        public String read(JsonReader reader) throws IOException {
            if(reader.peek()==JsonToken.NULL) {
                reader.nextNull();
                return "";
            }
            
            return reader.nextString();
        }

        @Override
        public void write(JsonWriter writer, String value) throws IOException {
            if(  value==null ) {
                writer.value("");
                return;
            }
            writer.value(value);
        }
    }
    
    
    public static class BigDecimalAdapter extends TypeAdapter<BigDecimal> {

        @Override
        public BigDecimal read(JsonReader reader) throws IOException {
            
            if(reader.peek()==JsonToken.NULL) {
                return BigDecimal.ZERO;
            }
            
            return new BigDecimal(reader.nextString());
        }

        @Override
        public void write(JsonWriter writer, BigDecimal value) throws IOException {
            if(  value==null ) {
                writer.value("");
                return;
            }
            writer.value(value.toPlainString());
        }
    }
    
    
    public static JsonObject toJsonObject( Object obj ) {
        return parser.parse(gson.toJson(obj)).getAsJsonObject();
    }
    
    public static JsonArray toJsonArray( Object obj ) {
        return parser.parse(gson.toJson(obj)).getAsJsonArray();
    }
    
    public static JsonElement getObject( Object obj ) {
        return gson.toJsonTree(obj);
    }
    
    public static JsonObject toJsonObject( String json ) 
    {
        JsonElement el = parser.parse(json);
        return el.getAsJsonObject();
    }
    
    public static JsonArray toJsonArray( String json ) 
    {
        JsonElement el = parser.parse(json);
        return el.getAsJsonArray();
    }
    
    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap( String json ) 
    {
        Map<String, Object> map = new HashMap<String, Object>();
        return (Map<String, Object>)gson.fromJson(json, map.getClass());
    }
    
    
}
