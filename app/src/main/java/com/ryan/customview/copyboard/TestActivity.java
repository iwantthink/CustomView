package com.ryan.customview.copyboard;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ryan.customview.R;

public class TestActivity extends AppCompatActivity {

    private TextView mTvClipboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mTvClipboard = (TextView) findViewById(R.id.tv_clipboard);
    }

    public void clip_click(View view) {

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = clipboardManager.getPrimaryClip();
        if (clipData == null) {
            Toast.makeText(this, "clipdata is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        ClipDescription clipDescription = clipData.getDescription();
        int itemCount = clipData.getItemCount();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < itemCount; i++) {
            ClipData.Item item = clipData.getItemAt(i);
            stringBuilder.append(i + " = " + item.getText());
        }
        mTvClipboard.setText(stringBuilder.toString());
        ClipData output = ClipData.newPlainText("label_output", "你好我是刘德华!");
        clipboardManager.setPrimaryClip(output);
    }

}
