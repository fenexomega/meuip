package jordyf.com.br.ip.model;

import com.orm.SugarRecord;

import java.util.Date;


/**
 * Created by jordy on 2/15/17.
 */

public class IPRegistry extends SugarRecord{
    private String ip;
    private Date date;

    public IPRegistry()
    {

    }

    public IPRegistry(String ip) {
        this.ip = ip;
        this.date = new Date();
    }

    public IPRegistry(String ip, Date date) {
        this.ip = ip;
        this.date = date;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
