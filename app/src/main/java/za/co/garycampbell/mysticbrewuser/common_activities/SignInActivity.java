package za.co.garycampbell.mysticbrewuser.common_activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;
import za.co.garycampbell.mysticbrewuser.Prevalent;
import za.co.garycampbell.mysticbrewuser.R;
import za.co.garycampbell.mysticbrewuser.models.UserModel;
import za.co.garycampbell.mysticbrewuser.user_activities.UserHomeActivity;


public class SignInActivity extends AppCompatActivity {

    TextInputLayout eMail, _Password;
    CheckBox keepMeSignedIn;
    AppCompatButton signIn, goToRegisterUser;

    ProgressDialog loadingBar;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference loginRef;

    private String vendorKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Paper.init(this);

        eMail = findViewById(R.id.editTextLayoutTextPersonEmail);
        _Password = findViewById(R.id.editTextLayoutTextPassword);
        signIn = findViewById(R.id.sign_in_button);
        goToRegisterUser = findViewById(R.id.go_to_register_button);
        loadingBar = new ProgressDialog(this);

        keepMeSignedIn = findViewById(R.id.stay_signed_in_chk_box);
        keepMeSignedIn.setChecked(false);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        loginRef = database.getReference();

        String userEmailKey = Paper.book().read(Prevalent.userEmailKey);
        String userPasswordKey = Paper.book().read(Prevalent.userPasswordKey);
        String keepMeLoggedInForever = Paper.book().read(Prevalent.keepMeLoggedIn);
        String userEmail = eMail.getEditText().getText().toString().trim();
        String userPassword = _Password.getEditText().getText().toString().trim();
        if (TextUtils.isEmpty(userEmail)
                && TextUtils.isEmpty(userPassword)
                && userPasswordKey != null
                && userEmailKey != null
                && keepMeLoggedInForever != null)
        {
            loadingBar.setTitle("Verifying your Credentials");
            loadingBar.setMessage("Please wait ...... ");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            mAuth.signInWithEmailAndPassword(userEmailKey, userPasswordKey).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
//                    loadingBar.setTitle("Verifying your Credentials");
//                    loadingBar.setMessage("Please wait ...... ");
//                    loadingBar.setCanceledOnTouchOutside(false);
//                    loadingBar.show();

                    Toast.makeText(SignInActivity.this, "User Signed In Successfully", Toast.LENGTH_SHORT).show();

                    Intent goToHome = new Intent(SignInActivity.this, UserHomeActivity.class);
                    startActivity(goToHome);
                    loadingBar.dismiss();
                    finish();
                }
            });
        }


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogInUser();
            }
        });

        goToRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToRegisterActivity = new Intent(SignInActivity.this, RegisterUserActivity.class);
                startActivity(goToRegisterActivity);
            }
        });
    }

    private void LogInUser () {

        loadingBar.setTitle("Validating Log In Credentials");
        loadingBar.setMessage("Please wait While we search the database for your log in details ...... ");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        String userEmail = eMail.getEditText().getText().toString().trim();
        String userPassword = _Password.getEditText().getText().toString().trim();
        if (keepMeSignedIn.isChecked()) {
            Paper.book().write(Prevalent.userEmailKey, userEmail);
            Paper.book().write(Prevalent.userPasswordKey, userPassword);
            Paper.book().write(Prevalent.keepMeLoggedIn, "true");
            mAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        String userID = mAuth.getUid();

                        Toast.makeText(SignInActivity.this, "User Signed In Successfully", Toast.LENGTH_SHORT).show();
                        Intent goToHome = new Intent(SignInActivity.this, UserHomeActivity.class);
                        startActivity(goToHome);
                        loadingBar.dismiss();
                        finish();
                    }
                }
            });
        }
        else
        {
            mAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        String userID = mAuth.getUid();
                        Paper.book().destroy();
                        Toast.makeText(SignInActivity.this, "User Signed In Successfully", Toast.LENGTH_SHORT).show();
                        Intent goToHome = new Intent(SignInActivity.this, UserHomeActivity.class);
                        startActivity(goToHome);
                        loadingBar.dismiss();
                        finish();
                    }
                }
            });
        }
    }
}
