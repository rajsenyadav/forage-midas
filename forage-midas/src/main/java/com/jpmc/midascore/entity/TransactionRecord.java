package com.jpmc.midascore.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "transactions")
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private UserRecord sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id")
    private UserRecord recipient;

    @Column(name = "amount")
    private float amount;

    // NEW: Add incentive field for Task 4
    @Column(name = "incentive")
    private float incentive;

    // Constructors
    public TransactionRecord() {}

    public TransactionRecord(UserRecord sender, UserRecord recipient, float amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.incentive = 0.0f; // Default incentive to 0
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserRecord getSender() { return sender; }
    public void setSender(UserRecord sender) { this.sender = sender; }

    public UserRecord getRecipient() { return recipient; }
    public void setRecipient(UserRecord recipient) { this.recipient = recipient; }

    public float getAmount() { return amount; }
    public void setAmount(float amount) { this.amount = amount; }

    // NEW: Incentive getter and setter
    public float getIncentive() { return incentive; }
    public void setIncentive(float incentive) { this.incentive = incentive; }

    @Override
    public String toString() {
        return "TransactionRecord{id=" + id + ", amount=" + amount + ", incentive=" + incentive + "}";
    }
}
