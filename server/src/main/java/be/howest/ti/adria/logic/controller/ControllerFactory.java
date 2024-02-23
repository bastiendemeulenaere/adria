package be.howest.ti.adria.logic.controller;

public interface ControllerFactory {
    EventController createEventController();

    CategoryController createCategoryController();

    UserController createUserController();

    InterestController createInterestController();

    NotificationController createNotificationController();
}
