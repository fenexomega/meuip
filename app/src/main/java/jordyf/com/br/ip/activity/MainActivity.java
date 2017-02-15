package jordyf.com.br.ip.activity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import jordyf.com.br.ip.R;
import jordyf.com.br.ip.services.InternetReceiver;
import jordyf.com.br.ip.util.HttpConnection;
import jordyf.com.br.ip.util.Prefs;

public class MainActivity extends AppCompatActivity {

    private int MY_SOCKET_TIMEOUT_MS = 10000;

    @BindView(R.id.text_ip)
    TextView _ip;

    @BindView(R.id.copy)
    ImageButton _btn_copy;

    @BindView(R.id.share)
    ImageButton _btn_share;

    @BindView(R.id.reload)
    ImageButton _btn_reload;

    @BindView(R.id.adView1)
    AdView _adView;

    @BindView(R.id.adView2)
    AdView _adView2;

    @BindView(R.id.adView3)
    AdView _adView3;

    @BindView(R.id.check_notification)
    CheckBox _notifications;

    @BindString(R.string.error_string)
    String _error;

    @BindString(R.string.ip_text)
    String _loading;




    @BindView(android.R.id.content)
    View rootView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        AdRequest adRequest = new AdRequest.Builder().build();
        _adView.loadAd(adRequest);
        _adView2.loadAd(adRequest);
        _adView3.loadAd(adRequest);

        Boolean value = Prefs.getBoolean(this,"enable_notifications");
        _notifications.setChecked(value);

        getIp();


    }

    public void getIp()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo wifi = cm.getActiveNetworkInfo();

        if (wifi != null && wifi.isConnected()) {
            // Your code here
            requestIp();

        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                    .setMessage(R.string.wifi_needed).setTitle("");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        }
    }

    @OnClick(R.id.reload)
    public void ClickReload() {
        _ip.setText(_loading);
        getIp();
    }

    @OnCheckedChanged(R.id.check_notification)
    public void enableNotifications(boolean check)
    {
        Prefs.setBoolean(MainActivity.this,"enable_notifications",check);
    }


    private void requestIp() {
        String url = "http://ip.jordyf.me";

        HttpConnection.Get(this, url, new HttpConnection.ICommand() {
            @Override
            public void Run(String response) {
                _ip.setText(response);
            }

            @Override
            public void Error(String message) {
                Toast.makeText(MainActivity.this, _error, Toast.LENGTH_SHORT).show();
            }
        }, MY_SOCKET_TIMEOUT_MS);

    }

    @OnClick(R.id.copy)
    public void Copy()
    {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", _ip.getText());
        clipboard.setPrimaryClip(clip);

        Snackbar.make(rootView,"Texto copiado!",Snackbar.LENGTH_LONG).show();

    }

    @OnClick(R.id.share)
    public void ShareIp()
    {
        ShareCompat.IntentBuilder
                .from(MainActivity.this) // getActivity() or activity field if within Fragment
                .setText(_ip.getText())
                .setType("text/plain") // most general text sharing MIME type
                .setChooserTitle("")
                .startChooser();
    }


}
