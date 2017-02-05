package com.project.esgi.shoppinglistesgi;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.project.esgi.shoppinglistesgi.Util.Constant;
import com.project.esgi.shoppinglistesgi.WebService.ConnectionListener;
import com.project.esgi.shoppinglistesgi.WebService.WebService;
import com.project.esgi.shoppinglistesgi.models.ShoppingList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.List;

/**
 * Created by delia on 01/01/2017.
 */
public class ShoppingListAdapter extends ArrayAdapter<String> {

    Context context;
    private int layout;
    private List<String> shoppingList;
    private static final String LOG = "log";

    private static class ViewHolder {
        public Button deleteBtn;
        public TextView item_title;
    }

    public ShoppingListAdapter(Context context, int list_item, List<String> lists) {
        super(context, R.layout.list_item, lists);
        layout = list_item;
        shoppingList = lists;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);

            viewHolder.deleteBtn = (Button) convertView.findViewById(R.id.deleteBtn);
            viewHolder.item_title = (TextView) convertView.findViewById(R.id.item_title);
            convertView.setTag(viewHolder);

        }
        viewHolder = (ViewHolder) convertView.getTag();
        //final ShoppingList list = getItem(position);


        //Au click sur le bouton supprimer
        viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*SharedPreferences sharedPreferences = getContext().getSharedPreferences("shoppingList", Context.MODE_PRIVATE);
                String token = sharedPreferences.getString("token", "");

                final WebService asyncTask = new WebService(context);
                String url;

                url = Constant.REMOVE_PRODUCT_URL + "?token=" + token + "&id=" + list.getId();
                asyncTask.execute(url);

                asyncTask.setListener(new ConnectionListener() {

                    @Override
                    public void onSuccess(JSONObject object) {

                        try {

                            Integer code = object.getInt("code");

                            if (code == 0) {

                                Toast toast = Toast.makeText(getContext(), "Produit supprimé avec succès", Toast.LENGTH_LONG);
                                toast.show();

                            } else {

                                Toast toast = Toast.makeText(getContext(), "Error, please try later", Toast.LENGTH_LONG);
                                toast.show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailed(String msg) {
                        Toast.makeText(getContext(), "Problème rencontré lors de la suppression", Toast.LENGTH_LONG).show();
                    }
                });*/
                //Toast.makeText(getContext(), "Click, position = " + position, Toast.LENGTH_SHORT).show();
                //lie le tableau à la position cliquer pour récupérer l'id : MainActivity.idItemList.get(position)
                //Toast.makeText(getContext(), MainActivity.idItemList.get(position), Toast.LENGTH_LONG).show();


                //URL de suppression d'un item de la liste
                String urlDelete = Constant.REMOVE_SHOPPINGLIST_URL+"?token="+MainActivity.tokenLogin+"&id="+ MainActivity.idItemList.get(position);
                //execution
                final WebService asyncTask = new WebService(getContext());
                asyncTask.setListener(new ConnectionListener() {
                    @Override
                    public void onSuccess(JSONObject json) {

                        try {
                            JSONObject resultJSON = json.getJSONObject("result");
                            Toast.makeText(getContext(), "Item supprimé", Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            //Toast.makeText(getContext(), "Erreur", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed(String msg) {
                        Toast.makeText(getContext(), "Problème rencontré lors de la suppression", Toast.LENGTH_LONG).show();
                    }
                });

                asyncTask.execute(urlDelete);
                //Rappeler la méthode getLists dans le ShoppingListFrament
                notifyDataSetChanged();

            }
        });
        viewHolder.item_title.setText(getItem(position));

        return convertView;
    }
}
