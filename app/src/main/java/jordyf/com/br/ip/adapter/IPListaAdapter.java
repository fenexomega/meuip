package jordyf.com.br.ip.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jordyf.com.br.ip.R;
import jordyf.com.br.ip.model.IPRegistry;

/**
 * Created by jordy on 2/15/17.
 */

public class IPListaAdapter extends RecyclerView.Adapter<IPListaAdapter.IPHolder> {

    private Context context;
    private List<IPRegistry> list;
    private Date nowDate;


    public IPListaAdapter(Context context) {
        this.context = context;
        list = IPRegistry.listAll(IPRegistry.class);
        this.nowDate = new Date();
    }

    @Override
    public IPHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ip_card_adapter,parent, false);
        IPHolder holder = new IPHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(IPHolder holder, int position) {
        String today = context.getString(R.string.today);
        boolean dateIsToday = false;
        SimpleDateFormat dateFormat;
        SimpleDateFormat hourFormat = new SimpleDateFormat("hh:mm");
//        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        String lang = context.getResources().getConfiguration().locale.getLanguage();
        IPRegistry object = list.get(position);

        if(lang.equals("en"))
        {
            dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        }
        else
        {
            dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        }
        String date1 = dateFormat.format(object.getDate());
        String date2 = dateFormat.format(nowDate);
        if( date1.equals(date2))
        {
            dateIsToday = true;
        }

        dateFormat = new SimpleDateFormat(dateFormat.toPattern() + " hh:mm");
        String hourString = hourFormat.format(object.getDate());


        holder._ip.setText(object.getIp());
        holder._time.setText( dateIsToday ? today + " " + hourString : dateFormat.format(object.getDate()));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class IPHolder extends RecyclerView.ViewHolder
    {
        TextView _ip;
        TextView _time;

        public IPHolder(View itemView) {
            super(itemView);
            _ip     =   (TextView) itemView.findViewById(R.id.ip);
            _time   = (TextView) itemView.findViewById(R.id.date);
        }
    }
}
