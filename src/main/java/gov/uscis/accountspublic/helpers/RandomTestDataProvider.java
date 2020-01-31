package gov.uscis.accountspublic.helpers;

import com.github.javafaker.Faker;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

public class RandomTestDataProvider {
    private static Logger logger = LoggerFactory.getLogger(RandomTestDataProvider.class);
    private static Faker faker = new Faker(new Locale("en-US"));

    private RandomTestDataProvider () {}

    static void deleteDirectoryFiles ( String directoryPath ) {
        File directory = new File(System.getProperty("user.dir") + (directoryPath.startsWith("/") ? directoryPath : "/" + directoryPath));
        try {
            FileUtils.deleteDirectory(directory);
        } catch ( IOException e ) {
            logger.error(e.getMessage());
        }
        boolean isDirectoryFile = directory.exists();
        if (isDirectoryFile) {
            File[] files = directory.listFiles();
            if (files != null && files.length > 0) {
                Arrays.stream(files).forEachOrdered(file -> {
                    boolean isFileDelete = file.delete();
                    if (isFileDelete)
                        logger.info("File \"{}\" deleted \" in {}\" directory", file.getName(), directory);
                });
            }
        }
    }

    /**
     * http://tutorials.jenkov.com/java-internationalization/simpledateformat.html
     * @param dateFormat - dateFormat
     * @return Current date text
     */
    public static String getCurrentDate ( String dateFormat ) {
        Date date = Calendar.getInstance().getTime();
        return new SimpleDateFormat(dateFormat).format(date);
    }

    /**
     * @param dateFormat - dateFormat
     * @param days - days
     * @return - Today minus days
     */
    public static String getPastDate ( String dateFormat, int days ) { return getFutureDate(dateFormat, -days); }

    /**
     * @param dateFormat - dateFormat
     * @param days - days
     * @return - Today plus days
     */
    public static String getFutureDate ( String dateFormat, int days ) {
        Date date = DateUtils.addDays(new Date(), days);
        return new SimpleDateFormat(dateFormat).format(date);
    }

    /**
     * Ex.: changeDateFormat("8/1/2019, 4:01:43 PM", "M/d/yyyy, h:mm:ss a", "M/d/yyyy");
     * @param date - date
     * @param oldFormat - oldFormat
     * @param newFormat - newFormat
     * @return - Formatted date text
     */
    public static String changeDateFormat ( String date, String oldFormat, String newFormat ) {
        String changedDateFormat;
        try {
            changedDateFormat = new SimpleDateFormat(newFormat).format(new SimpleDateFormat(oldFormat).parse(date));
        } catch ( ParseException e ) {
            String errorMessage = "Could not change date format from " + oldFormat + " to " + newFormat + " for " + date;
            logger.error(errorMessage);
            changedDateFormat = errorMessage;
        }
        logger.info("{} date format is changed from {} to {}", date, oldFormat, newFormat);
        return changedDateFormat;
    }

    public static String getRandomText ( int size, boolean isCharacter, boolean isNumber, boolean isSymbols ) {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder1 = new StringBuilder();
        String characters = "abcdefghijklmnopqrstuvwxyz";
        String symbols = "~!@#$%^&*()_+{}[]|\\\"':;/?.>,<`";
        String numbers = "0123456789";

        if ( isCharacter ) stringBuilder1.append(characters);
        if ( isSymbols ) stringBuilder1.append(symbols);
        if ( isNumber ) stringBuilder1.append(numbers);

        if ( size < 0 || size > stringBuilder1.length() ) {
            String errorMessage = "Invalid size " + size + " for Random Text Generator\nSize should be > 0 && <= " + stringBuilder1.length();
            logger.error(errorMessage);
            Common.failTest(errorMessage);
        }
        for ( int index = 0; index < size; index++ ) {
            int randomIndex = getRandomInt(0, stringBuilder1.length() - 1);
            stringBuilder.append(stringBuilder1.charAt(randomIndex));
        }
        return stringBuilder.toString();
    }

    public static int getRandomInt ( int min, int max ) {
        return (int)(Math.random() * (( max - min ) + 1 )) + min;
    }

    public static long getRandomLong ( long min, long max ) { return (long)(Math.random() * (( max - min ) + 1 )) + min; }

    public static double getRandomDouble ( double min, double max ) { return (Math.random() * (( max - min ) + 1 )) + min; }

    public static String formatDouble2D ( double number ) {
        return String.format("%.2f", number);
    }

    public static void keyboardKey ( int keyEvent ) {
        Robot robot = null;
        try {
            robot = new Robot();
            robot.keyPress(keyEvent);
            robot.keyRelease(keyEvent);
        } catch ( AWTException e ) {
            logger.error(e.getMessage());
        }
    }

    public static String getRandomFirstName () { return faker.name().firstName(); }

    public static String getRandomLastName () {
        return faker.name().lastName();
    }

    public static String getRandomName () { return faker.name().name(); }

    public static String getRandomEmailCompany () {
        List<String> emailCompanies = Arrays.asList("@gmail.com", "@yahoo.com", "@semut.net", "@lectuspede.edu", "@necluctus.org", "@posuereenim.com");
        return emailCompanies.get(getRandomInt(0, emailCompanies.size() - 1));
    }

    public static String getRandomEmailAddress ( String firstName, String lastName ) { return (firstName + "." + lastName + getRandomEmailCompany()).toLowerCase(); }

    public static String getRandomEmailAddress ( String name ) {
        name = name.replace("`", "").replace("'", "");
        return name.toLowerCase() + getRandomEmailCompany();
    }

    public static String getRandomPhoneNumber () { return faker.phoneNumber().cellPhone(); }

