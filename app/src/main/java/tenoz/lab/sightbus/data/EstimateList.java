package tenoz.lab.sightbus.data;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by AppleCaspar on 2016/12/4.
 */

public class EstimateList implements Serializable {
    public String stopid;
    public String name;
    public Integer seq;
    public Integer countdown;
    public Integer update;
    public Integer event;
    public String plate;
    public Boolean departure = false;
    public Boolean destination = false;
    public Boolean isPath = false;

    public EstimateList(JSONObject data) {
        this.stopid = data.optString("stop");
        this.name = data.optString("name");
        this.seq = data.optInt("seq");
        this.isPath = true;
    }

    public EstimateList(String id, String name, Integer seq, Boolean goBack, Integer time, Integer update, Integer event, String plate, Boolean departure, Boolean destination ) {
        this.stopid = id;
        this.name = name;
        this.seq = seq;
        this.countdown = time;
        this.update = update;
        this.event = event;
        this.plate = plate;
        this.departure = departure;
        this.destination = destination;
    }

    public String toString(){
        return  (
                this.stopid +", "+
                this.name +", "+
                this.seq +", "+
                this.countdown +", "+
                this.update +", "+
                this.event +", "+
                this.plate
        );
    }
}
