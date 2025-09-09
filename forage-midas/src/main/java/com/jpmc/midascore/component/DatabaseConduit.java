package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.repository.UserRepository;
import com.jpmc.midascore.repository.TransactionRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DatabaseConduit {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public DatabaseConduit(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    public void save(UserRecord userRecord) {
        userRepository.save(userRecord);
    }

    public void save(TransactionRecord transactionRecord) {
        transactionRepository.save(transactionRecord);
    }

    public boolean validateAndProcessTransaction(long senderId, long recipientId, float amount) {
        // Convert long to Long for findById - THIS IS THE KEY FIX!
        Optional<UserRecord> senderOpt = userRepository.findById(Long.valueOf(senderId));
        Optional<UserRecord> recipientOpt = userRepository.findById(Long.valueOf(recipientId));

        // Check if users exist using proper Optional handling
        if (senderOpt.isEmpty() || recipientOpt.isEmpty()) {
            System.out.println("❌ Invalid user ID - Sender: " + senderId + ", Recipient: " + recipientId);
            return false;
        }

        // Extract UserRecord from Optional
        UserRecord sender = senderOpt.get();
        UserRecord recipient = recipientOpt.get();

        // Validate sender has sufficient balance
        if (sender.getBalance() < amount) {
            System.out.println("❌ Insufficient balance - Sender: " + sender.getName() + " has " + sender.getBalance() + ", needs " + amount);
            return false;
        }

        // Process transaction - update balances
        sender.setBalance(sender.getBalance() - amount);
        recipient.setBalance(recipient.getBalance() + amount);

        // Save updated users and transaction record
        save(sender);
        save(recipient);
        save(new TransactionRecord(sender, recipient, amount));

        System.out.println("✅ Transaction processed: " + sender.getName() + " -> " + recipient.getName() + " amount: " + amount);
        System.out.println("📊 " + sender.getName() + " balance: " + sender.getBalance());
        System.out.println("📊 " + recipient.getName() + " balance: " + recipient.getBalance());

        return true;
    }

    // ADD THIS NEW METHOD HERE
    public void printAllUserBalances() {
        System.out.println("=== ALL USER BALANCES ===");
        userRepository.findAll().forEach(user -> {
            System.out.println("👤 " + user.getName() + ": " + user.getBalance());
            if ("waldorf".equals(user.getName())) {
                System.out.println("🎯 WALDORF BALANCE: " + user.getBalance());
                System.out.println("🎯 WALDORF ROUNDED DOWN: " + (int) Math.floor(user.getBalance()));
            }
        });
        System.out.println("========================");
    }

}
