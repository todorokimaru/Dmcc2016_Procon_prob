package prob.procon.dmcc2016.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class Tweet_Activity extends AppCompatActivity {

    private EditText mInputText;
    private TextView mTextLength;
    private Twitter mTwitter;
    private String mount_name;
    private String image_path;
    private boolean image_flag;
    private boolean check_flag = false;
    private CheckBox checkBox;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_);

        Intent intent = getIntent();
        mount_name = intent.getStringExtra("Mount_name");
        image_flag = intent.getBooleanExtra("Image_flag", false);
        if(image_flag) {
            image_path = intent.getStringExtra("Image_path");
            file = new File(image_path);
        }

        checkBox = (CheckBox)findViewById(R.id.image_add_check);
        mTwitter = Twitter_Util.getTwitterInstance(this);
        mInputText = (EditText) findViewById(R.id.tweet_text);
        mTextLength = (TextView)findViewById(R.id.text_length);

        mInputText.setText(" ヤッホー！"+mount_name+"からの投稿です！");

        findViewById(R.id.tweet_push).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(image_flag && check_flag) {
                    Log.d("Twitter", "image add");
                    tweet(file);
                }
                else {
                    tweet(null);
                    Log.d("Twitter", "image_flag"+ image_flag);
                    Log.d("Twitter", "check_flag"+ check_flag);
                }

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            // チェックボックスがクリックされた時に呼び出されます
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                
                // チェックボックスのチェック状態を取得します
                boolean checked = checkBox.isChecked();
                if(checked) check_flag = true;
                else check_flag = false;
            }
        });

        mInputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int textColor = Color.GRAY;

                // 入力文字数の表示
                int txtLength = charSequence.length();
                mTextLength.setText(Integer.toString(141 - txtLength));

                // 指定数文字オーバーで文字色を赤くする
                if(txtLength > 110){
                    textColor = Color.RED;
                }
                mTextLength.setTextColor(textColor);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void tweet(final File file) {
        AsyncTask<String, Void, Boolean> task = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    mTwitter.updateStatus(new StatusUpdate(params[0]).media(file));
                    return true;
                } catch (TwitterException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    showToast("ツイートが完了しました！");
                    finish();
                } else {
                    showToast("ツイートに失敗しました。。。");
                }
            }
        };
        task.execute(mInputText.getText().toString());
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
