package jordyf.com.br.ip.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import jordyf.com.br.ip.R;
import jordyf.com.br.ip.activity.ListActivity;
import jordyf.com.br.ip.activity.MainActivity;
import jordyf.com.br.ip.model.IPRegistry;
import jordyf.com.br.ip.util.ClipboardUtility;

/**
 * Created by jordy on 2/15/17.
 */

public class IPListaAdapter extends RecyclerView.Adapter<IPListaAdapter.IPHolder> {

    private Context context;
    private List<IPRegistry> list;
    private Date nowDate;


    private String no;

    private String yes;

    private String sure;

    private String textCopied;



    public IPListaAdapter(AppCompatActivity activity) {
        this.context = activity;
        list = IPRegistry.listAll(IPRegistry.class, "date DESC");
        this.nowDate = new Date();

        no = getStringForId(R.string.no);
        yes = getStringForId(R.string.yes);
        sure = getStringForId(R.string.sure_delete);
        textCopied = getStringForId(R.string.text_copied);
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
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
//        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        IPRegistry object = list.get(position);

        dateFormat = getDateFormat(object.getDate());

        String date1 = dateFormat.format(object.getDate());
        String date2 = dateFormat.format(nowDate);
        if( date1.equals(date2))
        {
            dateIsToday = true;
        }

        dateFormat = new SimpleDateFormat(dateFormat.toPattern() + " HH:mm");
        String hourString = hourFormat.format(object.getDate());


        holder._ip.setText(object.getIp());
        holder._time.setText( dateIsToday ?
                today + " " + hourString : dateFormat.format(object.getDate()));
        holder._copy.setOnClickListener(copyButtonClickListener(object.getIp()));
        holder._delete.setOnClickListener(deleteButtonClickListener(position));



    }

    private String getStringForId(int id)
    {
        return context.getString(id);
    }

    private View.OnClickListener deleteButtonClickListener(final int position)
    {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               
                list = IPRegistry.listAll(IPRegistry.class,"date DESC");
                AlertDialog.Builder builder = new  AlertDialog.Builder(context);
                builder.setPositiveButton(yes,okDialog(position));
                builder.setNegativeButton(no,noDialog());
                builder.setMessage(sure);
                builder.show();
            }
        };
    }

    private Dialog.OnClickListener noDialog()
    {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        };
    }

    private Dialog.OnClickListener okDialog(final int position)
    {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                IPRegistry obj = list.get(position);
                list.remove(obj);
                obj.delete();
                IPListaAdapter.this.notifyItemRemoved(position);
                IPListaAdapter.this.notifyItemRangeChanged(position,list.size());
            }
        };
    }

    private View.OnClickListener copyButtonClickListener(final String ip)
    {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardUtility.copyToClipBoard(context, ip);
                Toast.makeText(context, textCopied, Toast.LENGTH_LONG).show();
            }
        };
    }

    private SimpleDateFormat getDateFormat(Date object)
    {
        SimpleDateFormat dateFormat;
        String lang = context.getResources().getConfiguration().locale.getLanguage();

        if(lang.equals("en"))
        {
            dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        }
        else
        {
            dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        }
        return dateFormat;

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class IPHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.ip)
        TextView _ip;

        @BindView(R.id.date)
        TextView _time;

        @BindView(R.id.copy)
        ImageButton _copy;

        @BindView(R.id.delete)
        ImageButton _delete;

        public IPHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }

}
