package naverut.rooment;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import naverut.rooment.bean.MemberNo;
import naverut.rooment.file.dao.MemberDao;
import naverut.rooment.file.dao.RecordDao;
import naverut.rooment.entity.Member;
import naverut.rooment.entity.Record;
import naverut.rooment.enums.Status;
import naverut.rooment.service.RecordService;

/**
 * 打刻確認用Activity
 */
public class RecordCheckActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = this.getApplication();
        setContentView(R.layout.activity_check_record);

        // 履歴を取得
        final String recordStr = RecordService.getRecordString(context);

        // 打刻履歴を表示
        EditText recordCheckText = findViewById(R.id.recordText);
        recordCheckText.setText(recordStr);
        recordCheckText.setEnabled(false);


        // クリップボードコピーボタン時の動作
        Button copyToClipButton = findViewById(R.id.copyToClipButton);
        copyToClipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // クリップボードへ履歴をコピー

                // クリップボードに格納するItemを作成
                ClipData.Item item = new ClipData.Item(recordStr);

                // MIMETYPEの作成
                String[] mimeType = new String[1];
                mimeType[0] = ClipDescription.MIMETYPE_TEXT_PLAIN;

                //クリップボードに格納するClipDataオブジェクトの作成
                ClipData cd = new ClipData(new ClipDescription("text_data", mimeType), item);

                //クリップボードにデータを格納
                ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                cm.setPrimaryClip(cd);

                // コピーをToast通知
                Context context = getApplication();
                Toast ts = Toast.makeText(context, "打刻履歴をコピーしました。", Toast.LENGTH_SHORT);
                ts.show();
            }
        });
    }
}
