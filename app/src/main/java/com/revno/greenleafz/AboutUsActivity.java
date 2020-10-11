package com.revno.greenleafz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

public class AboutUsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);

        toolbar = findViewById(R.id.appbar);
        textView = findViewById(R.id.about_us_text);

        textView.setText("We are the Hui people");

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
        Intent intent = new Intent(getApplicationContext(),HistoryActivity.class);
        startActivity(intent);
    }
    public void toAbout(View view){
        if (!this.getClass().getSimpleName().toLowerCase().startsWith("about")) {
            Intent intent = new Intent(getApplicationContext(),AboutUsActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
