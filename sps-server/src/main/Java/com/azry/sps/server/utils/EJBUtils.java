package com.azry.sps.server.utils;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EJBUtils {

    public static <T> T getBean(Class<T> clazz) {
        try {
            // noinspection unchecked
            return (T) new InitialContext().lookup("java:global/sps/sps-server/" + clazz.getSimpleName());
        } catch (NamingException e) {
            throw new RuntimeException("Error occurred while " + clazz.getSimpleName() + " bean lookup", e);
        }
    }

}
