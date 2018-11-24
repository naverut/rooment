package naverut.rooment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import naverut.rooment.service.PasswordService;

/**
 * パスワード確認用Activity
 */
public class PasswordCheckActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 確認用パスワードなしで表示
        setContentView(R.layout.activity_password);
        EditText passwordCheckText = findViewById(R.id.inputPasswordCheck);
        passwordCheckText.setVisibility(View.GONE);

        final Context context = this.getApplication();

        Button passwordOkButton = findViewById(R.id.passwordOkButton);
        passwordOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 入力されたパスワードを読み込む
                EditText passwordText = findViewById(R.id.inputPassword);

                // パスワード確認OKであれば管理者モードへ移行
                if (PasswordService.checkPassword(context, passwordText.getText().toString())) {
                    Intent nextIntent = new Intent();
                    setResult(RESULT_OK, nextIntent);
                    finish();
                    return;
                }
                // 入力したパスワードが一致しない
                TextView view = findViewById(R.id.passwordTextView);
                view.setText("パスワードが異なります");
            }
        });
    }
}
