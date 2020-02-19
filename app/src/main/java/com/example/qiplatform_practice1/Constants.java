package com.example.qiplatform_practice1;
/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Defines several constants used between {@link BluetoothChatService} and the UI.
 */
class Constants {

    // Message types sent from the BluetoothChatService Handler
    static final int MESSAGE_STATE_CHANGE = 1;
     static final int MESSAGE_READ = 2;
     static final int MESSAGE_WRITE = 3;
     static final int MESSAGE_DEVICE_NAME = 4;
     static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
     static final String DEVICE_NAME = "device_name";
     static final String TOAST = "toast";

}
