package com.projectstu.connect;

import java.net.CookieStore;

import android.app.Application;

public class MyCookie extends Application {


    private CookieStore cookies;   
    public CookieStore getCookie(){    
        return cookies;
    }
    public void setCookie(CookieStore cks){
        cookies = cks;
    }
}