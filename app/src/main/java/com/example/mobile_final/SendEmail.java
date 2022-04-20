package com.example.mobile_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
//import android.se.omapi.Session;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);
        Button sendBtn = (Button)findViewById(R.id.btnSend);
        EditText sendTo = (EditText) findViewById(R.id.emailAddress);
        EditText psw = (EditText) findViewById(R.id.emailPassword);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        String onlineUserID = mUser.getUid();

        Intent intent = getIntent();
        final String username = intent.getStringExtra("username");
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> message = new ArrayList<>();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("tasks").child(onlineUserID);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Model model = dataSnapshot.getValue(Model.class);
                            String txt = model.getDate() +" " + model.getTask() + " " +
                                    model.getDescription() + " " + (model.getFinished()? "Finished" : "Not Finished Yet");
                            System.out.println(txt);
                            message.add(txt);
                        }
                        Properties props = new Properties();
                        props.put("mail.smtp.auth", "true");
                        props.put("mail.smtp.starttls.enable", "true");
                        props.put("mail.smtp.host", "smtp.gmail.com");
                        props.put("mail.smtp.port", "587");
                        Session session = Session.getInstance(props,
                                new javax.mail.Authenticator(){
                                    @Override
                                    protected PasswordAuthentication getPasswordAuthentication(){
                                        Toast.makeText(getApplicationContext(), "Email Send Successfully", Toast.LENGTH_LONG).show();
                                        return new PasswordAuthentication(username, psw.getText().toString());
                                    }
                                });
                        try{
                            MimeMessage message1 = new MimeMessage(session);
                            message1.setFrom(new InternetAddress(username));
                            message1.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(sendTo.getText().toString().trim()));
                            message1.setSubject("Hi, I would like to share my tasks to you!");
                            String sendVersion = helperFormat(message);
                            message1.setText(sendVersion);
                            Transport.send(message1);
                        } catch (MessagingException e){
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    public String helperFormat(ArrayList<String> list){
        String res = "";
        for(String str : list){
            res += str + "\n";
        }
        return res;
    }
}






