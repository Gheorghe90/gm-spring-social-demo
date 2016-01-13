package gm.spring.social.demo.controllers;

import gm.spring.social.demo.dao.UsersDao;
import gm.spring.social.demo.model.UserConnection;
import gm.spring.social.demo.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;

@Component
public class ApplicationControllerUtil {

    private static final String USER_CONNECTION = "MY_USER_CONNECTION";
    private static final String USER_PROFILE = "MY_USER_PROFILE";

    @Autowired
    private UsersDao usersDao;

    public void setModel(HttpServletRequest request, Principal currentUser, Model model) {

        String userId = currentUser == null ? null : currentUser.getName();
        HttpSession session = request.getSession();

        UserConnection connection = null;
        UserProfile profile = null;
        String displayName = null;

        if (userId != null) {
            connection = getUserConnection(session, userId);
            profile = getUserProfile(session, userId);
            displayName = getDisplayName(connection, profile);
        }

        Throwable exception = (Throwable)session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        model.addAttribute("exception",              exception == null ? null : exception.getMessage());
        model.addAttribute("currentUserId",          userId);
        model.addAttribute("currentUserProfile",     profile);
        model.addAttribute("currentUserConnection",  connection);
        model.addAttribute("currentUserDisplayName", displayName);
    }

    protected UserProfile getUserProfile(HttpSession session, String userId) {
        UserProfile profile = (UserProfile) session.getAttribute(USER_PROFILE);

        if (profile == null || !userId.equals(profile.getUserId())) {
            profile = usersDao.getUserProfile(userId);
            session.setAttribute(USER_PROFILE, profile);
        }
        return profile;
    }

    public UserConnection getUserConnection(HttpSession session, String userId) {
        UserConnection connection;
        connection = (UserConnection) session.getAttribute(USER_CONNECTION);

        if (connection == null || !userId.equals(connection.getUserId())) {
            connection = usersDao.getUserConnection(userId);
            session.setAttribute(USER_CONNECTION, connection);
        }
        return connection;
    }

    protected String getDisplayName(UserConnection connection, UserProfile profile) {
        if (connection.getDisplayName() != null) {
            return connection.getDisplayName();
        } else {
            return profile.getName();
        }
    }
}
