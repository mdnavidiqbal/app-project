package com.example.kabinregistration;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        EditText etName = findViewById(R.id.etFullName);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPhone = findViewById(R.id.etPhone);
        EditText etPassword = findViewById(R.id.etPassword);
        EditText etConfirm = findViewById(R.id.etConfirmPassword);
        Button btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String password = etPassword.getText().toString();
            String confirm = etConfirm.getText().toString();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Name, email, and password are required", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.equals(confirm)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create User in Firebase Auth
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String uid = firebaseUser != null ? firebaseUser.getUid() : "";

                            // Save to Firebase Firestore
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("uid", uid);
                            userData.put("fullName", name);
                            userData.put("email", email);
                            userData.put("phone", phone);
                            userData.put("createdAt", System.currentTimeMillis());

                            firestore.collection("users").document(uid).set(userData)
                                    .addOnSuccessListener(aVoid -> {
                                        // Save to Room as fallback
                                        AppDatabase.databaseWriteExecutor.execute(() -> {
                                            AppDatabase db = AppDatabase.get(this);
                                            User localUser = db.userDao().getByEmail(email);
                                            if (localUser == null) {
                                                db.userDao().insert(new User(name, email, PasswordUtil.hash(password), phone));
                                            }
                                        });

                                        Toast.makeText(this, "Registration successful! Please login.", Toast.LENGTH_SHORT).show();
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Registration successful! Please login.", Toast.LENGTH_SHORT).show();
                                        finish();
                                    });
                        } else {
                            String error = task.getException() != null ? task.getException().getMessage() : "Registration failed";
                            if (error != null && error.contains("CONFIGURATION_NOT_FOUND")) {
                                error = "Please enable 'Email/Password' provider in Firebase Console -> Authentication -> Sign-in method.";
                            }
                            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}
