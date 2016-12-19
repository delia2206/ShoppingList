package com.project.esgi.shoppinglistesgi;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.project.esgi.shoppinglistesgi.Util.Constant;
import com.project.esgi.shoppinglistesgi.WebService.ConnectionListener;
import com.project.esgi.shoppinglistesgi.WebService.WebService;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivityLogin extends AppCompatActivity {

    private static final String TAG = "Main Activity";
    private static String mail;
    private static String password;

    private EditText email;
    private EditText pwd;
    public Button loginButton;
    //private static final String URL_TO_HIT = "http://appspaces.fr/esgi/shopping_list/account/login.php?email=the&password=t";
    //private static final String URL_TO_HIT = "http://jsonparsing.parseapp.com/jsonData/moviesData.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //new JSONTask().execute(URL_TO_HIT);
        email = (EditText) findViewById(R.id.email);
        pwd = (EditText) findViewById(R.id.password);

        loginButton = (Button) findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    public void login() {
        if (!validate()) {
            return;
        }

        loginButton.setEnabled(false);

        mail = email.getText().toString();
        password = pwd.getText().toString();

        String url = Constant.LOGIN_URL+"?email="+email+"&password="+password;

        final WebService asyncTask = new WebService();
        asyncTask.setListener(new ConnectionListener() {
            @Override
            public void onSuccess(JSONObject json) {
                SharedPreferences sharedPreference = MainActivityLogin.this.getSharedPreferences("settings", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreference.edit();

                try {
                    JSONObject resultJSON = json.getJSONObject("result");
                    editor.putString("first_name", resultJSON.getString("firstname"));
                    editor.putString("last_name", resultJSON.getString("lastname"));
                    editor.putString("email", resultJSON.getString("email"));
                    editor.putString("user_token", resultJSON.getString("token"));
                    editor.putBoolean("is_connected", true);
                    editor.apply();

                    onLoginSuccess();

                } catch (JSONException e) {
                    onLoginFailed();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String msg) {
                onLoginFailed();
            }
        });

        asyncTask.execute(url);
    }

    public boolean validate() {
        boolean valid = true;

        String mail = email.getText().toString();
        String password = pwd.getText().toString();


        if (mail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            email.setError("Veuillez saisir une adresse email valide");
            valid = false;
        } else {
            email.setError(null);
        }

        if (password.isEmpty()) {
            pwd.setError("doit être renseigné");
            valid = false;
        } else {
            pwd.setError(null);
        }

        return valid;
    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        Toast.makeText(MainActivityLogin.this, "Connecté avec succès !", Toast.LENGTH_LONG).show();

    }


    public void onLoginFailed() {
        Toast.makeText(MainActivityLogin.this, "Erreur de connexion", Toast.LENGTH_LONG).show();

        loginButton.setEnabled(true);
    }
}
