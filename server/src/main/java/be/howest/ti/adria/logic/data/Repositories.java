package be.howest.ti.adria.logic.data;

import io.vertx.core.json.JsonObject;

public class Repositories {
    private static Repositories repos = null;

    public H2Connection getConnectionFactory() {
        return connectionFactory;
    }

    private H2Connection connectionFactory;

    public CategoryRepository getCategoryRepository() {
        return categoryRepository;
    }

    public EventRepository getEventRepository() {
        return eventRepository;
    }

    private CategoryRepository categoryRepository;
    private EventRepository eventRepository;
    private InterestRepository interestRepository;
    private UserRepository userRepository;
    private NotificationRepository notificationRepository;

    private Repositories() {

    }

    public static Repositories getInstance() {
        if (repos == null)
            repos = new Repositories();

        return repos;
    }

    public void configure(JsonObject dbProps) {
        connectionFactory = new H2Connection(dbProps.getString("url"),
                dbProps.getString("username"),
                dbProps.getString("password"),
                dbProps.getInteger("webconsole.port"));

        this.userRepository = new UserRepositoryImpl(connectionFactory);
        this.interestRepository = new InterestRepositoryImpl(connectionFactory);
        this.categoryRepository = new CategoryRepositoryImpl(connectionFactory);
        this.eventRepository = new EventRepositoryImpl(connectionFactory);
        this.notificationRepository = new NotificationRepositoryImpl(connectionFactory);
    }

    public void shutdown() {
        if (connectionFactory != null)
            connectionFactory.cleanUp();

        connectionFactory = null;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public InterestRepository getInterestRepository() {
        return interestRepository;
    }

    public NotificationRepository getNotificationRepository() {
        return notificationRepository;
    }
}
