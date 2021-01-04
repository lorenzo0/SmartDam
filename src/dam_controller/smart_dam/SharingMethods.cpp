#include "SharingMethods.h"
#include "Arduino.h"

/* 
 *  SharingMethods è una classe prevista nel controller
 *  necessaria per effettuare una conversione, da valore 
 *  percentile ad un valore di tipo INT, proporzionato
 *  all'apertura della diga e quindi del servomotore (angolo piatto - 180°)
 *  tramite la seguente;
 *  
 *    100:%=180:x
*/  
int SharingMethods::calculateOpeningServo(int newOpeningDAM){
  int currentOpening = (newOpeningDAM*180)/100;
  return currentOpening;
}
