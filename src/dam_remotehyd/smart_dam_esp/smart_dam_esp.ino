#define SONAR_TRIG 4  //D4
#define SONAR_ECHO 5  //D3
#define LED_UNO 16  //D0

#include "Sonar.h"
#include "MsgServiceHTTP.h"
#include "Globals.h"

/*#include <iostream>
#include <string>
#include <stdlib.h>*/

Sonar* sonar;
Led* led;
long currentTs, ts0;
String state;
double currentDistance;
float stateTemp;

void setup() {
  Serial.begin(9600);
  pinMode(LED_UNO, OUTPUT);
  pinMode(SONAR_TRIG, OUTPUT);
  pinMode(SONAR_ECHO, INPUT);
  
  sonar = new Sonar(SONAR_ECHO, SONAR_TRIG);
  led = new Led(LED_UNO);

  MsgService.init();
  led -> switchOff();
}

void loop() {
  ts0 = millis();
  //checkState();
  state = "PRE-ALLARM";
  executeState();

  /* sonar -> getDistance();*/
  
  /*float value = 2.2;
  MsgService.sendMsg(value);*/
}


/* 
 *  Se il livello dell'acqua è <L1 allora lo stato continua ad essere normale.
 *  Se il livello invece è >L1 allora lo stato cambierà alla conclusione del tick. 
 *  Viene dunque settata la task a completed.
 *  Se il livello è >L1 && >=L2 lo stato passa da uno stato di normalità ad uno stato di allarme.
 *  
 *  ----------------------------------------------------
 *  
 *  E' NECESSARIO CAMBIARE IL PROTOCOLLO DI RICEZIONE IN ECLIPSE
 *  Voglio inviare dei dati in stringhe che riportano lo stato corrente in base allo stato del sonar
 *  
 *  In Java ora Value (nella richiesta HTTP) ha valore Float --> String!!  
 *  
 *  Forse no... Riusciamo a fare due diversi pattern per i messaggi in entrata?
 *    1. value in float
 *    2. value in string
 *    
 *  Intanto, sono stati presi in esame dei valori sostituitivi alle stringhe per definire lo stato corrente.
 *  Valore min e max per float 1.17549e-038 ** 3.40282e+038
 *  100000.1 --> Stato NORMAL
 *  100000.2 --> Stato PRE-ALLARM
 *  100000.3 --> Stato ALLARM
 *  
 *  state = "PRE-ALLARM";
 *  state = "ALLARM";
 *  
 *  -----------------------------------------------------
*/

void checkState(){
  //sonar restituisce 0, toCheck!
  currentDistance = sonar -> getDistance();
  if(currentDistance > min_level){
    stateTemp = 100000.2;
    state = "PRE-ALLARM";
    if(currentDistance > max_level){
      stateTemp = 100000.3;
      state = "ALLARM";
    }
  }else if(currentDistance < min_level){
    stateTemp = 100000.1;
    state = "NORMAL";
  }

  //float es = str2float(state);
  //float es = strtod(state,NULL);
  
  MsgService.sendMsg(stateTemp);
  //executeState();
}


/* WDT riavvia l'applicazione anche in questo caso, uso dunque delay
   per ottenere una soluzione simile a quella iniziale*/
   
/*
 * while(currentTs - ts0 < min_freq){
 *    currentTs = millis();
 * }
*/
void executeState(){

  currentTs = millis();
  
  if(state == "PRE-ALLARM"){
      led -> switchOn();
      currentTs = millis();
      delay(min_freq - (currentTs-ts0));
      /*sonar restituisce 0, toCheck!*/
      //MsgService.sendMsg(sonar -> getDistance());
      /*Serial.println("Ts0: "+ String(ts0) + " - CurrentTs: " + String(currentTs));
      Serial.println("Difference: "+ String(currentTs - ts0) + " - min_freq: "+ String(min_freq));*/
      ts0 = millis();
  }else if(state == "ALLARM"){
      led -> blinking();
      currentTs = millis();
      delay(max_freq - (currentTs-ts0));
      /*sonar restituisce 0, toCheck!*/
      //MsgService.sendMsg(sonar -> getDistance());
      ts0 = millis();
  }else if(state == "NORMAL")
      led -> switchOff();
}


      
  
