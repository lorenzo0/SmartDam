#include "Interrupts.h"
#include "Arduino.h"
#include <EnableInterrupt.h>

boolean InterruptStart;
boolean InterruptStop;
boolean InterruptPir;
long timeInterrupts;

/* Gestione BOUNCING */
void handleInterruptsStart(){
  if(millis()-timeInterrupts>250){
    InterruptStart = true;
    timeInterrupts = millis();
  }
}

/* Gestione BOUNCING */
void handleInterruptsStop(){
  if(millis()-timeInterrupts>250){
    InterruptStop = true;
    timeInterrupts = millis();
  }
}

void handleInterruptsPir(){
  InterruptPir = true;
}

void Interrupts::init(){
  enableInterrupt(8, handleInterruptsStart, RISING);
  enableInterrupt(12, handleInterruptsStop, RISING);
  enableInterrupt(7, handleInterruptsPir, RISING);

  timeInterrupts = 0;
  InterruptStart = false;
  InterruptStop = false;
  InterruptPir = false;
}
