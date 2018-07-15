package keker.cetaki;

import java.util.HashMap;

public class Config {

    private static Config instance;
    private HashMap<String, Object> map;

    /*Keep doc about keys and its purpose if needed*/

    public static final String TOKEN = "token";
    public static final String USER = "User";
    public static final String RME = "Rme";

    /** A bean with A LOT of useful user info */ 
    public static final String USER_BEAN = "userBean";

    private Config() {
        map = new HashMap<String, Object>();
    }

    public static final Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public void setKey(String key, Object value) {
        map.put(key, value);
    }

    public Object getKey(String key) {
        return map.get(key);
    }
}
