package com.fanwe.lib.http.callback;

import com.fanwe.lib.http.Response;

/**
 * Created by zhengjun on 2017/10/10.
 */

public abstract class RequestCallback implements IRequestCallback
{
    private Response mResponse;

    @Override
    public final void setResponse(Response response)
    {
        mResponse = response;
    }

    @Override
    public final Response getResponse()
    {
        return mResponse;
    }

    @Override
    public void onStart()
    {
    }

    @Override
    public void onSuccessBackground() throws Exception
    {

    }

    @Override
    public abstract void onSuccess();

    @Override
    public void onError(Exception e)
    {
    }

    @Override
    public void onCancel()
    {
    }

    @Override
    public void onFinish()
    {
    }
}