    public static String getRandomAddress1 () { return faker.address().streetAddress(); }

    public static String getRandomAddress2 () { return faker.address().secondaryAddress(); }

    public static String getRandomCompany () { return faker.company().name(); }

    public static String getRandomCountry () { return faker.address().country(); }

    public static String getRandomState () { return faker.address().state(); }

    public static String getRandomStateCode () { return getStateCode(getRandomState()); }

    public static String getRandomCity () {
        return faker.address().city();
    }

    public static String getRandomText ( int numberOfCharacters ) {
        if ( numberOfCharacters <= 0 ) {
            Common.failTest("number of characters for random text can not be <= 0");
        }
        StringBuilder stringBuilder = new StringBuilder();
        boolean isAddParagraph = true;
        while ( isAddParagraph ) {
            int randomTextLength = stringBuilder.append(faker.lorem().paragraph()).length();
            isAddParagraph = randomTextLength < numberOfCharacters;
        }
        return stringBuilder.substring(0, numberOfCharacters).replaceAll("\\s+", " ").trim();
    }

    public static String getUsCurrency ( double dollarAmount ) { return NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(dollarAmount); }

    public static String getAbsoluteFilePath ( String relativeFilePath ) {
        relativeFilePath = relativeFilePath.startsWith("/") ? relativeFilePath : "/" + relativeFilePath;
        return System.getProperty("user.dir") + relativeFilePath;
    }

    public static String getFileName ( String relativeFilePath ) { return Paths.get(getAbsoluteFilePath(relativeFilePath)).getFileName().toString(); }

    public static List<String> jsonArrayToListString (JsonArray jsonArray ) {
        List<String> list = new ArrayList<>();
        jsonArray.forEach(jsonElement -> list.add(jsonElement.getAsString()));
        return list;
    }

    public static List<JsonElement> jsonArrayToList (JsonArray jsonArray ) {
        List<JsonElement> list = new ArrayList<>();
        jsonArray.forEach(list::add);
        return list;
    }

    static void setSpecName ( String specName ) {
        TestDataStore.storeObject("spec name", specName);
        logger.info("Spec Name set to {}", specName);
    }

    static String getSpecName () { return TestDataStore.getValueAsString("spec name"); }

    static void setScenarioName ( String scenarioName ) {
        TestDataStore.storeObject("scenario name", scenarioName);
        logger.info("Scenario Name set to {}", scenarioName);
    }

    static String getScenarioName () { return TestDataStore.getValueAsString("scenario name"); }

    static void setStepName ( String stepName ) {
        TestDataStore.storeObject("step name", stepName);
        logger.info("Step Name set to {}", stepName);
    }

    static String getStepName () { return TestDataStore.getValueAsString("step name"); }

    private static String getStateCode ( String state ) {
        Map<String, String> states = new HashMap<>();
        states.put("Alabama", "AL");
        states.put("Alaska", "AK");
        states.put("Alberta", "AB");
        states.put("American Samoa", "AS");
        states.put("Arizona", "AZ");
        states.put("Arkansas", "AR");
        states.put("Armed Forces (AE)", "AE");
        states.put("Armed Forces Americas", "AA");
        states.put("Armed Forces Pacific", "AP");
        states.put("British Columbia", "BC");
        states.put("California", "CA");
        states.put("Colorado", "CO");
        states.put("Connecticut", "CT");
        states.put("Delaware", "DE");
        states.put("District Of Columbia", "DC");
        states.put("Florida", "FL");
        states.put("Georgia", "GA");
        states.put("Guam", "GU");
        states.put("Hawaii", "HI");
        states.put("Idaho", "ID");
        states.put("Illinois", "IL");
        states.put("Indiana", "IN");
        states.put("Iowa", "IA");
        states.put("Kansas", "KS");
        states.put("Kentucky", "KY");
        states.put("Louisiana", "LA");
        states.put("Maine", "ME");
        states.put("Manitoba", "MB");
        states.put("Maryland", "MD");
        states.put("Massachusetts", "MA");
        states.put("Michigan", "MI");
        states.put("Minnesota", "MN");
        states.put("Mississippi", "MS");
        states.put("Missouri", "MO");
        states.put("Montana", "MT");
        states.put("Nebraska", "NE");
        states.put("Nevada", "NV");
        states.put("New Brunswick", "NB");
        states.put("New Hampshire", "NH");
        states.put("New Jersey", "NJ");
        states.put("New Mexico", "NM");
        states.put("New York", "NY");
        states.put("Newfoundland", "NF");
        states.put("North Carolina", "NC");
        states.put("North Dakota", "ND");
        states.put("Northwest Territories", "NT");
        states.put("Nova Scotia", "NS");
        states.put("Nunavut", "NU");
        states.put("Ohio", "OH");
        states.put("Oklahoma", "OK");
        states.put("Ontario", "ON");
        states.put("Oregon", "OR");
        states.put("Pennsylvania", "PA");
        states.put("Prince Edward Island", "PE");
        states.put("Puerto Rico", "PR");
        states.put("Quebec", "QC");
        states.put("Rhode Island", "RI");
        states.put("Saskatchewan", "SK");
        states.put("South Carolina", "SC");
        states.put("South Dakota", "SD");
        states.put("Tennessee", "TN");
        states.put("Texas", "TX");
        states.put("Utah", "UT");
        states.put("Vermont", "VT");
        states.put("Virgin Islands", "VI");
        states.put("Virginia", "VA");
        states.put("Washington", "WA");
        states.put("West Virginia", "WV");
        states.put("Wisconsin", "WI");
        states.put("Wyoming", "WY");
        states.put("Yukon Territory", "YT");
        return states.get(state);
    }
}
