package th.ac.ku.androidlab5;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubfilter;

public class MainActivity extends AppCompatActivity {

    private static final int SELECT_IMG_CODE = 1;
    private Button selectButton;
    private Button filterButton;
    private ImageView imgImageView;

    static
    {
        System.loadLibrary("NativeImageProcessor");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initInstance();
    }

    private void initInstance() {
        selectButton = (Button) findViewById(R.id.btn_select_img);
        filterButton = (Button) findViewById(R.id.btn_filter);
        imgImageView = (ImageView) findViewById(R.id.imgv_img);

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSelectImage();
            }
        });
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickFilter();
            }
        });
    }

    private void onClickFilter() {
        BitmapDrawable drawable = (BitmapDrawable) imgImageView.getDrawable();
        Bitmap immutableBitmap = Bitmap.createBitmap(drawable.getBitmap());
        Bitmap bitmap = immutableBitmap.copy(Bitmap.Config.ARGB_8888,true);

        Filter myFilter = new Filter();
//        myFilter.addSubFilter(new BrightnessSubfilter(30));
        myFilter.addSubFilter(new ContrastSubfilter(1.1f));

        bitmap = myFilter.processFilter(bitmap);

        imgImageView.setImageBitmap(bitmap);
    }

    private void onClickSelectImage() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
        }
        Log.d("onSelectImage", "pass");
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECT_IMG_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("lab5", "onRequestPermission");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if (requestCode == SELECT_IMG_CODE){
                Log.d("select image", "after select image");
                Uri uri = data.getData();
                imgImageView.setImageURI(uri);
            }
        }
    }
}
