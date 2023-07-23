package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.UserDictionary;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {

    EditText nameEditText,emailEditText,passwordEditText,confirmPasswordEditText;
    Button createAccountBtn;
    ProgressBar progressBar;
    TextView loginBtnTextView;

    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        nameEditText = findViewById(R.id.name_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);
        createAccountBtn = findViewById(R.id.create_account_btn);
        progressBar = findViewById(R.id.progress_bar);
        loginBtnTextView = findViewById(R.id.login_text_view_btn);

        createAccountBtn.setOnClickListener(v-> createAccount());
        loginBtnTextView.setOnClickListener(v-> finish());


    }

    void createAccount(){

        String email  = emailEditText.getText().toString();
        String name  = nameEditText.getText().toString();
        String password  = passwordEditText.getText().toString();
        String confirmPassword  = confirmPasswordEditText.getText().toString();

        boolean isValidated = validateData(name,email,password,confirmPassword);
        if(!isValidated){
            return;
        }

        createAccountInFirebase(name,email,password);


    }

    void createAccountInFirebase(String name,String email,String password){
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CreateAccountActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if(task.isSuccessful()){

                            //creating acc is done
                            Utility.showToast(CreateAccountActivity.this,"Cuenta creada con éxito, revisar el correo electrónico para verificar");
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                        }else{
                            //failure
                            Utility.showToast(CreateAccountActivity.this,task.getException().getLocalizedMessage());
                        }
                    }
                }
        );



    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            createAccountBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String name,String email,String password,String confirmPassword){
        //validate the data that are input by user.

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Correo no es valido");
            return false;
        }
        if(password.length()<6){
            passwordEditText.setError("Los caracteres no son válidos");
            return false;
        }
        if(!password.equals(confirmPassword)){
            confirmPasswordEditText.setError("Contraseña no coincidente");
            return false;
        }
        return true;
    }

}