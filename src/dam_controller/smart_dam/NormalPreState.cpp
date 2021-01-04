#include "NormalPreState.h"
#include "Arduino.h"

/*
 * Nello stato normale del sistema, non è prevista nessuna
 * azione, ad eccezione dello spengimento del led.
*/

NormalPreState::NormalPreState(int pinLed1){
  this -> pinLed1 = pinLed1;
}

void NormalPreState::init(){
  Task::init();
  led1 = new Led(pinLed1);
}

void NormalPreState::tick(){
    led1 -> switchOff();
}
