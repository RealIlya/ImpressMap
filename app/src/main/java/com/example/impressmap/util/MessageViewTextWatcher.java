package com.example.impressmap.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;

public class MessageViewTextWatcher implements TextWatcher
{
    private MenuItem sendButton;

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
        if (s.length() == 0)
        {
            sendButton.setEnabled(false);
        }
        else
        {
            sendButton.setEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s)
    {

    }

}
