# ing-sw-2021-padovano-palu-pelletti

# IT

## Copertura del codice

Uno screenshot della copertura del codice da parte dei casi di test può essere trovato cliccando [qui](https://github.com/StefanoPelletti/ing-sw-2021-padovano-palu-pelletti/blob/main/deliverables/ScreenshotTestCases.jpg).

Ultima versione del 21-jun-2021.

## Requisiti

Il programma è stato scritto usando una JDK 15 Oracle. 

È pertanto consigliato l'utilizzo di una JDK 15 o superiore.

Potrebbe funzionare anche usando anche una JDK 11 LTS, tuttavia non é garantita la piena compatibilità.

## Esecuzione tramite JAR

### download e posizionamento

Per eseguire il programma usando il JAR bisogna:

- Scaricare e posizionare il [jar](https://www.google.it/) in una cartella sul vostro computer
- Per permettere all'interfaccia grafica di accedere alle proprie risorse video, scaricare anche la [cartella resources](https://github.com/StefanoPelletti/ing-sw-2021-padovano-palu-pelletti/tree/main/resources) e metterla accanto al JAR scaricato prima.
   - Alla fine, nella vostra cartella destinazione dovreste avere sia il JAR sia la cartella resources.
    
### esecuzione

Il programma viene avviato tramite linea di comando.

Dirigetevi tramite il terminale nella cartella dove avete scaricato il programma (per esempio: C:\ISW\)

Da qui basta scrivere: 

```
java -jar GC31-1.0-SNAPSHOT.jar 
```

Per avviare il server (di default sulla porta 43210)

```
java -jar GC31-1.0-SNAPSHOT.jar server
```

Per avviare il client in CLI

```
java -jar GC31-1.0-SNAPSHOT.jar cli 
```

Per avviare il client in GUI

```
java -jar GC31-1.0-SNAPSHOT.jar gui
```