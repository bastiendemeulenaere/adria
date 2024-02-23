package be.howest.ti.adria.logic.data;

import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.BeforeEach;

import java.util.Map;

class AdriaH2RepositoryTest {
    private static final String URL = "jdbc:h2:./db-10";

    @BeforeEach
    void setupTestSuite() {
        Repositories.getInstance().shutdown();
        JsonObject dbProperties = new JsonObject(Map.of("url",URL,
                "username", "",
                "password", "",
                "webconsole.port", 9000 ));
        Repositories.getInstance().configure(dbProperties);
    }

}
