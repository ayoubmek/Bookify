package com.example.formulairesae2.Service;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.formulairesae2.Entity.Comment;
import com.example.formulairesae2.Entity.User;
import com.example.formulairesae2.Entity.Book;

import java.util.List;

@Dao
public interface CommentDao {

    @Insert
    void insert(Comment comment);

    @Transaction
    @Query("SELECT * FROM comments WHERE user_id = :userId")
    List<Comment> getCommentsByUser(int userId);

    @Transaction
    @Query("SELECT * FROM user WHERE id = :user_Id")
    User getUserWithComments(int user_Id);

}
