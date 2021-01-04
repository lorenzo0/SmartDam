#include "Arduino.h"
#include "MsgServiceHTTP.h"

MsgServiceHTTP MsgService;

/* Si istanzia una connessione tramite il nome di quest'ultima e la sua password */
void MsgServiceHTTP::init(){
  WiFi.begin(nameWIFI, pwdWIFI);
  Serial.print("Connecting to WIFI" + String(nameWIFI));
  
  while (WiFi.status() != WL_CONNECTED) {  
    delay(500);
    Serial.print(".");
  } 
  delay(100);
  Serial.println("Connected to WIFI: "+ String(nameWIFI));
}


/* 
 *  Procedura necessaria per inviare un messaggio tramite la connessione prima istaurata.
 *  Il sistema prevede un'API per invio dati ed una per invio e memorizzazione del log.
 *  Viene dunque preparato il messaggio in formato JSON mantenendo il pattern richiesto;
 *  
 *    { "campo": "valoreStringa", "campo": valoreNumerico }
 *    
 *  Se il return code è pari a 200, allora l'invio è stato effettuato correttamente.
*/

void MsgServiceHTTP::sendMsg(const float& value, const String& state){
  if (WiFi.status()== WL_CONNECTED){
     HTTPClient http;
     http.begin(String(addressWIFI) + "/api/data");      
     http.addHeader("Content-Type", "application/json");     
     String msg = String("{ \"distance\": ") + String(value) + ", \"state\": \"" + state + "\", \"sender\": \"" + sender +"\"}";
     Serial.println("Messaggio da inviare: " + msg);
     int retCode = http.POST(msg);   
     http.end();  

     if (retCode == 200){
       Serial.println("Message-Sent!");   
     } else {
       Serial.println("Message-Not-Sent!");
     }
     delay(5000); 
  }else{
    Serial.println("Error in WiFi connection");
  }
}

void MsgServiceHTTP::sendLogMsg(const String& message){
  if (WiFi.status()== WL_CONNECTED){
     HTTPClient http;
     http.begin(String(addressWIFI) + "/api/log");      
     http.addHeader("Content-Type", "application/json");     
     String msg = String("{ \"sender\": \"") + sender + "\", \"message\": \"" + message + "\"}";
     Serial.println("Messaggio da inviare: " + msg);
     int retCode = http.POST(msg);   
     http.end();  

     if (retCode == 200){
       Serial.println("Message-Sent!");   
     } else {
       Serial.println("Message-Not-Sent!");
     }
     delay(5000); 
  }else{
    Serial.println("Error in WiFi connection");
  }
}

/* Metodo usato in fase di testing e debugging, utile per compredere se esiste ancora la connessione */
void MsgServiceHTTP::checkConnection(){
  if (WiFi.status()== WL_CONNECTED)
    Serial.println("true");
  else
    Serial.println("false");
}
