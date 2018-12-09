package naverut.rooment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import naverut.rooment.bean.RecordHistoryOnSpinner;
import naverut.rooment.entity.RecordHistory;
import naverut.rooment.file.dao.RecordDao;
import naverut.rooment.file.dao.RecordHistoryDao;
import naverut.rooment.service.RecordHistoryService;
import naverut.rooment.service.RecordService;

/**
 * 打刻確認用Activity
 */
public class RecordCheckActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Context context = this.getApplication();
        setContentView(R.layout.activity_check_record);

        // 履歴を取得
        final String recordStr = RecordService.getRecordString(getApplicationContext(), RecordDao.fileName);

        // 打刻履歴を表示
        TextView recordCheckText = findViewById(R.id.recordText);
        recordCheckText.setText(recordStr);
        recordCheckText.setMovementMethod(ScrollingMovementMethod.getInstance());

        // 打刻履歴リスト読み込み
        RecordHistoryDao recordHistoryDao = new RecordHistoryDao(context);
        final List<RecordHistory> historyList = recordHistoryDao.selectAll();
        if (historyList.isEmpty()) {
            // 履歴がない場合は現在打刻を打刻履歴に設定
            RecordHistory recordHistory = RecordHistory.builder()
                    .historyFileName(RecordDao.fileName)
                    .historyMemo("現在の打刻")
                    .build();
            historyList.add(recordHistory);
            // ファイルにも保存
            recordHistoryDao.insert(recordHistory);
        }

        // 打刻履歴をスピナへ表示
        Spinner spinner = findViewById(R.id.recordHistorySpinner);
        // 履歴をスピナへ表示
        final List<RecordHistoryOnSpinner> memoList = new ArrayList<>();
        for (RecordHistory recordHistory : historyList) {
            memoList.add(new RecordHistoryOnSpinner(recordHistory));
        }
        // スピナへ反映
        ArrayAdapter<RecordHistoryOnSpinner> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                memoList
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // スピナー変更時の動作
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // スピナー要素を取得
                RecordHistoryOnSpinner selectedItem = (RecordHistoryOnSpinner) parent.getItemAtPosition(position);

                // 選択した要素で履歴を表示
                String historyRecordStr = RecordService.getRecordString(context, selectedItem.getRecordHistory().getHistoryFileName());
                TextView recordCheckText = findViewById(R.id.recordText);
                recordCheckText.setText(historyRecordStr);

                // 先頭以外はバックアップボタンをdisable
                Button historyMakeButton = findViewById(R.id.historyMakeButton);
                if (position == 0 && historyRecordStr.length() > 0) {
                    historyMakeButton.setEnabled(true);
                } else {
                    historyMakeButton.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 先頭を選択
                parent.setSelection(0);
            }
        });


        // クリップボードコピーボタン時の動作
        Button copyToClipButton = findViewById(R.id.copyToClipButton);
        copyToClipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // クリップボードへ打刻をコピー

                // クリップボードに格納するItemを作成
                TextView recordCheckText = findViewById(R.id.recordText);
                String recordStr = recordCheckText.getText().toString();
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

        // 履歴作成ボタン時の動作
        Button historyMakeButton = findViewById(R.id.historyMakeButton);
        historyMakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ダイアログクラスをインスタンス化
                HistoryMemoDialogFragment dialog = new HistoryMemoDialogFragment();
                // 表示  getFragmentManager()は固定、tagは識別タグ
                dialog.show(getFragmentManager(), "historyMemoDialogTag");
            }
        });
    }

    /**
     * 打刻履歴作成用ダイアログ
     */
    public static class HistoryMemoDialogFragment extends DialogFragment {
        // ダイアログが生成された時に呼ばれるメソッド
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            // ダイアログ生成  AlertDialogのBuilderクラスを指定してインスタンス化します
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
            // タイトル設定
            dialogBuilder.setTitle("打刻履歴作成");
            // 表示する文章設定
            dialogBuilder.setMessage("打刻のメモを入力してください");

            // 入力フィールド作成
            final EditText editText = new EditText(getActivity());
            dialogBuilder.setView(editText);

            // OKボタン作成
            dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // editTextから値を取得
                    String historyMemo = editText.getText().toString();
                    Context context = getActivity();
                    if (historyMemo.length() <= 0) {
                        Toast ts = Toast.makeText(context, "メモ内容は必須です", Toast.LENGTH_SHORT);
                        ts.show();
                        return;
                    }
                    // 打刻履歴作成
                    RecordHistoryService.backupRecord(getActivity(), historyMemo);
                    Toast ts = Toast.makeText(context, "バックアップを作成しました", Toast.LENGTH_SHORT);
                    ts.show();
                    // 画面再読み込み
                    dialog.cancel();
                    getActivity().recreate();
                }
            });

            // CANCELボタン作成
            dialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 何もしないで閉じる
                }
            });

            // dialogBuilderを返す
            return dialogBuilder.create();
        }
    }
}
