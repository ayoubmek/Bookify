package com.example.formulairesae2.Service;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.formulairesae2.Entity.User;
import com.example.formulairesae2.MyDatabaseHelper;
import com.example.formulairesae2.Entity.Comment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommentService implements CommentDao{
    private SQLiteDatabase db;

    private CommentDao commentDao;
    private SQLiteDatabase database;
    private MyDatabaseHelper dbHelper;

    public CommentService( ) {

    }

    public CommentService(Context context) {
        dbHelper = new MyDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }


    private void ensureDatabaseIsOpen() {
        if (database == null || !database.isOpen()) {
            database = dbHelper.getWritableDatabase();
        }
    }

    public int removeComment(int commentId) {
        if (database == null || !database.isOpen()) {
            database = dbHelper.getWritableDatabase();
        }


        String selection = MyDatabaseHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(commentId) };

        return database.delete(MyDatabaseHelper.TABLE_COMMENT, selection, selectionArgs);
    }
    public User getUserById(int userId) {
        String query = "SELECT * FROM user WHERE id = ?";
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {

            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
            return new User(userId, name);
        }
        return null;
    }

     public long addComment(String content, String author, int bookId, int userId) {
        ensureDatabaseIsOpen();

        ContentValues values = new ContentValues();
        values.put(MyDatabaseHelper.COLUMN_CONTENT, content);
        values.put(MyDatabaseHelper.COLUMN_AUTHOR, author);
        values.put(MyDatabaseHelper.COLUMN_BOOK_ID, bookId);
        values.put(MyDatabaseHelper.COLUMN_USER_ID, userId);
         values.put(MyDatabaseHelper.COLUMN_LIKES, 0);
        return database.insert(MyDatabaseHelper.TABLE_COMMENT, null, values);
    }

     public List<Comment> getCommentsByUserId(int userId) {
        List<Comment> comments = new ArrayList<>();

        String[] columns = {
                MyDatabaseHelper.COLUMN_ID,
                MyDatabaseHelper.COLUMN_CONTENT,
                MyDatabaseHelper.COLUMN_AUTHOR,
                MyDatabaseHelper.COLUMN_BOOK_ID,
                MyDatabaseHelper.COLUMN_USER_ID
        };

        String selection = MyDatabaseHelper.COLUMN_USER_ID + " = ?";
        String[] selectionArgs = { String.valueOf(userId) };

        Cursor cursor = null;
        try {
            cursor = database.query(
                    MyDatabaseHelper.TABLE_COMMENT,
                    columns,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Comment comment = new Comment();
                    comment.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_ID)));
                    comment.setContent(cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_CONTENT)));
                    comment.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_AUTHOR)));
                    comment.setBook_id(cursor.getInt(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_BOOK_ID)));
                    comment.setUser_id(cursor.getInt(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_USER_ID)));
                    comments.add(comment);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
         } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return comments;
    }

     public List<Comment> getCommentsByBookId(int bookId) {
        List<Comment> comments = new ArrayList<>();

        String[] columns = {
                MyDatabaseHelper.COLUMN_ID,
                MyDatabaseHelper.COLUMN_CONTENT,
                MyDatabaseHelper.COLUMN_AUTHOR,
                MyDatabaseHelper.COLUMN_BOOK_ID,
                MyDatabaseHelper.COLUMN_USER_ID
        };

        String selection = MyDatabaseHelper.COLUMN_BOOK_ID + " = ?";
        String[] selectionArgs = { String.valueOf(bookId) };

        Cursor cursor = null;
        try {
            cursor = database.query(
                    MyDatabaseHelper.TABLE_COMMENT,
                    columns,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Comment comment = new Comment();
                    comment.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_ID)));
                    comment.setContent(cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_CONTENT)));
                    comment.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_AUTHOR)));
                    comment.setBook_id(cursor.getInt(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_BOOK_ID)));
                    comment.setUser_id(cursor.getInt(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_USER_ID)));
                    comments.add(comment);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            // Log the exception
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return comments;
    }

    public void updateLikeCount(int newLikeCount, int commentId) {
        ensureDatabaseIsOpen();  // Ensure database is open

        ContentValues values = new ContentValues();
        values.put(MyDatabaseHelper.COLUMN_LIKES, newLikeCount);  // Correct column for likes count

        // Update the like count for the specific comment
        int rowsUpdated = database.update(
                MyDatabaseHelper.TABLE_COMMENT,
                values,
                MyDatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(commentId)}
        );

        if (rowsUpdated > 0) {
            Log.i("DatabaseSuccess", "Like count updated successfully.");
        } else {
            Log.e("DatabaseError", "Failed to update like count.");
        }
    }



    public int getLikeCountForComment(int commentId) {
        int likeCount = 0;
        ensureDatabaseIsOpen();
        Cursor cursor = null;
        try {
            cursor = database.query(
                    MyDatabaseHelper.TABLE_COMMENT,
                    new String[]{MyDatabaseHelper.COLUMN_LIKES},
                    MyDatabaseHelper.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(commentId)},
                    null, null, null
            );

            if (cursor != null && cursor.moveToFirst()) {
                likeCount = cursor.getInt(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_LIKES));
            }
        } catch (SQLException e) {
            Log.e("DatabaseError", "Error fetching like count", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return likeCount;
    }


    public List<Comment> showAllComments() {
        List<Comment> comments = new ArrayList<>();

        String[] columns = {
                MyDatabaseHelper.COLUMN_ID,
                MyDatabaseHelper.COLUMN_CONTENT,
                MyDatabaseHelper.COLUMN_AUTHOR,
                MyDatabaseHelper.COLUMN_BOOK_ID,
                MyDatabaseHelper.COLUMN_USER_ID
        };

        Cursor cursor = null;
        try {
            cursor = database.query(
                    MyDatabaseHelper.TABLE_COMMENT,
                    columns,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Comment comment = new Comment();
                    comment.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_ID)));
                    comment.setContent(cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_CONTENT)));
                    comment.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_AUTHOR)));
                    comment.setBook_id(cursor.getInt(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_BOOK_ID)));
                    comment.setUser_id(cursor.getInt(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_USER_ID)));
                    comments.add(comment);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            // Log the exception
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return comments;
    }
     public int updateComment(int commentId, String content, String author) {
        ensureDatabaseIsOpen();

        ContentValues values = new ContentValues();
        values.put(MyDatabaseHelper.COLUMN_CONTENT, content);
        values.put(MyDatabaseHelper.COLUMN_AUTHOR, author);

        String selection = MyDatabaseHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(commentId) };

        return database.update(MyDatabaseHelper.TABLE_COMMENT, values, selection, selectionArgs);
    }

     public int deleteComment(int commentId) {
        ensureDatabaseIsOpen();

        String selection = MyDatabaseHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(commentId) };

        return database.delete(MyDatabaseHelper.TABLE_COMMENT, selection, selectionArgs);
    }

     public void closeDatabase() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    @Override
    public void insert(Comment comment) {

    }

    @Override
    public List<Comment> getCommentsByUser(int userId) {
        return Collections.emptyList();
    }


    @Override
    public User getUserWithComments(int userId) {
        return commentDao.getUserWithComments(userId);
    }
}
