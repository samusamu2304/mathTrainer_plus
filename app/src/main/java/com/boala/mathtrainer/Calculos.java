package com.boala.mathtrainer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class Calculos extends AppCompatActivity {
    TextView calc;
    int a,b,c;
    Button regen,comp;
    EditText res;
    String extra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        extra = intent.getStringExtra(Menu.EXTRA_STRING);
        calc = findViewById(R.id.calc);
        regen = findViewById(R.id.regenB);
        comp = findViewById(R.id.compB);
        res = findViewById(R.id.res);
        res.requestFocus();
        regen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCalc();
            }
        });
        comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int resV = Integer.parseInt(res.getText().toString());
                if (resV == c){
                    Toast.makeText(getApplicationContext(),"correcto",Toast.LENGTH_SHORT).show();
                    res.setText("");
                    createCalc();
                }else{
                    Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
                    ObjectAnimator anim = ObjectAnimator.ofInt(res, "textColor", Color.BLACK,Color.RED,Color.BLACK);
                    anim.setDuration(1000);
                    anim.setEvaluator(new ArgbEvaluator());
                    anim.setRepeatCount(1);
                    anim.start();
                }
            }
        });
        createCalc();
    }
    void createCalc(){
        a = new Random().nextInt(100)+1;
        b = new Random().nextInt(100)+1;
        switch (extra){
            case "suma":
                c = a+b;
                calc.setText(a+"+"+b+"=");
                break;
            case "resta":
                c = a-b;
                calc.setText(a+"-"+b+"=");
                break;
        }
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
