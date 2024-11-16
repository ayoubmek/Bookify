package com.example.formulairesae2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formulairesae2.Entity.Comment;
import com.example.formulairesae2.Service.CommentService;

import java.util.List;
public class CommentActivityBack extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CommentAdapter1 commentAdapter;
    private List<Comment> commentList;
    private Button btnAddComment;
    private CommentService commentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        recyclerView = findViewById(R.id.recyclerViewComments);
        btnAddComment = findViewById(R.id.btnAddComment);
        commentService = new CommentService(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

         loadComments();


        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommentActivityBack.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadComments() {
        // Fetch the comments from the database
        commentList = commentService.showAllComments();
        commentAdapter = new CommentAdapter1(this, commentList);
        recyclerView.setAdapter(commentAdapter);
    }
}
