package be.howest.ti.adria.web;

import be.howest.ti.adria.logic.controller.AdriaController;
import be.howest.ti.adria.logic.controller.ControllerFactory;
import be.howest.ti.adria.logic.controller.ControllerFactoryImpl;
import be.howest.ti.adria.logic.data.Repositories;
import be.howest.ti.adria.logic.domain.RiskObserver;
import be.howest.ti.adria.logic.domain.SafeZoneFinder;
import be.howest.ti.adria.logic.domain.adria.Adria;
import be.howest.ti.adria.logic.domain.adria.World;
import be.howest.ti.adria.logic.domain.RandomUserDataGenerator;
import be.howest.ti.adria.logic.exceptions.ControllerException;
import be.howest.ti.adria.logic.util.Config;
import be.howest.ti.adria.web.bridge.OpenApiBridge;
import be.howest.ti.adria.web.bridge.RtcBridge;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;
import io.vertx.ext.web.openapi.RouterBuilder;

import java.util.logging.Level;
import java.util.logging.Logger;

public class WebServer extends AbstractVerticle {
    private static final String REALTIME_COMM_URI = "/events/*";
    private static final String API_PROTECTED_URI = "^/api/(?!public|auth).*";
    private static final String STATIC_USER_IMG = "/api/img/:adriaId";
    private static final Logger LOGGER = Logger.getLogger(WebServer.class.getName());
    private Promise<Void> startPromise;
    private final OpenApiBridge openApiBridge;
    private final RtcBridge rtcBridge;
    private JWTAuth jwtAuth;
    private Adria adria;

    public WebServer(OpenApiBridge bridge, RtcBridge rtcBridge) {
        this.openApiBridge = bridge;
        this.rtcBridge = rtcBridge;
    }

    public WebServer() {
        this(new OpenApiBridge(), new RtcBridge());
    }

    @Override
    public void start(Promise<Void> startPromise) {
        this.startPromise = startPromise;
        ConfigRetriever.create(vertx).getConfig()
                .onFailure(cause -> shutDown("Failed to load configuration", cause))
                .onSuccess(configuration -> {

                    //Add jwt token auth
                    JWTAuthOptions authOptions = new JWTAuthOptions()
                            .addPubSecKey(new PubSecKeyOptions()
                                    .setAlgorithm("HS256").setBuffer(Config.getInstance().read("key", "")));
                    jwtAuth = JWTAuth.create(vertx, authOptions);
                    openApiBridge.setAuth(jwtAuth);
                    LOGGER.log(Level.INFO, "Configuration loaded: {0}", configuration);
                    final int port = configuration.getJsonObject("http").getInteger("port");
                    LOGGER.log(Level.INFO, "Server will be listening at port {0}", port);

                    RouterBuilder.create(vertx, configuration.getJsonObject("api").getString("url"))
                            .onFailure(cause -> shutDown("Failed to load API specification", cause))
                            .onSuccess(routerBuilder -> {
                                LOGGER.log(Level.INFO, "API specification loaded: {0}",
                                        routerBuilder.getOpenAPI().getOpenAPI().getJsonObject("info").getString("version"));
                                Repositories.getInstance().configure(configuration.getJsonObject("db"));
                                ControllerFactory controllerFactory = new ControllerFactoryImpl(Repositories.getInstance());
                                //Initialize the controllers after the database connection has been made
                                openApiBridge.addControllers(controllerFactory);

                                Router mainRouter = Router.router(vertx);
                                mainRouter.route().handler(createCorsHandler());
                                mainRouter.routeWithRegex(API_PROTECTED_URI)
                                        .handler(JWTAuthHandler.create(jwtAuth));
                                mainRouter.route(STATIC_USER_IMG)
                                        .handler(this::serveUserImage);
                                mainRouter.route(REALTIME_COMM_URI)
                                        .handler(rtcBridge.getSockJSHandler(vertx));


                                Router openApiRouter = openApiBridge.buildRouter(routerBuilder);
                                mainRouter.mountSubRouter("/", openApiRouter);

                                vertx.createHttpServer()
                                        .requestHandler(mainRouter)
                                        .listen(port)
                                        .onFailure(cause -> shutDown("Failed to start server", cause))
                                        .onSuccess(server -> {
                                            LOGGER.log(Level.INFO, "Server is listening on port: {0}", server.actualPort());
                                            startPromise.complete();
                                        });
                                startApplication(controllerFactory);
                            });
                });
    }

    private void startApplication(ControllerFactory controllerFactory) {

        if (controllerFactory == null){
            throw new ControllerException("Error initializing controllerfactory");
        }

        World adriaWorld = new World();
        adria = new Adria(adriaWorld);
        adria.start();
        RiskObserver riskObserver = new RiskObserver(
                adriaWorld,
                controllerFactory.createEventController());
        SafeZoneFinder safeZoneFinder = new SafeZoneFinder(adriaWorld, controllerFactory.createEventController());
        AdriaController adriaController = new AdriaController(adriaWorld, riskObserver, safeZoneFinder, rtcBridge);
        openApiBridge.setAdriaController(adriaController);
        rtcBridge.setAdriaController(adriaController);
        openApiBridge.setAdria(adriaWorld);
        RandomUserDataGenerator randomUserDataGenerator = new RandomUserDataGenerator(vertx);
        openApiBridge.setRandomUserDataGenerator(randomUserDataGenerator);
    }

    private void serveUserImage(RoutingContext ctx) {
        String userId = ctx.request().getParam("adriaId");
        String filePath = "static/private/users/img/" + userId + (userId.endsWith(".jpg") ? "" : ".jpg");

        vertx.fileSystem().exists(filePath, exists -> {
            if (Boolean.TRUE.equals(exists.result())) {
                ctx.response().sendFile(filePath);
            } else {
                LOGGER.info("Tried to access an user image that was not found: " + filePath);
                ctx.response().setStatusCode(404).end();
            }
        });
    }

    private CorsHandler createCorsHandler() {
        return CorsHandler.create(".*.")
                .allowedHeader("x-requested-with")
                .allowedHeader("Access-Control-Allow-Origin")
                .allowedHeader("Access-Control-Allow-Credentials")
                .allowCredentials(true)
                .allowedHeader("origin")
                .allowedHeader("Content-Type")
                .allowedHeader("Authorization")
                .allowedHeader("accept")
                .allowedMethod(HttpMethod.HEAD)
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedMethod(HttpMethod.PATCH)
                .allowedMethod(HttpMethod.DELETE)
                .allowedMethod(HttpMethod.PUT);
    }

    private void shutDown(String quote, Throwable cause) {
        LOGGER.log(Level.SEVERE, quote, cause);
        LOGGER.info("Shutting down");
        vertx.close();
        startPromise.fail(cause);
    }

    @Override
    public void stop() {
        Repositories.getInstance().shutdown();
        adria.stop();
    }
}