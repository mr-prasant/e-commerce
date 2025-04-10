package com.ecommerce.service;

import com.ecommerce.entity.Transaction;

public interface TransactionService {
    Transaction addTransaction(Transaction transaction);
    Transaction getTransaction(String oid);
    Transaction getAllTransactions(String userid);
    Transaction getAllProductTransactions(String pid);
}
