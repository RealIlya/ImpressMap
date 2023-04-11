package com.example.impressmap.ui.util;

import com.google.firebase.auth.FirebaseAuth;

public class Constants
{
    public static String DATABASE_NAME = "impress_database";

    public static final FirebaseAuth AUTH = FirebaseAuth.getInstance();
    public static  String EMAIL;
    public static  String PASSWORD;

    public static class Key
    {
        public static final String GMARKERS = "gmarker";
    }
}
