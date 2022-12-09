import org.junit.jupiter.api.*;

import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.*;
import static org.testcontainers.shaded.org.hamcrest.Matchers.matchesPattern;
import static org.testcontainers.shaded.org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DatabaseTest extends IntegrationTestBase {

    @BeforeAll
    @DisplayName("Preparing test data in Database")
    public void insertTestingDataIntoDatabase() {
        insertIntoDatabase("insert into COUNTRIES (CODE, NAME) VALUES ('RUS', 'Russia')");
        insertIntoDatabase("insert into PHONE_CODE (PHN_CD, CNTR_CD) VALUES ('7', 'RUS')");
        insertIntoDatabase("insert into COUNTRIES (CODE, NAME) VALUES ('BEL', 'Belarus')");
        insertIntoDatabase("insert into PHONE_CODE (PHN_CD, CNTR_CD) VALUES ('33', 'BEL')");
        insertIntoDatabase("insert into COUNTRIES (CODE, NAME) VALUES ('KAZ', 'Kazakhstan')");
        insertIntoDatabase("insert into PHONE_CODE (PHN_CD, CNTR_CD) VALUES ('35', 'KAZ')");
        insertIntoDatabase("insert into COUNTERPARTIES (SID, INN, OGRN, SHORT_NAME, FULL_NAME, COUNTRY_CD, ADDRESS, PHONE)" +
                " VALUES ('2hd8r9485hfkvl04ifot', '594038576743', NULL, 'VIKTOR', 'VIKTOR VIKTORov', 'RUS', 'Mira, 5', NULL)");
        insertIntoDatabase("insert into COUNTERPARTIES (SID, INN, OGRN, SHORT_NAME, FULL_NAME, COUNTRY_CD, ADDRESS, PHONE)" +
                " VALUES ('3gd8r1111hfkvl04isss', '2342345432', 1010234, 'FIRMA-5', 'FIRMA-5 Company', 'RUS', 'Mira, 12', '89039458113')");
        insertIntoDatabase("insert into COUNTERPARTIES (SID, INN, OGRN, SHORT_NAME, FULL_NAME, COUNTRY_CD, ADDRESS, PHONE)" +
                " VALUES ('3gd8r1111hfkvl04ittt', '5342342303', 1010234, 'FIRMA-1', 'FIRMA-1 Company', 'BEL', 'Lenina, 4', '+33919458119')");
    }

    @Test
    @DisplayName("Check if SID is 20 symbols long containing digits or characters")
    public void sidStructureTest() {
        String str =  selectFromDatabase("SELECT SID FROM COUNTERPARTIES where SHORT_NAME = 'VIKTOR'");
        assertThat(str, matchesPattern("[A-Za-z0-9]{20}"));
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

    @Test
    @DisplayName("Check if INN contains only 10 or 12 digits")
    public void innStructureTest() {
        String str =  selectFromDatabase("SELECT INN FROM COUNTERPARTIES where SHORT_NAME = 'VIKTOR'");
        assertThat(str, matchesPattern("^\\d+{10,12}"));
    }

    @Test
    @DisplayName("Check if INN has incorrect length of digits")
    public void innIncorrectLengthTest() {
        insertIntoDatabase("insert into COUNTERPARTIES (SID, INN, OGRN, SHORT_NAME, FULL_NAME, COUNTRY_CD, ADDRESS, PHONE)" +
                " VALUES ('3gd8r1111hfkvl04ifoy', '594038', NULL, 'VIKTOR', 'VIKTOR VIKTORov', 'RUS', 'Mira, 5', '89219458003')");
        String str =  selectFromDatabase("SELECT INN FROM COUNTERPARTIES where SHORT_NAME = 'VIKTOR'");
        assertThat(str, not("^\\d+{10,12}"));
    }

    @Test
    @DisplayName("Check if INN doesn't contain only letters")
    public void innIncorrectSymbolsTest() {
        insertIntoDatabase("insert into COUNTERPARTIES (SID, INN, OGRN, SHORT_NAME, FULL_NAME, COUNTRY_CD, ADDRESS, PHONE)" +
                " VALUES ('3gd8r1111hfkvl04ifog', '.940F23g534_', NULL, 'VIKTOR', 'VIKTOR VIKTORov', 'RUS', 'Mira, 5', '89219458003')");
        String str =  selectFromDatabase("SELECT INN FROM COUNTERPARTIES where SID = '3gd8r1111hfkvl04ifog'");
        assertThat(str, not("^\\d+{10,12}"));
    }

    @Test
    @DisplayName("Check if OGRN is present only by companies and INN by physics is 12 digits long")
    public void ogrnOnlyByCompaniesTest() {
        String strFIRMA=  selectFromDatabase("SELECT OGRN FROM COUNTERPARTIES where SHORT_NAME = 'FIRMA-5'");
        String strVIKTOROgrn =  selectFromDatabase("SELECT OGRN FROM COUNTERPARTIES where SHORT_NAME = 'VIKTOR'");
        String strVIKTORInn =  selectFromDatabase("SELECT INN FROM COUNTERPARTIES where SHORT_NAME = 'VIKTOR'");
        assertNotNull(strFIRMA);
        assertNull(strVIKTOROgrn);
        assertThat(strVIKTORInn, matchesPattern("^\\d{12}"));

    }

    @Test
    @DisplayName("Check if Short_name has correct structure")
    public void shortNameStructureTest() {
        String strViktor =  selectFromDatabase("SELECT INN FROM COUNTERPARTIES where SHORT_NAME = 'VIKTOR'");
        String strFirma=  selectFromDatabase("SELECT OGRN FROM COUNTERPARTIES where SHORT_NAME = 'FIRMA-5'");
        assertThat(strViktor, matchesPattern("[A-Z0-9\\-\\']{0,20}"));
        assertThat(strFirma, matchesPattern("[A-Z0-9\\-\\']{0,20}"));
    }

    @Test
    @DisplayName("Check if Phone code is correct")
    public void phoneCodeTest() {
        String strPhoneCode =  selectFromDatabase("SELECT PHN_CD FROM PHONE_CODE where CNTR_CD = 'RUS'");
        assertThat(strPhoneCode, matchesPattern("(7|8)"));
    }

}
