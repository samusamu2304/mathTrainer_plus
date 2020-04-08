package com.boala.mathtrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class Calculos extends AppCompatActivity {
    TextView calc;
    int a,b,c;
    Button comp;
    ImageButton regen;
    EditText res;
    String calcType;
    ProgressBar timer;
    MyTimer myTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        calcType = intent.getStringExtra(Menu.EXTRA_STRING);
        calc = findViewById(R.id.calc);
        regen = findViewById(R.id.regenB);
        comp = findViewById(R.id.compB);
        res = findViewById(R.id.res);
        timer = findViewById(R.id.timer);
        timer.setMax(1000);
        res.requestFocus();
        regen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                res.setText("");
                createCalc(10);
            }
        });
        comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkRes();
            }
        });
        res.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE){
                    comp.performClick();
                    return true;
                }
                return false;
            }
            });
        createCalc(10);
    }
    //creacion de los calculos segun la opcion seleccionada
    void createCalc(long time){
        if (myTimer!=null){
            myTimer.cancel();
        }
        a = new Random().nextInt(100)+1;
        b = new Random().nextInt(100)+1;
        switch (calcType){
            case "suma":
                c = a+b;
                calc.setText(a+"+"+b+"=");
                break;
            case "resta":
                c = a-b;
                calc.setText(a+"-"+b+"=");
                break;
        }
        timer.setProgress(1000);
        //creacion del temporizador, el tiempo se asigna en segundos
        myTimer = new MyTimer(time,1);
        myTimer.start();
    }
    //comprobacion del resultado
    public boolean checkRes(){
        int resV;
        try {
            resV = Integer.parseInt(res.getText().toString());
        }catch (Exception e){
            e.printStackTrace();
            resV = 0;
        }
        if (resV == c){
            Toast.makeText(getApplicationContext(),"correcto",Toast.LENGTH_SHORT).show();
            res.setText("");
            createCalc(10);
            return true;
        }else{
            Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
            ObjectAnimator anim = ObjectAnimator.ofInt(res, "textColor", Color.BLACK,Color.RED,Color.BLACK);
            anim.setDuration(1000);
            anim.setEvaluator(new ArgbEvaluator());
            anim.setRepeatCount(1);
            anim.start();
            return false;
        }
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    //clase temporizador
    public class MyTimer extends CountDownTimer{
        int aux;
        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture*1000, countDownInterval);
            aux = (int) millisInFuture;
        }

        @Override
        public void onTick(long l) {
            int progress = (int) (l/aux);
            timer.setProgress(progress);
        }

        @Override
        public void onFinish() {
            timer.setProgress(0);
            res.setText("");
            createCalc(10);
        }
    }
}
