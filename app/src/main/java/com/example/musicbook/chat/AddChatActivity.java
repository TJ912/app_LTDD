package com.example.musicbook.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.TaskStackBuilder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.musicbook.R;
import com.example.musicbook.ui.adapter.ChatListAdapter;
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

public class AddChatActivity extends AppCompatActivity {

    ImageView mBackImv, mSearchImv;
    EditText mSearchEdt;
    RecyclerView mUserListRV;
    List<User> userList;
    FirebaseUser fUser;
    ProgressBar mProgess;
    SwipeRefreshLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chat);

        init();
    }

    public void init() {
        mProgess = findViewById(R.id.addchat_list_pb);
        mBackImv = findViewById(R.id.addchat_back_mess_iv);
        mSearchImv = findViewById(R.id.add_chat_search_iv);
        mSearchEdt = findViewById(R.id.add_chat_edt_search);
        mUserListRV = findViewById(R.id.add_chat_list_rv);


        userList = new ArrayList<>();
        mProgess = new ProgressBar(getApplicationContext());
        loading(true);
        userList = getUserList();


        ChatListAdapter adapter = new ChatListAdapter(userList, this);
        mUserListRV.setHasFixedSize(true);
        mUserListRV.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull @NotNull Rect outRect, int itemPosition, @NonNull @NotNull RecyclerView parent) {
                super.getItemOffsets(outRect, itemPosition, parent);
                outRect.bottom = 10;
                outRect.top = 10;
            }
        };
        mUserListRV.setItemViewCacheSize(10);
        mUserListRV.setAdapter(adapter);


        mBackImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSearchImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading(true);
                userList = getUserList(mSearchEdt.getText().toString());
                adapter.notifyDataSetChanged();
            }
        });
        mSearchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loading(true);
                userList = getUserList(String.valueOf(s));
                Log.d("Editalbe", String.valueOf(s));
                adapter.notifyDataSetChanged();

            }
        });
    }

    private List<User> getUserList(String name) {

        //get current user
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        //get path of database name "Users" containg user info
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        //get all data from path
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                userList.clear();
                loading(false);
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User user = ds.getValue(User.class);

                    //get all users except current signed in user
                    if (!user.getUid().equals(fUser.getUid())) {
                        if (user.getName().toLowerCase().trim().contains(name.toLowerCase().trim())) {
                            userList.add(user);

                        }


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }

        });

        return userList;
    }

    private List<User> getUserList() {

        //get current user
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        //get path of database name "Users" containg user info
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");


        //get all data from path
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User user = ds.getValue(User.class);

                    //get all users except current signed in user
                    if (user.getUid().equals(fUser.getUid()) == false) {
                        userList.add(user);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        loading(false);
        return userList;
    }

    public void loading(boolean isLoading) {
        if (isLoading) {
            mProgess.setVisibility(View.VISIBLE);


        } else {
            mProgess.setVisibility(View.INVISIBLE);

        }

    }

    @Override
    public void onPrepareSupportNavigateUpTaskStack(@NonNull TaskStackBuilder builder) {
        super.onPrepareSupportNavigateUpTaskStack(builder);

    }
}