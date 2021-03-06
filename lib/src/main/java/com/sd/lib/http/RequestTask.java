package com.sd.lib.http;

import com.sd.lib.http.callback.IUploadProgressCallback;
import com.sd.lib.http.callback.RequestCallback;
import com.sd.lib.http.task.FTask;
import com.sd.lib.http.utils.HttpLog;
import com.sd.lib.http.utils.TransmitParam;

class RequestTask extends FTask implements IUploadProgressCallback
{
    private final IRequest mRequest;
    private final RequestCallback mCallback;

    public RequestTask(IRequest request, RequestCallback callback)
    {
        mRequest = request;
        mCallback = callback;

        mRequest.setUploadProgressCallback(this);
    }

    private String getLogPrefix()
    {
        return String.valueOf(this);
    }

    @Override
    protected void onRun()
    {
        try
        {
            HttpLog.e(getLogPrefix() + " 1 onRun---------->");

            synchronized (RequestTask.this)
            {
                runOnUiThread(mStartRunnable);
                HttpLog.i(getLogPrefix() + " 2 waitThread");
                RequestTask.this.wait(); //等待开始回调完成
            }

            HttpLog.i(getLogPrefix() + " 4 resumeThread");

            final IResponse response = mRequest.execute();
            mCallback.setResponse(response);
            mCallback.onSuccessBackground();

            HttpLog.i(getLogPrefix() + " 5 onSuccess");

            runOnUiThread(mSuccessRunnable);
        } catch (Exception e)
        {
            onError(e);
        }
    }

    private final Runnable mStartRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            synchronized (RequestTask.this)
            {
                mCallback.onStart();
                HttpLog.i(getLogPrefix() + " 3 notifyThread");
                RequestTask.this.notifyAll();
            }
        }
    };

    private final Runnable mSuccessRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            mCallback.onSuccessBefore();
            mCallback.onSuccess();
        }
    };

    protected void onError(final Exception e)
    {
        HttpLog.i(getLogPrefix() + " onError:" + e);
        if (isCancelled())
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    mCallback.onCancel();
                }
            });
        } else
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    mCallback.onError(e);
                }
            });
        }
    }

    @Override
    protected void onFinally()
    {
        super.onFinally();
        HttpLog.i(getLogPrefix() + " onFinish");
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mCallback.onFinish();
            }
        });
    }

    @Override
    public void onProgressUpload(TransmitParam param)
    {
        mCallback.onProgressUpload(param);
    }
}
