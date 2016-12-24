package com.project.esgi.shoppinglistesgi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.project.esgi.shoppinglistesgi.Util.Constant;
import com.project.esgi.shoppinglistesgi.WebService.ConnectionListener;
import com.project.esgi.shoppinglistesgi.WebService.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ShoppingListFragment extends Fragment {
    public static String product;

    public Button addBtn;
    private EditText productName;
    private ListView shoppingListView;

    ArrayList<HashMap<String, String>> shoppingList;

    String token = "f35bd1319725e50327f488012436fc35";

    public ShoppingListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        addBtn = (Button) view.findViewById(R.id.addBtn);
        productName = (EditText) view.findViewById(R.id.productName);
        shoppingListView = (ListView) view.findViewById(R.id.shopping_listView);

        shoppingList = new ArrayList<>();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });

        getLists();
        return view;
    }

    public void getLists() {

        String url = Constant.LIST_SHOPPINGLIST_URL+"?token="+token;

        final WebService asyncTask = new WebService(this.getActivity());
        asyncTask.setListener(new ConnectionListener() {
            @Override
            public void onSuccess(JSONObject json) {

                try {
                    JSONArray resultJSON = json.getJSONArray("result");

                    for (int i = 0; i < resultJSON.length(); i++) {
                        JSONObject rj = resultJSON.getJSONObject(i);

                        String name = rj.getString("name");
                        String token = rj.getString("user_token");

                        HashMap<String, String> product = new HashMap<>();
                        product.put("name", name);
                        product.put("user_token", token);

                        shoppingList.add(product);
                        Log.INFO(name);
                    }

                    /*ArrayAdapter<String> a = new ArrayAdapter<String>(this,
                            android.R.layout.simple_list_item_1, shoppingList);
                    shoppingListView.setAdapter(a);*/


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String msg) {

            }
        });

        asyncTask.execute(url);
    }

    public void addProduct(){
        addBtn.setEnabled(true);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Ajout d'un produit...");
        progressDialog.show();

        product = productName.getText().toString();

        String url = Constant.CREATE_SHOPPINGLIST_URL+"?token="+token+"&name="+product;

        final WebService asyncTask = new WebService(this.getActivity());
        asyncTask.setListener(new ConnectionListener() {
            @Override
            public void onSuccess(JSONObject json) {
                SharedPreferences sharedPreference = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreference.edit();
                progressDialog.dismiss();

                try {
                    JSONObject resultJSON = json.getJSONObject("result");
                    editor.putString("name", resultJSON.getString("name"));
                    editor.putString("user_token", resultJSON.getString("token"));
                    editor.apply();

                    Toast.makeText(getActivity(), "Produit ajouté avec succès !", Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String msg) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Problème rencontré lors de l'ajout", Toast.LENGTH_LONG).show();
            }
        });

        asyncTask.execute(url);
    }
}
