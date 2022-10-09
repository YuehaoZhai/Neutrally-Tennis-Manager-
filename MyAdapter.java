package com.example.neutrallytennis;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    private ArrayList<User> mUserList;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User currentUser = mUserList.get(position);
        holder.emailContent.setText(currentUser.Email);
        holder.passwordContent.setText(currentUser.Password);
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView emailTitle;
        public TextView emailContent;
        public TextView passwordTitle;
        public TextView passwordContent;
        public CardView cardView;

        public MyViewHolder(View view){
            super(view);
            emailTitle = view.findViewById(R.id.eTitle);
            emailContent = view.findViewById(R.id.donaEmail);
            passwordTitle = view.findViewById(R.id.pTitle);
            passwordContent = view.findViewById(R.id.donaPassword);
            cardView = view.findViewById(R.id.userCard);

        }
    }

    public MyAdapter(ArrayList<User> userList){
        mUserList = userList;
    }


}
