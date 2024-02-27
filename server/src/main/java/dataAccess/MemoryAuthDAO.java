package dataAccess;
import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
    final private HashMap<String, AuthData> auths = new HashMap<>();
    @Override
    public AuthData createAuth(UserData user) {
        AuthData newauth = new AuthData(UUID.randomUUID().toString(), user.username());
        auths.put(user.username(), newauth);
        return newauth;
    }

    @Override
    public AuthData getAuth(UserData user) {
        return auths.get(user.username());//returns null if not in auths set
    }

    @Override
    public void deleteAuth(UserData user) {
        auths.remove(user.username()); //to be called with username
    }
    @Override
    public void clearAuth(){auths.clear();}
}
