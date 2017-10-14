package com.fanwe.www.http;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.fanwe.lib.http.Request;
import com.fanwe.lib.http.Response;

/**
 * 同步请求demo
 */
public class SyncRequestActivity extends AppCompatActivity
{
    public static final String TAG = "SyncRequestActivity";

    public static final String URL = "http://ilvbt3.fanwe.net/mapi/index.php";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_request);
    }

    public void onClickRequest(View view)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Response response = Request.post(URL).param("ctl", "app").param("act", "init")
                            .setTag(this)
                            .execute();

                    Log.i(TAG, response.getBody());
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}