package com.hyphenate.easeim.section.moment.ui.square;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.hyphenate.easeim.section.moment.activity.post.PostActivity;
import com.hyphenate.easeim.databinding.FragmentSquareBinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.easeim.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SquareFragment extends Fragment {

    private SquareViewModel mViewModel;
    private FragmentSquareBinding binding;

    public static SquareFragment newInstance() {
        return new SquareFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSquareBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(SquareViewModel.class);
        InitUI();  //初始化UI控件
        return binding.getRoot();
    }


    /**
     * 初始化UI控件
     */
    private void InitUI(){
        binding.squareNavFragment.post(new Runnable() {
            @Override
            public void run() {
                BindNav();  //绑定导航栏
            }
        });

        //发布动态按钮
        FloatingActionButton postBtn = binding.postBtn;
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PostActivity.class);
                startActivity(intent);
            }
        });

    }


    /**
     * 绑定底部导航栏
     */
    private void BindNav(){
        //TODO
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.follow, R.id.recommend).build();
        NavController navController = Navigation.findNavController(getActivity(),R.id.square_nav_fragment);
        NavigationUI.setupActionBarWithNavController((AppCompatActivity) getActivity(), navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.squareNavView, navController);
    }


}