package com.my.shop.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextInputEditText fullName, email, password, address, cardNo;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        fullName = findViewById(R.id.editFullname);
        email = findViewById(R.id.editEmailId);
        password = findViewById(R.id.editPassword);
        address = findViewById(R.id.editAddress);
        cardNo = findViewById(R.id.editCard);

        progressBar = findViewById(R.id.progressBar);

        Button signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(view -> {
            if (!Objects.requireNonNull(fullName.getText()).toString().isEmpty() && !Objects.requireNonNull(email.getText()).toString().isEmpty() && !Objects.requireNonNull(password.getText()).toString().isEmpty()) {
                progressBar.setVisibility(View.VISIBLE);
                // Implement sign-up logic with Firebase Auth
                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign Up success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    addData(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(SignUpActivity.this, "Authentication failed."+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                Toast.makeText(SignUpActivity.this, "Empty fields", Toast.LENGTH_SHORT).show();
            }
        });

        MaterialButton signInText = findViewById(R.id.alreadyHaveAccount);
        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                finishAffinity();
            }
        });
    }

    private void addData(FirebaseUser firebaseUser) {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first, middle, and last name
        Map<String, Object> user = new HashMap<>();
        user.put("userId", firebaseUser.getUid());
        user.put("fullName", Objects.requireNonNull(fullName.getText()).toString().trim());
        user.put("address", Objects.requireNonNull(address.getText()).toString().trim());
        user.put("cardNo", Objects.requireNonNull(cardNo.getText()).toString().trim());

// Add a new document with a generated ID
        db.collection("users")
                .document(firebaseUser.getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.GONE);
                        startActivity(new Intent(SignUpActivity.this, ProductListActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUpActivity.this, "Authentication failed."+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}