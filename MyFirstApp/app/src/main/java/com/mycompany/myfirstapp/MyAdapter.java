package com.mycompany.myfirstapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

/**
 * Created by tianchen on 21/05/15.
 * Adapter class for List view in MyActivity class.
 *
 */
class MyAdapter extends BaseAdapter {

    static class ViewHolder
    {
        public TextView tag;
        public ImageView image;
        public TextView title;
        public TextView ffr;
        public TextView content;
        public TextView responses;
    }

    private Context context;
    private LayoutInflater mInflater = null;
    private DataReader dr =  new DataReader();
    private List posts;

    public List getPosts() {
        return posts;
    }

    public MyAdapter(Context context) throws IOException {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        posts = dr.readJsonStream(context.getResources().openRawResource(R.raw.community));
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ((DataReader.Post)posts.get(position)).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if(convertView == null)
        {
            holder = new ViewHolder();

            convertView = mInflater.inflate(R.layout.posts_list, null);
            holder.tag = (TextView)convertView.findViewById(R.id.tag);
            holder.ffr = (TextView)convertView.findViewById(R.id.ffr_last_commentor);
            holder.title = (TextView)convertView.findViewById(R.id.title);
            holder.image = (ImageView) convertView.findViewById(R.id.profile_image);
            holder.content = (TextView)convertView.findViewById(R.id.content);
            holder.responses = (TextView)convertView.findViewById(R.id.responses_no);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

            DataReader.Post post = (DataReader.Post) posts.get(position);
            holder.tag.setText(post.getTag()+ " >");
            holder.ffr.setText(
                    ((DataReader.Comment) post.getComments().get(0))
                            .getAuthor().getFirst_name() + " Responsed:");
            holder.title.setText(post.getTitle());
            String url = ((DataReader.Comment) post.getComments().get(0))
                .getAuthor().getProfile_image();
            Picasso.with(context).load(url).resize(60, 60).centerCrop().into(holder.image);

            holder.content.setText(post.getContent());
            holder.responses.setText(post.getComments().size() + " Responses");


        return convertView;
    }
}
