#include "Scheduler.h"

int newOpeningDAM = -1;
MsgServiceBT msgServiceBLUETOOTH(2,3);

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

/* 
 *  Nella politica di scheduling vengono gestite le interruzioni.
 *  Queste, sono sempre attive ma non sempre prese in considerazione. 
 *  Facendo un esempio forviante, non si può interrompere l'esecuzione del task 
 *  (interazione con bottone BUTTON_STOP) se nel task di IDLE.
 *  
 *  L'interruzione del bottone STOP viene gestita solo nel caso in cui in esecuzione 
 *  è la task numero 3 - running.
 *  L'interruzione del bottone START viene gestita solo nel caso in cui in esecuzione 
 *  è la task numero 0 - idle.
 *  L'interruzione del sensore PIR viene gestita solo nel caso in cui in esecuzione 
 *  è la task numero 2 - sleep.
 *  
 *  Infine, viene presa in considerazione la casistica in cui il task completi la sua
 *  totale esecuzione (data dalle costanti definite in design time), quindi entri in uno stato di completed.
 *  
 *  La variabile 'i' è stata introdotta per rendere il codice più leggibile, sostituendo 'indexCurrentTaskActive'
*/
void Scheduler::schedule(){
  checkMessageReceivedSERIAL();  
  bluethoot_receiving();   
  taskList[indexCurrentTaskActive] -> tick();
}

/*
 * Dato che non è possibile fare uno switch analizzando il valore di una stringa,
 * viene strutturato un pattern basato su interi.
 * 
 * Normale o Pre-Allarme (Il loro comportamento è identico, quindi li gestisco
 * in una sola classe) 0
 * Allarme 1
 * Modalità di modifica 2 (Successiva SOLO allo stato allarme
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

void Scheduler::checkMessageReceivedSERIAL(){
  if(Serial.available() > 0){
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
}

void Scheduler::setIndexCurrentTaskActive(int index){
  this->indexCurrentTaskActive = index;
  Serial.println("Prossima Task in esecuzione: "+ (String) index);
}

void Scheduler::setNewOpeningDAM(int value){
  newOpeningDAM = value;
  //Serial.println((String) value + "Setted!");
  //sarebbe nel log questo
}
