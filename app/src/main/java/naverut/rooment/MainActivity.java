package naverut.rooment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import naverut.rooment.file.bool.AdminModeBool;
import naverut.rooment.file.dao.CardDao;
import naverut.rooment.file.dao.MemberDao;
import naverut.rooment.file.dao.PasswordDao;
import naverut.rooment.entity.Card;
import naverut.rooment.entity.Member;
import naverut.rooment.enums.Status;
import naverut.rooment.service.CardReadService;
import naverut.rooment.util.InFileUtil;

/**
 * メイン画面Activity
 */
public class MainActivity extends Activity {
    // 読み取ったNFCのIDを受け渡すためのキー
    public static final String EXTRA_STR_IDM = "naverut.rooment.idm";
    // 管理者モード移行用Request値
    public static final int REQ_TO_ADMIN_PASSWORD = 1000;

    // 管理者用ボタン
    private static final int[] ADMIN_BUTTONS = {
            // 通常モードへ戻る
            R.id.cancelAdminButton,
            // メンバ編集
            R.id.addMemberButton,
            // 履歴確認
            R.id.checkRecordButton,
    };
    // 通常用ボタン
    private static final int[] NORMAL_BUTTONS = {
            // 管理者モードへ
            R.id.toAdminButton,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String action = intent.getAction();
        Context context = this.getApplicationContext();

        // パスワードファイルがなければパスワードファイル作成画面へ遷移
        if(!InFileUtil.exists(context, PasswordDao.fileName)) {
            Intent nextIntent = new Intent(getApplication(), PasswordFileCreateActivity.class);
            startActivity(nextIntent);
        }

        // 管理者モード状態の取得
        AdminModeBool adminModeBool = new AdminModeBool(context);
        boolean adminMode = adminModeBool.isTrue();

        // ボタン押下時の動作を登録
        registerOnClickListener();

        // NFCかどうかActionの判定
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // メイン画面用テキストView
            TextView mainTextView = findViewById(R.id.mainTextView);

            // IDm(固有識別子)を取得
            String idm = getIdm(intent);
            if (idm == null) {
                mainTextView.setText("カード読み取り失敗");
                return;
            }

            // カードチェック
            CardDao cardDao = new CardDao(context);
            Card card = cardDao.selectById(idm);
            if (card == null) {
                // カードが登録されていない
                if (!adminMode) {
                    // 通常モードでは登録外のカードを処理しない
                    mainTextView.setText("登録されていないカードです");
                    return;
                }
                // 管理者モードの場合はカード登録画面へ遷移
                Intent nextIntent = new Intent(getApplication(), EditCardActivity.class);
                nextIntent.putExtra(EXTRA_STR_IDM, idm);
                startActivity(nextIntent);
                return;
            }
            // adminモードであれば、カードの持ち主修正
            if (adminMode) {
                // 管理者モードの場合はカード登録画面へ遷移
                Intent nextIntent = new Intent(getApplication(), EditCardActivity.class);
                nextIntent.putExtra(EXTRA_STR_IDM, idm);
                startActivity(nextIntent);
                return;
            }

            //// カード登録あり＆通常モード
            // 読み込んだカードの持ち主を打刻する
            Status status = CardReadService.writeCard(context, card);
            // メンバを取得
            MemberDao memberDao = new MemberDao(context);
            Member member = memberDao.selectById(card.getMemberNo());

            // 画面にようこそorおつかれさまを出力
            String view;
            if (Status.INTO == status) {
                view = "ようこそ" + member.getName() + "さん";
            } else {
                view = "おつかれさま" + member.getName() + "さん";
            }
            mainTextView.setText(view);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // OKで戻ってきた
        if(resultCode == RESULT_OK && null != intent) {
            if (requestCode == REQ_TO_ADMIN_PASSWORD) {
                // 管理者モード移行用画面からOKで戻ってきたので管理者モード移行
                Context context = this.getApplicationContext();
                AdminModeBool adminModeBool = new AdminModeBool(context);
                adminModeBool.setBool(true);
            }
        }
    }

    /**
     * ボタン動作登録
     */
    private void registerOnClickListener() {
        // toAdminボタン時の動作
        Button toAdminButton = findViewById(R.id.toAdminButton);
        toAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // パスワード確認画面へ遷移
                Intent nextIntent = new Intent(getApplication(), PasswordCheckActivity.class);
                startActivityForResult(nextIntent, REQ_TO_ADMIN_PASSWORD);
                changeViewMode(true);
            }
        });
        // cancelAdminボタン時の動作
        Button cancelAdminButton= findViewById(R.id.cancelAdminButton);
        cancelAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 通常モード移行
                Context context = getApplicationContext();
                AdminModeBool adminModeBool = new AdminModeBool(context);
                adminModeBool.setBool(false);
                changeViewMode(false);
            }
        });
        // メンバ編集ボタン時の動作
        Button editMemberButton = findViewById(R.id.addMemberButton);
        editMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // メンバ編集画面へ遷移
                Intent nextIntent = new Intent(getApplication(), EditMemberActivity.class);
                startActivity(nextIntent);
            }
        });
        // 履歴へボタン時の動作
        Button checkRecordButton = findViewById(R.id.checkRecordButton);
        checkRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 履歴画面へ遷移
                Intent nextIntent = new Intent(getApplication(), RecordCheckActivity.class);
                startActivity(nextIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Context context = this.getApplication();
        AdminModeBool adminModeBool = new AdminModeBool(context);
        boolean adminMode = adminModeBool.isTrue();
        changeViewMode(adminMode);
    }

    /**
     * モードによる表示を行う
     * @param adminMode 管理者モード
     */
    private void changeViewMode(boolean adminMode) {
        if (adminMode) {
            // 管理者用ボタンを有効にする
            for (int buttonId : ADMIN_BUTTONS) {
                Button cancelAdminButton = findViewById(buttonId);
                cancelAdminButton.setVisibility(View.VISIBLE);
            }
            // 通常ボタンを無効にする
            for (int buttonId : NORMAL_BUTTONS) {
                Button cancelAdminButton = findViewById(buttonId);
                cancelAdminButton.setVisibility(View.INVISIBLE);
            }
        } else {
            // 管理者用ボタンを無効にする
            for (int buttonId : ADMIN_BUTTONS) {
                Button cancelAdminButton = findViewById(buttonId);
                cancelAdminButton.setVisibility(View.INVISIBLE);
            }
            // 通常ボタンを有効にする
            for (int buttonId : NORMAL_BUTTONS) {
                Button cancelAdminButton = findViewById(buttonId);
                cancelAdminButton.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * IDmを取得する
     * @param intent 受信インテント
     * @return IDm文字列
     */
    private static String getIdm(Intent intent) {
        String idm = null;
        StringBuilder idmByte = new StringBuilder();
        byte[] rawIdm = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
        if (rawIdm != null) {
            for (int i = 0; i < rawIdm.length; i++) {
                idmByte.append(Integer.toHexString(rawIdm[i] & 0xff));
            }
            idm = idmByte.toString();
        }
        return idm;
    }
}
