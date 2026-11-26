package test;

import java.sql.Date;

public class Post {
    private int post_id;
    private String user_id;
    private String title;
    private String content;
    private Date created_at;

    public int getPost_id() { return post_id; }
    public void setPost_id(int post_id) { this.post_id = post_id; }
    public String getUser_id() { return user_id; }
    public void setUser_id(String user_id) { this.user_id = user_id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Date getCreated_at() { return created_at; }
    public void setCreated_at(Date created_at) { this.created_at = created_at; }
}