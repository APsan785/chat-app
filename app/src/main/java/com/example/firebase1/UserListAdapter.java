package com.example.firebase1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebase1.models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private ArrayList<Users> arr;
    private onItemClickListener mListener;

    public UserListAdapter(ArrayList<Users> arr){
            this.arr = arr;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cardview,parent,false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Users users = arr.get(position);
        if(users.getProfilepic()!=null){
        Picasso.get().load(users.getProfilepic()).into(holder.pfp);}
        holder.username.setText(users.getUsername());
        holder.lastmsg.setText("");
        //getting Last msg from Firebase
        FirebaseDatabase.getInstance().getReference().child("Chat Messages")
                .child(FirebaseAuth.getInstance().getUid()+users.getUid())
                .orderByChild("timeStamp")
                .limitToLast(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren()){
                            for(DataSnapshot snapshot1:snapshot.getChildren()){
                            holder.lastmsg.setText(snapshot1.child("text").getValue(String.class));}
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() { return arr.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username, lastmsg;
        public ImageView pfp;
        public ViewHolder(@NonNull View itemView, final onItemClickListener listener) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            lastmsg = itemView.findViewById(R.id.lastmsg);
            pfp = itemView.findViewById(R.id.pfp);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!= null){
                        int position = getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
    public interface onItemClickListener{
        void onItemClick(int position);
    }
    public void setonItemClickListener(onItemClickListener listener){
        mListener = listener;
    }
}
