package com.example.todo22;

import android.content.Context;
import android.net.Network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.nio.channels.AsynchronousChannelGroup;

public class BookLoader extends AsyncTaskLoader<String> {
    private String mQueryString;

    BookLoader(Context context, String QueryString){
        super(context);
        mQueryString = QueryString;
    }

    public BookLoader(@NonNull Context context){
        super(context);

    }
    @Override
    protected void onStartLoading(){
        super.onStartLoading();
        forceLoad();
    }


    @Nullable
    @Override
    public String loadInBackground() {
        return NetworkUtils.getBookInfo(mQueryString);
    }
}
