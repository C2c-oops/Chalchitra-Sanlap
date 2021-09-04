package com.c2c.chalchitrasanlap.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.c2c.chalchitrasanlap.R;
import com.c2c.chalchitrasanlap.listener.UsersListeners;
import com.c2c.chalchitrasanlap.models.User;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private List<User> users;
    private UsersListeners usersListeners;
    private List<User> selectedUser;

    public UsersAdapter(List<User> users, UsersListeners usersListeners) {
        this.users = users;
        this.usersListeners = usersListeners;
        selectedUser = new ArrayList<>();
    }

    public List<User> getSelectedUser() {
        return selectedUser;
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

    class UserViewHolder extends RecyclerView.ViewHolder {

        TextView txtFirstChar;
        TextView txtUsername;
        TextView txtEmail;

        ImageView imgVoiceCall;
        ImageView imgVideoCall;

        ConstraintLayout userContainer;
        ImageView imgUserSelected;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFirstChar = itemView.findViewById(R.id.txtFirstChar);
            txtUsername = itemView.findViewById(R.id.txtUserName);
            txtEmail = itemView.findViewById(R.id.txtEmail);

            imgVoiceCall = itemView.findViewById(R.id.imgVoiceCall);
            imgVideoCall = itemView.findViewById(R.id.imgVideoCall);

            userContainer = itemView.findViewById(R.id.userContainer);
            imgUserSelected = itemView.findViewById(R.id.imgUserSelected);
        }

        void setUserData(User user) {
            txtFirstChar.setText(user.firstName.substring(0, 1));
            txtUsername.setText(String.format("%s %s", user.firstName, user.lastName));
            txtEmail.setText(user.email);
            imgVoiceCall.setOnClickListener(v -> usersListeners.initiateAudioMeeting(user));
            imgVideoCall.setOnClickListener(v -> usersListeners.initiateVideoMeeting(user));

            userContainer.setOnLongClickListener(v-> {
                if (imgUserSelected.getVisibility() != View.VISIBLE) {
                    selectedUser.add(user);
                    imgUserSelected.setVisibility(View.VISIBLE);
                    imgVideoCall.setVisibility(View.GONE);
                    imgVoiceCall.setVisibility(View.GONE);
                    usersListeners.onMultipleUsersAction(true);
                }
                return true;
            });

            userContainer.setOnClickListener(v-> {
                if(imgUserSelected.getVisibility() == View.VISIBLE) {
                    selectedUser.remove(user);
                    imgUserSelected.setVisibility(View.GONE);
                    imgVideoCall.setVisibility(View.VISIBLE);
                    imgVoiceCall.setVisibility(View.VISIBLE);
                    if (selectedUser.isEmpty()) {
                        usersListeners.onMultipleUsersAction(false);
                    }
                } else {
                    if (selectedUser.size() > 0) {
                        selectedUser.add(user);
                        imgUserSelected.setVisibility(View.VISIBLE);
                        imgVideoCall.setVisibility(View.GONE);
                        imgVoiceCall.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
}
