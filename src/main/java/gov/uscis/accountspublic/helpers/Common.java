package gov.uscis.accountspublic.helpers;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Common {

    private static Logger logger = LoggerFactory.getLogger(Common.class);

    private Common () {}

//    /**
//     * Stops Web Driver for seconds
//     * @param seconds - seconds
//     */
//    public static void sleep ( int seconds ) {
//        try {
//            logger.info("Thread sleep for {} seconds", seconds);
//            Thread.sleep( (1000 * seconds) );
//        } catch ( Exception e ) {
//            logger.error(e.getMessage());
//        }
//    }

    public static JsonElement toJson (String parser ) {
        JsonParser jsonParser = new JsonParser();
        return jsonParser.parse(parser);
    }

    /**
     * @author erlan.beisen
     * @param errorMessage - errorMessage
     */
    public static void failTest ( String errorMessage ) {
        logger.error(errorMessage);
        Assert.fail(errorMessage);
    }
}
