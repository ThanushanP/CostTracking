package ca.brocku.costtracking;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestIdToken(getString(R.string.server_client_id))
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(view -> signIn());
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            String personName = account.getDisplayName();  // Get the user's display name
            String personGivenName = account.getGivenName();  // Get the user's first name
            String personFamilyName = account.getFamilyName();  // Get the user's last name
            String personEmail = account.getEmail();  // Get the user's email address
            String personId = account.getId();  // Get the user's ID

            // Log the name or use it as needed
            Log.i("Google Sign In", "User name: " + personName);

            Intent intent = new Intent(MainActivity.this, GasPrices.class);

            intent.putExtra("userAccount", account);
            intent.putExtra("accEmail",personEmail);

            startActivity(intent);

            finish();
        } catch (ApiException e) {

            Log.w("Google Sign In Error", "signInResult:failed code=" + e.getStatusCode());

            handleError(e.getStatusCode());
        }
    }

    private void handleError(int errorCode) {
        if (errorCode == GoogleSignInStatusCodes.NETWORK_ERROR) {
            showRetryDialog();
            return;
        }

        String message;
        switch (errorCode) {
            case GoogleSignInStatusCodes.INVALID_ACCOUNT:
                message = "Invalid account. Please check your account settings.";
                break;
            case GoogleSignInStatusCodes.SIGN_IN_REQUIRED:
                message = "Sign in required. Please sign in to continue.";
                break;
            default:
                message = "An error occurred. Please try signing in again.";
                break;
        }

        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }

    private void showRetryDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Network Error")
                .setMessage("Unable to connect. Would you like to retry?")
                .setPositiveButton("Retry", (dialog, which) -> signIn())
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

}
