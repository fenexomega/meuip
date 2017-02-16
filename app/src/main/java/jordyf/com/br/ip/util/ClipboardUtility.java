package jordyf.com.br.ip.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.design.widget.Snackbar;

/**
 * Created by jordy on 2/16/17.
 */

public class ClipboardUtility {
    public static void copyToClipBoard(Context context, String text)
    {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", text);
        clipboard.setPrimaryClip(clip);
    }
}
