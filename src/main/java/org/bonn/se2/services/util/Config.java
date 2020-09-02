package org.bonn.se2.services.util;

public class Config {

    public static class DBCredentials {
        public static final String USERNAME = "hwecke2s";
        public static final String URL = "jdbc:postgresql://dumbo.inf.h-brs.de/hwecke2s";
    }

    public static class Roles {
        public static final String CUSTOMER = "customer";
        public static final String SALESMAN = "salesman";
    }

    public static class Views {
        public static final String MAIN = "main";
        public static final String LOGIN = "login";
        public static final String REGISTRATION = "registration";
        public static final String USERPAGE = "userpage";
    }

    public static class ImagePaths {
        public static final String PLACEHOLDER = "images/placeholder.png";
        public static final String CORPORATE = "images/logo.png";
    }

}
