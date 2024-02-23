package be.howest.ti.adria.logic.controller;

import be.howest.ti.adria.logic.data.Repositories;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseControllerTest {

    private static final String URL = "jdbc:h2:./db-10";

    @BeforeAll
    void setupTestSuite() {
        Repositories.getInstance().shutdown();
        JsonObject dbProperties = new JsonObject(Map.of("url", "jdbc:h2:./db-10",
                "username", "",
                "password", "",
                "webconsole.port", 9000));
        Repositories.getInstance().configure(dbProperties);
    }

    @BeforeEach
    void setupTest() {
        Repositories.getInstance().getConnectionFactory().generateData();
    }

}
