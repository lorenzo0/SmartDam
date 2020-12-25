
#define SONAR_TRIG 4  //D4
#define SONAR_ECHO 5  //D3
#define LED_UNO 16  //D0

#include "Sonar.h"
#include "Scheduler.h"
#include "PreAllarmState.h"
#include "NormalState.h"
#include "MsgServiceHTTP.h"

Scheduler scheduler;
//MsgServiceHTTP connectionHTTP;

void setup() {
  Serial.begin(9600);
  pinMode(LED_UNO, OUTPUT);
  pinMode(SONAR_TRIG, OUTPUT);
  pinMode(SONAR_ECHO, INPUT);

  scheduler.init();
  MsgService.init();
  //connectionHTTP.init();

  Task* normalState = new NormalState(LED_UNO, SONAR_ECHO, SONAR_TRIG);
  PreAllarmState* preAllarmState = new PreAllarmState(LED_UNO, SONAR_ECHO, SONAR_TRIG);
  
  normalState -> init();
  preAllarmState -> init();

  scheduler.addTask(normalState);
  scheduler.addTask(preAllarmState);

  scheduler.setIndexCurrentTaskActive(1);
  preAllarmState -> sendDataHTTP();
}

void loop() {
  //connectionHTTP -> checkConnection();
  //scheduler.schedule();
  
  //MsgService.sendMsg("ciao");
  //float value = 2.2;
  //MsgService.sendMsg(value);
}
