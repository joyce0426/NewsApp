package android.example.mynewsapp;

public class NewsInfo {

    private String mTitle;
    private String mDate;
    private String mTime;
    private String mSectionName;
    private String mAuthor;
    private String mUrl;

    public NewsInfo(String title, String date, String time, String sectionName, String author, String url) {
        mTitle = title;
        mDate = date;
        mTime = time;
        mSectionName = sectionName;
        mAuthor = author;
        mUrl= url;
    }

    public String getTitle() { return mTitle;}
    public String getDate() { return mDate;}
    public String getTime() { return mTime;}
    public String getSectionName() { return mSectionName;}
    public String getAuthor() { return mAuthor;}
    public String getUrl() { return mUrl;}


}
