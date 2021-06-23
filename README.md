# ing-sw-2021-padovano-palu-pelletti

# IT

## Copertura del codice

Uno screenshot della copertura del codice da parte dei casi di test può essere trovato cliccando [qui](https://github.com/StefanoPelletti/ing-sw-2021-padovano-palu-pelletti/blob/main/deliverables/ScreenshotTestCases.jpg).

Ultima versione del 21-jun-2021.

## Requisiti

Il programma è stato scritto usando una JDK 15 Oracle. 

È pertanto consigliato l'utilizzo di una JDK 15 o superiore.

Il programma è stato testato su una macchina virtuale pulita con installata una JDK 11 LTS di Oracle.

Non sono sorti problemi durante la prova, tuttavia non possiamo essere certi della piena compatibilità.

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

## Code coverage

A screenshot regarding code coverage by the test cases can be found [here](https://github.com/StefanoPelletti/ing-sw-2021-padovano-palu-pelletti/blob/main/deliverables/ScreenshotTestCases.jpg).

Last version of 21-jun-2021.

## Requirements

The program was coded using an Oracle JDK 15.

We encourage you to utilize a JDK 15 or later.

The program was also tested on a fresh VM, on which a JDK 11 by Oracle was installed.

The test did not present any problem, but we cannot guarantee full compatibility for the reasons above.

Make sure the command `java` is working in the system terminal.

## Run using JAR

### download and positioning

To run the program using the JAR you must:

- Download and place the [jar](https://github.com/StefanoPelletti/ing-sw-2021-padovano-palu-pelletti/blob/main/deliverables/GC31-1.0-SNAPSHOT.jar) in a folder on your computer.
- To allow the GUI to load properly the images, you should also download the [cartella resources](https://github.com/StefanoPelletti/ing-sw-2021-padovano-palu-pelletti/tree/main/resources) and place it beside the JAR.
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


