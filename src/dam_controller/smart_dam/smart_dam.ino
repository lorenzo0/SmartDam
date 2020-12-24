/*
 * AVVISO PER MODIFICARE LE COSTANTI RICHIESTE IN SPECIFICA.
 * 
 * Per modificare la minima e massima frequenza (in Hz) è necessario accedere alla classe Potentiometer.h
 * Per modificare la massima durata delle varie task invece:
 *    - SLEEP_TIME in IdleTask.h
 *    - MAX_TIME in RunningTask.h
 *    - ERROR_TIME in ErrorTask.h
*/

#define LED_UNO 3
#define HC_RX 0
#define HC_TX 1
/*#define SERVO_MOTOR 9*/
/* Grazie a softwareSerial è possibile instaurare una comunicazione di tipo seriale ASINCRONO
con qualsiasi coppia di pin digitali

rdx di arduino comunica con tdx di hc-06*/

#define CALIBRATION_TIME_SEC 10
#include "Scheduler.h"
#include "NormalState.h"
#include "MsgService.h"
#include "servo_motor_impl.h"
#include "Globals.h"
#include <SoftwareSerial.h>

Scheduler scheduler;
ServoMotor* pMotor;
SoftwareSerial bluetooth(HC_RX, HC_TX);
String bluethoot_message;

/* 
 *  In setup viene prevista una calibratura dei sensori, in particolare
 *  viene messo in azione il servomotore. Vengono inizializzate le task che sono previste
 *  per il corretto funzionamento del sistema;
 *    - normalState, il sistema si trova in condizione Normale, Ottimale
 *    - preAllarmState, il sistema sta per scaturire un'allarme
 *    - allarmState, il sistema è in stato di allarme
*/

void setup(){
  pinMode(LED_UNO,OUTPUT);

  Serial.begin(9600);
  bluetooth.begin(9600);
  
  /*pMotor = new ServoMotorImpl(9);
  
  MsgService.sendMsg("CALIBRATION");
  pMotor->on();  
    for (int i = 0; i < 180; i++) {
      pMotor->setPosition(i);         
      delay(15);
    }
    for (int i = 180; i >= 0; i--) {
      pMotor->setPosition(i);         
      delay(15);
    }
   pMotor->off();
  
  Serial.println("SENSORS READY.");
  delay(200);*/

  
  Task* normalState = new NormalState(LED_UNO);
  
  scheduler.init(50);
  MsgService.init();

  /* Nel normalState, la rilevazione viene effettuata una volta ogni 10 secondi*/
  normalState -> init(min_freq);

  scheduler.setIndexCurrentTaskActive(0);

  scheduler.addTask(normalState);

}

void loop() {
  //scheduler.schedule();
  bluethoot_conversation();
}

void bluethoot_conversation(){
  /*while(bluetooth.available()){
    bluethoot_message+=char(bluetooth.read());
    Serial.println("While: ");
    Serial.println(bluethoot_message);
  }
  if(!bluetooth.available())
  {
    if(bluethoot_message!="")
    {
      if(bluethoot_message == "H"){
          digitalWrite(LED_UNO, HIGH);
          Serial.println("Led ON"); //show the data
          delay(20);
          bluethoot_message=""; //clear the data
       }
       else if(bluethoot_message == "L"){
          digitalWrite(LED_UNO, LOW);
          Serial.println("Led OFF"); //show the data
          delay(20);
          bluethoot_message=""; //clear the data
       }
       Serial.println("IF: ");
       Serial.println(bluethoot_message);
    }
  }*/

  char c = Serial.read();
  if (c == 'H')
  {
    digitalWrite(LED_UNO, HIGH);
  }
  if (c == 'L')
  {
    digitalWrite(LED_UNO, LOW);
  }

}
