package com.example.firebase1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.firebase1.databinding.ActivityUserChatsBinding;
import com.example.firebase1.fragments.ChatsFragment;
import com.example.firebase1.models.Messages;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class UserChatsActivity extends AppCompatActivity {

    ActivityUserChatsBinding binding;
    String SenderUid, ReceiverUid;
    DatabaseReference chatRef;
    ArrayList<Messages> chatMsgList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserChatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        Intent intent = getIntent();

        String Username =  intent.getStringExtra(ChatsFragment.username);
        SenderUid = FirebaseAuth.getInstance().getUid();
        ReceiverUid = intent.getStringExtra(ChatsFragment.uid);
        String Profilepic = intent.getStringExtra(ChatsFragment.profilepic);

        binding.username.setText(Username);
        Picasso.get().load(Profilepic).placeholder(R.drawable.ic_baseline_person_24).into(binding.profileImage);

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserChatsActivity.this, MainActivity.class));
            }
        });

        // RecyclerView
        chatMsgList = new ArrayList<>();
        final ChatAdapter adpt = new ChatAdapter(chatMsgList);
        binding.ChatrecyclerView.setAdapter(adpt);
        binding.ChatrecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Database
        chatRef = FirebaseDatabase.getInstance().getReference().child("Chat Messages");
        chatRef.child(SenderUid + ReceiverUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatMsgList.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Messages displaymsg = snapshot1.getValue(Messages.class);
                    chatMsgList.add(displaymsg);
                }
                adpt.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void sendMsg(View v){
        String currentmsg = binding.etMsg.getText().toString();
        binding.etMsg.setText("");
        TimeZone timeZone = TimeZone.getDefault();
        Calendar calendar = Calendar.getInstance(timeZone);
        final Messages newMsg = new Messages(currentmsg, ReceiverUid, SenderUid, new Date().getTime(),
        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        String SenderRoom = SenderUid + ReceiverUid;
        final String ReceiverRoom = ReceiverUid + SenderUid;
        chatRef.child(SenderRoom).push().setValue(newMsg)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        chatRef.child(ReceiverRoom).push().setValue(newMsg);
                    }
                });
    }
}