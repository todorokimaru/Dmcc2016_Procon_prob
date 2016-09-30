package prob.procon.dmcc2016.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Size;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.util.Collections;

// Camera.v2 Android ver.21 over only use //

public class SimpleCameraActivity extends AppCompatActivity {

    private AutoFitTextureView mTextureView;
    private ImageView mImageView;
    private Camera2StateMachine mCamera2;
    private String date_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_camera);

        Intent intent = getIntent();
        date_str = intent.getStringExtra("date");


        mTextureView = (AutoFitTextureView) findViewById(R.id.textureView);
        mImageView = (ImageView) findViewById(R.id.ImageView);
        mCamera2 = new Camera2StateMachine();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCamera2.open(this, mTextureView);
    }
    @Override
    protected void onPause() {
        mCamera2.close();
        super.onPause();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mImageView.getVisibility() == View.VISIBLE) {
            mTextureView.setVisibility(View.VISIBLE);
            mImageView.setVisibility(View.INVISIBLE);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void onClickShutter(View view) {
        mCamera2.takePicture(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                // 撮れた画像をImageViewに貼り付けて表示。
                final Image image = reader.acquireLatestImage();
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                image.close();

                mImageView.setImageBitmap(bitmap);
                mImageView.setVisibility(View.VISIBLE);
                mTextureView.setVisibility(View.INVISIBLE);
            }
        });
    }
}
