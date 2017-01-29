package com.project.esgi.shoppinglistesgi;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.project.esgi.shoppinglistesgi.Util.Constant;
import com.project.esgi.shoppinglistesgi.WebService.ConnectionListener;
import com.project.esgi.shoppinglistesgi.WebService.WebService;
import com.project.esgi.shoppinglistesgi.models.ShoppingList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ShoppingListFragment extends Fragment {
    public static String product;

    public Button addBtn;
    public Button disconnectBtn;
    private EditText productName;
    private ListView shoppingListView;

    ArrayList<String> shoppingList;

    ArrayAdapter<String> adapter;

    String token = MainActivity.tokenLogin;

    public ShoppingListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        addBtn = (Button) view.findViewById(R.id.addBtn);
        disconnectBtn = (Button) view.findViewById(R.id.disconnectBtn);
        productName = (EditText) view.findViewById(R.id.productName);
        shoppingListView = (ListView) view.findViewById(R.id.shopping_listView);

        shoppingList = new ArrayList<>();

        adapter = new ArrayAdapter<String >(getActivity(),
                R.layout.list_item, R.id.item_title, shoppingList);
        shoppingListView.setAdapter(adapter);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });

        disconnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnect();
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


                    if (resultJSON != null) {
                        int len = resultJSON.length();
                        for (int i=0;i<len;i++){
                            JSONObject item = resultJSON.getJSONObject(i);
                            shoppingList.add(item.getString("name"));
                        }
                    }

                    /*adapter = new ArrayAdapter<String >(getActivity(),
                            android.R.layout.simple_list_item_1, shoppingList);
                    shoppingListView.setAdapter(adapter);*/
                    adapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String msg) {
                Toast.makeText(getActivity(), "Problème rencontré lors de l'accès aux listes", Toast.LENGTH_LONG).show();
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

        shoppingList.add(product);
        adapter.notifyDataSetChanged();

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

    public void disconnect(){
        MainActivity.tokenLogin = "";
        Fragment fragment = new LoginFragment();
        FragmentManager fragmentManager;

        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentLogin, fragment)
                .commit();
        Toast.makeText(getActivity(), "Déconnecté avec succès !", Toast.LENGTH_LONG).show();
    }
}
