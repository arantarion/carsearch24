package org.bonn.se2.services.util;

import com.vaadin.server.VaadinSession;
import org.bonn.se2.model.objects.dto.User;

public class SessionFunctions {

    private static final String CURRENT = "currentUser";
    private static final String ROLE = "role";

    public static boolean isLoggedIn() {
        return VaadinSession.getCurrent().getAttribute(CURRENT) != null;
    }

    public static User getCurrentUser() {
        if (!isLoggedIn()) {
            return null;
        }
        return (User) VaadinSession.getCurrent().getAttribute(CURRENT);
    }

    public static void setCurrentUser(User user) {
        VaadinSession.getCurrent().setAttribute(CURRENT, user);
    }

    public static String getCurrentRole() {
        return (String) VaadinSession.getCurrent().getAttribute(ROLE);
    }

    public static void setCurrentRole(String role) {
        VaadinSession.getCurrent().setAttribute(ROLE, role);
    }

}
