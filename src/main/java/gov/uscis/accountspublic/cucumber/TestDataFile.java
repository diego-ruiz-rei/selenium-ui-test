package gov.uscis.accountspublic.cucumber;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TestDataFile {
    String[] files();
}