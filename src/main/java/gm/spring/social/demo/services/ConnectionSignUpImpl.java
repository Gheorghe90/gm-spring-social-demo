package gm.spring.social.demo.services;

import gm.spring.social.demo.dao.UsersDao;
import gm.spring.social.demo.model.UserProfile;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;

import java.util.UUID;

public class ConnectionSignUpImpl implements ConnectionSignUp {

    private final UsersDao usersDao;

    public ConnectionSignUpImpl(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    public String execute(Connection<?> connection) {
        org.springframework.social.connect.UserProfile profile = connection.fetchUserProfile();
        String userId = UUID.randomUUID().toString();
        usersDao.createUser(userId, new UserProfile(userId, profile));
        return userId;
    }
}