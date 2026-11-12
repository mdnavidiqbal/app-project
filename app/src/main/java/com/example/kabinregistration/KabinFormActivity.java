package com.example.kabinregistration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class KabinFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kabin_form);

        int userId = new SessionManager(this).getUserId();

        // One user = one application. If already applied, do not show a second form.
        AppDatabase.databaseWriteExecutor.execute(() -> {
            KabinApplication existing = AppDatabase.get(this).kabinDao().getByUserId(userId);
            if (existing != null) {
                runOnUiThread(() -> {
                    Toast.makeText(this,
                            "You have already submitted an application. View status in profile.",
                            Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, ProfileActivity.class));
                    finish();
                });
            }
        });

        EditText etGroom = findViewById(R.id.etGroomName);
        EditText etBride = findViewById(R.id.etBrideName);
        EditText etGroomNid = findViewById(R.id.etGroomNid);
        EditText etBrideNid = findViewById(R.id.etBrideNid);
        EditText etDate = findViewById(R.id.etMarriageDate);
        EditText etDenmohor = findViewById(R.id.etDenmohor);
        EditText etAddress = findViewById(R.id.etAddress);
        EditText etWitness1 = findViewById(R.id.etWitness1);
        EditText etWitness2 = findViewById(R.id.etWitness2);
        Button btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(v -> {
            EditText[] fields = {etGroom, etBride, etGroomNid, etBrideNid,
                    etDate, etDenmohor, etAddress, etWitness1, etWitness2};
            for (EditText field : fields) {
                if (field.getText().toString().trim().isEmpty()) {
                    Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            AppDatabase.databaseWriteExecutor.execute(() -> {
                AppDatabase.get(this).kabinDao().insert(new KabinApplication(
                        userId,
                        etGroom.getText().toString().trim(),
                        etBride.getText().toString().trim(),
                        etGroomNid.getText().toString().trim(),
                        etBrideNid.getText().toString().trim(),
                        etDate.getText().toString().trim(),
                        etDenmohor.getText().toString().trim(),
                        etAddress.getText().toString().trim(),
                        etWitness1.getText().toString().trim(),
                        etWitness2.getText().toString().trim()
                ));
                runOnUiThread(() -> {
                    Toast.makeText(this, "Application submitted successfully (Pending)", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, ProfileActivity.class));
                    finish();
                });
            });
        });
    }
}
