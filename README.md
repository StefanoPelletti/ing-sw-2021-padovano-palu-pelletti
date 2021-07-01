# ing-sw-2021-padovano-palu-pelletti

# IT

## Una piccola introduzione

Questo repository contiene codice sorgente e documentazione di un videogioco sviluppato in Java. Il gioco prende il nome e le caratteristiche dall'omonimo gioco da tavolo [Maestri del Rinascimento](http://www.craniocreations.it/prodotto/masters-of-renaissance/), che è implementato via software secondo un'architettura distribuita che utilizza il paradigma MVC. Il gioco può essere avviato tramite il JAR. Se si vuole importare il progetto in un IDE, le classi main da eseguire saranno il 'Server', il 'PhaseClient' per la CLI e il 'MainMenu' per la GUI.

### Piccole note implementative

Per creare una Lobby bisogna avviare un Server. Un client può chiedere quindi di creare una lobby di grandezza arbitraria. Gli verrà fornito il numero della stanza, che potrà fornire ai suoi amici. Se non tutti i giocatori si collegano entro un tempo prestabilito (di default 30 secondi), la Lobby viene cancellata dal Server. 

Se tutti i giocatori di una Lobby si scollegano, la Lobby viene cancellata dal Server.

La GUI è stata implementata usando SWING.

Sono state implementate le funzionalità aggiuntive di `Partita Locale`, `Riconnessione a una partita in corso`, `Partite Multiple`.

## Copertura del codice

Uno screenshot della copertura del codice da parte dei casi di test può essere trovato cliccando [qui](https://github.com/StefanoPelletti/ing-sw-2021-padovano-palu-pelletti/blob/main/deliverables/ScreenshotTestCases.jpg).

Ultima versione del 21-jun-2021.

## Test di prova

Il programma è stato testato su una macchina virtuale Windows pulita con installata una JDK 11 LTS di Oracle.

Non sono sorti problemi durante la prova, tuttavia non possiamo essere certi della piena compatibilità.

## Requisiti

Il programma è stato scritto usando una JDK 15 Oracle. 

È pertanto consigliato l'utilizzo di una JDK 15 o superiore. È inoltre richiesta una risoluzione dello schermo minima di 1920x1080p per l'utilizzo della GUI.

Per giocare in linea di comando, è necessario utilizzare un terminale che supporti i codici colori. Mac li supporta nativamente, Windows tramite il Windows Terminal li supporta.

Assicuratevi che il comando `java` sia funzionante nel terminale di sistema.

## Esecuzione tramite JAR

### download e posizionamento

Per eseguire il programma usando il JAR bisogna:

- Scaricare e posizionare il [jar](https://github.com/StefanoPelletti/ing-sw-2021-padovano-palu-pelletti/blob/main/deliverables/GC31-1.0-SNAPSHOT.jar) in una cartella sul vostro computer
- Per permettere all'interfaccia grafica di accedere alle immagini, scaricare anche l'intera [cartella resources](https://github.com/StefanoPelletti/ing-sw-2021-padovano-palu-pelletti/tree/main/resources) e metterla accanto al JAR scaricato prima.
   - Alla fine, nella vostra cartella destinazione dovreste avere sia il `JAR` sia la cartella `resources`.
    
### esecuzione

Il programma viene avviato tramite linea di comando.

Dirigetevi tramite il terminale nella cartella dove avete scaricato il programma.

Da qui basta scrivere: 

```
java -jar GC31-1.0-SNAPSHOT.jar 
```

Per avviare il server (di default sulla porta 43210)

```
java -jar GC31-1.0-SNAPSHOT.jar server
```

Per avviare il server su una porta customizzata (ad esempio la 343)

```
java -jar GC31-1.0-SNAPSHOT.jar server 343
```

Per avviare il client in CLI

```
java -jar GC31-1.0-SNAPSHOT.jar cli 
```

Per avviare il client in GUI

```
java -jar GC31-1.0-SNAPSHOT.jar gui
```

# EN

## A short introduction

This repository contains the source code and documentation of a videogame developed in Java. The game is named after the physical version of the board game [Masters of Renaissance](https://craniointernational.com/products/masters-of-renaissance/), which is implemented via software according to the MVP paradigm and using a networking, distribuited architecture. The game can be launched using the JAR. If you want to import the project in an IDE, the main classes to be executed will be the 'Server', the 'PhaseClient' for the CLI and the 'MainMenu' for the GUI.

### A few implementation notes

To create a Lobby a Server must be started. A client can then ask to create a Lobby of arbitrary dimension. A Lobby Number will be given to the Client, which can then share it with his friends. If not all players connect to that Lobby within a certain amount of time (by default 30 seconds), the Lobby will be deleted from the Server.

If all the players of a Lobby have disconnected, the Lobby will be deleted from the Server.

The GUI was implemented using SWING.

Additiona functionalities of `Local Game`, `Reconnect to an existing Game`, `Multiple Games` were implemented.

## Code coverage

A screenshot regarding code coverage by the test cases can be found [here](https://github.com/StefanoPelletti/ing-sw-2021-padovano-palu-pelletti/blob/main/deliverables/ScreenshotTestCases.jpg).

Last version of 21-jun-2021.

## Bench test 

The program was also tested on a fresh Windows VM, on which a JDK 11 by Oracle was installed.

The test did not present any problem, but we cannot guarantee full compatibility for the reasons above.

## Requirements

The program was coded using an Oracle JDK 15.

We encourage you to utilize a JDK 15 or later. ALso, a 1920x1080p resolution is required to use the GUI.

To play in command line, it is necessary to use a terminal that supports color codes. Mac supports them natively, Windows can handle them using Windows Terminal.

Make sure the command `java` is working in the system terminal.

## Run using JAR

### download and positioning

To run the program using the JAR you must:

- Download and place the [jar](https://github.com/StefanoPelletti/ing-sw-2021-padovano-palu-pelletti/blob/main/deliverables/GC31-1.0-SNAPSHOT.jar) in a folder on your computer.
- To allow the GUI to load properly the images, you should also download the [resource folder](https://github.com/StefanoPelletti/ing-sw-2021-padovano-palu-pelletti/tree/main/resources) and place it beside the JAR.
  - In the end, in that folder there should be both the `JAR` and the `resource` folder.

### run

The program is launched by command line.

Use the terminal to navigate the directories and locate the folder where you have downloaded the program.

From here, it is enough to type:

```
java -jar GC31-1.0-SNAPSHOT.jar 
```

To launch the server (by default the port is 43210)

```
java -jar GC31-1.0-SNAPSHOT.jar server
```

To launch the server using a custom port (for example 343)

```
java -jar GC31-1.0-SNAPSHOT.jar server 343
```

To launch the client in CLI

```
java -jar GC31-1.0-SNAPSHOT.jar cli 
```

To launch the client in GUI

```
java -jar GC31-1.0-SNAPSHOT.jar gui
```
