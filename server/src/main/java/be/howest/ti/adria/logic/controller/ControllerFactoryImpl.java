package be.howest.ti.adria.logic.controller;

import be.howest.ti.adria.logic.data.Repositories;

public class ControllerFactoryImpl implements ControllerFactory {
    private final Repositories repositories;

    public ControllerFactoryImpl(Repositories repositories) {
        this.repositories = repositories;
    }

    @Override
    public EventController createEventController() {
        return new EventControllerImpl(repositories.getEventRepository());
    }

    @Override
    public CategoryController createCategoryController() {
        return new CategoryControllerImpl(repositories.getCategoryRepository());
    }

    @Override
    public UserController createUserController() {
        return new UserController(repositories.getUserRepository());
    }

    @Override
    public InterestController createInterestController() {
        return new InterestControllerImpl(repositories.getInterestRepository());
    }

    @Override
    public NotificationController createNotificationController() {
        return new NotificationController(repositories.getNotificationRepository());
    }
}