package com.mycompany.myfirstapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class SecondActivity extends AppCompatActivity {

    private DataReader.Post post;//the post to be shown
    private ListView commentsList;//its comments under

    public SecondActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        commentsList = (ListView) findViewById(R.id.comment_list);
        post = (DataReader.Post) this.getIntent().getParcelableExtra("post");
        setTitle(post.getTag());
        setContentView(R.layout.activity_second);

        ((TextView) findViewById(R.id.title)).setText(post.getTitle());
        ((TextView) findViewById(R.id.content)).setText(post.getContent());
        ((TextView) findViewById(R.id.views_no)).
                setText(String.valueOf(post.getViews())+" Views");
        ((TextView) findViewById(R.id.comments_no)).
                setText(String.valueOf(post.getComments().size()) + " Comments");
        ((TextView) findViewById(R.id.poster)).
                setText("Posted by " + post.getAuthor().getFirst_name());
        ((TextView) findViewById(R.id.time_posted)).
                setText(calculateTime(post.getTime_created()*1000));//times in file in seconds

        commentsList = (ListView) findViewById(R.id.comment_list);

        BaseAdapter adapter = new BaseAdapter() {

            private LayoutInflater mInflater = LayoutInflater.from(SecondActivity.this);

            class ViewHolder
            {
                public TextView name;
                public TextView time;
                public TextView content;
                public ImageView image;
            }

            @Override
            public int getCount() {
                return post.getComments().size();
            }

            @Override
            public Object getItem(int position) {
                return post.getComments().get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {


                DataReader.Comment comment = (DataReader.Comment) post.getComments().get(position);
                ViewHolder holder = null;

                if(convertView == null)
                {
                    holder = new ViewHolder();
                    convertView = mInflater.inflate(R.layout.comments_list,null);
                    holder.name = (TextView)convertView.findViewById(R.id.commentor_name);
                    holder.time = (TextView)convertView.findViewById(R.id.commented_time);
                    holder.content = (TextView)convertView.findViewById(R.id.comment_content);
                    holder.image = (ImageView)convertView.findViewById(R.id.avatar);

                    convertView.setTag(holder);
                }else{
                    holder = (ViewHolder)convertView.getTag();
                }

                holder.name.setText(comment.getAuthor().getFirst_name());
                holder.time.setText(calculateTime(comment.getTime_created() * 1000));
                holder.content.setText(comment.getContent());
                String url = comment.getAuthor().getProfile_image();
                Picasso.with(SecondActivity.this).load(url).resize(50, 50).centerCrop().into(holder.image);

                return convertView;
            }
        };
        commentsList.setAdapter(adapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    * Show differences between current and past time
    * in either year/month/day/hour/min/second
    * @param: past is unix time in millisecond
    * */
    public String calculateTime(long past){

        long passed = System.currentTimeMillis()-past;

        //in seconds
        passed = passed/1000;
        if(passed<60){
            if(Math.floor(passed)==1)
                return "1 Second Ago";
            return String.valueOf(passed) + " Seconds Ago";

        }
        //in minutes
        passed = passed/60;
        if(passed<60) {
            if (Math.floor(passed) == 1)
                return "1 Minute Ago";
            return String.valueOf((int) Math.floor(passed)) + " Minutes Ago";

        }
        //in hours
        passed = passed/60;
        if(passed<24){
            if(Math.floor(passed)==1)
                return "1 hour Ago";
            return String.valueOf((int) Math.floor(passed)) + " Hours Ago";

        }
        //TO DO days/months/years not accurate here!!find better methods later
        //in days
        passed = passed/24;
        if(passed<31){
            if(Math.floor(passed)==1)
                return "1 Day Ago";
            return String.valueOf((int) Math.floor(passed)) + " Days Ago";
        }

        //in months
        passed = passed/30;
        if(passed<12){
            if(Math.floor(passed)==1)
                return "1 Month Ago";
            return String.valueOf((int) Math.floor(passed)) + " Months Ago";
        }
        //in years
        passed = passed/12;

        if(Math.floor(passed)==1)
            return "1 Year Ago";
        return String.valueOf((int) Math.floor(passed)) + " Years Ago";
    }

}
