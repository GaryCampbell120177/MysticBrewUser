package za.co.garycampbell.mysticbrewuser.common_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import za.co.garycampbell.mysticbrewuser.R;


public class RegisterUserActivity extends AppCompatActivity {

    TextInputLayout userName, userSurname, userMobile, userEmail, userAddress, userPassword;
    AppCompatButton registerUser;

    FirebaseAuth firebaseAuth;
    DatabaseReference userRef;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userRef = firebaseDatabase.getReference("users");

        userName = findViewById(R.id.input_register_name);
        userSurname = findViewById(R.id.input_register_surname);
        userMobile = findViewById(R.id.input_register_mobile);
        userEmail = findViewById(R.id.input_register_email);
        userAddress = findViewById(R.id.input_register_address);
        userPassword = findViewById(R.id.input_register_password);

        registerUser = findViewById(R.id.register_user_button);
        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                CreateFirebaseUser();
            }
        });
    }

    private void CreateFirebaseUser()
    {
        String email = userEmail.getEditText().getText().toString().trim();
        String password = userPassword.getEditText().getText().toString().trim();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(RegisterUserActivity.this, "User Authentication Credentials created Successfully", Toast.LENGTH_SHORT).show();
                        String userID = firebaseAuth.getUid();
                        uploadDataToDatabase(userID);
                    }
                    else {
                        String message = task.getException().getMessage();
                        Toast.makeText(RegisterUserActivity.this, "Authentication Credentials not Created: " + message, Toast.LENGTH_LONG).show();
                    }
        });
    }


    private void uploadDataToDatabase(String userID)
    {
        String _username = userName.getEditText().getText().toString();
        String _usersurname = userSurname.getEditText().getText().toString();
        String _useremail = userEmail.getEditText().getText().toString();
        String _usermobile = userMobile.getEditText().getText().toString();
        String _useraddress = userAddress.getEditText().getText().toString();
        String _userpassword = userPassword.getEditText().getText().toString();

        HashMap<String, Object> userData = new HashMap<>();
        userData.put("userName", _username);
        userData.put("userSurname", _usersurname);
        userData.put("userEmail", _useremail);
        userData.put("userMobile", _usermobile);
        userData.put("userAddress", _useraddress);
        userData.put("userPassword", _userpassword);
        userData.put("userID", userID);

        userRef.child(userID).updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    Toast.makeText(RegisterUserActivity.this, "User data saved to the DataBase Successfully", Toast.LENGTH_SHORT).show();
                    Intent userRegistered = new Intent(RegisterUserActivity.this, SignInActivity.class);
                    userRegistered.putExtra("userID", userID);
                    startActivity(userRegistered);
                    finish();
                }
            }
        });
    }
}