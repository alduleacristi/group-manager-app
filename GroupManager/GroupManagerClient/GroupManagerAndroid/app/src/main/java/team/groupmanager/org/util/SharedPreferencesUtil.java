package team.groupmanager.org.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Cristi on 4/18/2015.
 */
public class SharedPreferencesUtil {
    private Context ctx;

    public SharedPreferencesUtil(Context ctx){
        this.ctx = ctx;
    }

    public String getToken(){
        SharedPreferences tokenPref = ctx.getSharedPreferences("TokenPref",
                Context.MODE_PRIVATE);
        return tokenPref.getString("Token", "");
    }

    public void setToken(String token) {
        SharedPreferences tokenPref = ctx.getSharedPreferences("TokenPref",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor tokenEditor = tokenPref.edit();
        tokenEditor.putString("Token", token);
        tokenEditor.commit();
    }

    public void deleteToken() {
        SharedPreferences tokenPref = ctx.getSharedPreferences("TokenPref",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = tokenPref.edit();
        editor.clear();
        editor.commit();
    }

    public String getEmail(){
        SharedPreferences tokenPref = ctx.getSharedPreferences("UserEmail",
                Context.MODE_PRIVATE);
        return tokenPref.getString("Email", "");
    }

    public void setEmail(String email){
        SharedPreferences tokenPref = ctx.getSharedPreferences("UserEmail",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor tokenEditor = tokenPref.edit();
        tokenEditor.putString("Email", email);
        tokenEditor.commit();
    }

    public void deleteEmail() {
        SharedPreferences tokenPref = ctx.getSharedPreferences("UserEmail",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = tokenPref.edit();
        editor.clear();
        editor.commit();
    }
}
