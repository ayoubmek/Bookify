package com.example.formulairesae2;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formulairesae2.Entity.Comment;
import com.example.formulairesae2.Service.CommentService;

import java.util.List;
public class CommentAdapter1 extends RecyclerView.Adapter<CommentAdapter1.CommentViewHolder> {

    private Context context;
    private List<Comment> commentList;
    private CommentService commentService;

    public CommentAdapter1(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
        this.commentService = new CommentService(context);
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_itemback, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Comment comment = commentList.get(position);
        holder.commentContent.setText(comment.getContent());
        holder.commentAuthor.setText(comment.getAuthor());

         holder.btnRemoveComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeComment(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

     private void removeComment(int position) {
         int commentId = commentList.get(position).getId();

         int rowsAffected = commentService.removeComment(commentId);

        if (rowsAffected > 0) {
             commentList.remove(position);
            notifyItemRemoved(position);
        }
    }

     public static class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView commentContent;
        TextView commentAuthor;
         Button btnRemoveComment;

        public CommentViewHolder(View itemView) {
            super(itemView);
            commentContent = itemView.findViewById(R.id.comment_content);
            commentAuthor = itemView.findViewById(R.id.comment_author);

            btnRemoveComment = itemView.findViewById(R.id.btnRemoveComment);
        }
    }
}
