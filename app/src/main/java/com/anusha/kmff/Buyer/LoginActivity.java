package com.anusha.kmff.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.anusha.kmff.Model.Users;
import com.anusha.kmff.Prevalent.Prevalent;
import com.anusha.kmff.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private ImageView imageView;
    private EditText InputPassword, InputPhone;
    private ProgressDialog progressDialog;
    private Button loginBtn;
    private String parentDBname = "Users";
    private CheckBox checkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImageView imageView = findViewById(R.id.login_logo);
        String s = "https://firebasestorage.googleapis.com/v0/b/kmf-cart.appspot.com/o/category%2FKMF%20CART.png?alt=media&token=ef132298-d677-401b-bffc-b299ae5e0545";
        Glide.with(getApplicationContext()).load(s).into(imageView);

        InputPassword = findViewById(R.id.login_password_edit_text);
        InputPhone = findViewById(R.id.login_phone_number_edit_text);
        progressDialog = new ProgressDialog(this);
        loginBtn = findViewById(R.id.login_login_btn);
        checkBoxRememberMe = findViewById(R.id.login_checkbox);

        Paper.init(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

    }

    private void loginUser() {
        String phone = InputPhone.getText().toString();
        String password = InputPassword.getText().toString();
        if (phone.isEmpty()) {
            Toast.makeText(this, "please enter your phone number..!", Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            Toast.makeText(this, "please enter your password...!", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setTitle("Logging in....");
            progressDialog.setMessage("Please wait while we are logging into your account....!");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            AuthoriseUser(phone, password);
        }


    }

    private void AuthoriseUser(final String phone, final String password) {

        if (checkBoxRememberMe.isChecked()) {
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }
        final DatabaseReference databaseReference;

        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if (datasnapshot.child(parentDBname).child(phone).exists()) {
                    Users usersData = datasnapshot.child(parentDBname).child(phone).getValue(Users.class);
                    if (usersData.getPhone().equals(phone)) {
                        if (usersData.getPassword().equals(password)) {
                            Toast.makeText(LoginActivity.this, "Logged in successfully...!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Password is incorrect..!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}