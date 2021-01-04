import javax.swing.*;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.IOException;  
import java.util.TimerTask;
import java.util.Timer;

/*
 * Viene implementata una semplice dashboard che si connette, tramite
 * HTTP al servizio smart_dam e riceve i dati in modo costante.
 * 
 * Questa interfaccia è prevista per una - sola lettura - dei dati.
 * Le informazioni, tramite GET, vengono richieste da un task asincrono,
 * ogni 5000 millisecondi, quindi 5 secondi, per garantire un servizio efficiente.
 * 
 * Viene previsto un dato di tipo DataPoint in entrata che coinvolge i 5 elementi presenti
 * e caricati dai vari sottosistemi nel server;
 * 		
 * 		float distance --> Distanza da sonar, corrisponde a livello dell'acqua
 *		String time --> TimeStamp ultima rilevazione DATA | ORA
 *		String state --> Stato ultima rilevazione
 *		int damAngle --> Angolo apertura diga
 *		String sender --> Entità ultima rilevazione e/o ultimo upload dato
*/

public class View implements ActionListener{  
    JTextField tofill_state, tofill_time, tofill_distance, tofill_angle, tofill_sender;  
    JLabel state, time, distance, angle, sender, welcome_dash;
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    
    HTTPRequest request = new HTTPRequest();
	DataPoint dp;
	
    View(){  
        JFrame f= new JFrame();  
        
        welcome_dash = new JLabel();
        welcome_dash.setText("-- Dasboard Smart Dam --");
        welcome_dash.setBounds(130,0,150,20);
        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        f.setLocation(dim.width/2-f.getSize().width/2, dim.height/2-f.getSize().height/2);
        
        tofill_state=new JTextField();  
        tofill_state.setBounds(200,50,150,20); 
        
        state = new JLabel();
        state.setText("State: ");
        state.setBounds(50,50,150,20);
        
        tofill_time=new JTextField();  
        tofill_time.setBounds(200,100,150,20);  
        
        time = new JLabel();
        time.setText("Last data time: ");
        time.setBounds(50,100,150,20);
        
        tofill_distance=new JTextField();  
        tofill_distance.setBounds(200,150,150,20);  
        
        distance = new JLabel();
        distance.setText("Level water: ");
        distance.setBounds(50,150,150,20);
        
        tofill_angle=new JTextField();  
        tofill_angle.setBounds(200,200,150,20);  
        
        angle = new JLabel();
        angle.setText("Opening Dam: ");
        angle.setBounds(50,200,150,20);
        
        tofill_sender=new JTextField();  
        tofill_sender.setBounds(200,250,150,20);  
        
        sender = new JLabel();
        sender.setText("Sender: ");
        sender.setBounds(50,250,150,20);
        
        f.add(welcome_dash);
        
        f.add(tofill_state);
        f.add(tofill_time);
        f.add(tofill_distance);
        f.add(tofill_angle);
        f.add(tofill_sender);
        
        f.add(state);
        f.add(time);
        f.add(distance);
        f.add(angle);
        f.add(sender);
        
        f.setSize(450,400);  
        f.setLayout(null);  
        f.setVisible(true);  
        
        tofill_state.setEditable(false);
        tofill_time.setEditable(false);
        tofill_distance.setEditable(false);
        tofill_angle.setEditable(false);
        tofill_sender.setEditable(false);
        
        try {
			dp = request.sendGET();
			updateFields(dp);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }         
    
    void updateFields(DataPoint dp) {
    	if(dp != null) {
	    	tofill_state.setText(dp.getState());
	        tofill_time.setText(dp.getTime());
	        tofill_distance.setText(Float.toString(dp.getDistance()));
	        tofill_angle.setText(Integer.toString(dp.getDamAngle()));
	        tofill_sender.setText(dp.getSender());
    	}else {
    		tofill_state.setText("No data...");
            tofill_time.setText("No data...");
            tofill_distance.setText("No data...");
            tofill_angle.setText("No data...");
            tofill_sender.setText("No data...");
    	}   
    }
    
public static void main(String[] args) { 
    Timer     timer = new Timer();
    UpdateInfo ui = new UpdateInfo();
    timer.schedule( ui, 5000, 10000 );
}



@Override
public void actionPerformed(ActionEvent e) {
} 

}

class UpdateInfo extends TimerTask {
	HTTPRequest request = new HTTPRequest();
	DataPoint dp;
	View v;
	
	public UpdateInfo() {
		v = new View();
	}
	
    public void run(){
		 requestData();
    }
    
    public void requestData() {
    	try {
			dp = request.sendGET();
			v.updateFields(dp);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}