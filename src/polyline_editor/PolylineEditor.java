package polyline_editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PolylineEditor {

    private PrintWriter writer;
    private BufferedReader reader;
    private String	name;

    public PolylineEditor(Socket s, String n) {
        name = n;
        setUpNetworking(s);	//bufferedReader, writer 생성
        go();
        Thread t = new Thread(new PolylineEditor.IncomingReader());
        t.start();
    }

    private boolean setUpNetworking(Socket s) {
        try {
            Socket sock = s;
            reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            writer = new PrintWriter(s.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return  false;
        }
        return true;
    }

    public class IncomingReader implements Runnable{
        public void run(){
            System.out.println("ready to receive");
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    String[] tokens = message.split(":");
                    if(tokens[0].equals(name)) continue;
                    if (tokens[1].equals("clear")) {
                        pline.clear();
                        drawPanel.repaint();
                    }
                    if(tokens[1].equals("close")){
                        pline.closedPoint();
                        drawPanel.repaint();
                    }
                    else{
                        pline.executeCommand(tokens[1]);
                        drawPanel.repaint();
                    }

                }
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }



    class Broadcaster{
        public void broadcastCommand(String cmd){
            writer.println(name + ":" + cmd);
            System.out.println(name + ":" + cmd);
            writer.flush();
        }
    }
    private Broadcaster caster = new Broadcaster();



    private Polyline pline;
    private MyDrawPanel drawPanel;
    public void go(){
        JFrame frame = new JFrame("Polyline Editor: " + name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pline = new Polyline(caster);
        drawPanel =  new MyDrawPanel();

        drawPanel.setmPolyline(pline);
        drawPanel.addMouseListener(pline);
        drawPanel.addMouseMotionListener(pline);

        drawPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        frame.getContentPane().add(BorderLayout.CENTER, drawPanel);

        JButton clearButton = new JButton("clear");
        JButton closedButton = new JButton("closed");
        JPanel panel = new JPanel();
        frame.add(BorderLayout.SOUTH, panel);
        panel.add(clearButton);
        panel.add(closedButton);

        clearButton.addActionListener((event) -> {
            pline.clear(); drawPanel.repaint();
            caster.broadcastCommand("clear");
        });

        closedButton.addActionListener((event) -> {
            pline.closedPoint();
            drawPanel.repaint();
            caster.broadcastCommand("close");
        });

        BorderLayout layout = (BorderLayout) frame.getContentPane().getLayout();
        layout.setHgap(10);
        layout.setVgap(10);

        frame.setSize(400, 400);
        frame.setVisible(true);

    }
}
