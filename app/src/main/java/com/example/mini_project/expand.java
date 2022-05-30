package com.example.mini_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class expand extends AppCompatActivity {
    TextView name,price,desc;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand);
        name = findViewById(R.id.expand_name_edit);
        price = findViewById(R.id.p_price_expand);
        desc = findViewById(R.id.p_desc_expand);
        img = findViewById(R.id.expand_img);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        name.setText(b.getString("name"));
        price.setText(b.getString("price"));
        desc.setText(b.getString("desc"));
        Glide.with(expand.this).load(b.getString("url")).into(img);
    }
}