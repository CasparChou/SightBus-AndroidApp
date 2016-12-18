package tenoz.lab.sightbus.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by AppleCaspar on 2016/12/4.
 */

public class EstimateStopList {
    public String routeID;
    public String routeName;
    public GoPack goDestination;
    public GoPack goDeparture;

    public EstimateStopList(JSONObject unpack) throws JSONException {
        this.routeID = unpack.getString("route");
        this.routeName = unpack.getString("rname");
        this.goDestination = new GoPack(
                false,
                unpack.getString("dst"),
                unpack.getString("dstesti"),
                unpack.getString("dstrecord")
        );
        this.goDeparture = new GoPack(
                true,
                unpack.getString("dep"),
                unpack.getString("depesti"),
                unpack.getString("deprecord")
        );
    }

    public String toString(){
        return  (
            String.format(
                "{ Route: %s, Name: %s, %s, %s }",
                this.routeID,
                this.routeName,
                this.goDestination,
                this.goDeparture
            )
        );
    }
    class GoPack{
        final boolean isBack;
        final String destination;
        final Integer countDown;
        final Integer updateTime;
        GoPack(boolean isBack, String destination, Integer countDown, Integer updateTime) {
            this.isBack = isBack;
            this.destination = destination;
            this.countDown = countDown;
            this.updateTime = updateTime;
        }
        GoPack(boolean isBack, String destination, String countDown, String updateTime) {
            this.isBack = isBack;
            this.destination = destination;
            if(countDown.equals(null) || countDown.equals("null") || countDown == null ){
                this.countDown = null;
            } else {
                this.countDown = Integer.parseInt(countDown);
            }

            if(updateTime.equals(null) || updateTime.equals("null") || updateTime == null ){
                this.updateTime = null;
            } else {
                this.updateTime = Integer.parseInt(updateTime);
            }
        }

        @Override
        public String toString() {
            try {
                return String.format(
                        " [ %s  %s : %d, %d ]",
                        (!this.isBack ? ("Go Destination") : ("Go Back")),
                        this.destination,
                        this.countDown,
                        this.updateTime,
                        new Date(new Long(this.updateTime))
                );
            }catch (NullPointerException n){
                return " [ null ] ";
            }
        }
    }
}

