#define SONAR_TRIG 4  //D4
#define SONAR_ECHO 5  //D3
#define LED_UNO 16  //D0

#include "Sonar.h"
#include "MsgServiceHTTP.h"
#include "Globals.h"
#include "Led.h"

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

  /* Fase di testing */
  float value = 2.233333333;
  state = "PRE-ALLARM";
  MsgService.sendMsg(value, state);
}

void loop() {
  //ts0 = millis();
  //checkState();
  //executeState();  
}


/* 
 *  Se il livello dell'acqua è <L1 allora lo stato continua ad essere normale.
 *  Se il livello invece è >L1 allora lo stato cambierà alla conclusione del tick. 
 *  Viene dunque settata la task a completed.
 *  Se il livello è >L1 && >=L2 lo stato passa da uno stato di normalità ad uno stato di allarme.
 *  
*/

void checkState(){
  //sonar restituisce 0, toCheck!
  currentDistance = sonar -> getDistance();
  if(currentDistance > min_level){
    state = "PRE-ALLARM";
    if(currentDistance > max_level)
      state = "ALLARM";
  }else if(currentDistance < min_level)
    state = "NORMAL";
  
  
  //MsgService.sendMsg(stateTemp);
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

void calculateOpeningDam(){
  
}


      
  
