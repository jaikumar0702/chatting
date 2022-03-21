package com.example.chatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button register;
    private EditText username, password, email;
    private TextView loginUser;

    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);




        register= findViewById(R.id.register);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        email=findViewById(R.id.email);


        loginUser= findViewById(R.id.login_user);
        mRootRef= FirebaseDatabase.getInstance().getReference();
        mAuth= FirebaseAuth.getInstance();



        loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtUserName=username.getText().toString();

                String txtPassword=password.getText().toString();
                String txtEmail=email.getText().toString();

                if(TextUtils.isEmpty(txtUserName) || TextUtils.isEmpty(txtEmail)
                        || TextUtils.isEmpty(txtPassword) ){
                    Toast.makeText(RegisterActivity.this, "Please Fill the Required Fields", Toast.LENGTH_SHORT).show();
                }
                else if(txtPassword.length()<6){
                    Toast.makeText(RegisterActivity.this, "Weak Password", Toast.LENGTH_SHORT).show();
                }
                else{
                    registerUser(txtUserName,txtEmail,txtPassword);
                }

            }
        });

    }

    private void registerUser(String userName, String email, String password) {


        mAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                HashMap<String,Object> map= new HashMap<>();
                map.put("email",email);
                map.put("userName",userName);
                map.put("id",mAuth.getCurrentUser().getUid());
                map.put("bio","");
                map.put("ImageURL","default");


                mRootRef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "Update the Profile for better Experience", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        }

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
