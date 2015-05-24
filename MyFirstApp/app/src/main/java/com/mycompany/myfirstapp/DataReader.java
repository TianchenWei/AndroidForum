package com.mycompany.myfirstapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This class is used to deal with a json file
 * holding data for the community.
 * There are three inner class for initiating the post,
 * the comment and the author objects relatively, which are all parcelable.
 * With readJsonStream(InputStream in), a list of sorted post for the json file
 * will be returned, containing all useful information.
 * Created by tianchen on 21/05/15.
 */
public class DataReader {

    /*
    * This class initiates a comparator used to sort posts
    * according to time they were created. The latest post
    * will be the first item on a list.
    * */

    static class PostComparator implements Comparator<Post> {
        @Override
        public int compare(Post p1, Post p2) {
            return (int) (p2.getTime_created()-p1.getTime_created());
        }
    }

    static class Post implements Parcelable{

        private long id;
        private String title, content, tag;
        private int views;
        private long time_created;
        private Author author;
        private List<Comment> comments = new ArrayList<>();

        Post(long id, String title, String content, String tag, int views,
             long time_created, Author author, List<Comment> comments){
            this.id = id;
            this.title = title;
            this.content = content;
            this.tag = tag;
            this.views = views;
            this.time_created = time_created;
            this.author = author;
            this.comments = comments;
        }

        public Post(Parcel in) {
            title = in.readString();
            content = in.readString();
            views = in.readInt();
            time_created = in.readLong();
            author = in.readParcelable(Author.class.getClassLoader());
            in.readList(comments, Comment.class.getClassLoader());
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(title);
            dest.writeString(content);
            dest.writeInt(views);
            dest.writeLong(time_created);
            dest.writeParcelable(author, 0);
            dest.writeList(comments);

        }

        public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
            public Post createFromParcel(Parcel in) {
                return new Post(in);
            }

            public Post[] newArray(int size) {
                return new Post[size];
            }
        };

        public long getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public String getTag() {
            return tag;
        }

        public int getViews() {
            return views;
        }

        public long getTime_created() {
            return time_created;
        }

        public Author getAuthor() {
            return author;
        }

        public List<Comment> getComments() {
            return comments;
        }

    }

    static class Author implements Parcelable{

        private long id;
        private String first_name, last_name, profile_image = null;

        public Author(long id, String first, String last, String image){
            this.id = id;
            this.first_name = first;
            this.last_name = last;
            this.profile_image = image;
        }

        public Author(Parcel in) {
            id = in.readLong();
            first_name = in.readString();
            last_name = in.readString();
            profile_image = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(id);
            dest.writeString(first_name);
            dest.writeString(last_name);
            dest.writeString(profile_image);
        }

        public static final Parcelable.Creator<Author> CREATOR = new Parcelable.Creator<Author>() {
            public Author createFromParcel(Parcel in) {
                return new Author(in);
            }

            public Author[] newArray(int size) {
                return new Author[size];
            }
        };

        public long getId() {
            return id;
        }

        public String getFirst_name() {
            return first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public String getProfile_image() {
            return profile_image;
        }
    }

    static class Comment implements Parcelable{

        private long id;
        private String content;
        private long time_created;
        private Author author;

        public Comment(long id, long time_created, String content, Author author){
            this.id = id;
            this.time_created = time_created;
            this.content = content;
            this.author = author;
        }

        public Comment(Parcel in) {
            id = in.readLong();
            time_created = in.readLong();
            content = in.readString();
            author = in.readParcelable(Author.class.getClassLoader());
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(id);
            dest.writeLong(time_created);
            dest.writeString(content);
            dest.writeParcelable(author,0);
        }

        public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
            public Comment createFromParcel(Parcel in) {
                return new Comment(in);
            }

            public Comment[] newArray(int size) {
                return new Comment[size];
            }
        };

        public long getId() {
            return id;
        }

        public String getContent() {
            return content;
        }

        public long getTime_created() {
            return time_created;
        }

        public Author getAuthor() {
            return author;
        }
    }


    public DataReader(){
    }

    public List readJsonStream(InputStream in) throws IOException {

        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readPostsArray(reader);
        }finally{
            reader.close();
        }
    }

    public List readPostsArray(JsonReader reader) throws IOException {
        List posts = new ArrayList();

        reader.beginArray();
        while (reader.hasNext()) {
            posts.add(readPost(reader));
        }
        reader.endArray();
        //in time order
        Collections.sort(posts, new PostComparator());
        return posts;
    }

    public Post readPost(JsonReader reader) throws IOException {
        long id = -1;
        String title = null;
        String content = null;
        String tag = null;
        int views = 0;
        long time_created = 0;
        Author author = null;
        List comments = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                id = reader.nextLong();
            } else if (name.equals("title")) {
                title = reader.nextString();
            } else if (name.equals("content")) {
                content = reader.nextString();
            } else if (name.equals("tag")) {
                tag = reader.nextString();
            } else if (name.equals("views")) {
                views = reader.nextInt();
            } else if (name.equals("time_created")) {
                time_created = reader.nextLong();
            } else if (name.equals("author")) {
                author = readAuthor(reader);
            } else if (name.equals("comments") && reader.peek() != JsonToken.NULL) {
                comments = readCommentsArray(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Post(id, title, content, tag, views, time_created, author, comments);
    }

    public List readCommentsArray(JsonReader reader) throws IOException {
        List comments = new ArrayList();

        reader.beginArray();
        while (reader.hasNext()) {
            comments.add(readComment(reader));
        }
        reader.endArray();
        return comments;
    }

    public Comment readComment(JsonReader reader) throws IOException {
        long id = -1;
        String content = null;
        long time_created = 0;
        Author author = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                id = reader.nextLong();
            } else if (name.equals("content")) {
                content = reader.nextString();
            } else if (name.equals("time_created")) {
                time_created = reader.nextLong();
            } else if (name.equals("author")) {
                author = readAuthor(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Comment(id, time_created, content, author);
    }

    public Author readAuthor(JsonReader reader) throws IOException {
        long id = -1;
        String first_name = null;
        String last_name = null;
        String profile_image = null;


        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (reader.peek()==JsonToken.NULL) {
                reader.skipValue();
            }else {
                if (name.equals("id")) {
                    id = reader.nextLong();
                } else if (name.equals("first_name")) {
                    first_name = reader.nextString();
                } else if (name.equals("last_name")) {
                    last_name = reader.nextString();
                } else if (name.equals("profile_image")) {
                    profile_image = reader.nextString();
                } else {
                    reader.skipValue();
                }

            }

        }
        reader.endObject();

        return new Author(id, first_name, last_name, profile_image);
    }

}
