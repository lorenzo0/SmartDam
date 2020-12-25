#define SONAR_TRIG 4  //D4
#define SONAR_ECHO 5  //D3
#define LED_UNO 16  //D0

#include "Sonar.h"
#include "Scheduler.h"
#include "PreAllarmState.h"
#include "NormalState.h"
#include "MsgServiceHTTP.h"

Scheduler scheduler;
MsgServiceHTTP* connectionHTTP;

char* nameWIFI = "xxxx";
char* pwdWIFI = "xxxx";
char* addressWIFI = "xxxxx";

void setup() {
  Serial.begin(9600);
  pinMode(LED_UNO, OUTPUT);
  pinMode(SONAR_TRIG, OUTPUT);
  pinMode(SONAR_ECHO, INPUT);

  connectionHTTP = new MsgServiceHTTP(nameWIFI, pwdWIFI, addressWIFI);
  
  scheduler.init();
  connectionHTTP -> init();

  Task* normalState = new NormalState(LED_UNO, SONAR_ECHO, SONAR_TRIG);
  Task* preAllarmState = new PreAllarmState(LED_UNO, SONAR_ECHO, SONAR_TRIG, connectionHTTP);
  
  normalState -> init();
  preAllarmState -> init();

  scheduler.addTask(normalState);
  scheduler.addTask(preAllarmState);

  scheduler.setIndexCurrentTaskActive(1);
}

void loop() {
  //connectionHTTP -> checkConnection();
  //scheduler.schedule();
}
