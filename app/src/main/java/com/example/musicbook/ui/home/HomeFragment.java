package com.example.musicbook.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.musicbook.ChatActivity;
import com.example.musicbook.ChatListActivity;
import com.example.musicbook.R;
import com.example.musicbook.SearchActivity;
import com.example.musicbook.databinding.FragmentHomeBinding;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    ImageView mSearchImV,mChatImV;
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        homeViewModel =
//                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(HomeViewModel.class);

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
        View root = binding.getRoot();


//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//
//            }
//        });


        mChatImV = root.findViewById(R.id.chat_imv);
        mSearchImV=root.findViewById(R.id.iv_search);
        
        mSearchImV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });
        mChatImV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChatListActivity.class));
            }
        });
        return root;

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    
//    public void onSearchClick(View view){
//        Toast.makeText(getActivity(), "Button Search Click", Toast.LENGTH_SHORT).show();
//    }
}