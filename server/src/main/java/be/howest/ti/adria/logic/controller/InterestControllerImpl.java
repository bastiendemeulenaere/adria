package be.howest.ti.adria.logic.controller;

import be.howest.ti.adria.logic.data.InterestRepository;
import be.howest.ti.adria.logic.domain.Category;

import java.util.List;
import java.util.NoSuchElementException;

public class InterestControllerImpl implements InterestController {
    private static final String MSG_UNKNOWN = "No %s available";
    private final InterestRepository interestRepository;

    public InterestControllerImpl(InterestRepository interestRepository) {
        this.interestRepository = interestRepository;
    }

    @Override
    public List<Category> getUserInterests(String userId) {
        List<Category> interests = interestRepository.getUserInterests(userId);

        if (null == interests)
            throw new NoSuchElementException(String.format(MSG_UNKNOWN, "category"));

        return interests;
    }

    @Override
    public void setUserInterests(List<Integer> interestIds, String adriaId) {
        interestRepository.setUserInterests(interestIds, adriaId);
    }
}
