package com.example.emotion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText name,email,password,rppassword;
    Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);
        mAuth = FirebaseAuth.getInstance();
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        rppassword=findViewById(R.id.rppassword);
        signup= findViewById(R.id.signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailUser=email.getText().toString().trim();
                final String passwordlUser=password.getText().toString().trim();
                mAuth.createUserWithEmailAndPassword(emailUser,passwordlUser)
                        .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                User user= new User(name.getText().toString().trim(),emailUser,passwordlUser);
                                if(task.isSuccessful()){
                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(Signup.this, "Đăng ký thành công" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(Signup.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                                    Intent intent  = new Intent(Signup.this, Signin.class);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(Signup.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}