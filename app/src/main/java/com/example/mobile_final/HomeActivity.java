package com.example.mobile_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
//import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.widget.*;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    //    private FloatingActionButton floatingActionButton1;
    private FloatingActionButton floatingActionButton2;

    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String onlineUserID;

    private ProgressDialog loader;

    private String key = "";
    private String task;
    private String description;
    private boolean isFinished;
    private int finishedCount;

    private int count;
    private SpeechRecognizer sr;
    private LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.homeToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Todosiast");
        mAuth = FirebaseAuth.getInstance();
        lottieAnimationView = findViewById(R.id.animationView);
        lottieAnimationView.bringToFront();

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView2 = findViewById(R.id.recyclerView2);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setReverseLayout(true);
        linearLayoutManager1.setStackFromEnd(true);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(linearLayoutManager1);

        loader = new ProgressDialog(this);

        mUser = mAuth.getCurrentUser();
        onlineUserID = mUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("tasks").child(onlineUserID);
        count = 0; //initialize it to 0
        floatingActionButton2 = findViewById(R.id.fab);
        floatingActionButton2.setOnClickListener(v -> addTask());

        //enable daily login rewards
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR), month = calendar.get(Calendar.MONTH), day = calendar.get(Calendar.DAY_OF_MONTH);
        String todaysDate = year + "" + month + "" + day;
        SharedPreferences pref = getSharedPreferences("PREF", 0);
        boolean isToday = pref.getBoolean(todaysDate, false);

        if (!isToday){
            Toast.makeText(this, "Daily reward!!!", Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(todaysDate, true);
            editor.apply();
        } else {
            Toast.makeText(this, "You have earned daily reward today!!!", Toast.LENGTH_LONG).show();
        }
    }

    private void addTask() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View myView = inflater.inflate(R.layout.input_file, null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final EditText task = myView.findViewById(R.id.task);
        final EditText description = myView.findViewById(R.id.description);


        ImageButton imageButton = myView.findViewById(R.id.speakBtn);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }

