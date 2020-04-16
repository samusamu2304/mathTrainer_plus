package com.boala.mathtrainer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Random;

public class Calculos extends AppCompatActivity {
    private TextView calc;
    private int a,b,c;
    private ImageButton check;
    private EditText res;
    private String calcType;
    private ProgressBar timer;
    private MyTimer myTimer;
    private RecyclerView rvRes;
    private ResAdapter adapter;
    private ArrayList<Result> resData;
    private int time = 0;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculo);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        calcType = intent.getStringExtra(Menu.EXTRA_STRING);
        time = intent.getIntExtra("time",10);
        calc = findViewById(R.id.calc);
        check = findViewById(R.id.regenB);
        res = findViewById(R.id.res);
        timer = findViewById(R.id.timer);
        rvRes = findViewById(R.id.rvRes);
        timer.setMax(1000);
        res.requestFocus();
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkRes();
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
        createCalc(time);
        resData = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvRes.setLayoutManager(linearLayoutManager);
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
                do {
                    a = randomInt(100);
                    b = randomInt(10);
                    c = a/b;
                }while (b>a||a%b!=0);
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
            //Toast.makeText(getApplicationContext(),"correcto",Toast.LENGTH_SHORT).show();
            result = new Result(calc.getText()+(res.getText().toString()),true);
            resData.add(result);
            adapter.notifyDataSetChanged();
            createCalc(time);
            res.setText("");
            rvRes.smoothScrollToPosition(adapter.getItemCount());
            if (mAuth.getCurrentUser() != null) {
                setPoints();
            }
            return true;
        }else{
            //Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
            //animacion innecesaria por nueva metodologia
            /*if (resV!=0){
            ObjectAnimator anim = ObjectAnimator.ofInt(res, "textColor", Color.BLACK,Color.RED,Color.BLACK);
            anim.setDuration(1000);
            anim.setEvaluator(new ArgbEvaluator());
            anim.setRepeatCount(1);
            anim.start();}*/
            if (res.getText().toString().equals("")){
                res.setText("0");
            }
            result = new Result(calc.getText()+(res.getText().toString()),false);
            res.setText("");
            resData.add(result);
            adapter.notifyDataSetChanged();
            rvRes.smoothScrollToPosition(adapter.getItemCount());
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
            createCalc(time);
        }
    }
    //generador de numeros aleatorios
    public int randomInt(int range){
        int random = new Random().nextInt(range)+1;
        return random;
    }
    public void setPoints(){
        int points = 0;
        switch (calcType){
            case "multiplicacion":
                points = (int) ((110-time)*0.3);
                break;
            case "division":
                points = (int) ((110-time)*0.2);
                break;
            default:
                points = (int) ((110-time)*0.1);
                break;
        }
        final double finalPoints = points;
        db.collection("usersPoints").document(mAuth.getUid()).update("points", FieldValue.increment(points));
    }

    @Override
    protected void onPause() {
        super.onPause();
        myTimer.cancel();
    }
}
