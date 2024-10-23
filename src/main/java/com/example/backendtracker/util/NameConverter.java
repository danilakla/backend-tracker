package com.example.backendtracker.util;

public class NameConverter {

    public static String convertNameToDb(String lastname, String firstname, String surname) {
        return lastname + "_" + firstname + "_" + surname;
    }

    public static UserInfo convertNameToJavaClass(String flp_name) {
        String[] userInfo = flp_name.split("_");

        return UserInfo.builder().lastname(userInfo[0]).name(userInfo[1]).surname(userInfo[2]).build();
    }

}
