package com.example.impressmap.database.firebase.data;

import static com.example.impressmap.util.Constants.DATABASE_REF;
import static com.example.impressmap.util.Constants.Keys.USERS_NODE;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.impressmap.model.data.OwnerUser;
import com.example.impressmap.model.data.Post;
import com.example.impressmap.model.data.User;
import com.example.impressmap.model.data.db.PostDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class PostLiveData extends LiveData<Post>
{
    private final DatabaseReference postRef;
    private final DatabaseReference usersRef;

    private final ValueEventListener listener = new ValueEventListener()
    {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot)
        {
            PostDatabase value = snapshot.getValue(PostDatabase.class);

            if (value == null)
            {
                return;
            }

            usersRef.child(value.getOwnerId())
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
                            post.setDateTime(value.getDateTime());
                            post.setTitle(value.getTitle());
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
