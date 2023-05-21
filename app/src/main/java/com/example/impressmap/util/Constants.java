package com.example.impressmap.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Constants
{
    public static final FirebaseAuth AUTH = FirebaseAuth.getInstance();
    public static final DatabaseReference DATABASE_REF;
    public static final String LAT_LNG_KEY = "LAT_LNG_KEY";
    public static String DATABASE_NAME = "impress_database";
    public static String UID;

    static
    {
        DATABASE_REF = FirebaseDatabase.getInstance(
                                               "https://impressmap-939c5-default-rtdb.europe-west1.firebasedatabase.app")
                                       .getReference();
    }

    public static class Keys
    {
        public static final String MAIN_LIST_NODE = "mainList";

        public static final String ADDRESSES_NODE = "addresses";
        public static final String GMARKERS_NODE = "gmarkers";
        public static final String POSTS_NODE = "posts";
        public static final String COMMENTS_NODE = "comments";
        public static final String USERS_NODE = "users";
        public static final String OWNERS_NODE = "owners";

        public static final String CHILD_ID_NODE = "id";
        public static final String DATE_NODE = "date";
        public static final String TITLE_NODE = "title";
        public static final String TEXT_NODE = "text";
        public static final String FULL_ADDRESS_NODE = "fullAddress";
        public static final String FULL_NAME_NODE = "fullName";
        public static final String POSITION_NODE = "position";
        public static final String IMAGE_URLS_NODE = "imageUrls";
        public static final String PHONE_NUMBER_NODE = "phoneNumber";
        public static final String EMAIL_NODE = "email";
        public static final String USER_IDS_NODE = "userIds";
        public static final String NOT_PUBLIC_NODE = "notPublic";
        public static final String OWNER_ID_NODE = "ownerId";
        public static final String DESC_NODE = "desc";
        public static final String GMARKER_ID = "gmarkerId";
        public static final String TYPE_NODE = "type";
        public static final String COMMENT_IDS_NODE = "commentIds";

    }
}
