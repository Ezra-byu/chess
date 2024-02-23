package service;

import dataAccess.MemoryUserDAO;
import dataAccess.UserDAO;
import model.UserData;

//The Service classes implement the actual functionality of the server.
//More specifically, the Service classes implement the logic associated with the web endpoints.
public class UserService {
    UserDAO userDAO = new MemoryUserDAO(); //change upon completion of SQL database
    public UserData getUser(UserData user) { // should return RegisterResponse?
        return userDAO.getUser(user);
    }
}
