
    'Java -jar ../Cobol2Cobol.jar -i In/DTAR020.bin     -InputCopybook  DTAR020.cbl -InputDialect  Mainframe -InputFileStructure  Fixed_Length    -InputCharacterSet CP037' ,
                                 '-o Out/DTAR021.txt -OutputCopybook DTAR021.cbl -OutputDialect Fujitsu   -OutputFileStructure Text'
                                 
    'Java -jar ../Cobol2Cobol.jar -o Out/DTAR020_c.bin   -OutputCopybook DTAR020.cbl -OutputDialect Mainframe -OutputFileStructure Fixed_Length    -OutputCharacterSet CP037' ,
                                 '-i In/DTAR021.txt  -InputCopybook  DTAR021.cbl -InputDialect  Fujitsu   -InputFileStructure  Text'
                                