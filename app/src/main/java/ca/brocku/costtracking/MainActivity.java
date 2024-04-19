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

/**
 * Main Activity that handles user authentication using Google Sign-In.
 *
 * @author Thanushan, Adrian, and Hamza
 */
public class MainActivity extends AppCompatActivity {
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    /**
     * Initializes the activity, Google Sign-In options and the sign-in button.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied. Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestIdToken(getString(R.string.server_client_id))
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(view -> signIn());
    }

    /**
     * Initiates the Google Sign-In process.
     */
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.
     *
     * @param requestCode The integer request code originally supplied to
     * startActivityForResult(), allowing you to identify who this result came from.
     * @param resultCode The integer result code returned by the child activity
     * through its setResult().
     * @param data An Intent, which can return result data to the caller.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    /**
     * Handles the result of the Google Sign-In process.
     *
     * @param completedTask Task containing the Google Sign-In account result.
     */
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            Intent intent = new Intent(MainActivity.this, GasPrices.class);
            intent.putExtra("userAccount", account);
            intent.putExtra("accEmail", account.getEmail());

            startActivity(intent);
            finish();
        } catch (ApiException e) {
            Log.w("Google Sign In Error", "signInResult:failed code=" + e.getStatusCode());
            handleError(e.getStatusCode());
        }
    }

    /**
     * Handles errors that may occur during the sign-in process.
     *
     * @param errorCode An integer error code that represents different errors
     * that may occur.
     */
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

    /**
     * Shows a dialog asking the user if they would like to retry the sign-in process
     * after a network error.
     */
    private void showRetryDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Network Error")
                .setMessage("Unable to connect. Would you like to retry?")
                .setPositiveButton("Retry", (dialog, which) -> signIn())
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
