package com.example.kabinregistration;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        int userId = new SessionManager(this).getUserId();
        TextView tvName = findViewById(R.id.tvName);
        TextView tvEmail = findViewById(R.id.tvEmail);
        TextView tvPhone = findViewById(R.id.tvPhone);
        TextView tvApplication = findViewById(R.id.tvApplication);
        TextView tvStatus = findViewById(R.id.tvStatus);
        Button btnAcceptDemo = findViewById(R.id.btnAcceptDemo);

        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.get(this);
            User user = db.userDao().getById(userId);
            KabinApplication application = db.kabinDao().getByUserId(userId);
            runOnUiThread(() -> {
                tvName.setText("Name: " + (user != null ? user.fullName : ""));
                tvEmail.setText("Email: " + (user != null ? user.email : ""));
                String phone = (user != null && user.phone != null && !user.phone.isEmpty())
                        ? user.phone : "Not provided";
                tvPhone.setText("Phone: " + phone);

                if (application == null) {
                    tvApplication.setText("Marriage Application: Not Applied Yet");
                    tvStatus.setText("Status: N/A");
                    btnAcceptDemo.setVisibility(View.GONE);
                } else {
                    tvApplication.setText("Marriage Application: Groom " + application.groomName
                            + ", Bride " + application.brideName
                            + "\nMarriage Date: " + application.marriageDate
                            + "\nDower (Mahr): " + application.denmohor + " BDT");
                    if ("ACCEPTED".equals(application.status)) {
                        tvStatus.setText("Status: Accepted");
                        btnAcceptDemo.setVisibility(View.GONE);
                    } else {
                        tvStatus.setText("Status: Pending");
                        // Demo only: lets you show both states in a presentation.
                        // In a real system an admin would change this status.
                        btnAcceptDemo.setVisibility(View.VISIBLE);
                        btnAcceptDemo.setOnClickListener(v ->
                                AppDatabase.databaseWriteExecutor.execute(() -> {
                                    application.status = "ACCEPTED";
                                    db.kabinDao().update(application);
                                    runOnUiThread(() -> {
                                        Toast.makeText(this,
                                                "Demo: Application has been Accepted",
                                                Toast.LENGTH_SHORT).show();
                                        recreate();
                                    });
                                }));
                    }
                }
            });
        });
    }
}
