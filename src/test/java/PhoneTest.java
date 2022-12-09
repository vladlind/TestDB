import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;
import static org.testcontainers.shaded.org.hamcrest.Matchers.matchesPattern;

public class PhoneTest extends DatabaseSetup {
    @Test
    @DisplayName("Check if Phone code is correct")
    public void phoneCodeTest() {
        String strPhoneCode =  selectFromDatabase("SELECT PHN_CD FROM PHONE_CODE where CNTR_CD = 'RUS'");
        assertThat(strPhoneCode, matchesPattern("(7|8)"));
    }

}
