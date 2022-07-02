package com.apt.aptservice;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ApartmentMaintenanceCalculator {

    @Value("${apt.maintenance.bank.transaction.file}")
    String aptMaintenanceBankTransactionFile;

    @Value("${apt.maintenance.bank.transaction.file.date.column.index}")
    int dateColumnIndex;

    @Value("${apt.maintenance.bank.transaction.file.tranid.column.index}")
    int tranidColumnIndex;

    @Value("${apt.maintenance.bank.transaction.file.remarks.column.index}")
    int remarksColumnIndex;

    @Value("${apt.maintenance.bank.transaction.file.deposits.column.index}")
    int depositsColumnIndex;

    @Value("#{${flat.key.string.map}}")
    Map<String,String> flatKeyStringMap;

    public void calculate() {
        System.out.println("Calculate apartment maintenance from file - "+aptMaintenanceBankTransactionFile);
        List<BankTransaction> transactions = constructBankTransactions();
        List<MaintenanceReport> reports = generateMaintenanceReport(transactions);
        reports.forEach(report -> System.out.println(report));
//        generateMaintenanceReportFile(reports);
    }

    private void generateMaintenanceReportFile(List<MaintenanceReport> reports) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Maintenance Report");
        int index = 0;
        for (MaintenanceReport report : reports) {
            Row row = sheet.createRow(index++);
            Cell cellFlat = row.createCell(0);
            cellFlat.setCellValue(report.getFlat());
            Cell cellPaidDate = row.createCell(1);
            cellPaidDate.setCellValue(report.getPaidDate());
            Cell cellAmount = row.createCell(2);
            cellAmount.setCellValue(report.getAmount());
        }
        try(FileOutputStream fileOutputStream = new FileOutputStream("banktransaction_result.xls")) {
            workbook.write(fileOutputStream);
        }catch (Exception e){
            System.out.println("Error while generating file");
        }
    }

    private List<MaintenanceReport> generateMaintenanceReport(List<BankTransaction> transactions) {
        List<MaintenanceReport> reports = new ArrayList<>();
        transactions.forEach(transaction -> {
            String keyStringFromRemarks = getKeyStringFromRemarks(transaction.getRemarks());
            if (keyStringFromRemarks!=null){
                String flatNumber = getFlatNumber(keyStringFromRemarks);
                if (flatNumber!=null){
                    MaintenanceReport report = new MaintenanceReport(flatNumber,transaction.getDate(),transaction.getDeposit());
                    reports.add(report);
                }else {
                    System.out.println("Could not find match for tran id - "+transaction.getTranid());
                }
            }else {
                System.out.println("Could not find Key String from Remarks for tran id - "+transaction.getTranid());
            }
        });
        reports.sort((MaintenanceReport mr1, MaintenanceReport mr2) -> {
            if ((mr1.getFlat().startsWith("G") && !mr2.getFlat().startsWith("G")) || (!mr1.getFlat().startsWith("G") && mr2.getFlat().startsWith("G")))
                return mr2.getFlat().compareTo(mr1.getFlat());
            else
                return mr1.getFlat().compareTo(mr2.getFlat());
        });
        return reports;
    }

    private String getFlatNumber(String remarks) {
        for (Map.Entry<String, String> entry : flatKeyStringMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (value.contains(remarks)) {
                return key;
            }
        }
        return null;
    }

    private String getKeyStringFromRemarks(String remarks) {
        String value = null;
        if (remarks.startsWith("UPI")){
            String[] split = remarks.split("/");
            if (split.length>=5) {
                String s = split[5];
                if (s.contains("@")){
                    value = s.split("@")[0].trim();
                }else {
                    value = s.trim();
                }
            }
        } else if (remarks.startsWith("NEFT")) {
            String[] split = remarks.split(":");
            if (split.length>=2) {
                String s = split[1];
                value = s.substring(0, s.lastIndexOf(" "));
            }
        } else if (remarks.startsWith("IMPS")) {
            value = remarks.substring(remarks.lastIndexOf("/")+1);
        } else if (remarks.startsWith("eTXN")) {
            value = remarks.substring(remarks.lastIndexOf("/")+1);
        }
        return value;
    }

    private List<BankTransaction> constructBankTransactions() {
        try {
            List<BankTransaction> transactions = new ArrayList<>();
            FileInputStream inputStream = new FileInputStream(new File(aptMaintenanceBankTransactionFile));
            Workbook workbook = new HSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = sheet.iterator();
            while (iterator.hasNext()) {
                Row nextRow = iterator.next();
                if (nextRow.getRowNum()==0){
                    continue;
                }
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                BankTransaction transaction = new BankTransaction();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    if (cell.getColumnIndex()==dateColumnIndex){
                        transaction.setDate(cell.getStringCellValue());
                    }else if (cell.getColumnIndex()==tranidColumnIndex){
                        transaction.setTranid(cell.getStringCellValue());
                    }else if (cell.getColumnIndex()==remarksColumnIndex){
                        transaction.setRemarks(cell.getStringCellValue());
                    }else if (cell.getColumnIndex()==depositsColumnIndex){
                        transaction.setDeposit(cell.getNumericCellValue());
                    }
                }
                transactions.add(transaction);
            }
            workbook.close();
            inputStream.close();
            return transactions;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
