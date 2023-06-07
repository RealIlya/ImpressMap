package com.example.impressmap.model;

public class CommentId
{
    private String id;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    } // если сеттер нигде не используется, то можно его удалить, а переменную сделать final.
      // Иммутабельность это всегда хорошо
}
