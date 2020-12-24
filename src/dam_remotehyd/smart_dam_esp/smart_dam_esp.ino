// defines pins numbers

#define SONAR_TRIG 2  //D4
#define SONAR_ECHO 0  //D3
#define LED 16

#include "Sonar.h";
Sonar* sonar;

void setup() {
Serial.begin(9600);
sonar = new Sonar(SONAR_ECHO, SONAR_TRIG);
}

void loop() {
  Serial.println(sonar -> getDistance());
}
