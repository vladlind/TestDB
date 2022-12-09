import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;
import static org.testcontainers.shaded.org.hamcrest.Matchers.matchesPattern;
import static org.testcontainers.shaded.org.hamcrest.Matchers.not;

public class SidTest extends DatabaseSetup {

    @Test
    @DisplayName("Check if SID is 20 symbols long containing digits or characters")
    public void sidStructureTest() {
        String str =  selectFromDatabase("SELECT SID FROM COUNTERPARTIES where SHORT_NAME = 'VIKTOR'");
        assertThat(str, matchesPattern("^[A-Za-z0-9]{20}$"));
    }

    @Test
    @DisplayName("Check if SID length is shorter as expected")
    public void innIncorrectLengthTest() {
        insertIntoDatabase("insert into COUNTERPARTIES (SID, INN, OGRN, SHORT_NAME, FULL_NAME, COUNTRY_CD, ADDRESS, PHONE)" +
                " VALUES ('2hd8r948', '594038', NULL, 'VIKTOR', 'VIKTOR VIKTORov', 'RUS', 'Mira, 5', '89219458003')");
        String str = selectFromDatabase("SELECT INN FROM COUNTERPARTIES where SHORT_NAME = 'VIKTOR'");
        assertThat(str, not("^[A-Za-z0-9]{20}$"));
    }

    @Test
    @DisplayName("Check if SID doesn't contain only letters and digits")
    public void innIncorrectSymbolsTest() {
        insertIntoDatabase("insert into COUNTERPARTIES (SID, INN, OGRN, SHORT_NAME, FULL_NAME, COUNTRY_CD, ADDRESS, PHONE)" +
                " VALUES ('2h-8r9 85hf.vl04ifot', '594038', NULL, 'VIKTOR', 'VIKTOR VIKTORov', 'RUS', 'Mira, 5', '89219458003')");
        String str =  selectFromDatabase("SELECT INN FROM COUNTERPARTIES where SID = '3gd8r1111hfkvl04ifog'");
        assertThat(str, not("^[A-Za-z0-9]{20}$"));
    }

    @Test
    @DisplayName("Check if SID longer that 20 symbols will throw SQL exception")
    public void sidLongerThatAllowedTest() {
        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            insertIntoDatabase("insert into COUNTERPARTIES (SID, INN, OGRN, SHORT_NAME, FULL_NAME, COUNTRY_CD, ADDRESS, PHONE)" +
                    " VALUES ('2hd8r9485hfkvl04ifotrdfgt', '594038576755', NULL, 'VIKTOR', 'VIKTOR VIKTORov', 'RUS', 'Mira, 5', '89219458003')");
        });
        Assertions.assertEquals("org.postgresql.util.PSQLException: ERROR: value too long for type character varying(20)",
                thrown.getMessage());
    }
}
