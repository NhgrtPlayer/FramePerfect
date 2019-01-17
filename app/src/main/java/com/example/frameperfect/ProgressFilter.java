package com.example.frameperfect;

import android.app.Activity;
import android.widget.ProgressBar;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.windowsazure.mobileservices.http.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;

public class ProgressFilter extends Activity implements ServiceFilter {

    private ProgressBar mProgressBar;

    ProgressFilter(ProgressBar progressBar) {
        mProgressBar = progressBar;
    }

    @Override
    public ListenableFuture<ServiceFilterResponse> handleRequest(ServiceFilterRequest request, NextServiceFilterCallback nextServiceFilterCallback) {

        final SettableFuture<ServiceFilterResponse> resultFuture = SettableFuture.create();


        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.VISIBLE);
            }
        });

        ListenableFuture<ServiceFilterResponse> future = nextServiceFilterCallback.onNext(request);

        Futures.addCallback(future, new FutureCallback<ServiceFilterResponse>() {
            @Override
            public void onFailure(Throwable e) {
                mProgressBar.setVisibility(ProgressBar.GONE);
                resultFuture.setException(e);
            }

            @Override
            public void onSuccess(ServiceFilterResponse response) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.GONE);
                    }
                });

                resultFuture.set(response);
            }
        });

        return resultFuture;
    }
}