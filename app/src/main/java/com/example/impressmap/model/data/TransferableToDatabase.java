package com.example.impressmap.model.data;

import java.util.Map;

public interface TransferableToDatabase
{
    Map<String, Object> prepareToTransferToDatabase();
}
