package com.boala.mathtrainer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.Random;

public class Calculos extends AppCompatActivity {
    private TextView calc;
    private int a,b,c;
    private ImageButton regen;
    private EditText res;
    private String calcType;
    private ProgressBar timer;
    private MyTimer myTimer;
    private RecyclerView rvRes;
    private ResAdapter adapter;
    private ArrayList<Result> resData;
    private int time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        calcType = intent.getStringExtra(Menu.EXTRA_STRING);
        calc = findViewById(R.id.calc);
        regen = findViewById(R.id.regenB);
        res = findViewById(R.id.res);
        timer = findViewById(R.id.timer);
        rvRes = findViewById(R.id.rvRes);
        timer.setMax(1000);
        res.requestFocus();
        regen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                res.setText("");
                createCalc(10);
            }
        });
        res.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE){
                    checkRes();
                    return true;
                }
                return false;
            }
            });
        createCalc(10);
        resData = new ArrayList<>();
        rvRes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ResAdapter(this,resData);
        rvRes.setAdapter(adapter);
    }
    //creacion de los calculos segun la opcion seleccionada
    void createCalc(long time){
        if (myTimer!=null){
            myTimer.cancel();
        }
        switch (calcType){
            case "suma":
                a = randomInt(100);
                b = randomInt(100);
                c = a+b;
                calc.setText(a+"+"+b+"=");
                break;
            case "resta":
                a = randomInt(100);
                b = randomInt(100);
                if (a == b){
                    a++;
                }
                c = a-b;
                calc.setText(a+"-"+b+"=");
                break;
            case "multiplicacion":
                a = randomInt(100);
                b = randomInt(10);
                c = a*b;
                calc.setText(a+"x"+b+"=");
                break;
            case "division":
                a = randomInt(100);
                b = randomInt(10);
                c = a/b;
                calc.setText(a+"/"+b+"=");
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
        Result result;
        try {
            resV = Integer.parseInt(res.getText().toString());
        }catch (Exception e){
            e.printStackTrace();
            resV = 0;
        }
        if (resV == c){
            Toast.makeText(getApplicationContext(),"correcto",Toast.LENGTH_SHORT).show();
            result = new Result(calc.getText()+(res.getText().toString()),true);
            resData.add(result);
            adapter.notifyDataSetChanged();
            createCalc(10);
            res.setText("");
            return true;
        }else{
            Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
            if (resV!=0){
            ObjectAnimator anim = ObjectAnimator.ofInt(res, "textColor", Color.BLACK,Color.RED,Color.BLACK);
            anim.setDuration(1000);
            anim.setEvaluator(new ArgbEvaluator());
            anim.setRepeatCount(1);
            anim.start();}
            result = new Result(calc.getText()+(res.getText().toString()),false);
            resData.add(result);
            adapter.notifyDataSetChanged();
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
        int aux;//variable auxiliar para poder asignar el tiempo
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
            checkRes();
            res.setText("");
            createCalc(10);
        }
    }
    //generador de numeros aleatorios
    public int randomInt(int range){
        int random = new Random().nextInt(range)+1;
        return random;
    }
}
