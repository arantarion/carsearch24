package org.bonn.se2.process.control;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import org.bonn.se2.model.objects.dto.User;
import org.bonn.se2.services.util.SessionFunctions;

public class ProfileControl {

    public static User getCurrentUser() {
        return SessionFunctions.getCurrentUser();
    }

    public static void salesmanProfile() {
        User user = getCurrentUser();

        if (user == null) {
            showError();
        }

    }

    public static void customerProfile() {
        User user = getCurrentUser();

        if (user == null) {
            showError();
        }

    }

    private static void showError() {
        Notification notification = new Notification("User nicht gefunden", Notification.Type.ERROR_MESSAGE);
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.setDelayMsec(4000);
        notification.show(Page.getCurrent());
    }

}
