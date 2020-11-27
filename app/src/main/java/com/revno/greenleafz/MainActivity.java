package com.revno.greenleafz;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;
import org.tensorflow.lite.support.label.TensorLabel;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class MainActivity<ActivityResultLauncher, ActivityResultCallback, ImageCapture> extends AppCompatActivity {
    public static ImageView imageView;
    public static Bitmap imageBitmap;
    public FloatingActionButton photoButton;
    public FloatingActionButton uploadButton;
    public static String encoded = "";
    public TextView title;
    public Toolbar toolbar;
    public Button button;
    public Button more;
    public TextView textView;
    protected Interpreter tflite;
    private MappedByteBuffer tfliteModel;
    private TensorImage inputImageBuffer;
    private  int imageSizeX;
    private  int imageSizeY;
    private  TensorBuffer outputProbabilityBuffer;
    private  TensorProcessor probabilityProcessor;
    private static final float IMAGE_MEAN = 0.0f;
    private static final float IMAGE_STD = 1.0f;
    private static final float PROBABILITY_MEAN = 0.0f;
    private static final float PROBABILITY_STD = 255.0f;
    private List<String> labels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.camera);
        photoButton = findViewById(R.id.photo_button);
        uploadButton = findViewById(R.id.gallery_button);
        toolbar = findViewById(R.id.appbar);
        title = toolbar.findViewById(R.id.title);
        button = findViewById(R.id.classify);
        textView = findViewById(R.id.tv);
        more = findViewById(R.id.know);
        toolbar.setTitle("");

        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
        }

        new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        try{
            tflite=new Interpreter(loadmodelfile(this));
        }catch (Exception e) {
            e.printStackTrace();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(imageView.getDrawable() != null){
                    button.setVisibility(View.GONE);
                    more.setVisibility(View.VISIBLE);
                    int imageTensorIndex = 0;
                    int[] imageShape = tflite.getInputTensor(imageTensorIndex).shape(); // {1, height, width, 3}
                    imageSizeY = imageShape[1];
                    imageSizeX = imageShape[2];
                    DataType imageDataType = tflite.getInputTensor(imageTensorIndex).dataType();

                    int probabilityTensorIndex = 0;
                    int[] probabilityShape =
                            tflite.getOutputTensor(probabilityTensorIndex).shape(); // {1, NUM_CLASSES}
                    DataType probabilityDataType = tflite.getOutputTensor(probabilityTensorIndex).dataType();

                    inputImageBuffer = new TensorImage(imageDataType);
                    outputProbabilityBuffer = TensorBuffer.createFixedSize(probabilityShape, probabilityDataType);
                    probabilityProcessor = new TensorProcessor.Builder().add(getPostprocessNormalizeOp()).build();

                    inputImageBuffer = loadImage(imageBitmap);

                    tflite.run(inputImageBuffer.getBuffer(),outputProbabilityBuffer.getBuffer().rewind());
                    showresult();
                }
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = textView.getText().toString();
                String term = name + " plant";
                Intent browserIntent = new Intent(Intent.ACTION_WEB_SEARCH);
                browserIntent.putExtra(SearchManager.QUERY, term);
                startActivity(browserIntent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private MappedByteBuffer loadmodelfile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor=activity.getAssets().openFd("model_0_2.tflite");
        FileInputStream inputStream=new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel=inputStream.getChannel();
        long startoffset = fileDescriptor.getStartOffset();
        long declaredLength=fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startoffset,declaredLength);
    }

    private TensorImage loadImage(final Bitmap bitmap) {
        // Loads bitmap into a TensorImage.
        inputImageBuffer.load(bitmap);

        // Creates processor for the TensorImage.
        int cropSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
        // td(b/143564309): Fuse ops inside ImageProcessor.
        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeWithCropOrPadOp(cropSize, cropSize))
                        .add(new ResizeOp(imageSizeX, imageSizeY, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                        .add(getPreprocessNormalizeOp())
                        .build();
        return imageProcessor.process(inputImageBuffer);
    }

    private TensorOperator getPreprocessNormalizeOp() {
        return new NormalizeOp(IMAGE_MEAN, IMAGE_STD);
    }
    private TensorOperator getPostprocessNormalizeOp(){
        return new NormalizeOp(PROBABILITY_MEAN, PROBABILITY_STD);
    }

    private void showresult(){

        try{
            labels = FileUtil.loadLabels(this,"labels.txt");
        }catch (Exception e){
            e.printStackTrace();
        }
        Map<String, Float> labeledProbability =
                new TensorLabel(labels, probabilityProcessor.process(outputProbabilityBuffer))
                        .getMapWithFloatValue();
        float maxValueInMap =(Collections.max(labeledProbability.values()));

        for (Map.Entry<String, Float> entry : labeledProbability.entrySet()) {
            if (entry.getValue()==maxValueInMap) {
                textView.setText(entry.getKey());
            }
        }
    }


    public void takePhoto(View view) {
        dispatchTakePictureIntent();
        button.setVisibility(View.VISIBLE);
        more.setVisibility(View.GONE);
        textView.setText("");
    }

    public void uploadPhoto(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, UploadImageActivity.class);
        context.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
        textView.setText("");
        button.setVisibility(View.VISIBLE);
        more.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void toHome(View view){
        if (!this.getClass().getSimpleName().toLowerCase().startsWith("main")){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_left);
        }
    }
    public void toTutorial(View view){
        Intent intent = new Intent(getApplicationContext(),TutorialActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
    }
    public void toHistory(View view){
        Intent intent = new Intent(getApplicationContext(),HistoryActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
    }
    public void toAbout(View view){
        Intent intent = new Intent(getApplicationContext(),AboutUsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_left);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    /*public static void toBase64() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        encoded = Base64.encodeToString(byteArray, Base64.NO_WRAP);
        Log.i("REVNO",encoded);
    }

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
    }*/
}