package com.exercise;

public class Main {
    private static volatile boolean running = false; // Variabile di controllo
    private static LogProducer producer;
    private static Thread logThread; // Dichiarazione del thread di logging

    public static void main(String[] args) {
        String topic = "logs";
        String bootstrapServers = "kafka:9092"; // Indirizzo del broker Kafka
        producer = new LogProducer(bootstrapServers);

        // Avvia il logging in un thread separato
        logThread = new Thread(() -> {
            try {
                int logCounter = 1;
                while (true) {
                    if (running) {
                        String message = "Log " + logCounter + " scritto con successo!";
                        producer.sendLog(topic, message);
                        logCounter++;
                    }

                    // Aspetta 1 secondo tra i log
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Ripristina lo stato di interruzione
            }
        });

        // Inizializza il thread di logging e inizia il ciclo
        logThread.start();

        // Avvia il logging
        running = true;
        System.out.println("Logging avviato.");

        // Esegui il ciclo principale per il controllo
        new Thread(() -> {
            try {
                while (true) {
                    // Fai scrivere per 1 minuto
                    Thread.sleep(60000); // 60000 ms = 1 minuto
                    running = false; // Ferma il logging
                    System.out.println("Logging fermato per 1 minuto.");

                    // Manda un messaggio di attesa
                    producer.sendLog(topic, "********** ATTENDERE 1 MINUTO, GRAZIE! **********");

                    // Aspetta 1 minuto
                    Thread.sleep(60000); // 60000 ms = 1 minuto
                    running = true; // Riprendi il logging
                    System.out.println("Logging ripreso.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Ripristina lo stato di interruzione
            }
        }).start();
    }
}




/*
public class Main {
    public static void main(String[] args) {
        // Specifica il server Kafka a cui connettersi
        // Assicurati che 'kafka' sia l'hostname corretto nel tuo setup Docker
        String bootstrapServers = "kafka:9092"; // Usa 'kafka' se il broker è in un container Docker
        LogProducer logProducer = new LogProducer(bootstrapServers);

        // Nome del topic a cui inviare i log
        String topic = "logs";

        // Variabile per tenere traccia del numero di log inviati
        int logCount = 1;

        // Ciclo infinito per inviare log in modo continuativo
        while (true) {
            // Crea il messaggio di log con un numero incrementale
            String message = "....Log " + logCount++ + " scritto con successo!....";
            logProducer.sendLog(topic, message);

            // Attende un intervallo di tempo prima di inviare il prossimo log
            try {
                Thread.sleep(1000); // Invia un log ogni 1000 ms (1 secondo)
            } catch (InterruptedException e) {
                // Se il thread viene interrotto, esci dal ciclo
                Thread.currentThread().interrupt();
                break;
            }
        }

        // Chiude il produttore quando il ciclo termina (non raggiungibile in questo esempio)
        logProducer.close();
    }
}
*/