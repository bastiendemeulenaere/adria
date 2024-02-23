//package be.howest.ti.adria.web;
//
//import be.howest.ti.adria.logic.controller.MockCategoryController;
//import be.howest.ti.adria.logic.data.Repositories;
//import be.howest.ti.adria.web.bridge.OpenApiBridge;
//import be.howest.ti.adria.web.bridge.RtcBridge;
//import io.vertx.core.Vertx;
//import io.vertx.core.buffer.Buffer;
//import io.vertx.core.json.JsonObject;
//import io.vertx.ext.web.client.HttpResponse;
//import io.vertx.ext.web.client.WebClient;
//import io.vertx.junit5.VertxExtension;
//import io.vertx.junit5.VertxTestContext;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.platform.commons.util.StringUtils;
//
//import java.util.concurrent.CompletableFuture;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@ExtendWith(VertxExtension.class)
//@SuppressWarnings({"PMD.JUnitTestsShouldIncludeAssert","PMD.AvoidDuplicateLiterals"})
///*
// * PMD.JUnitTestsShouldIncludeAssert: VertxExtension style asserts are marked as false positives.
// * PMD.AvoidDuplicateLiterals: Should all be part of the spec (e.g., urls and names of req/res body properties, ...)
// */
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class OpenAPITest {
//
//    private static final int PORT = 8080;
//    private static final String HOST = "localhost";
//    public static final String MSG_200_EXPECTED = "If all goes right, we expect a 200 status";
//    public static final String MSG_201_EXPECTED = "If a resource is successfully created.";
//    public static final String MSG_204_EXPECTED = "If a resource is successfully deleted";
//    private Vertx vertx;
//    private WebClient webClient;
//
//    @BeforeAll
//    void deploy(final VertxTestContext testContext) {
//        Repositories.shutdown();
//        vertx = Vertx.vertx();
//
//        WebServer webServer = new WebServer(new OpenApiBridge(new MockCategoryController()), new RtcBridge());
//        vertx.deployVerticle(
//                webServer,
//                testContext.succeedingThenComplete()
//        );
//        webClient = WebClient.create(vertx);
//    }
//
//    @AfterAll
//    void close(final VertxTestContext testContext) {
//        vertx.close(testContext.succeedingThenComplete());
//        webClient.close();
//        Repositories.shutdown();
//    }
//
//    @Test
//    void getQuote(final VertxTestContext testContext) {
//        webClient.get(PORT, HOST, "/api/quotes/1")
//                .putHeader("Authorization", createJWT())
//                .send()
//                .onFailure(testContext::failNow)
//                .onSuccess(response -> testContext.verify(() -> {
//                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
//                    assertTrue(
//                            StringUtils.isNotBlank(response.bodyAsJsonObject().getString("value")),
//                            "Empty quotes are not allowed"
//                    );
//                    testContext.completeNow();
//                }));
//    }
//
//
//
//    @Test
//    void getQuoteWithInvalidID(final VertxTestContext testContext) {
//        webClient.get(PORT, HOST, "/api/quotes/333")
//                .putHeader("Authorization", createJWT())
//                .send()
//                .onFailure(testContext::failNow)
//                .onSuccess(response -> testContext.verify(() -> {
//                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
//                    assertTrue(
//                            StringUtils.isNotBlank(response.bodyAsJsonObject().getString("value")),
//                            "Empty quotes are not allowed"
//                    );
//                    testContext.completeNow();
//                }));
//    }
//
//    @Test
//    void createQuote(final VertxTestContext testContext) {
//        String testQuote = "some value";
//        webClient.post(PORT, HOST, "/api/quotes")
//                .putHeader("Authorization", createJWT())
//                .sendJsonObject(createQuote(testQuote))
//                .onFailure(testContext::failNow)
//                .onSuccess(response -> testContext.verify(() -> {
//                    assertEquals(201, response.statusCode(), MSG_201_EXPECTED);
//                    assertEquals(
//                            testQuote,
//                            response.bodyAsJsonObject().getString("value"),
//                            "Quote does not match " + testQuote);
//                    testContext.completeNow();
//                }));
//    }
//
//    @Test
//    void updateQuote(final VertxTestContext testContext) {
//        String testQuote = "some value";
//        String jwt = createJWT();
//        webClient.put(PORT, HOST, "/api/quotes/0")
//                .putHeader("Authorization", jwt)
//                .sendJsonObject(createQuote(testQuote))
//                .onFailure(testContext::failNow)
//                .onSuccess(response -> testContext.verify(() -> {
//                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
//                    assertEquals(
//                            testQuote,
//                            response.bodyAsJsonObject().getString("value"),
//                            "Quote does not match " + testQuote);
//                    testContext.completeNow();
//                }));
//    }
//
//    @Test
//    void deleteQuote(final VertxTestContext testContext) {
//        webClient.delete(PORT, HOST, "/api/quotes/1")
//                .putHeader("Authorization", createJWT())
//                .send()
//                .onFailure(testContext::failNow)
//                .onSuccess(response -> testContext.verify(() -> {
//                    assertEquals(204, response.statusCode(), MSG_204_EXPECTED);
//                    testContext.completeNow();
//                }));
//    }
//
//    private JsonObject createQuote(String quote) {
//        return new JsonObject().put("quote", quote);
//    }
//    private String jwt = "";
//    private String createJWT() {
//
//        if (!jwt.isEmpty()){
//            return jwt;
//        }
//
//        JsonObject requestPayload = new JsonObject().put("adriaId", "test");
//
//        CompletableFuture<String> jwtFuture = new CompletableFuture<>();
//
//
//        webClient.post(PORT, HOST, "/auth/login")
//                .putHeader("Content-Type", "application/json")
//                .sendJson(requestPayload, ar -> {
//                    if (ar.succeeded()) {
//                        //handle the response
//                        HttpResponse<Buffer> response = ar.result();
//                        int statusCode = response.statusCode();
//
//                        if (statusCode == 200) {
//                            String responseBody = response.bodyAsString();
//                            JsonObject responseJson = new JsonObject(responseBody);
//                            String jwt = responseJson.getString("jwt");
//
//                            jwtFuture.complete("Bearer " + jwt);
//                        } else {
//                            jwtFuture.completeExceptionally(new Exception("Failed to obtain JWT"));
//                        }
//                    } else {
//                        jwtFuture.completeExceptionally(ar.cause());
//                    }
//                });
//
//        try {
//            jwt = jwtFuture.get();
//            return jwt;
//        } catch (Exception e) {
//            return null;
//        }
//    }
//}