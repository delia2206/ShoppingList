package com.project.esgi.shoppinglistesgi;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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


public class SignupFragment extends Fragment {
    public static String firstname;
    public static String lastname;
    public static String email;
    public static String password;

    private EditText fname;
    private EditText lname;
    private EditText mail;
    private EditText pwd;
    public Button signupBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_signup, container, false);

        fname = (EditText) view.findViewById(R.id.firstname);
        lname = (EditText) view.findViewById(R.id.lastname);
        mail = (EditText) view.findViewById(R.id.email);
        pwd = (EditText) view.findViewById(R.id.password);

        signupBtn = (Button) view.findViewById(R.id.btn_signup);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        return view;
    }


    public void signup() {
        if (!validation()) {
            return;
        }

        signupBtn.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Enregistrement...");
        progressDialog.show();

        firstname = fname.getText().toString();
        lastname = lname.getText().toString();
        email = mail.getText().toString();
        password = pwd.getText().toString();

        String url = Constant.SIGNUP_URL+"?firstname="+firstname+"&lastname="+lastname+"&email="+email+"&password="+password;

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

                    onSignupSuccess();

                } catch (JSONException e) {
                    onSignupFailed();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String msg) {
                progressDialog.dismiss();
                onSignupFailed();
            }
        });

        asyncTask.execute(url);
    }

    public void onSignupSuccess() {
        signupBtn.setEnabled(true);
        Toast.makeText(getActivity(), "Enregistré avec succès !", Toast.LENGTH_LONG).show();

        Fragment fragment = new ShoppingListFragment();
        FragmentManager fragmentManager;

        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentLogin, fragment)
                .commit();
    }


    public void onSignupFailed() {
        Toast.makeText(getActivity(), "Un problème a eu lieu à l'enregistrement", Toast.LENGTH_LONG).show();

        signupBtn.setEnabled(true);
    }

    public boolean validation() {
        boolean valid = true;

        String firstname = fname.getText().toString();
        String lastname = lname.getText().toString();
        String email = mail.getText().toString();
        String password = pwd.getText().toString();

        if (firstname.isEmpty()) {
            fname.setError("Veuillez entrer votre prénom ");
            valid = false;
        } else {
            fname.setError(null);
        }

        if (lastname.isEmpty()) {
            lname.setError("Veuillez entrer votre nom ");
            valid = false;
        } else {
            lname.setError(null);
        }

        if (email.isEmpty()) {
            mail.setError("Veuillez entrer votre adresse email ");
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
