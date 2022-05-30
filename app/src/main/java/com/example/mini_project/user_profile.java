package com.example.mini_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class user_profile extends AppCompatActivity {
        Button logout, add;
        EditText email;
    adapter adapter_obj;
        List<String> p_name = new ArrayList<>();
    List<String> p_desc = new ArrayList<>();
    List<String> p_price = new ArrayList<>();
    List<String> p_image = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        add = findViewById(R.id.prod_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(user_profile.this, product_add.class));
            }
        });

        logout = findViewById(R.id.logout_btn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(user_profile.this, MainActivity.class));
            }
        });

        email = findViewById(R.id.user_email);
        email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
List<String> docx = new ArrayList<>();
    get_collection(new for_collection() {
        @Override
        public void onCallback(List<String> collection) {
        for(String s: collection)
        {get_document(new Get_documents() {
            @Override
            public void onCallback(List<String> name, List<String> price, List<String> desc, List<String> url) {
                System.out.println(s + " jjjjjjjj");
                docx.add(s);
                LinearLayoutManager mainlayoutManager = new LinearLayoutManager(user_profile.this, LinearLayoutManager.VERTICAL, false);
                RecyclerView main_recycler = findViewById(R.id.recycler_user_profile);
                main_recycler.setLayoutManager(mainlayoutManager);
//    main_screen_adapter main_screen_adapter = new main_screen_adapter(MainActivity.this, districts, notes, active, confirmed, migrated, deceased, recovered, d_confirmed, d_recovered, d_deceased);
                adapter_obj = new adapter(user_profile.this,name, desc, price,url,collection);
                main_recycler.setAdapter(adapter_obj);
            }
        }, s, FirebaseAuth.getInstance().getCurrentUser().getEmail());

        }
        }
        });
            }

    private interface for_collection
    {void onCallback(List<String> collection);}

    private interface Get_documents
    {void onCallback(List<String> name, List<String> price, List<String> desc, List<String>url);}

    void get_collection(user_profile.for_collection for_collection)
    {List<String> collection = new ArrayList<>();
        System.out.println();
        FirebaseFirestore.getInstance()
                .collection(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot document : task.getResult())
                        {collection.add(document.getId());}
                        for_collection.onCallback(collection);
                    }
                });
    }


    void get_document(user_profile.Get_documents get_documents, String collection, String doc_name) {
        System.out.println("in get_document");
        getter_setter gs = new getter_setter();
        DocumentSnapshot document;
        FirebaseFirestore.getInstance()
                .collection(doc_name)
                .document(collection)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        p_name.add(documentSnapshot.getString("name"));
                        p_desc.add(documentSnapshot.getString("desc"));
                        p_price.add(documentSnapshot.getString("price"));
                        p_image.add(documentSnapshot.getString("url"));
                        get_documents.onCallback(p_name, p_price, p_desc, p_image);
                    }
                });
    }

}