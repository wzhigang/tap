package com.tapmobi.common.util;

import java.lang.reflect.Field;  
import java.lang.reflect.Method;  
import java.util.ArrayList;
import java.util.List;  
import java.lang.reflect.Type;
import java.util.Arrays;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public class JSONUtil {  
    public static <T> String fromList(List<T> list) throws Throwable {  
        StringBuilder json = new StringBuilder();  
          
        if (list == null || list.size() == 0) {  
            return null;  
        }  
          
        json.append("[");  
          
        for(int i = 0; i < list.size(); i++) {  
            json.append("{");  
            T t = list.get(i);  
            Class clazz = t.getClass();  
            Field[] fields = t.getClass().getDeclaredFields();
            for(int j=0; j<fields.length; j++) {  
                Field field = fields[j];  
                String strFields = field.getName();  
                String getMethodName = "get"+ strFields.substring(0, 1).toUpperCase() + strFields.substring(1);  
                Method method =clazz.getMethod(getMethodName, new Class[]{});  
                Object value = method.invoke(t, new Object[]{});  
                json.append("\"" + strFields + "\"" + ":" + "\"" + value + "\""); 
                if (j < fields.length - 1) {  
                    json.append(",");  
                }  
            }  
            json.append("}");  
            if (i < list.size() - 1) {  
                json.append(",");  
            }  
        }  
        json.append("]");  
        return json.toString();  
    }  
    
    public static Object getInstanceByJson(Class<?> clazz, String json)
    {
        Object obj = null;
        Gson gson = new Gson();
        obj = gson.fromJson(json, clazz);
        return obj;
    }

    /**
     * @param json
     * @param clazz
     * @return
     */
    public static <T> List<T> jsonToList(String json, Class<T[]> clazz)
    {
        Gson gson = new Gson();
        T[] array = gson.fromJson(json, clazz);
        return Arrays.asList(array);
    }

    /**
     * @param json
     * @param clazz
     * @return
     */
    public static <T> ArrayList<T> jsonToArrayList(String json, Class<T> clazz)
    {
        Type type = new TypeToken<ArrayList<JsonObject>>()
        {}.getType();
        ArrayList<JsonObject> jsonObjects = new Gson().fromJson(json, type);

        ArrayList<T> arrayList = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects)
        {
            arrayList.add(new Gson().fromJson(jsonObject, clazz));
        }
        return arrayList;
    }
      
}  
