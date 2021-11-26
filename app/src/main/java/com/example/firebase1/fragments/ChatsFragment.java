package com.example.firebase1.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.firebase1.R;
import com.example.firebase1.UserChatsActivity;
import com.example.firebase1.UserListAdapter;
import com.example.firebase1.models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatsFragment extends Fragment {

    private ArrayList<Users> list;
    private FirebaseDatabase db;
    private FirebaseAuth auth;
    UserListAdapter adpt;
    public static String username = "Username";
    public static String uid = "UserId";
    public static String profilepic = "Profile Pic";
    private int loading_check = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv = getView().findViewById(R.id.UserRecycler);
        list = new ArrayList<>();
        adpt = new UserListAdapter(list);
        adpt.setonItemClickListener(new UserListAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Users users = list.get(position);
                Intent intent = new Intent(getContext(), UserChatsActivity.class);
                intent.putExtra("Username", users.getUsername());
                intent.putExtra("UserId", users.getUid());
                intent.putExtra("Profile Pic", users.getProfilepic());
                startActivity(intent);
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        rv.setAdapter(adpt);

        final ProgressBar progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);

        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        DatabaseReference ref = db.getReference().child("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users users = dataSnapshot.getValue(Users.class);
                    if(!auth.getUid().equals(users.getUid())){
                        list.add(users);
                    }
                }

                adpt.notifyDataSetChanged();
                progressBar.setVisibility(ProgressBar.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}