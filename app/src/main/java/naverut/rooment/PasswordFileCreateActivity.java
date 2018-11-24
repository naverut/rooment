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
 * パスワード作成用Activity
 */
public class PasswordFileCreateActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_password);

        final Context context = this.getApplication();

        Button passwordOkButton = findViewById(R.id.passwordOkButton);
        passwordOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 入力されたパスワードを読み込む
                EditText passwordText = findViewById(R.id.inputPassword);
                EditText passwordCheckText = findViewById(R.id.inputPasswordCheck);
                // 入力したパスワードをファイルに書き込み
                if (PasswordService.writePassword(context, passwordText.getText().toString(), passwordCheckText.getText().toString())) {
                    finish();
                } else {
                    TextView view = findViewById(R.id.passwordTextView);
                    view.setText("パスワード欄に同じパスワードを入力してください");
                }
            }
        });
    }
}
