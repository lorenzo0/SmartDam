#include "ModifyState.h"
#include "Arduino.h"

extern int newOpeningDAM;

/*
 * Nel caso di stato di modifica, ARDUINO prevede un comportamento 
 * composto dalla variazione del LED e del SERVO MOTORE.
 * Il led infatti, assume uno stato di accensione fissa.
 * Il servoMotore invece, va ad assumere uno spostamento dettato
 * dalla variabile newOpeningDAM (esterna, definita nello scheduler).
 * 
 * Per effettuare la proporzione tra apertura diga e la sua percentuale
 * richiesta, è stata istanziata un oggetto di tipo SharingMethod.
 * Questo, viene pensato, perchè i due stati; ALLARM e MODIFY, lo richiedono
 * entrambi e sarebbe un errore e ridondanza concettuale.
 * 
 * int newOpeningToServo = sharingMethods -> calculateOpeningServo(newOpeningDAM);
 * 
 * Questa riga si occupa quindi di passare, da una percentuale (presente come intero
 * nella variabile newOpeningDAM) in una variabile di tipo double attraverso una
 * proporzione pensata nel file SharingMethod.
*/

ModifyState::ModifyState(int pinLed1, int pinServoMotor){
  this -> pinLed1 = pinLed1;
}

void ModifyState::init(){
  Task::init();
  
  led1 = new Led(pinLed1);
}

void ModifyState::tick(){

  led1 -> switchOn();

  if(newOpeningDAM != -1 && newOpeningDAM != oldValue){
    oldValue = newOpeningDAM;
    int newOpeningToServo = sharingMethods -> calculateOpeningServo(newOpeningDAM);
    
    if(newOpeningToServo > oldOpeningServo){
      pMotor->on();  
      for (int i = oldOpeningServo; i < newOpeningToServo; i++) {
        pMotor->setPosition(i);         
        delay(15);
      }
     pMotor->off();
    }else{
      pMotor->on();  
      for (int i = newOpeningToServo; i > oldOpeningServo; i--) {
        pMotor->setPosition(i);         
        delay(15);
      }
     pMotor->off();
    }
  }
}
