package com.example.formulairesae2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formulairesae2.Entity.Comment;
import com.example.formulairesae2.Service.CommentService;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context context;
    private List<Comment> commentList;
    private CommentService commentService;

    public CommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
        this.commentService = new CommentService(context); // Initialize the service
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);

        // Set comment data
        holder.contentTextView.setText(comment.getContent());
        holder.authorTextView.setText(comment.getAuthor());

        // Display the like count using getLike()
        holder.btnLike.setText("Like (" + comment.getLike() + ")");

        // Set up the like button click listener
        holder.btnLike.setOnClickListener(v -> {
            // Increment like count
            incrementLike(position, comment);
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    private void incrementLike(int position, Comment comment) {
        int currentLikeCount = comment.getLike();
        int newLikeCount = currentLikeCount + 1;

        // Update the like count in the database
        commentService.updateLikeCount(newLikeCount, comment.getId());

        // Update the local comment object
        comment.setLike(newLikeCount);

        // Notify the adapter that the data for this specific item has changed
        notifyItemChanged(position);
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView contentTextView;
        TextView authorTextView;
        TextView btnLike;

        public CommentViewHolder(View itemView) {
            super(itemView);
            contentTextView = itemView.findViewById(R.id.comment_content);
            authorTextView = itemView.findViewById(R.id.comment_author);
            btnLike = itemView.findViewById(R.id.btn_like);
        }
    }
}
