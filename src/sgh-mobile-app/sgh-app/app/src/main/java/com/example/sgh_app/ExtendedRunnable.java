package com.example.sgh_app;

public interface ExtendedRunnable extends Runnable {
    void write(byte[] bytes);
    void cancel();
}
