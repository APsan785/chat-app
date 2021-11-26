package com.example.firebase1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebase1.models.Messages;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {

    ArrayList<Messages> msgList;

    final int OUTGOING_KEY = 1;
    final int INCOMING_KEY = 2;

    public ChatAdapter( ArrayList<Messages> msgList) {
        this.msgList = msgList;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == OUTGOING_KEY){
            return new SenderViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.outgoing_chat_bubble, parent, false));
        }else{
            return new ReceiverViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.incoming_chat_bubble, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messages model = msgList.get(position);
        if(holder.getClass().equals(SenderViewHolder.class)){
            ((SenderViewHolder) holder).tv_msg.setText(model.getText());
            ((SenderViewHolder) holder).timeStamp.setText(model.getHours()+":"+model.getMins());
        }else{
            ((ReceiverViewHolder) holder).tv_msg.setText(model.getText());
            ((ReceiverViewHolder) holder).timeStamp.setText(model.getHours()+":"+model.getMins());
        }
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_msg;
        public TextView timeStamp;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_msg = itemView.findViewById(R.id.msg_outgoing);
            timeStamp = itemView.findViewById(R.id.time_out);
        }
    }
    public class ReceiverViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_msg;
        public TextView timeStamp;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_msg = itemView.findViewById(R.id.msg_incoming);
            timeStamp = itemView.findViewById(R.id.time_in);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(msgList.get(position).getSenderId().equals(FirebaseAuth.getInstance().getUid())){
            return OUTGOING_KEY;
        }else{
            return INCOMING_KEY;
        }
    }
}
