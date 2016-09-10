      *================================================================*
      *                                                                *
      *                 TORONTO-DOMINION BANK (C)                      *
      *                                                                *
      *              CRPWBILL COPY CODE DESCRIPTION                    *
      *                                                                *
      *----------------------------------------------------------------*
      *                                                                *
      * DESCRIPTION     : WPS WIRE PAYMENTS OUTPUT LAYOUT              *
      *                                                                *
      * USED BY         : PRPW2200, PRPW2210 AND RPW2220               *
      *                                                                *
      *================================================================*
      *              M A I N T E N A N C E  L O G                      *
      *================================================================*
      *                                                                *
      *  ACCEPT.     WRITTEN/CHANGED BY              IMPLEMENTATION    *
      *  TEST #     (FULL 1ST AND LAST NAME)         DATE (MMM, YYYY)  *
      *  -------     ------------------------        ----------------  *
      *  A709385    NITIN GOEL                            FEB, 2017    *
      *  (OBS)                                                         *
      *                                                                *
      * DESCRIPTION:                                                   *
      * -------------                                                  *
      * - INITIAL VERSION.                                             *
      *                                                                *
      *================================================================*
       01 WPS-RECORD.
         05 WPS-RECORD-TYPE-IND           PIC X(01).
            88 WPS-HEADER-REC             VALUE 'A'.
            88 WPS-DETAIL-REC             VALUE 'D'.
            88 WPS-TRAILER-REC            VALUE 'Z'.
         05 WPS-HEADER-RECORD.
            10 WPS-HEADER-DATE            PIC X(10).
            10 FILLER                       PIC X(169).
         05 WPS-DETAIL-RECORD REDEFINES WPS-HEADER-RECORD.
            10 WPS-CUSTID                 PIC X(05).
            10 WPS-PRO-DATE               PIC X(06).
            10 WPS-PAYMT-AMT              PIC 9(13)V99.
            10 WPS-PAYMT-CURR             PIC X(03).
            10 WPS-DEST                   PIC X(35).
            10 WPS-BR-TO-BR               PIC X.
            10 WPS-BANKID                 PIC X(35).
            10 WPS-FXSPREAD               PIC 9(13)V99.
            10 WPS-CHGSBEN                PIC X.
            10 WPS-FULLDEL                PIC X.
            10 WPS-FXIND                  PIC X.
            10 WPS-BILL-CODE              PIC X(04).
            10 WPS-VOLUME                 PIC 9(03).
            10 WPS-PAYMT-BR               PIC 9(04).
            10 WPS-PAYMT-ACCT             PIC 9(09).
            10 WPS-ACCT-TYPE              PIC X(01).
            10 WPS-TRANS-DATE             PIC X(10).
            10 WPS-TRANS-TIME             PIC X(08).
            10 FILLER                       PIC X(22).
         05 WPS-TRAILER-RECORD REDEFINES WPS-HEADER-RECORD.
            10 WPS-TOTAL-DETAIL-RECORD    PIC 9(09).
            10 FILLER                       PIC X(170).
