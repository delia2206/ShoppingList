package com.project.esgi.shoppinglistesgi.WebService;

import org.json.JSONObject;

/**
 * Created by delia on 10/12/2016.
 */
public interface ConnectionListener {
    public void onSuccess(JSONObject obj);
    public void onFailed(String msg);

}
