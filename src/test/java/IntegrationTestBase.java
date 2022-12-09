import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.sql.*;

public class IntegrationTestBase {

    private static final String URL = "jdbc:postgresql://localhost:5433/integration-tests-db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "mysecretpassword";
    private static final DockerImageName postgres = DockerImageName.parse("postgres:latest");

    private static final GenericContainer postgreSQLContainer = new PostgreSQLContainer<>(postgres)
            .withDatabaseName("integration-tests-db")
            .withUsername(USER)
            .withPassword(PASSWORD)
            .withInitScript("schema.sql")
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(5433), new ExposedPort(5432)))));


    public String selectFromDatabase(String query) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {
             if (rs.next()) {
                 return rs.getString(query.split(" ")[1]);
             } else {
                 return null;
             }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int insertIntoDatabase(String query) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = con.createStatement()) {
            return st.executeUpdate(query);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    public static void start() {
        postgreSQLContainer.start();
    }

    @AfterAll
    public static void stop() {
        postgreSQLContainer.stop();
    }
}