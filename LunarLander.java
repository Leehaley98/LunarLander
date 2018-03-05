
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import java.io.*;
import java.net.*;

public class LunarLander extends JFrame implements Runnable {
    public static final long serialVersionUID = 2L;
    public static void main ( String[] args ) throws SocketException {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() { new CarModel(); }
        } );
    }

    DatagramPanel receive = new DatagramPanel();
    LanderDynamics lander = new LanderDynamics();
    public LunarLander() {
        super("Car Model");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel content = new JPanel( );
        content.setLayout( new BoxLayout( content, BoxLayout.Y_AXIS) );

        try {
            receive.setAddress(InetAddress.getLocalHost().getHostAddress(), false);
           receive.setPort( 20769, false);
        }catch(UnknownHostException e){
            System.err.println(e.getMessage());
        }
        content.add(receive);

        JPanel debug = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));




        content.add(debug);

        this.setContentPane(content);
        this.pack();
        this.setVisible(true);

        (new javax.swing.Timer((int)(lander.dt*1000),new LanderDynamics())).start();
        /* start thread that handles comminications */
        (new Thread(this)).start();
    }



    public void run() {
        try{
        /* set up socket for reception */
            SocketAddress address = receive.getSocketAddress();
            DatagramSocket socket = new DatagramSocket(address);

            while(true) {
                try{
                    /* start with fresh datagram packet */
                    byte[] buffer = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive( packet );
                    /* extract message and pick appart into
                       lines and key:value pairs
                    */
                    String message = new String(packet.getData());
                    String[] lines = message.trim().split("\n");
                    String[] pair  = lines[0].split(":");

                    switch( pair[0] ) {/*<-- Java now lets you do switches on strings :-) */
                        case "state":
                            if( pair[1].equals("?")){
                                String reply = lander.state();
                                packet.setData(reply.getBytes());
                                socket.send(packet);
                            }
                        break;
                        case "condition":
                            if( pair[1].equals("?")){
                                String reply = lander.condition();
                                packet.setData(reply.getBytes());
                                socket.send(packet);
                            }
                        break;
                        case "command":
                            if( pair[1].equals("!")){
                                String reply = lander.command(lines);
                                packet.setData(reply.getBytes());
                                socket.send(packet);
                            }
                        break;
                    }
                }catch(IOException e){
                    System.err.println(e.getMessage());
                }
            }
        }catch(SocketException e){
            System.err.println(e.getMessage());
        }
    }

    class LanderDynamics implements ActionListener {
        public double dt = 0.05;
        public double x;
        public double y;
        public double O;
        public double xdot;
        public double ydot;
        public double Odot;
        public double fuel;
        public double altitude;
        public String contact;
        public double throttle;
        public double vertical;
        public double horizontal;
        public double roll;
        public void actionPerformed(ActionEvent t) {
            /* where are my physics notes */

        }
        String state(){
            String m = new String("state:=\n");
            m.concat(String.format("x:%f\n",x));
            m.concat(String.format("y:%f\n",y));
            m.concat(String.format("O:%f\n",O));
            m.concat(String.format("x':%f\n",xdot));
            m.concat(String.format("y':%f\n",ydot));
            m.concat(String.format("O':%f\n",Odot));
            return m;
        }
        String condition(){
            String m = new String("condition:=\n");
            m.concat(String.format("fuel:%f%%\n",fuel));
            m.concat(String.format("altitude:%f\n",altitude));
            m.concat(String.format("contact:%s\n",contact));
            return m;
        }
        String command(String[] cmds){
            for(int i=1 ; i<cmds.length ; i++) {
                String[] pair = cmds[i].trim().split(":");
                switch( pair[0] ){
                    case "main-engine":
                        throttle = Double.parseDouble(pair[1]);
                        break;
                    case "rcs-vertical":
                        vertical = Double.parseDouble(pair[1]);
                        break;
                    case "rcs-horizontal":
                        horizontal = Double.parseDouble(pair[1]);
                        break;
                    case "rcs-roll":
                        roll = Double.parseDouble(pair[1]);
                        break;
                }
            }
            return new String("command:=\n");
        }
    }
    class Terrain {
        /* use 5m grid, 200 = 1000m = 1km */
        float[] grid = new float[2000];
        public Terrain() {
            for(int g=0 ; g<grid.length ; g++ ) grid[g] = g*5;
        }
    }
}
