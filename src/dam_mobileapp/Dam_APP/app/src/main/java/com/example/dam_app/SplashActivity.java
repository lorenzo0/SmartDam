package com.example.dam_app;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dam_app.NetUtils.HTTPRequest;

public class SplashActivity extends AppCompatActivity {
    HTTPRequest httpRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        httpRequests = new HTTPRequest();

        Intent intent = new Intent(SplashActivity.this, MainActivity.class);

        Thread background = new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    needTimeToLoad();
                }catch (Throwable t)
                {
                    System.err.println("Thread Exception IN Splash Screen->" + t.toString());
                }finally {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("httpReq", httpRequests);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            }
        });
        background.start();
    }

    protected void needTimeToLoad(){
        httpRequests.fillInArray(httpRequests.HTTPSync());
    }

}

