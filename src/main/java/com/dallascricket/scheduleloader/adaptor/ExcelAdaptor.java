package com.dallascricket.scheduleloader.adaptor;

import com.dallascricket.scheduleloader.excel.model.MatchData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelAdaptor {
    private static int firstNonEmptyCellIndex;
    private static boolean isFirstNonEmptyCellIndexInitialized;
    private final static Logger logger = LogManager.getLogger(ExcelAdaptor.class);

    public List<MatchData> readExcelSheet(InputStream inputStream) throws IOException{
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        return readScheduleExcel(sheet);
    }


    public List<MatchData> readScheduleExcel(XSSFSheet sheet) {
        List<MatchData> matchDataList = new ArrayList<MatchData>();
        logger.debug("Reading excel file sheet - " + sheet.getSheetName());
        Iterator<Row> rowIterator = sheet.iterator();
        int j = 0;
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (isRowEmpty(row))
                continue;
            j++;
            if (j != 1) {
                if (!isFirstNonEmptyCellIndexInitialized) {
                    firstNonEmptyCellIndex = getFirstNonEmptyCellIndex(row);
                    isFirstNonEmptyCellIndexInitialized = true;
                }

                if (row.getCell(firstNonEmptyCellIndex).getCellType() != CellType.NUMERIC)
                    break;
                else {
                    MatchData matchData = loadMatchData(row,
                            firstNonEmptyCellIndex);

                    matchDataList.add(matchData);
                }
            }
        }

        logger.debug("No. of entries - " + matchDataList.size());

        return matchDataList;
    }

    private MatchData loadMatchData(Row row, int i) {
        MatchData matchData = new MatchData();
        matchData.setWeek("" + row.getCell(i).getNumericCellValue());
        matchData.setDate(row.getCell(++i).getDateCellValue());
        matchData.setDivision(row.getCell(++i).getStringCellValue());
        matchData.setTournamentId((row.getCell(++i).getNumericCellValue()));
        matchData.setTeam1(row.getCell(++i).getStringCellValue());
        matchData.setTeam2(row.getCell(++i).getStringCellValue());
        matchData.setVenue(row.getCell(++i).getStringCellValue());
        matchData.setUmpire1(row.getCell(++i).getStringCellValue());
        matchData.setUmpire2(row.getCell(++i).getStringCellValue());
        ++i;//skipping one column as spring 2016 schedule has unwanted
        // unpiring column
        matchData.setDay(row.getCell(++i).getStringCellValue());
        matchData.setTime(row.getCell(++i).getStringCellValue());
        return matchData;
    }

    private int getFirstNonEmptyCellIndex(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK)
                return c;
        }
        return 0;
    }

    public boolean isRowEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK)
                return false;
        }
        return true;
    }

}
