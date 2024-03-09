package dataAccess.memoryDAO;
import dataAccess.AuthDAO;
import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    final private HashMap<String, AuthData> auths = new HashMap<>();
    @Override
    public AuthData createAuth(UserData user) {
        AuthData newauth = new AuthData(UUID.randomUUID().toString(), user.username());
        auths.put(newauth.authToken(), newauth);
        return newauth;
    }

    @Override
    public AuthData getAuth(String authToken) {
        return auths.get(authToken);//returns null if not in auths set
    }

    @Override
    public void deleteAuth(AuthData auth) {
        auths.remove(auth.authToken()); //to be called with username
    }
    @Override
    public void clearAuth(){auths.clear();}

    @Override
    public Boolean checkAuth(String authToken){
        return auths.containsKey(authToken);
    };
}
