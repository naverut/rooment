package naverut.rooment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import naverut.rooment.bean.MemberNo;
import naverut.rooment.bean.MemberOnSpinner;
import naverut.rooment.file.bool.EditMemberBool;
import naverut.rooment.file.dao.MemberDao;
import naverut.rooment.entity.Member;

/**
 * メンバ編集用Activity
 */
public class EditMemberActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_member);

        final Context context = this.getApplication();

        // メンバリスト読み込み
        MemberDao memberDao = new MemberDao(context);
        final List<Member> memList = memberDao.selectAll();
        final List<MemberOnSpinner> nameList = new ArrayList<>();
        for (Member member : memList) {
            nameList.add(new MemberOnSpinner(member));
        }
        // スピナへ反映
        Spinner spinner = findViewById(R.id.selMemSpinner);
        ArrayAdapter<MemberOnSpinner> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                nameList
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // 編集ボタン時の動作
        final Button editButton = findViewById(R.id.editMemberOk);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // チェックボタンの状態を取得
                CheckBox editModeCheckBox = findViewById(R.id.editModeCheckBox);
                boolean editMode = editModeCheckBox.isChecked();

                // メンバ番号・メンバ名称を取得
                EditText memberNoEdit = findViewById(R.id.editMemberNo);
                String memberNoStr = memberNoEdit.getText().toString();
                if ("".equals(memberNoStr)) {
                    Toast ts = Toast.makeText(context, "メンバ番号は必須です", Toast.LENGTH_SHORT);
                    ts.show();
                    return;
                }
                MemberNo memberNo = MemberNo.builder().memberNo(memberNoStr).build();
                EditText memberNameEdit = findViewById(R.id.editMemberName);
                String memberName = memberNameEdit.getText().toString();
                if ("".equals(memberName)) {
                    Toast ts = Toast.makeText(context, "メンバ名称は必須です", Toast.LENGTH_SHORT);
                    ts.show();
                    return;
                }

                // メンバ作成
                Member newMember = Member.builder().memberNo(memberNo).name(memberName).build();
                MemberDao memberDao = new MemberDao(context);
                // チェック状態に合わせてファイルに反映
                if (editMode) {
                    // 編集モード
                    memberDao.update(newMember);
                    Toast ts = Toast.makeText(context, "編集しました", Toast.LENGTH_SHORT);
                    ts.show();
                    recreate();
                } else {
                    // 追加モード
                    // 追加済みでなければ登録
                    for (Member member : memList) {
                        if (member.getMemberNo().equals(memberNo)) { // 同一番号あり
                            Toast ts = Toast.makeText(context, "メンバ番号の重複があります", Toast.LENGTH_SHORT);
                            ts.show();
                            return;
                        }
                    }
                    // 追加
                    memberDao.insert(newMember);

                    Toast ts = Toast.makeText(context, "追加しました", Toast.LENGTH_SHORT);
                    ts.show();
                    recreate();
                }
            }
        });

        // チェックボックス押下時の動作
        final CheckBox editModeCheckBox = findViewById(R.id.editModeCheckBox);
        editModeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // チェックステータス取得
                boolean editMode = editModeCheckBox.isChecked();
                // editモードを保持(reCreateで画面再描画するため)
                EditMemberBool editMemberBool = new EditMemberBool(context);
                editMemberBool.setBool(editMode);
                changeViewMode(editMode);
            }
        });

        // スピナー変更時の動作
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!editModeCheckBox.isChecked()) {
                    // 追加モードの場合は何もしない
                    return;
                }
                // スピナー要素を取得
                MemberOnSpinner selectedItem = (MemberOnSpinner) parent.getItemAtPosition(position);

                // 選択した要素でメンバ番号・メンバ名称を書き換え
                EditText memberNoEdit = findViewById(R.id.editMemberNo);
                memberNoEdit.setText(selectedItem.getMember().getMemberNo().getMemberNo());
                EditText memberNameEdit = findViewById(R.id.editMemberName);
                memberNameEdit.setText(selectedItem.getMember().getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 先頭を選択
                parent.setSelection(0);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Context context = this.getApplication();
        EditMemberBool editMemberBool = new EditMemberBool(context);
        changeViewMode(editMemberBool.isTrue());
    }

    /**
     * 表示モードを変更
     * @param editMode モード
     */
    private void changeViewMode(boolean editMode) {
        CheckBox editModeCheckBox = findViewById(R.id.editModeCheckBox);
        Spinner spinner = findViewById(R.id.selMemSpinner);
        EditText memberNoEdit = findViewById(R.id.editMemberNo);
        Button editMemberButton = findViewById(R.id.editMemberOk);

        if (editMode) {
            // チェックボックス有効
            editModeCheckBox.setChecked(true);
            // メンバ番号をdisable
            memberNoEdit.setEnabled(false);
            // 選択をenable
            spinner.setEnabled(true);
            // ボタンのテキスト
            editMemberButton.setText("選択したメンバの名称を修正");
        } else {
            // チェックボックス無効
            editModeCheckBox.setChecked(false);
            // メンバ番号をenable
            memberNoEdit.setEnabled(true);
            // 選択をdisable
            spinner.setEnabled(false);
            // ボタンのテキスト
            editMemberButton.setText("上記メンバを追加");
            // 入力ボックスを空に
            memberNoEdit.setText("");
            EditText memberNameEdit = findViewById(R.id.editMemberName);
            memberNameEdit.setText("");
        }
    }
}
