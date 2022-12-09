import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;
import static org.testcontainers.shaded.org.hamcrest.Matchers.matchesPattern;

public class ShortNameTest extends DatabaseSetup {

    @Test
    @DisplayName("Check if Short_name has correct structure")
    public void shortNameStructureTest() {
        String strViktor =  selectFromDatabase("SELECT INN FROM COUNTERPARTIES where SHORT_NAME = 'VIKTOR'");
        String strFirma=  selectFromDatabase("SELECT OGRN FROM COUNTERPARTIES where SHORT_NAME = 'FIRMA-5'");
        assertThat(strViktor, matchesPattern("[A-Z0-9\\-\\']{0,20}"));
        assertThat(strFirma, matchesPattern("[A-Z0-9\\-\\']{0,20}"));
    }
}
