package com.example.kabinregistration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SessionManager session = new SessionManager(this);
        int userId = session.getUserId();
        if (userId == -1) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        TextView tvWelcome = findViewById(R.id.tvWelcome);
        TextView tvStatus = findViewById(R.id.tvStatus);
        Button btnProfile = findViewById(R.id.btnProfile);
        Button btnKabin = findViewById(R.id.btnKabin);
        Button btnLogout = findViewById(R.id.btnLogout);

        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.get(this);
            User user = db.userDao().getById(userId);
            KabinApplication application = db.kabinDao().getByUserId(userId);
            runOnUiThread(() -> {
                tvWelcome.setText("Welcome, " + (user != null ? user.fullName : "User"));
                if (application == null) {
                    tvStatus.setText("Application Status: Not Applied Yet");
                } else if ("ACCEPTED".equals(application.status)) {
                    tvStatus.setText("Application Status: Accepted");
                    btnKabin.setText("Marriage Registration (Submitted)");
                } else {
                    tvStatus.setText("Application Status: Pending");
                    btnKabin.setText("Marriage Registration (Submitted)");
                }
            });
        });

        btnProfile.setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class)));
        btnKabin.setOnClickListener(v ->
                startActivity(new Intent(this, KabinFormActivity.class)));
        btnLogout.setOnClickListener(v -> {
            session.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
