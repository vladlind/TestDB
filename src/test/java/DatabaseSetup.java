import org.junit.jupiter.api.*;

import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.*;
import static org.testcontainers.shaded.org.hamcrest.Matchers.matchesPattern;
import static org.testcontainers.shaded.org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DatabaseSetup extends IntegrationTestBase {

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

}
