package com.example.impressmap.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;

public class MessageViewTextWatcher implements TextWatcher
{
    private final MenuItem sendButton;

    public MessageViewTextWatcher(MenuItem sendButton)
    {
        this.sendButton = sendButton;
    }

    @Override
    public void beforeTextChanged(CharSequence s,
                                  int start,
                                  int count,
                                  int after)
    {

    }

    @Override
    public void onTextChanged(CharSequence s,
                              int start,
                              int before,
                              int count)
    {
        sendButton.setEnabled(s.length() != 0);
    }

    @Override
    public void afterTextChanged(Editable s)
    {

    }
}
