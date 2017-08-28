package storage;

import com.google.gson.JsonParser;
import org.json.JSONObject;
import utils.Utils;

import java.io.*;
import java.util.HashMap;

public class FileDriver {
    //FILEDRIVE IS NOT TRANSLATED - CAUSES ERRORS BECAUSE LANGUAGE IS NOT LOADED
    public String CONFIG = "config.json";

    private static FileDriver instance;
    private static File file;
    private static JSONObject config;

    /**
     * Get Instance
     *
     * @return Class Instance
     */
    public static FileDriver getInstance() {
        if (instance == null) {
            instance = new FileDriver();
        }
        return instance;
    }

    public boolean checkIfFileExists(String filename) {
        return file != null && file.exists();
    }

    public boolean checkIfFileisEmpty(String filename) {
        return config == null && config.keySet().size() == 0;
    }

    /**
     * Create new File
     */
    public void createNewFile() {
        try {
            String[] parts = CONFIG.split("/");
            for (int i = 0; i < parts.length - 1; i++) {
                File file = new File(parts[i]);
                if (!file.exists()) {
                    if (!file.mkdir()) {
                        System.out.println("Cant create Folder: " + file.getAbsolutePath());
                    }
                }
            }
            file = new File(CONFIG);
            if (!file.exists()) {
                if (file.createNewFile()) {
                    System.out.println(CONFIG + " created at " + file.getAbsolutePath());
                }
            } else {
                System.out.println(CONFIG + " loaded at " + file.getAbsolutePath());
            }
            loadJson();
        } catch (Exception ex) {
            System.out.println("File can not be accessed: " + CONFIG);
            ex.printStackTrace();
        }
    }

    /**
     * Parse the String to a JSONObject
     *
     * @param string Content
     * @return Jsonobject
     */
    private JSONObject parseJson(String string) {
        JSONObject json = new JSONObject();
        try {
            if (!string.equals("")) {
                JsonParser jsonParser = new JsonParser();
                json = new JSONObject(jsonParser.parse(string).getAsJsonObject().toString());
            }
        } catch (Exception ex) {
            System.out.println("Parsing error");
            ex.printStackTrace();
        }
        return json;
    }

    /**
     * Load Json from File
     */
    public void loadJson() {
        try {
            System.out.println("===LOADFILES===");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            config = parseJson(content.toString());

        } catch (Exception ex) {
            System.out.println("File can not be loaded");
            ex.printStackTrace();
        }
    }

    /**
     * Save Json to File
     */
    public void saveJson() {
        try {
            System.out.println("===SAVEFILES===");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            String json = config.toString();
            json = Utils.crunchifyPrettyJSONUtility(json);
            writer.write(json);
            writer.close();
        } catch (Exception ex) {
            System.out.println("File can not be saved");
            ex.printStackTrace();
        }
    }

    /**
     * Set A property in a specific file
     *
     * @param option option
     * @param value  value
     */
    public void setProperty(String option, Object value) {
        try {
            if (config != null) {
                if (config.has(option)) {
                    removeProperty(option);
                }
                config.put(option, value);
            } else {
                config = new JSONObject();
                config.put(option, value);
            }
        } catch (Exception ex) {
            System.out.println("Can not set Property: ");
            ex.printStackTrace();
        }
    }

    /**
     * Get Property of File with defaultvalue
     *
     * @param option       option
     * @param defaultvalue defaultvalue
     * @return value
     */
    public Object getProperty(String option, Object defaultvalue) {
        try {
            if (config == null || !config.has(option)) {
                setProperty(option, defaultvalue);
            }
        } catch (Exception ex) {
            setProperty(option, defaultvalue);
            ex.printStackTrace();
        }
        return config.get(option);
    }

    public boolean hasKey(String filename, String option) {
        try {
            return config.has(option);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Get Property of File but without a Default Value
     *
     * @param filename filename
     * @param option   option
     * @return value
     */
    public Object getPropertyOnly(String option) {
        if (config.has(option)) {
            return config.get(option);
        } else {
            return null;
        }

    }

    /**
     * Removes A property in a specific file
     *
     * @param option option
     */
    public void removeProperty(String option) {
        try {
            if (config.has(option)) {
                config.remove(option);
            }
        } catch (Exception ex) {
            System.out.println("Can not remove Property: ");
            ex.printStackTrace();
        }
    }

    public HashMap<String, Object> getAllKeysWithValues() {
        HashMap<String, Object> objects = new HashMap<>();
        try {
            for (Object key : config.keySet()) {
                objects.put(key.toString(), config.get(key.toString()));
            }
        } catch (Exception ex) {
            System.out.println("Can not list Property: ");
            ex.printStackTrace();
        }
        return objects;
    }


}
