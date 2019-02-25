package lander;
import java.util.Arrays;
public class Controller {
    Model lander;
    public Controller(Model model) {
        lander = model;
    }

    public String handle(String[] message) {
        String reply = "";
        /* first line of message */
        String[] type = message[0].split(":");
        int lines = message.length; /*count lines after first*/
        String[] payload = Arrays.copyOfRange(message,1,lines);
        switch( type[0] ) {
        case "command":
            reply = doCommand(payload);
            break;
        case "state":
            reply = doState(payload);
            break;
        }
        return reply;
    }

    String doCommand(String[] content) {
        StringBuffer response = new StringBuffer("command:=\n");
        for(String line : content) {
            String[] pair = line.split(":");
            switch( pair[0] ) {
            case "throttle":
                lander.throttle = Double.parseDouble( pair[1] );
                break;
            case "roll":
                lander.roll = Double.parseDouble( pair[1] );
                break;
            }
        }
        response = response.append("altitude:"+lander.altitude+"\n");
        response = response.append("fuel:"+lander.fuel+"\n");
        response = response.append("flying:"+ ((lander.isflying)?1:0) +"\n");
        response = response.append("crashed:"+ ((lander.iscrashed)?1:0) +"\n");
        response = response.append("orientation:"+ lander.O +"\n");
        response = response.append("Vx:"+ lander.xdot +"\n");
        response = response.append("Vy:"+ lander.ydot +"\n");

        return response.toString();
    }

    String doState(String[] content) {
        StringBuffer response = new StringBuffer("state:=\n");
        for(String line : content) {
            String[] pair = line.split(":");
            switch( pair[0] ) {
            case "throttle":
                lander.throttle = Double.parseDouble( pair[1] );
                break;
            case "roll":
                lander.roll = Double.parseDouble( pair[1] );
                break;
            }
        }
        return response.toString();
    }
}
