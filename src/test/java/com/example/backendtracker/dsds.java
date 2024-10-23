package com.example.backendtracker;

import org.apache.commons.lang3.StringUtils;

public class dsds {
    public static void main(String[] args) {
//        System.out.printf(StringUtils.chop("pon%admin%"));
        String ds= "pon%admin%";

        System.out.println(        StringUtils.substring(ds, 0, ds.length() - 1));

    }

}
