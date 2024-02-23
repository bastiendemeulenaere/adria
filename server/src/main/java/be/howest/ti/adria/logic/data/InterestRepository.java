package be.howest.ti.adria.logic.data;

import be.howest.ti.adria.logic.domain.Category;

import java.util.List;

public abstract class InterestRepository extends Repository {
    protected InterestRepository(H2Connection h2Connection) {
        super(h2Connection);
    }
    public abstract List<Category> getUserInterests(String userId);
    public abstract void setUserInterests(List<Integer> interestIds, String adriaId);
}
