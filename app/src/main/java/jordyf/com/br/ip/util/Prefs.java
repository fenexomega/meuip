package jordyf.com.br.ip.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

/**
 * Created by jordy on 2/15/17.
 */

public class Prefs {
    public static String PREFS_ID = "MeuIP";

    public static void setBoolean(Context context, String chave, boolean value)
    {
        SharedPreferences pref = context.getSharedPreferences(PREFS_ID,0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(chave, value);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String chave)
    {
        SharedPreferences pref = context.getSharedPreferences(PREFS_ID,0);

        return pref.getBoolean(chave, true);
    }
}
