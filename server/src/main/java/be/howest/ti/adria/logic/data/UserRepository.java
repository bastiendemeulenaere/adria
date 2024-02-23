package be.howest.ti.adria.logic.data;

import be.howest.ti.adria.logic.domain.User;
import be.howest.ti.adria.logic.domain.UserCreateObject;


public abstract class UserRepository extends Repository{
    protected UserRepository(H2Connection h2Connection) {
        super(h2Connection);
    }

    public abstract boolean userExists(String userId);


    public abstract User getUserById(String adriaId);

    public abstract User createUser(UserCreateObject user);

    public abstract String editAboutMe(String adriaId, String aboutMe);
}
