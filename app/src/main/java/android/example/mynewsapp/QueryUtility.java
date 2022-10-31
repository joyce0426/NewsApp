package android.example.mynewsapp;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtility {

    //Tag for the log messages
    private static final String LOG_TAG = QueryUtility.class.getSimpleName();
    private static final int LOG_RESPONSE_TAG = 200;

    private QueryUtility() {
    }

    public static List<NewsInfo> fetchNewsInfo(String queryUrl) {
        URL url = createUrl(queryUrl);

        //Perform HTTP request to the URL and receive JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "No Internet Connection. Issue with HTTP request. ", e);
        }
        List<NewsInfo> newsData = extractNewsInfoJson(jsonResponse);
        return newsData;

    }

    private static URL createUrl(String queryUrl) {
        URL url = null;
        try {
            url = new URL(queryUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Issue finding the URL", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonNews = "";

        //If url is null, return early
        if (url == null) {
            return jsonNews;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputS = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == LOG_RESPONSE_TAG) {
                inputS = urlConnection.getInputStream();
                jsonNews = readFromStream(inputS);
            } else {
                Log.e(LOG_TAG, "Error Response Code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Issues receiving the news JSON results", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputS != null) {
                inputS.close();
            }
        }
        return jsonNews;
    }

    private static String readFromStream(InputStream inputS) throws IOException {
        StringBuilder outputS = new StringBuilder();

        if (inputS != null) {
            InputStreamReader inputSR = new InputStreamReader(inputS, Charset.forName("UTF-8"));
            BufferedReader bReader = new BufferedReader(inputSR);
            String line = bReader.readLine();
            while (line != null) {
                outputS.append(line);
                line = bReader.readLine();
            }
        }
        return outputS.toString();
    }

    private static List<NewsInfo> extractNewsInfoJson(String jsonNews) {

        // Create an ArrayList that we can add news to
        List<NewsInfo> news = new ArrayList<>();

        //parse the json response string
        try {
            //create JSON object from the JSON string
            JSONObject newJsonObject = new JSONObject(jsonNews);

            //create JSON object associated with "response"
            JSONObject responseJson = newJsonObject.getJSONObject("response");

            //create JSON object associated with "results"
            JSONArray jsonArray = responseJson.getJSONArray("results");

            //For each news article in jsonArray, create an object
            for (int i = 0; i < jsonArray.length(); i++) {

                //get specific news items at correct i position
                JSONObject currentJsonObject = jsonArray.getJSONObject(i);

                //Get value for webTitle
                String webTitle = currentJsonObject.getString("webTitle");

                //Get value for sectionName
                String sectionName = currentJsonObject.getString("sectionName");

                //Get value for webUrl
                String webUrl = currentJsonObject.getString("webUrl");

                //get value for webPublicationDate
                String webPublicationDate = currentJsonObject.getString("webPublicationDate");

                //StringBuilder for author name of articles
                StringBuilder author = new StringBuilder("");

                JSONArray authorTags = currentJsonObject.getJSONArray ("tags");

                if (authorTags != null && authorTags.length () >0 ) {
                    for (int b = 0; b < authorTags.length(); b++) {

                        JSONObject currentAuthorTagsObject = authorTags.getJSONObject(b);

                        String authorTag = currentAuthorTagsObject.optString("webTitle");

                    if (authorTags.length () > 1) {
                        author.append(authorTag);
                        author.append("\t\t\t");
                    } else {
                        author.append ( authorTag );
                    }
                }
            } else {
                author.replace ( 0, 5, "No author(s) listed" );
                }

                NewsInfo newsInfo = new NewsInfo (webTitle,sectionName, webUrl, webPublicationDate, webUrl, author.toString());
                news.add(newsInfo);
            }

            } catch(JSONException e){
                    Log.e("QueryUtility", "Issue retrieving JSON results" + e);
                }
                return news;
            }
        }

