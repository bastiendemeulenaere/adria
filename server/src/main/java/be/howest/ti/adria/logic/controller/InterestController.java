package be.howest.ti.adria.logic.controller;

import be.howest.ti.adria.logic.domain.Category;

import java.util.List;

public interface InterestController {
    List<Category> getUserInterests(String userId);

    void setUserInterests(List<Integer> interestIds, String adriaId);
}
