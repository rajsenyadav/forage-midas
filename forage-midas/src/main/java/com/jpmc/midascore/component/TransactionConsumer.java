package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionConsumer {

    private static final Logger logger = LoggerFactory.getLogger(TransactionConsumer.class);
    private final DatabaseConduit databaseConduit;

    public TransactionConsumer(DatabaseConduit databaseConduit) {
        this.databaseConduit = databaseConduit;
    }

    @KafkaListener(topics = "${general.kafka-topic}")
    public void receiveTransaction(Transaction transaction) {
        logger.info("📥 Received transaction: {}", transaction);

        System.out.println("=== PROCESSING TRANSACTION ===");
        System.out.println("Sender ID: " + transaction.getSenderId());
        System.out.println("Recipient ID: " + transaction.getRecipientId());
        System.out.println("Amount: " + transaction.getAmount());

        // Validate and process transaction
        boolean success = databaseConduit.validateAndProcessTransaction(
                transaction.getSenderId(),
                transaction.getRecipientId(),
                transaction.getAmount()
        );

        if (success) {
            System.out.println("✅ Transaction completed successfully");
            // ADD THIS LINE TO PRINT ALL BALANCES AFTER EACH TRANSACTION
            databaseConduit.printAllUserBalances();
        } else {
            System.out.println("❌ Transaction rejected");
        }
        System.out.println("==============================");
    }

}
