package com.boala.mathtrainer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;



public class Ranking extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirestoreRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rvRank;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rvRank = findViewById(R.id.rankRV);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvRank.setLayoutManager(linearLayoutManager);
        rvRank.addItemDecoration(new DividerItemDecoration(rvRank.getContext(),DividerItemDecoration.VERTICAL));
        getRanking();

    }
    private void getRanking() {
        Query query = db.collection("usersPoints").orderBy("points", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<UsersPoints> ranking = new FirestoreRecyclerOptions.Builder<UsersPoints>().setQuery(query, UsersPoints.class).build();
        adapter = new FirestoreRecyclerAdapter<UsersPoints, UsersHolder>(ranking) {

            @NonNull
            @Override
            public UsersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rank, parent, false);
                return new UsersHolder(view);
            }

            @Override
            protected void onBindViewHolder(UsersHolder usersHolder, int i, UsersPoints usersPoints) {
                usersHolder.tvName.setText(usersPoints.getName());
                usersHolder.tvRank.setText(String.valueOf((int) usersPoints.getPoints()));
                usersHolder.position.setText(String.valueOf(i+1));
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                super.onError(e);
                Log.e("error", e.getMessage());
            }
        };
        adapter.notifyDataSetChanged();
        rvRank.setAdapter(adapter);
    }

    public class UsersHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvRank, position;
        public UsersHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.nameTV);
            tvRank = itemView.findViewById(R.id.rankTV);
            position = itemView.findViewById(R.id.position);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}

