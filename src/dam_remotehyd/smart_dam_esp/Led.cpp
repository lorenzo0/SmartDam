#include "Led.h"
#include "Arduino.h"

Led::Led(int pin){
  this->pin = pin;
  pinMode(pin,OUTPUT);
  fadeValue = 0;
  time = 0;
}

void Led::switchOn(){
  digitalWrite(pin,HIGH);
}

void Led::switchOff(){
  digitalWrite(pin,LOW);
};

/* Gestione Blinking Led */
void Led::blinking(){
  for(int i=0; i<17; i++){
    fadeValue += 15;
    analogWrite(pin, fadeValue);
    delay(50);
  }

  for(int i=17; i>0; i--){
    fadeValue -= 15;
    analogWrite(pin, fadeValue);
    delay(50);
  }
}
