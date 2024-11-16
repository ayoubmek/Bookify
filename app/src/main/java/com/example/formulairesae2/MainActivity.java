package com.example.formulairesae2;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formulairesae2.Entity.Comment;
import com.example.formulairesae2.Entity.User;
import com.example.formulairesae2.Service.CommentService;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnAddComment;
    private RecyclerView recyclerView;
    private EditText etCommentContent;
    private CommentService commentService;
    private MyDatabaseHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up navigation to CommentActivityBack
        MaterialCardView cardView = findViewById(R.id.btnGoToCommentActivity);
        cardView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CommentActivityBack.class);
            startActivity(intent);
        });

        // Initialize components
        dbHelper = new MyDatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewComments);
        btnAddComment = findViewById(R.id.btnAddComment);
        etCommentContent = findViewById(R.id.etCommentContent);
        commentService = new CommentService(this);

        // Add comment logic
        btnAddComment.setOnClickListener(v -> {
            String content = etCommentContent.getText().toString().trim();
            int bookId = 1;  // Book ID is hardcoded for now
            int userId = 1;  // User ID is hardcoded for now

            User user = commentService.getUserById(userId);
            String author = (user != null) ? user.getName() : "Unknown";

            if (!content.isEmpty()) {
                try {
                    // Add comment and display a success message
                    commentService.addComment(content, author, bookId, userId);
                    Toast.makeText(MainActivity.this, "Comment added successfully", Toast.LENGTH_SHORT).show();

                    // Clear the comment content field
                    etCommentContent.setText("");

                    // Refresh the comments list
                    showAllComments();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error adding comment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Please fill the field!", Toast.LENGTH_SHORT).show();
            }
        });

        // Show all comments initially
        showAllComments();
    }

     private void showAllComments() {
        List<Comment> comments = commentService.showAllComments();

        if (comments != null && !comments.isEmpty()) {
            CommentAdapter adapter = new CommentAdapter(this, comments);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No comments found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
         commentService.closeDatabase();
    }
}
