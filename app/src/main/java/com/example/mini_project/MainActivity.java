package com.example.mini_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText u_name, pass;
    Button login, sign_up;
    FirebaseAuth f_auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        u_name = findViewById(R.id.main_uname);
        pass = findViewById(R.id.main_password);
        login = findViewById(R.id.main_button);
        sign_up = findViewById(R.id.main_register);
        SharedPreferences sharedPreferences = getSharedPreferences("uid", MODE_PRIVATE);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
            startActivity(new Intent(MainActivity.this, home.class));



        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
                {startActivity(new Intent(MainActivity.this, register.class));}
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticate();
            }
        });

    }
    @Override
    public void onBackPressed() {
        finishAffinity();

    }
    private void authenticate()
    {f_auth = FirebaseAuth.getInstance();

        f_auth.signInWithEmailAndPassword(u_name.getText().toString(), pass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();

                SharedPreferences sharedPreferences = getSharedPreferences("uid", MODE_PRIVATE);
                SharedPreferences.Editor shared = sharedPreferences.edit();
                shared.putString("uid", f_auth.getUid());
                shared.apply();

                startActivity(new Intent(MainActivity.this, home.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
            }
        });


    }

}