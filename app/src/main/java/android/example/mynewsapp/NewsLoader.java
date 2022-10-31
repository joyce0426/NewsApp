package android.example.mynewsapp;

import android.content.Context;
import android.content.AsyncTaskLoader;


import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<NewsInfo>> {

    //Query Url
    private String mURL;

   public NewsLoader(Context context, String url) {
        super(context);
        mURL=url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad(); }

    @Override
    public List<NewsInfo> loadInBackground() {
        if (mURL == null) {
            return null;
        }

        List<NewsInfo> DataList = QueryUtility.fetchNewsInfo(mURL);
        return DataList;

    }

}
