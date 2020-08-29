package org.bonn.se2.process.control;

import com.vaadin.ui.UI;
import org.bonn.se2.model.dao.CustomerDAO;
import org.bonn.se2.model.dao.SalesmanDAO;
import org.bonn.se2.model.dao.UserDAO;
import org.bonn.se2.model.objects.dto.Salesman;
import org.bonn.se2.model.objects.dto.User;
import org.bonn.se2.model.objects.dto.UserAtLogin;
import org.bonn.se2.process.control.exceptions.DatabaseException;
import org.bonn.se2.process.control.exceptions.InvalidCredentialsException;
import org.bonn.se2.services.util.Config;
import org.bonn.se2.services.util.CryptoFunctions;
import org.bonn.se2.services.util.SessionFunctions;
import org.bonn.se2.services.util.UIFunctions;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginControl {

    public static void checkAuthentication(UserAtLogin loginUser) throws InvalidCredentialsException, DatabaseException {

        User user = new UserDAO().retrieve(loginUser.getEmail());

        if (CryptoFunctions.checkPw(CryptoFunctions.hash(loginUser.getPassword()), user.getPassword())) {
            SessionFunctions.setCurrentUser(user);
            SessionFunctions.setCurrentRole(getRole(user));
            UIFunctions.gotoMain();
        } else {
            throw new InvalidCredentialsException();
        }
    }

    public static void logoutUser() {
        UI.getCurrent().close();
        UI.getCurrent().getSession().close();
        UI.getCurrent().getPage().setLocation("");
    }

    public static String getRole(User user) {
        try {
            new CustomerDAO().retrieve(user.getUserID());
            return Config.Roles.CUSTOMER;

        } catch (DatabaseException e) {
            try {
                new SalesmanDAO().retrieve(user.getUserID());
                return Config.Roles.SALESMAN;
            } catch (DatabaseException ex) {
                Logger.getLogger(LoginControl.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }

}
