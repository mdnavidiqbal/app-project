package com.example.kabinregistration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        SessionManager session = new SessionManager(this);

        // If already logged in, go straight to Home.
        if (session.isLoggedIn()) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvGoRegister = findViewById(R.id.tvGoRegister);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString();
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Firebase Authentication Sign In
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                session.saveUserUid(user.getUid());
                            }
                            // Also sync Room database ID
                            AppDatabase.databaseWriteExecutor.execute(() -> {
                                User localUser = AppDatabase.get(this).userDao().getByEmail(email);
                                if (localUser != null) {
                                    session.saveUserId(localUser.id);
                                }
                            });

                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, HomeActivity.class));
                            finish();
                        } else {
                            String errorMsg = task.getException() != null ? task.getException().getMessage() : "Invalid email or password";
                            if (errorMsg != null && errorMsg.contains("CONFIGURATION_NOT_FOUND")) {
                                errorMsg = "Please enable 'Email/Password' provider in Firebase Console -> Authentication -> Sign-in method.";
                            }

                            final String finalErrorMsg = errorMsg;
                            // Fallback to local Room Database check
                            AppDatabase.databaseWriteExecutor.execute(() -> {
                                User localUser = AppDatabase.get(this).userDao().getByEmail(email);
                                runOnUiThread(() -> {
                                    if (localUser != null && localUser.passwordHash.equals(PasswordUtil.hash(password))) {
                                        session.saveUserId(localUser.id);
                                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(this, HomeActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(this, finalErrorMsg, Toast.LENGTH_LONG).show();
                                    }
                                });
                            });
                        }
                    });
        });

        tvGoRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }
}
