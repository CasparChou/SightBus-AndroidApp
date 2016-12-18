package tenoz.lab.sightbus.data;

/**
 * Created by AppleCaspar on 2016/12/4.
 */

public class EstimateList {
    public String stopid;
    public String name;
    public Integer seq;
    public Integer countdown;
    public Integer update;
    public Integer event;
    public String plate;
    public Boolean departure;
    public Boolean destination;


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
