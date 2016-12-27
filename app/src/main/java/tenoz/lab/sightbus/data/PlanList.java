package tenoz.lab.sightbus.data;

import org.json.JSONObject;

/**
 * Created by AppleCaspar on 2016/12/4.
 */

public class PlanList {

    final public String route;
    final public String name;
    final public String depId;
    final public String dstId;
    final public String destination;
    final public Boolean goBack;
    final public Integer update;
    final public Integer far;
    final public Integer time;
    final public Double avgTime;


    public PlanList(String route, String name, String depId, String dstId, String destination, Boolean goBack, Integer update, Integer far, Integer time, Double avgTime) {
        this.route = route;
        this.name = name;
        this.depId = depId;
        this.dstId = dstId;
        this.destination = destination;
        this.goBack = goBack;
        this.update = update;
        this.far = far;
        this.time = time;
        this.avgTime = avgTime;
    }

    public PlanList(JSONObject object){
        this.route = object.optString("route");
        this.name = object.optString("name");
        this.depId = object.optString("depid");
        this.dstId = object.optString("dstid");;
        this.destination = object.optString("dst");
        this.goBack = object.optInt("direction") == 0 ?false :true;
        this.update = Integer.valueOf(object.optString("update"));;
        this.far = object.optInt("far");
        this.time = Integer.valueOf(object.optString("time"));
        this.avgTime = object.optDouble("avgTime");
    }

    public String toString(){
        return  (
                ""
//                this.stopid +", "+
//                this.name +", "+
//                this.seq +", "+
//                this.countdown +", "+
//                this.update +", "+
//                this.event +", "+
//                this.plate
        );
    }
}
