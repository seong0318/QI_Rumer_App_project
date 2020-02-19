package com.example.qiplatform_practice1;

public class AppController {

    public HomeFragment hf;
    private static AppController instance = new AppController();
    public static AppController getInstance() {
        return instance;
    }

    public BluetoothChatService mChatService = null;

    public boolean validateNetAccess() {

        return false;
    }
}