package com.project.spring.digitalwallet.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.project.spring.digitalwallet.model.Wallet;
import com.project.spring.digitalwallet.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class WalletControllerTest {

    private static final Long ID = 123L;

    @Mock
    private WalletService walletService;

    @InjectMocks
    private WalletController walletController;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getWalletById() {
        Wallet expected = mock(Wallet.class);
        when(walletService.getWalletById(ID)).thenReturn(expected);

        Wallet actual = walletController.getWalletById(ID);

        assertEquals(expected, actual);
    }
}
