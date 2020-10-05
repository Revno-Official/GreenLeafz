package com.revno.greenleafz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

//Test123

public class MainActivity extends AppCompatActivity {
    public static ImageView imageView;
    public static Bitmap imageBitmap;
    public FloatingActionButton photoButton;
    public FloatingActionButton uploadButton;
    public static String encoded = "";
    public TextView title;
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.camera);
        photoButton = findViewById(R.id.photo_button);
        uploadButton = findViewById(R.id.gallery_button);
        toolbar = findViewById(R.id.appbar);
        title = toolbar.findViewById(R.id.title);
        toolbar.setTitle("");


        new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();
    }

    public void takePhoto(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, TakePhotoActivity.class);
        context.startActivity(intent);
    }

    public void uploadPhoto(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, UploadImageActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    public void toHome(View view){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
    public void toTutorial(View view){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
    public void toHistory(View view){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
    public void toAbout(View view){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }











































































    /*public static void toBase64() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        encoded = Base64.encodeToString(byteArray, Base64.NO_WRAP);
        Log.i("REVNO",encoded);*/
        //encoded = ImageUtil.convert(imageBitmap);
        //Log.i("REVNO",encoded);
        //imageView.setImageBitmap(ImageUtil.convert(encoded));}

    /*public void send(){
        BackgroundMail bm = new BackgroundMail(MainActivity.this);
        bm.setGmailUserName("venuskanda2005@gmail.com");
        bm.setGmailPassword("venu2005");
        bm.setMailTo("rohitrangaraj2005@gmail.com");
        bm.setFormSubject("Subject");
        bm.setFormBody("hui");
        bm.send();

        BackgroundMail.Builder builder = BackgroundMail.newBuilder(this);
        builder.withUsername("client.greenleafz@gmail.com");
        builder.withPassword("Velhasi123");
        builder.withMailto("server.greenleafz@gmail.com");
        builder.withSubject("ui");
        builder.withBody(encoded);
        builder.send();
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    */
}