package com.revno.greenleafz;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private HistoryAdapter adapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);


        recyclerView = findViewById(R.id.recycler_view);
        toolbar = findViewById(R.id.appbar);
        layoutManager = new LinearLayoutManager(this);
        adapter = new HistoryAdapter();

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

    }
    public void toHome(View view){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
    public void toTutorial(View view){
        Intent intent = new Intent(getApplicationContext(),TutorialActivity.class);
        startActivity(intent);
    }
    public void toHistory(View view){
        if (!this.getClass().getSimpleName().toLowerCase().startsWith("hist")) {
            Intent intent = new Intent(getApplicationContext(),HistoryActivity.class);
            startActivity(intent);
        }
    }
    public void toAbout(View view){
        Intent intent = new Intent(getApplicationContext(),AboutUsActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        this.finish();
    }
}