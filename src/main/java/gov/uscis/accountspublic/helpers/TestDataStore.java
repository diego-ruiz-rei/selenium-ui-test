package gov.uscis.accountspublic.helpers;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestDataStore {

    private static Logger logger = LoggerFactory.getLogger(TestDataStore.class);
    private static Map <String, Object> dataStore;

    static {
        if ( dataStore == null ) dataStore = new HashMap<>();
    }

    private TestDataStore () {}

    public static void storeObject ( String key, Object value ) {
        dataStore.put(key, value);
        logger.info("{} : {} - stored in Data Store", key, value);
    }

    public static Object getValueAsObject ( String key ) {
        Object object = null;
        if ( isObjectKey( key ) ) object = dataStore.get(key);
        else Assert.fail("Object \"" + key + "\" was not found");
        return object;
    }

    public static String getValueAsString ( String key ) {
        Object object = getValueAsObject(key);
        return object != null ? object.toString() : "";
    }

    public static int getValueAsInt ( String key ) { return Integer.parseInt(getValueAsString(key)); }

    @SuppressWarnings("unchecked")
    public static List<String> getValueAsList (String key ) { return (List) getValueAsObject(key); }

    public static double getValueAsDouble ( String key ) { return Double.parseDouble(getValueAsString(key)); }

    public static boolean isObjectKey ( String key ) { return dataStore.get(key) != null; }

    public static void clearTestData () { dataStore.clear(); }
}
