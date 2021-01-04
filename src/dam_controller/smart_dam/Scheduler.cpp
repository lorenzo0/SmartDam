#include "Scheduler.h"

int newOpeningDAM = -1;
MsgServiceBT msgServiceBLUETOOTH(2,3);

/* 
 *  Come anticipato in precedenza, la classe scheduler di occupa
 *  della gestione del servizio bluetooth, istanziato tramite una variabile 
 *  esterna. Allo stesso momento, lo scheduler deve controllare la presenza di messaggi
 *  presenti via seriale da sottosistema dam_service.
 *  
 *  Viene utilizzata una variabile 'indexCurrentTaskActive' per indicare
 *  l'indice della task correntemente in esecuzione.
 *  
 *    0 --> normal/pre-allarm state
 *    1 --> allarm state
 *    2 --> modify state
 *      
*/

void Scheduler::init(Led* led_pin){
  nTasks = 0;
  indexCurrentTaskActive = 0;
  msgServiceBLUETOOTH.init(); 
  led = new Led(led_pin);
}

bool Scheduler::addTask(Task* task){
  if (nTasks < MAX_TASKS-1){
    taskList[nTasks] = task;
    nTasks++;
    return true;
  } else {
    return false; 
  }
}


void Scheduler::schedule(){
  checkMessageReceivedSERIAL();  
  bluethoot_receiving();   
  taskList[indexCurrentTaskActive] -> tick();
}

/*
 * La comunicazione via bluetooth va a collaborare, in modo 
 * in modo incociato a quella seriale, per definire al meglio gli stati.
 * Viene stabilito anche qui un pattern di comunicazione tra i due 
 * sottosistemi.
*/
void Scheduler::bluethoot_receiving(){
   if (msgServiceBLUETOOTH.isMsgAvailable()) {
    String messageReceived = msgServiceBLUETOOTH.receiveMsg();

    if(messageReceived == "ALLARM")
        setIndexCurrentTaskActive(1);
    else if(messageReceived == "MOD-OP")
        setIndexCurrentTaskActive(2);
    else  
      setNewOpeningDAM(messageReceived.toDouble());
  }
}

/* 
 *  Viene utilizzato il metodo readStringUntil della classe
 *  Serial per effettuare uno split del messaggio ricevuto.
 *  Questo, viene previsto, a seguito di una costruzione di un pattern
 *  di messaggio tramite seriale tra Service (Servizio) - Controller (Arduino).
 *  
 *  Il messaggio ricevuto è così strutturato:
 *    STATO|Valore percentile apertura diga.
*/

void Scheduler::checkMessageReceivedSERIAL(){
  if(Serial.available() > 0){
    led->switchOn();
    delay(1000);
    String msgReceiveSERIALState, msgReceiveSERIALOpening;
    while (Serial.available() > 0) {
      msgReceiveSERIALState = Serial.readStringUntil('|'); 
      Serial.read(); 
      msgReceiveSERIALOpening = Serial.readStringUntil('\n');
      Serial.read(); 
    }

    if(msgReceiveSERIALState == "PRE-ALLARM")
        setIndexCurrentTaskActive(0);
    else if(msgReceiveSERIALState == "ALLARM"){
        setIndexCurrentTaskActive(1);
        setNewOpeningDAM(msgReceiveSERIALOpening.toInt());
    }
  }
  led->switchOff();
}

void Scheduler::setIndexCurrentTaskActive(int index){
  this->indexCurrentTaskActive = index;
}

void Scheduler::setNewOpeningDAM(int value){
  newOpeningDAM = value;
}
