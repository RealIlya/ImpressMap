package com.example.impressmap.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Constants
{
    public static String DATABASE_NAME = "impress_database";

    public static final FirebaseAuth AUTH = FirebaseAuth.getInstance();
    public static String UID;

    public static final DatabaseReference DATABASE_REF;

    static
    {
        DATABASE_REF = FirebaseDatabase.getInstance(
                                               "https://impressmap-939c5-default-rtdb.europe-west1.firebasedatabase.app")
                                       .getReference();
    }

    public static class Keys
    {
        public static final String MAIN_LIST_NODE = "main_list";

        public static final String ADDRESSES_NODE = "addresses";
        public static final String GMARKERS_NODE = "gmarkers";
        public static final String POSTS_NODE = "posts";
        public static final String USERS_NODE = "users";

        public static final String CHILD_ID_NODE = "id";
        public static final String DATE_NODE = "date";
        public static final String TITLE_NODE = "title";
        public static final String TEXT_NODE = "text";
        public static final String FULL_ADDRESS_NODE = "full_address";
        public static final String FULL_NAME_NODE = "full_name";
        public static final String POSITION_NODE = "position";
        public static final String IMAGE_URLS_NODE = "image_urls";
        public static final String PHONE_NUMBER_NODE = "phone_number";
        public static final String EMAIL_NODE = "email";
        public static final String USER_IDS_NODE = "user_ids";
        public static final String OWNER_ID_NODE = "owner_id";
        public static final String GMARKER_ID = "gmarker_id";
        public static final String TYPE_NODE = "type";
    }
}
