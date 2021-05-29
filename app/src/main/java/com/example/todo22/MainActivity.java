package com.example.todo22;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{
    private EditText mBookInput;
    private TextView mTitleText;
    private TextView mAuthorText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBookInput = (EditText)findViewById(R.id.bookInput);
        mAuthorText = (TextView)findViewById(R.id.titleText);
        mTitleText = findViewById(R.id.titleText);

        if (getSupportLoaderManager().getLoader(0)!= null){
            getSupportLoaderManager().initLoader(0,null,this);
        }


    }
    public void searchBook(View view){
        String queryString = mBookInput.getText().toString();
        new FetchBook(mTitleText,mAuthorText).execute(queryString);


        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if(connMgr != null){
            networkInfo = connMgr.getActiveNetworkInfo();
        }
        if(networkInfo != null && networkInfo.isConnected() && queryString.length() != 0){
            Bundle queryBundle = new Bundle();
            queryBundle.putString("queryString",queryString);
            getSupportLoaderManager().restartLoader(0,queryBundle,this);
            mAuthorText.setText("");
            mTitleText.setText("Loading...");
        }
        else{
            if(queryString.length() == 0){
                mAuthorText.setText("");
                mTitleText.setText("please enter a search term");
            }
            else{
                mAuthorText.setText("");
                mTitleText.setText("Please check your network connection and retry");
            }
        }

    }


    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String queryString="";

        if (args != null){
            queryString = args.getString("queryString");
        }
        return new BookLoader(this,queryString);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray itemsArray = jsonObject.getJSONArray("items");
            int i = 0;
            String title = null;
            String authors = null;
            while (i < itemsArray.length() && (authors == null && title == null)) {
                JSONObject book = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");
                try {
                    title = volumeInfo.getString("title");
                    authors = volumeInfo.getString("authors");

                } catch (Exception e) {
                    e.printStackTrace();
                }
                i++;
            }
            if (title != null && authors != null) {
                mTitleText.setText(title);
                mAuthorText.setText(authors);
            } else {
                mTitleText.setText("No Results found");
                mAuthorText.setText("");
            }
        } catch (JSONException e) {
            mTitleText.setText("no results found");
            mAuthorText.setText("");
            e.printStackTrace();
        }
        //super.onPostExecute(s);
    }



    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}