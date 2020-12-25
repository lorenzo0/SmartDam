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
  time = millis();
  fadeValue = 128+127*cos(2*PI/2000*time);
  analogWrite(pin, fadeValue);
}
