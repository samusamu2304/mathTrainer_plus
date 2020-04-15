package com.boala.mathtrainer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String EXTRA_STRING = "operacion";
    private static final int RC_SIGN_IN = 69;
    private int time = 10;;
    private ImageButton suma, resta, multiplicacion, division;
    private IndicatorSeekBar seekTime;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private NavigationView navigationView;
    private TextView hName, hMail;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        suma = findViewById(R.id.suma);
        resta = findViewById(R.id.resta);
        multiplicacion = findViewById(R.id.multiplicacion);
        division = findViewById(R.id.division);
        seekTime = findViewById(R.id.seekTime);
        mAuth = FirebaseAuth.getInstance();
        navigationView = findViewById(R.id.navigation_view);
        hName = navigationView.getHeaderView(0).findViewById(R.id.hName);
        hMail = navigationView.getHeaderView(0).findViewById(R.id.hMail);
        setNavigationViewListener();
        if (mAuth.getCurrentUser() == null){
            loggedOutUI();
        }else{
            loggedInUI();
        }
        seekTime.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                time = seekParams.progress;
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
            }
        });
        final Intent intent = new Intent(this, Calculos.class);
        suma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra(EXTRA_STRING,"suma");
                intent.putExtra("time",time);
                startActivity(intent);

            }
        });
        resta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra(EXTRA_STRING,"resta");
                intent.putExtra("time",time);
                startActivity(intent);

            }
        });
        multiplicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra(EXTRA_STRING,"multiplicacion");
                intent.putExtra("time",time);
                startActivity(intent);

            }
        });
        division.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra(EXTRA_STRING,"division");
                intent.putExtra("time",time);
                startActivity(intent);

            }
        });
    }

    public void signIn(){
        List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build());
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build(), RC_SIGN_IN);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.signIn:
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    signIn();
                }
                break;
            case R.id.signOut:
                AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        loggedOutUI();
                    }
                });
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void setNavigationViewListener(){
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                loggedInUI();
            }else{

            }
        }
    }
    public void loggedInUI(){
        navigationView.getMenu().findItem(R.id.signIn).setVisible(false);
        navigationView.getMenu().findItem(R.id.signOut).setVisible(true);
        hName.setText(mAuth.getCurrentUser().getDisplayName());
        updatePoints();
    }
    public void loggedOutUI(){
        navigationView.getMenu().findItem(R.id.signOut).setVisible(false);
        navigationView.getMenu().findItem(R.id.signIn).setVisible(true);
        hName.setText("");
        hMail.setText("");
    }
    public void updatePoints(){
        final double[] points = {0};
        if (mAuth.getCurrentUser() != null) {
            db.collection("usersPoints").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            points[0] = document.getDouble("points");
                            hMail.setText("points: " + (int) points[0]);
                        } else {
                            Map<String, Object> userPoints = new HashMap<>();
                            userPoints.put("name", mAuth.getCurrentUser().getDisplayName());
                            userPoints.put("points", 0);
                            db.collection("usersPoints").document(mAuth.getUid()).set(userPoints);
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePoints();
    }
}

