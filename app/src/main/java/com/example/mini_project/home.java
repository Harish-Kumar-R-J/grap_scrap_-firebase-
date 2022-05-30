package com.example.mini_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
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

public class home extends AppCompatActivity {

    RecyclerView recyclerView;
    adapter adapter_obj;
    EditText search;
    Button add, logout, print, disp, user_page; Button user_profile;
    List<String> p_name = new ArrayList<>();
    List<String> p_desc = new ArrayList<>();
    List<String> p_price = new ArrayList<>();
    List<String> p_image = new ArrayList<>();

    List<String> root_document = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        search = findViewById(R.id.home_search);
        user_page = findViewById(R.id.user_profile);
        user_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home.this, com.example.mini_project.user_profile.class));
            }
        });



        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .build());
//        user_page = findViewById(R.id.user_page);
//        add = findViewById(R.id.product_add);
//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(home.this, product_add.class));
//            }
//        });
//
//        logout = findViewById(R.id.product_logout);
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                SharedPreferences sharedPreferences = getSharedPreferences("uid", MODE_PRIVATE);
//                SharedPreferences.Editor sharedprefs = sharedPreferences.edit();
//                sharedprefs.clear();
//                sharedprefs.apply();
//                startActivity(new Intent(home.this, MainActivity.class));
//            }
//        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());

            }
        });



        get_root(new For_root() {
            @Override
            public void onCallback(List<String> list) {
                for(int i=0;i<root_document.size();i++)
                {String doc_name = root_document.get(i);
                    get_collection(new For_collection() {
                        @Override
                        public void onCallback(List<String> list) {
                            for(String s: list)
                            {get_document(new FirestoreCallback() {
                                    @Override
                                    public void onCallback(List<String> name, List<String> price, List<String> desc)
                                    {p_name = name;
                                     p_desc = desc;
                                     p_price = price;
                                        LinearLayoutManager mainlayoutManager = new LinearLayoutManager(home.this, LinearLayoutManager.VERTICAL, false);
                                        RecyclerView main_recycler = findViewById(R.id.recycler_home);
                                        main_recycler.setLayoutManager(mainlayoutManager);
//    main_screen_adapter main_screen_adapter = new main_screen_adapter(MainActivity.this, districts, notes, active, confirmed, migrated, deceased, recovered, d_confirmed, d_recovered, d_deceased);
                                        adapter_obj = new adapter(home.this,p_name, p_desc, p_price,p_image, null);
                                        main_recycler.setAdapter(adapter_obj);}
                                }, doc_name, s);}
                        }
                    }, root_document.get(i));

}

            }
        });

//        print = findViewById(R.id.product_print);
//        print.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
// }
//        });
//
//        disp = findViewById(R.id.product_disp);
//        disp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.out.println(p_name.size());
//                for (int i = 0; i < p_name.size(); i++) {
//                    System.out.println(p_name.get(i) + " = p_name");
//                    System.out.println(p_price.get(i));
//                    System.out.println(p_desc.get(i));}
//
////                recyclerView = findViewById(R.id.recycler_home);
////                recyclerView.setLayoutManager(new LinearLayoutManager(home.this));
////                adapter_obj = new adapter(home.this,p_name, p_desc, p_price);
////                recyclerView.setAdapter(adapter_obj);
//
//
//
//            }
//        });

    }

    void filter(String text)
    {ArrayList<String> a = new ArrayList<>();
        for(String item : p_name)
        {
            if(item.toLowerCase().contains(text.toLowerCase()))
            {a.add(item);
//    filteredList.add(new whatever_name(item.getDistrict(), item.getNotes(), item.getActive(), item.getMigrated(), item.getConfirmed(), item.getDeceased(), item.getRecovered(), item.getD_confirmed(), item.getD_deceased(), item.getD_recovered()));
                System.out.println(item + " item.getDistrict()");
            }

        }

//        Madapter.filterList(a);
        LinearLayoutManager mainlayoutManager = new LinearLayoutManager(home.this, LinearLayoutManager.VERTICAL, false);
        RecyclerView main_recycler = findViewById(R.id.recycler_home);
        main_recycler.setLayoutManager(mainlayoutManager);
//    main_screen_adapter main_screen_adapter = new main_screen_adapter(MainActivity.this, districts, notes, active, confirmed, migrated, deceased, recovered, d_confirmed, d_recovered, d_deceased);
        adapter_obj = new adapter(home.this,a, p_desc, p_price,p_image, null);
        main_recycler.setAdapter(adapter_obj);
    }

    void get_root(For_root for_root)
    {
        FirebaseFirestore.getInstance()
            .collection("root")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {

                        for (DocumentSnapshot document : task.getResult()) {
                            root_document.add(document.getString("email"));
                        }for_root.onCallback(root_document);

                    }
                }
            });
    }

    private interface For_root
    {void onCallback(List<String> list);}

    void get_collection(For_collection for_collection, String doc_name)
        {List<String> collection = new ArrayList<>();
            System.out.println();
            FirebaseFirestore.getInstance()
                    .collection(doc_name)
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

    private interface For_collection
    {void onCallback(List<String> list);}

    void get_document(FirestoreCallback firestoreCallback, String doc_name, String collection) {
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
                        firestoreCallback.onCallback(p_name, p_price, p_desc);
                    }
                });
    }

    private interface FirestoreCallback
    {void onCallback(List<String> name, List<String> price, List<String> desc);}

}