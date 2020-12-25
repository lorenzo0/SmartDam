#include "Arduino.h"
#include "Sonar.h"

Sonar::Sonar(int pinSonarEcho, int pinSonarTrig){
  this->pinSonarEcho = pinSonarEcho;  
  this->pinSonarTrig = pinSonarTrig;
}

/* 
 * Avendo il sensore di temperatura, possiamo calcolare la velocità del suono.
 * Sfruttiamo quest'ultima nella formula con la quale viene definita la distanza tra il
 * sonar e l'oggetto.
*/

float Sonar::getDistance(){
  
  digitalWrite(pinSonarTrig,LOW);
  delayMicroseconds(2);
  digitalWrite(pinSonarTrig,HIGH);
  delayMicroseconds(10);
  digitalWrite(pinSonarTrig,LOW);
  
  long tUS = pulseIn(pinSonarEcho, HIGH);
  
  double t = tUS*0.034/2;
  return t;
}
