package com.boala.mathtrainer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Random;

public class Calculos extends AppCompatActivity {
    private TextView calc;
    private int a, b, c;
    private ImageButton check;
    private EditText res;
    private String calcType;
    private ProgressBar timer;
    private MyTimer myTimer;
    private RecyclerView rvRes;
    private ResAdapter adapter;
    private ArrayList<Result> resData;
    private int time = 10;
    private int lvl = 1;
    private int timeLeft = 0;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculo);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        calcType = intent.getStringExtra(Menu.EXTRA_STRING);
        getSupportActionBar().setTitle(getResources().getIdentifier(calcType, "string", getPackageName()));
        lvl = intent.getIntExtra("lvl", 1);
        assignTime();
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
                if (i == EditorInfo.IME_ACTION_DONE) {
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
        adapter = new ResAdapter(this, resData);
        rvRes.setAdapter(adapter);
    }

    void suma(int lvl){
        switch (lvl){
            case 1:
            case 2:
                a = randomInt(10);
                b = randomInt(10);
                break;
            case 3:
            case 4:
                a = randomInt(100);
                b = randomInt(100);
                break;
            case 5:
                a = randomInt(1000);
                b = randomInt(1000);
                break;

        }
        c = a + b;
        calc.setText(a + "+" + b + "=");
    }
    void resta(int lvl){
        switch (lvl){
            case 1:
            case 2:
                a = randomInt(10);
                b = randomInt(10);
                break;
            case 3:
            case 4:
                a = randomInt(100);
                b = randomInt(100);
                break;
            case 5:
                a = randomInt(1000);
                b = randomInt(1000);
                break;

        }
        if (a == b) {
            a++;
        }
        c = a - b;
        calc.setText(a + "-" + b + "=");
    }
    void multiplicacion(int lvl){
        switch (lvl){
            case 1:
            case 2:
                a = randomInt(100);
                b = randomInt(9) + 1;
                break;
            case 3:
            case 4:
                a = randomInt(100);
                b = randomInt(90) + 1;
                break;
            case 5:
                a = randomInt(1000);
                b = randomInt(90) + 1;
                break;

        }

        c = a * b;
        calc.setText(a + "x" + b + "=");
    }
    void division(int lvl){
        switch (lvl){
            case 1:
            case 2:
                do {
                    a = randomInt(90) + 10;
                    b = randomInt(9) + 1;
                    c = a / b;
                } while (a % b != 0);
                break;
            case 3:
            case 4:
                do {
                    a = randomInt(900) + 10;
                    b = randomInt(90) + 1;
                    c = a / b;
                } while (a % b != 0);
                break;
            case 5:
                a = randomInt(900) + 10;
                b = randomInt(90) + 1;
                c = a / b;
                break;

        }

        calc.setText(a + "/" + b + "=");
    }

    //creacion de los calculos segun la opcion seleccionada
    void createCalc(long time) {
        if (myTimer != null) {
            myTimer.cancel();
        }
        switch (calcType) {
            case "suma":
                suma(lvl);
                break;
            case "resta":
                resta(lvl);
                break;
            case "multiplicacion":
                multiplicacion(lvl);
                break;
            case "division":
                division(lvl);
                break;
            case "random":

                if (totalPoints < 1000){
                    lvl = 1;
                } else if (totalPoints < 2500){
                    lvl = 2;
                } else if (totalPoints < 5000){
                    lvl = 3;
                } else{
                    lvl = 4;
                }
                switch (randomInt(4)){
                    case 1: suma(lvl); break;
                    case 2: resta(lvl); break;
                    case 3: multiplicacion(lvl); break;
                    case 4: division(lvl); break;
                }
                break;
        }
        timer.setProgress(1000);
        //creacion del temporizador, el tiempo se asigna en segundos
        myTimer = new MyTimer(time, 1);
        myTimer.start();
    }

    //se asigna el tiempo segun el nivel
    public void assignTime(){
        switch (lvl){
            case 1:
                time = 30;
                break;
            case 2:
            case 3:
                time = 20;
                break;
            case 4:
            case 5:
                time = 10;
                break;

        }
    }

    //comprobacion del resultado
    public boolean checkRes() {
        int resV;
        Result result;
        try {
            resV = Integer.parseInt(res.getText().toString());
        } catch (Exception e) {
            Log.e("error","empty result");
            resV = 0;
        }
        if (resV == c) {
            //Toast.makeText(getApplicationContext(),"correcto",Toast.LENGTH_SHORT).show();
            result = new Result(calc.getText() + (res.getText().toString()),"", true);
            if (mAuth.getCurrentUser() != null) {
                setPoints();
                result.setTextCorrect("+"+points+"pts");
            }
            resData.add(result);
            adapter.notifyDataSetChanged();
            createCalc(time);
            res.setText("");
            rvRes.smoothScrollToPosition(adapter.getItemCount());
            return true;
        } else {
            //Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
            //animacion innecesaria por nueva metodologia
            /*if (resV!=0){
            ObjectAnimator anim = ObjectAnimator.ofInt(res, "textColor", Color.BLACK,Color.RED,Color.BLACK);
            anim.setDuration(1000);
            anim.setEvaluator(new ArgbEvaluator());
            anim.setRepeatCount(1);
            anim.start();}*/
            if (res.getText().toString().equals("")) {
                res.setText("0");
            }
            result = new Result(calc.getText() + (res.getText().toString()),calc.getText()+String.valueOf(c), false);
            res.setText("");
            resData.add(result);
            adapter.notifyDataSetChanged();
            createCalc(time);
            rvRes.smoothScrollToPosition(adapter.getItemCount());
            return false;
        }
    }

    //asigna accion volver en toolbar
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    //clase temporizador
    public class MyTimer extends CountDownTimerPausable {
        int aux;//variable auxiliar para poder asignar el tiempo

        MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture * 1000, countDownInterval);
            aux = (int) millisInFuture;
        }

        @Override
        public void onTick(long l) {
            int progress = (int) (l / aux);
            timer.setProgress(progress);
            timeLeft = (int) (l / 1000);
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
    public int randomInt(int range) {
        return new Random().nextInt(range) + 1;
    }

    //Asigna los puntos al usuario
    int points = 0;
    int totalPoints = 0;
    public void setPoints() {
        switch (calcType) {
            case "multiplicacion":
                points = (110 - (time - timeLeft)) * 3 * lvl;
                break;
            case "division":
                points = (110 - (time - timeLeft)) * 2 * lvl;
                break;
            default:
                points = (110 - (time - timeLeft)) * lvl;
                break;
        }
        totalPoints += points;
        db.collection("usersPoints").document(mAuth.getUid()).update("points", FieldValue.increment(points));
        db.collection("stats").document(mAuth.getUid()).update(calcType+lvl,FieldValue.increment(1));
    }

    @Override
    protected void onResume() {
        super.onResume();
        myTimer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        myTimer.pause();
    }
}