//        if(SpeechRecognizer.isRecognitionAvailable(this)) {
//            //SpeechRecognizer sr = SpeechRecognizer.createSpeechRecognizer(this);
//            System.out.println("found on device");
//        } else {
//            System.out.println("no sr found on device");
//        }
        //sr = SpeechRecognizer.createSpeechRecognizer(this, ComponentName.unflattenFromString("com.google.android.googlequicksearchbox/com.google.android.voicesearch.serviceapi.GoogleRecognitionService"));

        sr = SpeechRecognizer.createSpeechRecognizer(this);

        Intent srIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        srIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        srIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        imageButton.setOnClickListener(view -> {
            if (count == 0) {
                imageButton.setImageDrawable(getDrawable(R.drawable.ic_baseline_mic_24));
                sr.startListening(srIntent);
                count = 1;
            } else {
                imageButton.setImageDrawable(getDrawable(R.drawable.ic_baseline_mic_off_24));
                sr.stopListening();
                count = 0;
            }
        });
        sr.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
            }

            @Override
            public void onBeginningOfSpeech() {
            }

            @Override
            public void onRmsChanged(float v) {
            }

            @Override
            public void onBufferReceived(byte[] bytes) {
            }

            @Override
            public void onEndOfSpeech() {
            }

            @Override
            public void onError(int i) {
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> data = results.getStringArrayList(sr.RESULTS_RECOGNITION);
                System.out.println(data);
                if (data != null) {
                    String tmp = description.getText().toString();
                    tmp += data.get(0);
                    description.setText(tmp);
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {
            }

            @Override
            public void onEvent(int i, Bundle bundle) {
            }
        });

        Button save = myView.findViewById(R.id.saveBtn);
        Button cancel = myView.findViewById(R.id.cancelBtn);

        cancel.setOnClickListener(v -> dialog.dismiss());

        save.setOnClickListener(v -> {
            String mTask = task.getText().toString().trim();
            String mDescription = description.getText().toString().trim();
            String id = reference.push().getKey();
            String date = DateFormat.getDateInstance().format(new Date());

            if (TextUtils.isEmpty(mTask)) {
                task.setError("Task Required");
                return;
            }
            if (TextUtils.isEmpty(mDescription)) {
                description.setError("Description Required");
                return;
            } else {
                loader.setMessage("Adding your data");
                loader.setCanceledOnTouchOutside(false);
                loader.show();

                Model model = new Model(mTask, mDescription, id, date);
                reference.child(id).setValue(model).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(HomeActivity.this, "Task has been inserted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        String error = task1.getException().toString();
                        Toast.makeText(HomeActivity.this, "Failed: " + error, Toast.LENGTH_SHORT).show();
                    }
                    loader.dismiss();
                });

            }

            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Model> options = new FirebaseRecyclerOptions.Builder<Model>()
                .setQuery(reference.orderByChild("finished").equalTo(false), Model.class)
                .build();

        FirebaseRecyclerAdapter<Model, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Model, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull final Model model) {
                holder.setDate(model.getDate());
                holder.setTask(model.getTask());
                holder.setDesc(model.getDescription());
                holder.setFinish(model.getFinished());

                holder.mView.setOnClickListener(v -> {
                    key = getRef(position).getKey();
                    task = model.getTask();
                    description = model.getDescription();
                    isFinished = model.getFinished();

                    updateTask();
                });

                holder.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.setFinish(!model.getFinished());
                        notifyDataSetChanged();
                        reference.child(getRef(position).getKey()).child("finished").setValue(!model.getFinished());
                        lottieAnimationView.setVisibility(View.VISIBLE);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                lottieAnimationView.setVisibility(View.INVISIBLE);
                            }
                        }, 2000);
                    }
                });
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieved_layout, parent, false);
                return new MyViewHolder(view);
            }
        };
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                adapter.notifyDataSetChanged();
            }
        });

        recyclerView.setAdapter(adapter);
        adapter.startListening();

        FirebaseRecyclerOptions<Model> options2 = new FirebaseRecyclerOptions.Builder<Model>()
                .setQuery(reference.orderByChild("finished").equalTo(true), Model.class)
                .build();
        FirebaseRecyclerAdapter<Model, FinishViewHolder> adapter2 = new FirebaseRecyclerAdapter<Model, FinishViewHolder>(options2) {
            @Override
            protected void onBindViewHolder(@NonNull FinishViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull final Model model) {
                holder.setFinish(model.getFinished());
                holder.setTask(model.getTask());

                holder.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.setFinish(!model.getFinished());
                        notifyDataSetChanged();
                        reference.child(getRef(position).getKey()).child("finished").setValue(!model.getFinished());
                    }
                });
            }

            @NonNull
            @Override
            public FinishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.finished_layout, parent, false);
                return new FinishViewHolder(view);
            }
        };
        adapter2.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                adapter2.notifyDataSetChanged();
            }
        });
        recyclerView2.setAdapter(adapter2);
        adapter2.startListening();
    }

    private void updateTask() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.update_data, null);
        myDialog.setView(view);

        final AlertDialog dialog = myDialog.create();

        final EditText mTask = view.findViewById(R.id.mEditTextTask);
        final EditText mDescription = view.findViewById(R.id.mEditTextDescription);

        mTask.setText(task);
        mTask.setSelection(task.length());

        mDescription.setText(description);
        mDescription.setSelection(description.length());

        Button delButton = view.findViewById(R.id.btnDelete);
        Button updateButton = view.findViewById(R.id.btnUpdate);

        updateButton.setOnClickListener(v -> {
            task = mTask.getText().toString().trim();
            description = mDescription.getText().toString().trim();

            String date = DateFormat.getDateInstance().format(new Date());

            Model model = new Model(task, description, key, date);

            reference.child(key).setValue(model).addOnCompleteListener(task -> {

                if (task.isSuccessful()) {
                    Toast.makeText(HomeActivity.this, "Data has been updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    String err = task.getException().toString();
                    Toast.makeText(HomeActivity.this, "update failed " + err, Toast.LENGTH_SHORT).show();
                }

            });

            dialog.dismiss();

        });

        delButton.setOnClickListener(v -> {
            reference.child(key).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(HomeActivity.this, "Task deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    String err = task.getException().toString();
                    Toast.makeText(HomeActivity.this, "Failed to delete task " + err, Toast.LENGTH_SHORT).show();
                }
            });
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                mAuth.signOut();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
            case R.id.sendEmail:
//                AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
//                LayoutInflater inflater = LayoutInflater.from(this);
//                View view = inflater.inflate(R.layout.send_email, null);
//                myDialog.setView(view);
//                final AlertDialog dialog = myDialog.create();
//
//                Button cancelBtn = view.findViewById(R.id.btnCancel);
//                Button sendBtn = view.findViewById(R.id.btnSend);
//                EditText task = view.findViewById(R.id.emailAddress);
//
//                cancelBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//
//                sendBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                });
//                dialog.show();
                loader.setMessage("Loading, please wait");
                loader.show();
                Intent sendEmail = new Intent(this, SendEmail.class);
                Intent intent1 = getIntent();
                sendEmail.putExtra("username", intent1.getStringExtra("username"));
                sendEmail.putExtra("password", intent1.getStringExtra("password"));
                startActivity(sendEmail);
                loader.dismiss();
                break;

            case R.id.reward:
                loader.setMessage("Loading, please wait");
                loader.show();
                countFinish();
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    loader.dismiss();
                    startEvent();
                }, 1000);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Grated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void countFinish() {
        Query query = reference.orderByChild("finished").equalTo(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                finishedCount = (int) snapshot.getChildrenCount();
                Log.d("HomeActivity", finishedCount + " in count");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void startEvent() {
        Intent intent1 = new Intent(HomeActivity.this, RewardActivity.class);
        intent1.putExtra("FinishedCount", finishedCount);
        Log.d("HomeActivity", finishedCount + " in start");
        startActivity(intent1);
    }
}
