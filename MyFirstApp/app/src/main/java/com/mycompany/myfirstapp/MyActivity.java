package com.mycompany.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;

/*
* This activity shows a list of posts in the order of time created.
* It contains a list view whose adapter is of MyAdapter class.
* */

public class MyActivity extends AppCompatActivity {

    private ListView postsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = new Intent(this, SecondActivity.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        postsList = (ListView) findViewById(R.id.postslist);

        postsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                go2SecondActivity(position);
            }
        });

        try {
            MyAdapter adapter = new MyAdapter(this);
            postsList.setAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
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
    * @param position: the index of the post clicked
    * Start a new activity show post details
    * */
    public void go2SecondActivity(int position){

        Intent intent = new Intent(this, SecondActivity.class);
        DataReader.Post post = (DataReader.Post)
                (((MyAdapter) postsList.getAdapter()).getPosts().get(position));
        intent.putExtra("post", (Parcelable)post);
        startActivity(intent);

    }
}
