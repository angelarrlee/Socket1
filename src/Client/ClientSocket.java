package Client;


import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.json.JSONException;
import org.json.JSONObject;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

/*
 * 
 */
public class ClientSocket {
    
    //ip and port
    private static String ip = "localhost";
    private static int port = 4444;
    
    private static JSONObject output_json, addContent;
    private static String message = "";
    
    public static void main(String[] args) {
        
        
        //object that will store the parsed command line arguments
        //CmdLineArgs argsBean = new CmdLineArgs();
        //parser provided by args4j
        //CmdLineParser parser = new CmdLineParser(argsBean);
        
        try{
            
            Socket socket = new Socket(ip, port);
            //output and input stream
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            
            output.writeUTF("I want to connect!");
            message = (String)input.readUTF();
            System.out.println(message);
            
            output.writeUTF(command("color","green",0,0,0,0,"").toString());
            /*output.writeUTF(command("line","",1,2,3,4,"").toString());
            output.writeUTF(command("rect","",5,6,7,8,"").toString());
            output.writeUTF(command("oval","",9,10,11,12,"").toString());
            output.writeUTF(command("circle","",13,14,15,16,"").toString());
            output.writeUTF(command("eraser","",17,18,19,20,"").toString());
            output.writeUTF(command("text","",21,22,0,0,"This is Text").toString());
            output.writeUTF(command("clear","",0,0,0,0,"").toString());
            */
            while(!socket.isClosed()){
                
                if(input.available() > 0){
                    message = input.readUTF();
                    System.out.println(message);
                }
                TimeUnit.MILLISECONDS.sleep(100);
            }
            input.close();
            output.close();
            socket.close();
            
        }catch(UnknownHostException e){
            System.err.println("Please enter the correct hostname to connect!");
        }catch(ConnectException e){
            System.err.println("Please enter the correct port number to connect!");
        }catch(IOException e){
            System.err.println(e.getMessage());
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        } 
        
    }
    
    public static JSONObject command(String method, String color, int oldX, int oldY, int currentX, int currentY, String inputText){
        JSONObject command = new JSONObject();
        JSONObject arguments = new JSONObject();
        
        try {
                switch(method){
                    case "color":
                        arguments.put("color", color);
                        break;
                    case "line":
                    case "rect":
                    case "oval":
                    case "circle":
                    case "eraser":
                        arguments.put("oldX", oldX);
                        arguments.put("oldY", oldY);
                        arguments.put("currentX", currentX);
                        arguments.put("currentY", currentY);
                        break;
                    case "text":
                        arguments.put("oldX", oldX);
                        arguments.put("oldY", oldY);
                        arguments.put("inputText", inputText);
                        break;
                    case "clear":
                        break;
                    default:
                        break;
                }
                
                command.put("method", method);
                command.put("arguments", arguments);

        } catch (JSONException e) {
            System.err.println(e.getMessage());
        }
        return command;
    }
    
    
}
