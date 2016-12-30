package com.project.esgi.shoppinglistesgi;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.project.esgi.shoppinglistesgi.Util.Constant;
import com.project.esgi.shoppinglistesgi.WebService.ConnectionListener;
import com.project.esgi.shoppinglistesgi.WebService.WebService;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginFragment extends Fragment {
    public static String email;
    public static String password;

    private EditText mail;
    private EditText pwd;
    public Button loginBtn;
    public TextView signUpLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_login, container, false);

        mail = (EditText) view.findViewById(R.id.email);
        pwd = (EditText) view.findViewById(R.id.password);

        loginBtn = (Button) view.findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        signUpLink = (TextView) view.findViewById(R.id.link_signup);
        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        return view;
    }

    private void signup() {
        Fragment fragment = new SignupFragment();
        FragmentManager fragmentManager;

        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentLogin, fragment)
                .commit();
    }

    public void login() {
        if (!validation()) {
            return;
        }

        loginBtn.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authentification...");
        progressDialog.show();

        email = mail.getText().toString();
        password = pwd.getText().toString();

        String url = Constant.LOGIN_URL+"?email="+email+"&password="+password;

        final WebService asyncTask = new WebService(this.getActivity());
        asyncTask.setListener(new ConnectionListener() {
            @Override
            public void onSuccess(JSONObject json) {
                SharedPreferences sharedPreference = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreference.edit();
                progressDialog.dismiss();

                try {
                    JSONObject resultJSON = json.getJSONObject("result");
                    editor.putString("first_name", resultJSON.getString("firstname"));
                    editor.putString("last_name", resultJSON.getString("lastname"));
                    editor.putString("email", resultJSON.getString("email"));
                    editor.putString("user_token", resultJSON.getString("token"));
                    editor.putBoolean("is_connected", true);
                    editor.apply();
                    MainActivity.tokenLogin = resultJSON.getString("token");

                    onLoginSuccess();

                } catch (JSONException e) {
                    onLoginFailed();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String msg) {
                progressDialog.dismiss();
                onLoginFailed();
            }
        });

        asyncTask.execute(url);
    }

    public void onLoginSuccess() {
        loginBtn.setEnabled(true);
        Toast.makeText(getActivity(), "Connecté avec succès !", Toast.LENGTH_LONG).show();

        Fragment fragment = new ShoppingListFragment();
        FragmentManager fragmentManager;

        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentLogin, fragment)
                .commit();
    }


    public void onLoginFailed() {
        Toast.makeText(getActivity(), "Veuillez renseigner des identifiants corrects", Toast.LENGTH_LONG).show();

        loginBtn.setEnabled(true);
    }

    public boolean validation() {
        boolean valid = true;

        String email = mail.getText().toString();
        String password = pwd.getText().toString();


        if (email.isEmpty()) {
            mail.setError("Veuillez entrer une adresse email ");
            valid = false;
        } else {
            mail.setError(null);
        }

        if (password.isEmpty()) {
            pwd.setError("Le mot de pass ne doit pas être vide");
            valid = false;
        } else {
            pwd.setError(null);
        }

        return valid;
    }
}
