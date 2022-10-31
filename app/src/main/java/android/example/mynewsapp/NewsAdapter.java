package android.example.mynewsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Locale;

public class NewsAdapter extends ArrayAdapter<android.example.mynewsapp.NewsInfo> {

    public NewsAdapter(Context context, ArrayList<android.example.mynewsapp.NewsInfo> arrayList) {
        super (context, 0, arrayList);
    }

@NonNull
@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listView = convertView;

        if (listView ==null) {
            listView= LayoutInflater.from(getContext()).inflate(R.layout.list_layout, parent,false);
        }
        //Find the news article position
        NewsInfo mNewsInfo = getItem(position);

        TextView title = listView.findViewById(R.id.title);
        title.setText(mNewsInfo.getTitle());

        TextView date = listView.findViewById(R.id.date);
        date.setText(mNewsInfo.getDate());

        TextView time = listView.findViewById(R.id.time);
        time.setText(mNewsInfo.getTime());

        TextView author = listView.findViewById(R.id.author);
        author.setText("" + mNewsInfo.getAuthor());

        TextView sectionName = listView.findViewById(R.id.sectionName);
        sectionName.setText(mNewsInfo.getSectionName());

        return listView;
}

    }

