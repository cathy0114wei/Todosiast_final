package com.example.mobile_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
//import android.se.omapi.Session;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail extends AppCompatActivity {

//    private String realPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);
        Button sendBtn = (Button)findViewById(R.id.btnSend);
        EditText sendTo = (EditText) findViewById(R.id.emailAddress);
        EditText psw = (EditText) findViewById(R.id.emailPassword);

        Intent intent = getIntent();
        final String username = intent.getStringExtra("username");
//        final String password = intent.getStringExtra("password");

//        realPassword = psw.getText().toString();
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), psw.getText().toString(), Toast.LENGTH_LONG).show();
//                Toast.makeText(getApplicationContext(), sendTo.getText().toString(), Toast.LENGTH_LONG).show();
//                Toast.makeText(getApplicationContext(), username, Toast.LENGTH_LONG).show();

                //Toast.makeText(SendEmail.this, username, Toast.LENGTH_SHORT).show();
                String message = "hello I'm Cathy";
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");
//                Toast.makeText(getApplicationContext(), "*************************************************************", Toast.LENGTH_LONG).show();
                Session session = Session.getInstance(props,
                        new javax.mail.Authenticator(){
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication(){
                                Toast.makeText(getApplicationContext(), "Email Send Successufully!:)", Toast.LENGTH_LONG).show();
                                return new PasswordAuthentication(username, psw.getText().toString());
                            }
                        });
                try{
                    MimeMessage message1 = new MimeMessage(session);
                    message1.setFrom(new InternetAddress(username));
                    message1.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(sendTo.getText().toString().trim()));
                    message1.setSubject("Sending Email without opening Gmail Apps");
                    message1.setText(message);
                    Transport.send(message1);
                } catch (MessagingException e){
                    throw new RuntimeException(e);
                }
            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}















