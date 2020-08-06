package com.example.funboxtest.backend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.funboxtest.R;

public class BackEndActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_and);

        ImageButton imageButtonAdd = findViewById(R.id.back_end_toolbar_button_add);
        imageButtonAdd.setOnClickListener(view -> {
            Intent intent = new Intent(this, ItemProductEditActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}
