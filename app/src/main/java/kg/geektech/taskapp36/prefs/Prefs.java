package kg.geektech.taskapp36.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {

    private SharedPreferences preferences;

    public Prefs(Context context){
        preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
    }
    public void saveBoardState(){
        preferences.edit().putBoolean("isShown", true).apply();
    }
    public boolean isBoardShown(){
        return preferences.getBoolean("isShown", false);
    }
    public void setString(String s) {
        preferences.edit().putString("profileName", s).apply();
    }

    public String getString() {
        return preferences.getString("profileName", null);
    }

    public void setStringImg(String s) {
        preferences.edit().putString("profileImg", s).apply();
    }

    public String getStringImg() {
        return preferences.getString("profileImg", null);
    }
}