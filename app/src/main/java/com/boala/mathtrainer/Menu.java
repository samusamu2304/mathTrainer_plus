package com.boala.mathtrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {
    public static final String EXTRA_STRING = "operacion";
    Button suma, resta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        suma = findViewById(R.id.suma);
        resta = findViewById(R.id.resta);
        final Intent intent = new Intent(this, Calculos.class);
        suma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra(EXTRA_STRING,"suma");
                startActivity(intent);

            }
        });
        resta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra(EXTRA_STRING,"resta");
                startActivity(intent);

            }
        });
    }
}
