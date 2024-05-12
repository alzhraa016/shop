package com.my.shop.app;

import android.app.Application;

import java.util.ArrayList;

public class MyApp extends Application {
    public static ArrayList<CartItem> cartList = new ArrayList<>();

    @Override
    public void onTerminate() {
        super.onTerminate();
        cartList.clear();
    }
}
