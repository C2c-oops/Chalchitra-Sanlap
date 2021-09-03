package com.c2c.chalchitrasanlap.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.c2c.chalchitrasanlap.R;
import com.c2c.chalchitrasanlap.models.User;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private List<User> users;

    public UsersAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_user,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {

        TextView txtFirstChar;
        TextView txtUsername;
        TextView txtEmail;

        ImageView imgVoiceCall;
        ImageView imgVideoCall;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFirstChar = itemView.findViewById(R.id.txtFirstChar);
            txtUsername = itemView.findViewById(R.id.txtUserName);
            txtEmail = itemView.findViewById(R.id.txtEmail);

            imgVoiceCall = itemView.findViewById(R.id.imgVoiceCall);
            imgVideoCall = itemView.findViewById(R.id.imgVideoCall);
        }

        void setUserData(User user) {
            txtFirstChar.setText(user.firstName.substring(0, 1));
            txtUsername.setText(String.format("%s %s", user.firstName, user.lastName));
            txtEmail.setText(user.email);
        }
    }
}
