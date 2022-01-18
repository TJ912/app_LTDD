package com.example.musicbook.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicbook.R;
import com.example.musicbook.ui.model.Status;
import com.example.musicbook.ui.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.MyViewHolder> {
    List<Status> statusList;
    LayoutInflater layoutInflater;
    Context context;

    public StatusAdapter(List<Status> statusList, Context context) {
        this.statusList = statusList;
        this.context = context;
    }

    @NonNull

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view =LayoutInflater.from(context).inflate(R.layout.post,viewGroup,false);

        //init recyclerview



        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //get data
        String userImage=statusList.get(position).getImage();
        //   String userName=statusList.get(position).get;
        String userCaption=statusList.get(position).getCaption();

        //set data
        FirebaseDatabase.getInstance().getReference("Users").child(statusList.get(position).getUser_id()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                holder.mNameTv.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.mDateTV.setText(statusList.get(position).getTime());
        holder.mCaptionTv.setText(userCaption);
        try {
            Picasso.get().load(userImage).placeholder(R.drawable.blankavatar).into(holder.mAvatarIv);
        }
        catch(Exception e){

        }
        //handle item click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.likeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Status status=statusList.get(position);
                List<String> listLiked=status.getListLike();
                if(listLiked.indexOf(FirebaseAuth.getInstance().getCurrentUser().getUid())==-1){
                    holder.likeImage.setImageResource(R.drawable.ic_liked);
                    listLiked.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    Log.d("LIKE", ":LIKED");
                }else{
                    holder.likeImage.setImageResource(R.drawable.ic_like);
                    listLiked.remove(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    Log.d("LIKE", ":DISLIKED");
                }

                FirebaseDatabase.getInstance().getReference("Status").child(String.valueOf(status.getId())).setValue(status);
            }
        });


    }



    @Override
    public int getItemCount() {
        return statusList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView mAvatarIv,mPictureIv;
        TextView mNameTv,mCaptionTv,mDateTV;
        RelativeLayout likeLayout;
        ImageView likeImage;

        public MyViewHolder(@NonNull View view) {
            super(view);

            //init views
            mAvatarIv=view.findViewById(R.id.post_avatar_imv);
            mNameTv=view.findViewById(R.id.post_name_tv);
            mDateTV=view.findViewById(R.id.post_time_tv);
            mCaptionTv=view.findViewById(R.id.post_caption_tv);
            likeLayout=view.findViewById(R.id.likeLayout);
            likeImage=view.findViewById(R.id.like_img);

        }
    }
}