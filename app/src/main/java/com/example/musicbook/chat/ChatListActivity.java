package com.example.musicbook.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.TaskStackBuilder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicbook.R;
import com.example.musicbook.ui.adapter.ChatListAdapter;
import com.example.musicbook.ui.adapter.MessageAdapter;
import com.example.musicbook.ui.model.Messages;
import com.example.musicbook.ui.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChatListActivity extends AppCompatActivity {
    ImageView mBackImv, mAddImv;
    TextView mNoMessTV;
    RecyclerView mChatListRV;
    List<User> chatList;
    FirebaseUser fUser;
    ProgressBar mProgess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        init();
    }

    public void init() {
        mProgess = findViewById(R.id.chat_list_pb);
        mBackImv = findViewById(R.id.back_mess_iv);
        mAddImv = findViewById(R.id.add_mess_iv);
        mNoMessTV = findViewById(R.id.no_mess_tv);
        mChatListRV = findViewById(R.id.chat_list_rv);

        mNoMessTV.setVisibility(View.GONE);
        chatList = new ArrayList<>();
        mProgess = new ProgressBar(getApplicationContext());
        loading(true);
        chatList = getUserList();


        ChatListAdapter adapter = new ChatListAdapter(chatList, this);
        mChatListRV.setHasFixedSize(true);
        mChatListRV.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull @NotNull Rect outRect, int itemPosition, @NonNull @NotNull RecyclerView parent) {
                super.getItemOffsets(outRect, itemPosition, parent);
                outRect.bottom = 10;
                outRect.top = 10;
            }
        };
        mChatListRV.setItemViewCacheSize(10);
        mChatListRV.setAdapter(adapter);

//        if (chatList.isEmpty()) {
//            mNoMessTV.setVisibility(View.VISIBLE);
//        }

        mBackImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAddImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddChatActivity.class));
            }
        });
    }

    private List<User> getUserList() {

        //get current user
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        int uidLength = fUser.getUid().length();

        //get path of database name "Users" containg user info
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Chats");

        //get all data from path
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                chatList.clear();
                loading(false);
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User user = ds.getValue(User.class);

                    //get all users except current signed in user
                    if (!user.getUid().equals(fUser.getUid())) {


                        }


                    }
                }


            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        loading(false);
        return chatList;
    }

    public void loading(boolean isLoading) {
        if (isLoading) {
            mProgess.setVisibility(View.VISIBLE);
            mNoMessTV.setVisibility(View.INVISIBLE);
            mChatListRV.setVisibility(View.INVISIBLE);

        } else {
            mProgess.setVisibility(View.INVISIBLE);
            mNoMessTV.setVisibility(View.VISIBLE);
            mChatListRV.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onPrepareSupportNavigateUpTaskStack(@NonNull TaskStackBuilder builder) {
        super.onPrepareSupportNavigateUpTaskStack(builder);

    }
}