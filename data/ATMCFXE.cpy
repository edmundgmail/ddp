      ******************************************************************
      * COBOL DECLARATION FOR TABLE T85DBC.BATMFXE                     *
      ******************************************************************
       01  DCLBATMFXE.
           10                      PIC X(6).
           10 EVENT-ID             PIC X(13).
           10 TERMINAL-ID          PIC X(8).
           10 EVENT-TIMESTAMP      PIC X(26).
           10 TRACE-NUMBER         PIC S9(9) USAGE COMP.
           10 REQUEST-TYPE         PIC X(1).
           10 EVENT-DESC           PIC X(10).
           10 EXCHANGE-OPERATION   PIC X(1).
           10 BIN                  PIC X(11).
           10 CURRENCY-NUM         PIC X(3).
           10 AMT-ATM-CURRENCY     PIC S9(8)V9(9) USAGE COMP-3.
           10 FEE-ATM-CURRENCY     PIC S9(8)V9(9) USAGE COMP-3.
           10 STATUS-CODE          PIC X(1).
           10 TRADE-ID             PIC X(12).
           10 RATE-TDS             PIC S9(8)V9(7) USAGE COMP-3.
           10 RATE-MASTERCARD      PIC S9(8)V9(7) USAGE COMP-3.
           10 RATE-CODE            PIC X(1).                            
           10 AMT-CARD-CURRENCY    PIC S9(8)V9(9) USAGE COMP-3.
           10 FEE-CARD-CURRENCY    PIC S9(8)V9(9) USAGE COMP-3.
           10 AMT-REVENUE          PIC S9(3)V9(2) USAGE COMP-3.
      ******************************************************************
      * THE NUMBER OF COLUMNS DESCRIBED BY THIS DECLARATION IS 19      *
      ******************************************************************