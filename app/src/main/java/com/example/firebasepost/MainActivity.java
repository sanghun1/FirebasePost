package com.example.firebasepost;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.firebasepost.adapter.PostAdapter;
import com.example.firebasepost.model.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    private RecyclerView mPostRecyclerview;

    private PostAdapter mAdapter;
    private List<Post> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPostRecyclerview = findViewById(R.id.main_recyclerview);

        findViewById(R.id.main_post_edit).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatas = new ArrayList<>();
        mStore.collection(FirebaseID.post)
                .orderBy(FirebaseID.timestamp, Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        mDatas.clear();
                        for(DocumentSnapshot snap : value.getDocuments()){
                            Map<String, Object> shot = snap.getData();
                            String documentId = String.valueOf(shot.get(FirebaseID.documentId));
                            String nickname = String.valueOf(shot.get(FirebaseID.nickname));
                            String title = String.valueOf(snap.get(FirebaseID.title));
                            String contents = String.valueOf(shot.get(FirebaseID.contents));
                            Post data = new Post(documentId, nickname, title, contents);
                            mDatas.add(data);
                        }
                        mAdapter = new PostAdapter(mDatas);
                        mPostRecyclerview.setAdapter(mAdapter);
                    }
                });
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(MainActivity.this, PostActivity.class));
    }
}