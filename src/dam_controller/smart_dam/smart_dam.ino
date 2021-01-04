
/*
 * Il microprocessore ARDUINO UNO è un componente previsto nel sistema
 * smart_dam. Questo, ha il ruolo di gestire l'apertura e la chiusura della diga,
 * simulata tramite servo motore. Inoltre, si occupa della comunicazione tramite 
 * bluetooth con il sottosistema Dam_APP grazie al modulo HC-06. 
 * Quest'ultimo,  permette di trasformare una porta UART\USART, più comunemente conosciuta come seriale, 
 * in una porta Bluetooth, generalmente con profilo SPP(Serial Port Profile), diventando cosi una seriale tramite Bluetooth. 
 * 
 *  BT module connection:  
 *  - RX is pin 2 
 *  - TX is pin 3 
 *
 * E' prevista la comunicazioen incrociata tra HC-06 ed ARDUINO.
 * Quindi, la tdx di HC-06 (pin 3) comunicherà con rdx di ARDUINO UNO, stesso principio
 * viene applicato ad tdx di ARDUINO UNO, che comunicherà con  la rx di HC-06 (pin 2).
 * 
 * Viene inoltre utilizzata la libreria softwareSerial che permette di instaurare una comunicazione 
 * di tipo seriale ASINCRONO con qualsiasi coppia di pin digitali (nel nostro caso 2,3).
 * 
 * La gestione del modulo bluetooth viene associata allo scheduler.
 */ 

#define LED_UNO 10
#define SERVO_MOTOR 9


#define CALIBRATION_TIME_SEC 10
#include "Scheduler.h"
#include "servo_motor_impl.h"
#include "Globals.h"
#include "NormalPreState.h"
#include "AllarmState.h"
#include "ModifyState.h"
#include <SoftwareSerial.h>

Scheduler scheduler;

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
  Led* led_uno = new Led(LED_UNO);
  
  ServoMotor* pMotor = new ServoMotorImpl(9);

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
  delay(200);

  
  scheduler.init(LED_UNO);

  Task* normalPreState = new NormalPreState(LED_UNO);
  Task* allarmState = new AllarmState(LED_UNO, SERVO_MOTOR);
  Task* modifyState = new ModifyState(LED_UNO, SERVO_MOTOR);

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
