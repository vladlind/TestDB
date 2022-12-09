import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;
import static org.testcontainers.shaded.org.hamcrest.Matchers.matchesPattern;
import static org.testcontainers.shaded.org.hamcrest.Matchers.not;

public class InnTest extends DatabaseSetup {

    @Test
    @DisplayName("Check if INN contains only 10 or 12 digits")
    public void innStructureTest() {
        String str =  selectFromDatabase("SELECT INN FROM COUNTERPARTIES where SHORT_NAME = 'VIKTOR'");
        assertThat(str, matchesPattern("^(\\d{10}|\\d{12})"));
    }

    @Test
    @DisplayName("Check if INN has incorrect length of digits")
    public void innIncorrectLengthTest() {
        insertIntoDatabase("insert into COUNTERPARTIES (SID, INN, OGRN, SHORT_NAME, FULL_NAME, COUNTRY_CD, ADDRESS, PHONE)" +
                " VALUES ('3gd8r1111hfkvl04ifoy', '59403834567', NULL, 'VIKTOR', 'VIKTOR VIKTORov', 'RUS', 'Mira, 5', '89219458003')");
        String str =  selectFromDatabase("SELECT INN FROM COUNTERPARTIES where SHORT_NAME = 'VIKTOR'");
        assertThat(str, not("^(\\d{10}|\\d{12})"));
    }

    @Test
    @DisplayName("Check if INN doesn't contain only letters")
    public void innIncorrectSymbolsTest() {
        insertIntoDatabase("insert into COUNTERPARTIES (SID, INN, OGRN, SHORT_NAME, FULL_NAME, COUNTRY_CD, ADDRESS, PHONE)" +
                " VALUES ('3gd8r1111hfkvl04ifog', '.940F23g534_', NULL, 'VIKTOR', 'VIKTOR VIKTORov', 'RUS', 'Mira, 5', '89219458003')");
        String str =  selectFromDatabase("SELECT INN FROM COUNTERPARTIES where SID = '3gd8r1111hfkvl04ifog'");
        assertThat(str, not("^\\d{10,12}"));
    }
}
