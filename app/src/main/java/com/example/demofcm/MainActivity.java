package com.example.demofcm;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    EditText edtToken, edtTitle, edtMessage;
    Button btnSendToToken,btnSendAll;
    String Token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtToken = findViewById(R.id.edtToken);
        edtTitle = findViewById(R.id.edtTitle);
        edtMessage = findViewById(R.id.edtMessage);
        btnSendToToken = findViewById(R.id.btnSendToToken);
        btnSendAll = findViewById(R.id.btnSendAll);
        // getToken
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d(TAG, msg);
                        //Toast.makeText(MainActivity.this, "Your device registration token is " + token, Toast.LENGTH_SHORT).show();
                        edtToken.setText(token);
                        Token = token;
                    }
                });
        btnSendToToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtTitle.getText().toString().trim();
                String message = edtMessage.getText().toString().trim();
                if(!title.equals("") && !message.equals("")){
                    FCMSend.pushNotification(MainActivity.this,Token,title,message);
                }
            }
        });
        btnSendAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // subcribe
                FirebaseMessaging.getInstance().subscribeToTopic("all");
                String title = edtTitle.getText().toString().trim();
                String message = edtMessage.getText().toString().trim();
                if(!title.equals("") && !message.equals("")){
                    FCMSend.pushNotification(MainActivity.this,"/topics/all",title,message);
                }
            }
        });
    }
}