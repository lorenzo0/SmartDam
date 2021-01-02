#define SONAR_TRIG 2  //D2
#define SONAR_ECHO 4  //D1
#define LED_UNO 5  //D0

#include "Sonar.h"
#include "MsgServiceHTTP.h"
#include "Globals.h"
#include "Led.h"

/* supponendo di eseguire il test  in un ambiente a 20 °C */
const float vs = 331.45 + 0.62*20;

Sonar* sonar;
Led* led;
long currentTs, ts0;
String state;
float currentDistance;

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
  checkState();
}

void loop() {
  ts0 = millis();
  executeState();
}

/* 
 *  Se il livello dell'acqua è <L1 allora lo stato continua ad essere normale.
 *  Se il livello invece è >L1 allora lo stato cambierà alla conclusione del tick. 
 *  Viene dunque settata la task a completed.
 *  Se il livello è >L1 && >=L2 lo stato passa da uno stato di normalità ad uno stato di allarme.
 *  
*/

void checkState(){
  currentDistance = sonar->getDistance();
  if(currentDistance > min_level){
    state = "PRE-ALLARM";
    if(currentDistance > max_level)
      state = "ALLARM";
  }else if(currentDistance < min_level)
    state = "NORMAL";
}


/* WDT riavvia l'applicazione anche in questo caso, uso dunque delay
   per ottenere una soluzione simile a quella iniziale*/
 
void executeState(){

  currentTs = millis();
  
  if(state == "PRE-ALLARM"){
    MsgService.sendMsg(currentDistance, state);
      while(min_freq - (currentTs-ts0)>=0){
        led -> blinking();
        currentTs = millis();
      }
  }else if(state == "ALLARM"){
      led -> switchOn();
      currentTs = millis();
      MsgService.sendMsg(currentDistance, state);
      delay(max_freq - (currentTs-ts0));
      
  }else if(state == "NORMAL")
      led -> switchOff();
  
  checkState();
  ts0 = millis();
}



      
  
