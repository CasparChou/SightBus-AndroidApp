package tenoz.lab.sightbus.data;

/**
 * Created by AppleCaspar on 2016/12/4.
 */

public class RoutesList {
    public String routeid;
    public String name;
    public String departure;
    public String destination;

    public RoutesList(String id, String name, String departure, String destination) {
        this.routeid = id;
        this.name = name;
        this.departure = departure;
        this.destination = destination;
    }
}
