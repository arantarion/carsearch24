package org.bonn.se2.services.util;

import com.vaadin.ui.UI;

public class UIFunctions {

    public static void gotoMain() {
        UI.getCurrent().getNavigator().navigateTo(Config.Views.MAIN);
    }

    public static void gotoLogin() {
        UI.getCurrent().getNavigator().navigateTo(Config.Views.LOGIN);
    }

    public static void gotoUserPage() {
        UI.getCurrent().getNavigator().navigateTo(Config.Views.USERPAGE);
    }

}
