package jordyf.com.br.ip.util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import jordyf.com.br.ip.activity.MainActivity;

/**
 * Created by jordy on 2/15/17.
 */

public class HttpConnection {

    public interface ICommand
    {
        void Run(String response);

        void Error(String message);
    }

    public static void Get(Context context, String url, final ICommand command, int Timeout)
    {
        RequestQueue queue = Volley.newRequestQueue(context);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        command.Run(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                command.Error(error.getMessage());

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }
}
