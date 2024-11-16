package com.example.formulairesae2.Entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


@Entity(
        tableName = "comments",
        foreignKeys = {
                @ForeignKey(entity = User.class,
                        parentColumns = "id",
                        childColumns = "user_id",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Book.class,
                        parentColumns = "id",
                        childColumns = "book_id",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class Comment {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String content;
    private String author;
    private int user_id;
    private int book_id;
    private int like;

    public Comment(String content, String author, int user_id, int book_id) {
        this.content = content;
        this.author = author;
        this.user_id = user_id;
        this.book_id = book_id;
    }
    public Comment(){}


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public int getUser_id() { return user_id; }
    public void setUser_id(int user_id) { this.user_id = user_id; }

    public int getBook_id() { return book_id; }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public void setBook_id(int book_id) { this.book_id = book_id; }
}
