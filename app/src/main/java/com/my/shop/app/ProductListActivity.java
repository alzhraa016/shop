package com.my.shop.app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProductListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;
    private List<String> imagesList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private TextView userNameTv;
    private ProgressBar progressBar;

    // Create object of ViewPager2
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        userNameTv = findViewById(R.id.userNameTv);
        progressBar = findViewById(R.id.progressBar);
        viewPager2 = findViewById(R.id.viewpager);

        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        //get userName
        DocumentReference docRef = db.collection("users").document(Objects.requireNonNull(mAuth.getUid()));
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // DocumentSnapshot contains the data read from the Firestore document
                    // You can access the data using the getData() method
                    Map<String, Object> data = documentSnapshot.getData();
                    if (data.containsKey("fullName")) {
                        String fullName = (String) data.get("fullName");
                        userNameTv.setText(fullName);
                    }
                }
            }
        });

        setUpRecycler();
        fetchImages();
    }

    private void setUpRecycler() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns grid

        productList = new ArrayList<>();
        adapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(adapter);

        // Fetch product data from Firebase Database
        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("pro_list", document.getId() + " => " + document.getData());
                                // Convert Firestore document to Product object
                                Product product = Product.fromDocumentSnapshot(document);
                                productList.add(product);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.w("pro_list", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void fetchImages() {
        progressBar.setVisibility(View.VISIBLE);
        // Fetch product list data from Firebase Database
        db.collection("products_list")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            imagesList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("pro_list", document.getId() + " => " + document.getData());
                                String path = document.getString("image");
                                imagesList.add(path);
                            }
                            setUpViewpager(imagesList);
                        } else {
                            Log.w("pro_list", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    private void setUpViewpager(List<String> images) {
        WormDotsIndicator wormDotsIndicator = findViewById(R.id.worm_dots_indicator);
        ViewPager2Adapter viewPager2Adapter = new ViewPager2Adapter(this, images);

        viewPager2.setAdapter(viewPager2Adapter);
        wormDotsIndicator.attachTo(viewPager2);

        // To get swipe event of viewpager2
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            // This method is triggered when there is any scrolling activity for the current page
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            // triggered when you select a new page
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            // triggered when there is
            // scroll state will be changed
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }
}