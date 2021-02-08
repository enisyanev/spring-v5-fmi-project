package com.project.spring.digitalwallet.service;

import com.project.spring.digitalwallet.dao.SlipRepository;
import com.project.spring.digitalwallet.dao.TransactionRepository;
import com.project.spring.digitalwallet.dto.transaction.TransactionDto;
import com.project.spring.digitalwallet.dto.transaction.TransactionsHistoryPagedDto;
import com.project.spring.digitalwallet.exception.NonexistingEntityException;
import com.project.spring.digitalwallet.model.transaction.Direction;
import com.project.spring.digitalwallet.model.transaction.Slip;
import com.project.spring.digitalwallet.model.transaction.Transaction;
import com.project.spring.digitalwallet.model.transaction.TransactionStatus;
import com.project.spring.digitalwallet.model.user.User;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

@Service
public class TransactionService {
    private static final String CSV_CONTENT_TYPE = "text/csv";
    private static final String TRANSACTION_HISTORY_CSV_FILE_NAME = "transactions.csv";
    private static final String CSV_HEADER_KEY = "Content-Disposition";
    private static final String CSV_HEADER_VALUE = "attachment; filename=";

    private TransactionRepository transactionRepository;
    private AccountService accountService;
    private SlipRepository slipRepository;
    private UserService userService;

    public TransactionService(TransactionRepository transactionRepository,
                              AccountService accountService,
                              SlipRepository slipRepository, UserService userService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
        this.slipRepository = slipRepository;
        this.userService = userService;
    }

    public Transaction getById(Long id) {
        return transactionRepository.findById(id).orElseThrow(() ->
            new NonexistingEntityException(
                String.format("Transaction with ID:%s does not exist.", id)));
    }

    public Transaction getBySlipId(Long slipId) {
        return transactionRepository.findFirstBySlipId(slipId).orElseThrow(() ->
            new NonexistingEntityException(
                String.format("Transaction with slip id:%s does not exist.", slipId)));
    }

    @Transactional
    public List<Transaction> createTransactions(List<Transaction> transactions) {
        List<Transaction> created = new ArrayList<>();
        Long slipId = generateSlipId();
        for (Transaction transaction : transactions) {
            BigDecimal amount = transaction.getAmount();
            if (transaction.getDirection() == Direction.W) {
                amount = amount.negate();
            }
            created.add(storeTransaction(transaction, slipId, amount));
        }

        return created;
    }

    private Long generateSlipId() {
        return slipRepository.save(new Slip()).getId();
    }

    @Transactional
    public Transaction storeTransaction(Transaction transaction, Long slipId, BigDecimal amount) {
        transaction.setSlipId(slipId);
        accountService.updateBalance(transaction.getAccountId(), amount);
        return transactionRepository.save(transaction);
    }

    public void updateStatus(Long trnId, TransactionStatus status) {
        Transaction trn = getById(trnId);
        trn.setStatus(status);
        transactionRepository.save(trn);
    }

    public TransactionsHistoryPagedDto getTransactionsHistory(int pageNo, int pageSize) {
        User user = getLoggedUser();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<Transaction> transactionHistory = transactionRepository.findByWalletId(user.getWalletId(), pageable);
        return new TransactionsHistoryPagedDto(convertTransactionsToDto(transactionHistory.getContent()),
                transactionHistory.getTotalPages(), transactionHistory.getNumber() + 1, transactionHistory.getSize());
    }

    public List<TransactionDto> getTransactionsHistory() {
        User user = getLoggedUser();
        List<Transaction> transactionHistory = transactionRepository.findByWalletId(user.getWalletId());
        return convertTransactionsToDto(transactionHistory);
    }

    public void downloadTransactionsHistoryAsCsv(HttpServletResponse response) throws IOException {
        response.setContentType(CSV_CONTENT_TYPE);
        String headerValue = CSV_HEADER_VALUE + TRANSACTION_HISTORY_CSV_FILE_NAME;
        response.setHeader(CSV_HEADER_KEY, headerValue);
        ICsvBeanWriter writer = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"Transaction ID", "From Account ID", "Amount", "Currency", "Created Time", "Description"};
        String[] nameMapping = {"id", "accountId", "amount", "currency", "createdTime", "description"};

        List<TransactionDto> transactionsHistory = getTransactionsHistory();
        transactionsHistory.sort(Comparator.comparing(TransactionDto::getCreatedTime).reversed());

        writer.writeHeader(csvHeader);
        for (TransactionDto transactionDto : transactionsHistory) {
            writer.write(transactionDto, nameMapping);
        }
        writer.close();
    }

    private User getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserByUsername(authentication.getName());
    }

    private List<TransactionDto> convertTransactionsToDto(List<Transaction> transactionsHistory) {
        return transactionsHistory.stream()
            .map(t -> new TransactionDto(t.getId(), t.getAmount(), t.getCreatedTime(),
                t.getAccountId(), t.getDirection(), t.getCurrency())).collect(Collectors.toList());
    }

}
