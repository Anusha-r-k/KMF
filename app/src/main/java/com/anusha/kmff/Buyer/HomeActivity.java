package com.anusha.kmff.Buyer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.anusha.kmff.R;
import com.bumptech.glide.Glide;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {

    private ImageView logo;
    private Button mainJoinNowBtn , mainLoginBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

       ImageView logo = findViewById(R.id.logo);
        String s="https://firebasestorage.googleapis.com/v0/b/kmf-cart.appspot.com/o/category%2FKMF%20CART.png?alt=media&token=ef132298-d677-401b-bffc-b299ae5e0545";
        Glide.with(getApplicationContext()).load(s).into(logo);

        mainJoinNowBtn = findViewById(R.id.main_join_now_btn);
        mainLoginBtn = findViewById(R.id.main_login_btn);


        Paper.init(this);

        mainJoinNowBtn.setOnClickListener(v -> {
            Intent i =new Intent(HomeActivity.this, RegisterActivity.class);
            startActivity(i);
        });


        mainLoginBtn.setOnClickListener(v -> {
            Intent i =new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(i);
        });


    }
}