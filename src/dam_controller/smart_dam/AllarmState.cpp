#include "AllarmState.h"
#include "Arduino.h"

extern int newOpeningDAM;

/*
 * Nel caso di stato di allarme, ARDUINO prevede un comportamento 
 * composto dalla variazione del LED e del SERVO MOTORE.
 * Il led infatti, assume uno stato di BLINKING (fading in + fading out).
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
 * 
 * Non è possibile però prevedere anche il comportamento presente nella procedura
 * tick nell'oggetto SharingMethod perchè prevede l'utilizzo del servo motore.
*/

AllarmState::AllarmState(int pinLed1, int pinServo){
  this -> pinLed1 = pinLed1;
  this -> pinServo = pinServo;
}

void AllarmState::init(){
  Task::init();
  
  led1 = new Led(pinLed1);
  pMotor = new ServoMotorImpl(pinServo);

  oldValue = -10;
}

void AllarmState::tick(){

  led1->blinking();
  
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
