package android.example.mynewsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsInfo>> {

    private static final String LOG_TAG = MainActivity.class.getName();

    private NewsAdapter newsAdapter;
    private TextView emptyTextView;
    private ProgressBar progressLoop;
    private static final int LOADER_ID = 1;


    // URL to grab the articles from the Guardian website.
    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search?api-key=833f7c4d-68f1-41bc-8f23-45ba5f2d232e";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //display when the textview is empty
        emptyTextView = findViewById(R.id.empty_text);

        //Display the loading loop
        progressLoop = findViewById(R.id.progress_loop);

        newsAdapter = new NewsAdapter(this, new ArrayList<NewsInfo>());

        ListView dataListView = (ListView) findViewById(R.id.list_view);
        dataListView.setEmptyView(emptyTextView);
        dataListView.setAdapter(newsAdapter);

        dataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Find the news article clicked
                NewsInfo currentInfo = newsAdapter.getItem(position);

                //convert the string url into uri object to pass the intent constructor
                Uri newsUri = Uri.parse(currentInfo.getUrl());

                // Create a new intent to view the guardian article URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        //Check ConnectivityManager for the state of network connection
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        //if network connection, get data
        if (networkInfo != null && networkInfo.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(LOADER_ID, null, this);
        } else {
            progressLoop.setVisibility(View.GONE);
            emptyTextView.setText("No Internet");
        }
    }

    @Override
    public Loader <List<NewsInfo>> onCreateLoader(int i, Bundle bundle) {
        Uri base = Uri.parse(GUARDIAN_REQUEST_URL);

        Uri.Builder uriBuilder = base.buildUpon();

        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("api-key", "test");

        return new NewsLoader(this, uriBuilder.toString());
        // return new NewsLoader(this, GUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsInfo>> loader, List<NewsInfo> data) {

        progressLoop.setVisibility(View.GONE);
        emptyTextView.setText("No Internet Connection. Please check your Wifi connection.");
        newsAdapter.clear();

        if (data != null && !data.isEmpty()) {
            newsAdapter.addAll(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<NewsInfo>> loader) {
        newsAdapter.clear();
    }
}