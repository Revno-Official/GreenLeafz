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
import androidx.room.Room;

import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private HistoryAdapter adapter;
    public static HistoryDatabase database;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        database = Room.databaseBuilder(getApplicationContext(),HistoryDatabase.class, "histories")
                .allowMainThreadQueries()
                .build();
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

        adapter.reload();
    }






















    public void toHome(View view){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
    }
    public void toTutorial(View view){
        Intent intent = new Intent(getApplicationContext(),TutorialActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
    }
    public void toHistory(View view){
        if (!this.getClass().getSimpleName().toLowerCase().startsWith("hist")) {
            Intent intent = new Intent(getApplicationContext(),HistoryActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_left);
        }
    }
    public void toAbout(View view){
        Intent intent = new Intent(getApplicationContext(),AboutUsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
    }
    @Override
    public void onBackPressed() {
        this.finish();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }
}
