package org.bonn.se2.services.util;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;

public class Notifier {

    private Position position;
    private int delay;
    private String message;
    private Notification.Type type;

    public Notifier createErrorNotification(String message) {
        type = Notification.Type.ERROR_MESSAGE;
        this.message = message;
        delay = 3000;
        position = Position.MIDDLE_CENTER;
        return this;
    }

    public Notifier createWarningNotification(String message) {
        type = Notification.Type.WARNING_MESSAGE;
        this.message = message;
        delay = 3000;
        position = Position.MIDDLE_CENTER;
        return this;
    }

    public Notifier createNotification(String message) {
        type = Notification.Type.HUMANIZED_MESSAGE;
        this.message = message;
        delay = 3000;
        position = Position.MIDDLE_CENTER;
        return this;
    }

    public Notifier at(Position position) {
        this.position = position;
        return this;
    }

    public Notifier withMessage(String message) {
        this.message = message;
        return this;
    }

    public Notifier withDelay(int delay) {
        this.delay = delay;
        return this;
    }

    public void show() {
        Notification notification = new Notification(message, type);
        notification.setDelayMsec(delay);
        notification.setPosition(position);
        notification.show(Page.getCurrent());
    }

}
