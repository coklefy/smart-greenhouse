package com.example.sgh_app;

public class Utils {

    public static final String APP_LOG_TAG = "BT CLN";
    public static final String LIB_TAG = "BluetoothLib";

    public class bluetooth {
        public static final int ENABLE_BT_REQUEST = 1;
        public static final String BT_DEVICE_ACTING_AS_SERVER_NAME = "HC-06"; //MODIFICARE QUESTA COSTANTE CON IL NOME DEL DEVICE CHE FUNGE DA SERVER
        public static final String BT_SERVER_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    }

    public class channel {
        public static final int MESSAGE_RECEIVED = 0;
        public static final int MESSAGE_SENT = 1;
    }

    public class message {
        public static final char MESSAGE_TERMINATOR = '\n';
    }

}
