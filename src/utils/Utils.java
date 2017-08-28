package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by ModdyLP on 10.08.2017. Website: https://moddylp.de/
 */
public class Utils {
    public static int runden(int s, int v){
        double z = 0;
        z = s - s*v/20;
        z = Math.round(z);
        s = (int)z ;
        if (s<1){
            s=1;
        }
        return s;

    }
    public static String crunchifyPrettyJSONUtility(String simpleJSON) {
        JsonParser crunhifyParser = new JsonParser();
        JsonObject json = crunhifyParser.parse(simpleJSON).getAsJsonObject();

        Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();

        return prettyGson.toJson(json);
    }
    public static JSONArray objectToJSONArray(Object object){
        Object json = null;
        JSONArray jsonArray = null;
        try {
            json = new JSONTokener(object.toString()).nextValue();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (json instanceof JSONArray) {
            jsonArray = (JSONArray) json;
        }
        return jsonArray;
    }
    public static JSONObject objectToJSONObject(Object object){
        Object json = null;
        JSONObject jsonObject = null;
        try {
            json = new JSONTokener(object.toString()).nextValue();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (json instanceof JSONObject) {
            jsonObject = (JSONObject) json;
        }
        return jsonObject;
    }
}
