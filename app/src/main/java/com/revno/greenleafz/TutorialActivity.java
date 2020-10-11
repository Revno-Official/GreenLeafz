package com.revno.greenleafz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

public class TutorialActivity extends AppCompatActivity {
    private Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial);

        toolbar = findViewById(R.id.appbar);

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
        if (!this.getClass().getSimpleName().toLowerCase().startsWith("tut")) {
            Intent intent = new Intent(getApplicationContext(),TutorialActivity.class);
            startActivity(intent);
        }
    }
    public void toHistory(View view){
        Intent intent = new Intent(getApplicationContext(),HistoryActivity.class);
        startActivity(intent);
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
