import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;
import static org.testcontainers.shaded.org.hamcrest.Matchers.matchesPattern;

public class OgrnTest extends DatabaseSetup {
    @Test
    @DisplayName("Check if OGRN is present only by companies and INN by physics is 12 digits long")
    public void ogrnOnlyByCompaniesTest() {
        String strFIRMA=  selectFromDatabase("SELECT OGRN FROM COUNTERPARTIES where SHORT_NAME = 'FIRMA-5'");
        String strFIRMAInn=  selectFromDatabase("SELECT INN FROM COUNTERPARTIES where SHORT_NAME = 'FIRMA-5'");
        String strVIKTOROgrn =  selectFromDatabase("SELECT OGRN FROM COUNTERPARTIES where SHORT_NAME = 'VIKTOR'");
        String strVIKTORInn =  selectFromDatabase("SELECT INN FROM COUNTERPARTIES where SHORT_NAME = 'VIKTOR'");
        assertNotNull(strFIRMA);
        assertNull(strVIKTOROgrn);
        assertThat(strFIRMAInn, matchesPattern("^\\d{10}"));
        assertThat(strVIKTORInn, matchesPattern("^\\d{12}"));

    }
}
