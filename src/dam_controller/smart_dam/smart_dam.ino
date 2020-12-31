 /*
 * AVVISO PER MODIFICARE LE COSTANTI RICHIESTE IN SPECIFICA.
 * 
 * Per modificare la minima e massima frequenza (in Hz) è necessario accedere alla classe Potentiometer.h
 * Per modificare la massima durata delle varie task invece:
 *    - SLEEP_TIME in IdleTask.h
 *    - MAX_TIME in RunningTask.h
 *    - ERROR_TIME in ErrorTask.h
*/

/*
 *  BT module connection:  
 *  - RX is pin 2 => to be connected to TXD of BT module
 *  - TX is pin 3 => to be connected to RXD of BT module
 *
 */ 

#define LED_UNO 10
#define SERVO_MOTOR 9

/* Grazie a softwareSerial è possibile instaurare una comunicazione di tipo seriale ASINCRONO
con qualsiasi coppia di pin digitali

rdx di arduino comunica con tdx di hc-06*/

#define CALIBRATION_TIME_SEC 10
#include "Scheduler.h"
#include "servo_motor_impl.h"
#include "Globals.h"
#include "MsgServiceSERIAL.h"
#include "NormalPreState.h"
#include "AllarmState.h"
#include "ModifyState.h"
#include <SoftwareSerial.h>

Scheduler scheduler;
ServoMotor* pMotor;
String bluethoot_message;

Task* allarmState;

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

  while (!Serial){}
  Serial.println("ready to go."); 
  Led* led_uno = new Led(LED_UNO);
  
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

  
  scheduler.init();
  MsgServiceSERIAL.init();

  Task* normalPreState = new NormalPreState(LED_UNO);
  Task* allarmState = new AllarmState(LED_UNO,SERVO_MOTOR);
  Task* modifyState = new ModifyState(LED_UNO);

  /* Nel normalState, la rilevazione viene effettuata una volta ogni 10 secondi*/
  normalPreState -> init();
  allarmState -> init();
  modifyState -> init();

  scheduler.addTask(normalPreState);
  scheduler.addTask(allarmState);
  scheduler.addTask(modifyState);

  led_uno -> switchOff();
}

void loop() {
  scheduler.schedule();
}
