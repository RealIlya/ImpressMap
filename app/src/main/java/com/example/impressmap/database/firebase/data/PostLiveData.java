package com.example.impressmap.database.firebase.data;

import static com.example.impressmap.util.Constants.DATABASE_REF;
import static com.example.impressmap.util.Constants.Keys.USERS_NODE;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.impressmap.model.data.OwnerUser;
import com.example.impressmap.model.data.Post;
import com.example.impressmap.model.data.PostDataBase;
import com.example.impressmap.model.data.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class PostLiveData extends LiveData<Post>
{
    private final DatabaseReference postRef;
    private final DatabaseReference usersRef;

    private final ValueEventListener listener = new ValueEventListener()
    {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot)
        {
            PostDataBase value = snapshot.getValue(PostDataBase.class);

            usersRef.child(value.getOwnerUserId())
                    .addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot)
                        {
                            User user = snapshot.getValue(User.class);

                            Post post = new Post();
                            post.setId(value.getId());
                            OwnerUser ownerUser = new OwnerUser();
                            ownerUser.setId(user.getId());
                            ownerUser.setFullName(user.getFullName());
                            post.setOwnerUser(ownerUser);
                            post.setDate(new Date(value.getDate()));
                            post.setText(value.getText());

                            setValue(post);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error)
                        {

                        }
                    });

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error)
        {

        }
    };

    public PostLiveData(DatabaseReference postRef)
    {
        this.postRef = postRef;
        usersRef = DATABASE_REF.child(USERS_NODE);
    }

    @Override
    protected void onActive()
    {
        postRef.addListenerForSingleValueEvent(listener);
    }

    @Override
    protected void onInactive()
    {
        postRef.removeEventListener(listener);
    }
}
