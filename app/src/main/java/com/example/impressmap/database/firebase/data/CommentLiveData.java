package com.example.impressmap.database.firebase.data;

import static com.example.impressmap.util.Constants.DATABASE_REF;
import static com.example.impressmap.util.Constants.Keys.USERS_NODE;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.impressmap.model.data.Comment;
import com.example.impressmap.model.data.OwnerUser;
import com.example.impressmap.model.data.User;
import com.example.impressmap.model.data.db.CommentDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class CommentLiveData extends LiveData<Comment>
{
    private final DatabaseReference commentRef;
    private final DatabaseReference usersRef;

    private final ValueEventListener listener = new ValueEventListener()
    {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot)
        {
            CommentDatabase value = snapshot.getValue(CommentDatabase.class);

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

                            Comment comment = new Comment();
                            comment.setId(value.getId());
                            OwnerUser ownerUser = new OwnerUser();
                            ownerUser.setId(user.getId());
                            ownerUser.setFullName(user.getFullName());
                            comment.setOwnerUser(ownerUser);
                            comment.setDate(new Date(value.getDate()));
                            comment.setText(value.getText());

                            setValue(comment);
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

    public CommentLiveData(DatabaseReference commentRef)
    {
        this.commentRef = commentRef;
        usersRef = DATABASE_REF.child(USERS_NODE);
    }

    @Override
    protected void onActive()
    {
        commentRef.addValueEventListener(listener);
    }

    @Override
    protected void onInactive()
    {
        commentRef.removeEventListener(listener);
    }
}
