package naverut.rooment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import naverut.rooment.bean.MemberOnSpinner;
import naverut.rooment.file.dao.CardDao;
import naverut.rooment.file.dao.MemberDao;
import naverut.rooment.entity.Card;
import naverut.rooment.entity.Member;

/**
 * カード登録用Activity
 */
public class EditCardActivity extends Activity {
    // スピナ表示するメンバリスト
    private List<Member> memList;
    // メンバが登録済みか
    private boolean existMember = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        setContentView(R.layout.activity_edit_card);

        Context context = this.getApplication();

        // カードIDM値を画面受け渡し用から取得
        final String idm = intent.getStringExtra(MainActivity.EXTRA_STR_IDM);

        // メンバリスト読み込み
        MemberDao memberDao = new MemberDao(context);
        memList = memberDao.selectAll();
        final List<MemberOnSpinner> nameList = new ArrayList<>();
        for (Member member : memList) {
            nameList.add(new MemberOnSpinner(member));
        }
        // スピナへ反映
        final Spinner spinner = findViewById(R.id.selMemSpinner);
        ArrayAdapter<MemberOnSpinner> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                nameList
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // OKボタン時の動作
        Button returnButton = findViewById(R.id.memoOkButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplication();

                // 選択されているアイテムを取得
                MemberOnSpinner memberOnSpinner = (MemberOnSpinner) spinner.getSelectedItem();

                // 備考欄取得
                EditText memoEdit = findViewById(R.id.memoEditText);
                String memo = memoEdit.getText().toString();

                // カードを登録
                Card card = Card.builder().cardId(idm).memberNo(memberOnSpinner.getMember().getMemberNo()).memo(memo).build();
                CardDao cardDao = new CardDao(context);
                if (existMember) {
                    cardDao.update(card);
                } else {
                    cardDao.insert(card);
                }
                // 画面を閉じる
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeViewMode();
    }

    /**
     * 表示モード切替
     */
    private void changeViewMode() {
        Intent intent = getIntent();
        final String idm = intent.getStringExtra(MainActivity.EXTRA_STR_IDM);
        Context context = getApplication();
        Spinner spinner = findViewById(R.id.selMemSpinner);

        // カードの持ち主がすでにある場合はその情報を表示
        CardDao cardDao = new CardDao(context);
        Card card = cardDao.selectById(idm);
        if (card != null) {
            int position = 0;
            for (Member member : memList) {
                if (member.getMemberNo().equals(card.getMemberNo())) {
                    // カードの持ち主をspinner表示
                    spinner.setSelection(position);
                    // カードの備考を備考欄に表示
                    EditText memoEdit = findViewById(R.id.memoEditText);
                    memoEdit.setText(card.getMemo());

                    existMember = true;
                    break;
                }
                position++;
            }
        }
    }
}
