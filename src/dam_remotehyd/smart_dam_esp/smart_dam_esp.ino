#define SONAR_TRIG 2  
#define SONAR_ECHO 4  
#define LED_UNO 16  

#include "Sonar.h"
#include "MsgServiceHTTP.h"
#include "Globals.h"
#include "Led.h"

/* 
 *  Il microprocessore ESP8266 è un componenete essenziale per questo sistema.
 *  Il suo ruolo è quello di richiedere al sensore sonar la distanza dell'oggetto (se esiste)
 *  di fronte a lui. Vista l'assenza del sensore di temperatura dal sistema, viene
 *  supposto di eseguire il test in un ambiente esterno con temperatura pari a 20 °C.
 *  
 *  Una volta definito il sonar ed il led tramite i pin rispettivi;
 *    GPIO 2 <--> D2
 *    GPIO 4 <--> D1
 *    GPIO 16 <--> D0
 *    
 *  viene richiesta la distanza (calcolata in cm/s visto l'ambiente dove si svolge
 *  l'esperimento con un campo ridotto) e gestito, in base ai valori globari (fittizi) presenti
 *  in specifiche, lo stato corrente del sistema.
 *  
 *  Una volta trovata la distanza dell'oggetto ed ottenuto lo stato corrente del sistema, viene
 *  inviato un messaggio, tramite HTTP al server. Per ottimizzare l'efficienza del sistema, non viene
 *  istanziato un puntatore ad un oggetto di tipo MsgServiceHTTP ma viene creata una variabile esterna
 *  pronta ad invocare i metodi necessari, MsgService.
 *  
 *  *** PER MODIFICHE ***
 *  Le costanti è possibile variarle all'interno del file Globals.h (anche la temperatura esterna al sistema,
 *  in caso di aggiunta del sensore di temperatura (TMP36 o altri).
 *  E' possibile modificare il link per la connessione HTTP nella classe MsgServiceHTTP.h
 *  
*/

Sonar* sonar;
Led* led;
long currentTs, ts0;
String state;
float currentDistance;

void setup() {
  Serial.begin(9600);
  pinMode(LED_UNO, OUTPUT);
  pinMode(SONAR_TRIG, OUTPUT);
  pinMode(SONAR_ECHO, INPUT);
  
  sonar = new Sonar(SONAR_ECHO, SONAR_TRIG);
  led = new Led(LED_UNO);

  MsgService.init();
  led -> switchOff();
  
  checkState();
}

void loop() {
  ts0 = millis();
  currentDistance = sonar->getDistance();
  executeState();
}

/* 
 *  Se il livello dell'acqua è <L1 allora lo stato continua ad essere NORMAL.
 *  Se il livello invece è >L1 allora lo stato cambierà in PRE-ALLARM.
 *  Se il livello è >L1 && >=L2 lo stato passa ad uno stato di ALLARM.
 *  E' possibile variare gli stati in modo non vincolante, non è necessario quindi
 *  passare da uno stato di PRE-ALLARM per trovarsi in uno stato di ALLARM.
*/

void checkState(){
  currentDistance = sonar->getDistance();
  if(currentDistance > min_level){
    state = "PRE-ALLARM";
    if(currentDistance > max_level)
      state = "ALLARM";
  }else if(currentDistance < min_level)
    state = "NORMAL";
}



/*
 * ESP necessita il controllo del suo flusso di controllo nel file.ino,
 * non è possibile quindi l'approccio a stati finiti richiamando uno scheduler.
 * Affermo questo perchè il Watch-Dog-Timer, dopo (circa) 3 secondi,
 * verifica che l'utilizzo di CPU è riservata ad uno scheduler che non prevede
 * un'approccio di tipo yielding per tutti i task, compresi quelli con bassa priorità.
 * Ad esempio, uno scheduler che attende la conclusione di un tick di un qualsiasi task,
 * viene visto dal WDT come un effettivo blocco del sistema e questo comporta un riavvio immediato.
 * ("Task WatchDog got triggered")
*/
 
void executeState(){

  currentTs = millis();
  
  if(state == "PRE-ALLARM"){
    MsgService.sendMsg(currentDistance, state);
      while(min_freq - (currentTs-ts0)>=0){
        led -> blinking();
        currentTs = millis();
      }
  }else if(state == "ALLARM"){
      led -> switchOn();
      currentTs = millis();
      MsgService.sendMsg(currentDistance, state);
      MsgService.sendLogMsg("Invio dato " + String(currentDistance) + " nello stato di " + state);
      delay(max_freq - (currentTs-ts0));
      
  }else if(state == "NORMAL")
      led -> switchOff();

  checkState();  
  ts0 = millis();
}



      
  
