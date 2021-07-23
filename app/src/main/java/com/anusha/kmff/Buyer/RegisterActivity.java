package com.anusha.kmff.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.anusha.kmff.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.ImageView;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText registerName, registerPassword, registerPhone;
    private ImageView imageView;
    private Button registerBtn;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        android.widget.ImageView imageView = findViewById(R.id.register_logo);
        String s = "https://firebasestorage.googleapis.com/v0/b/kmf-cart.appspot.com/o/category%2FKMF%20CART.png?alt=media&token=ef132298-d677-401b-bffc-b299ae5e0545";
        Glide.with(getApplicationContext()).load(s).into(imageView);

        registerBtn = findViewById(R.id.register_btn);
        registerName = findViewById(R.id.register_user_name);
        registerPassword = findViewById(R.id.register_password);
        registerPhone = findViewById(R.id.register_phone_number);
        progressDialog = new ProgressDialog(this);


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              createAccount();
            }
        });


    }

    private void createAccount() {
        String name = registerName.getText().toString();
        String phone = registerPhone.getText().toString();
        String password = registerPassword.getText().toString();

        if (name.isEmpty())
        {
            Toast.makeText(this, "please enter your name..!", Toast.LENGTH_SHORT).show();
        }
        else if (phone.isEmpty())
        {
            Toast.makeText(this, "please enter your phone number..!", Toast.LENGTH_SHORT).show();
        }
        else if (password.isEmpty())
        {
            Toast.makeText(this, "please enter your password...!", Toast.LENGTH_SHORT).show();
        }
        else
            {
            progressDialog.setTitle("Loading..");
            progressDialog.setMessage("Please wait while we are creating your account....!");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            validateUser(name, phone, password);
        }

    }

    private void validateUser(String name, String phone, String password)
    {
        final DatabaseReference databaseReference;

        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (!(dataSnapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String, Object> registeruser = new HashMap<>();
                    registeruser.put("name", name);
                    registeruser.put("phone", phone);
                    registeruser.put("password", password);
                    databaseReference.child("Users").child(phone).updateChildren(registeruser)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Congratulations,your account is created", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(i);
                                        progressDialog.dismiss();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Network issue:please try again after some time...!", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                }
                else
                    {
                    Toast.makeText(RegisterActivity.this, "This number exist already ,please try another number", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}