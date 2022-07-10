package com.alumni.entities;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class UserExcelExporter {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	
	private List<User> lisUsers;
	
	
	public UserExcelExporter(List<User> lisUsers) {
		
		this.lisUsers = lisUsers;
		workbook= new XSSFWorkbook();
		sheet= workbook.createSheet("users");
		
		
	}

	private void writeHeaderRow() {
		Row row= sheet.createRow(0);
		
		CellStyle style= workbook.createCellStyle();
		XSSFFont font= workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		style.setFont(font);
		
		Cell cell= row.createCell(0);
		cell.setCellValue("User ID");
		cell.setCellStyle(style);
		
		
		 cell= row.createCell(1);
		cell.setCellValue("User Email");
		cell.setCellStyle(style);
		
		
		
		 cell= row.createCell(2);
		cell.setCellValue("User Name");
		cell.setCellStyle(style);
		
	
	      cell= row.createCell(3);
		cell.setCellValue("Enabled");
		cell.setCellStyle(style);

	}
	
	private void WriteDataRows() {
		
		int rowCount=1;
		CellStyle style= workbook.createCellStyle();
		XSSFFont font= workbook.createFont();
		font.setFontHeight(14);
		style.setFont(font);
		
		
		
		for(User user:lisUsers) {
			Row row= sheet.createRow(rowCount++);
			
			
			
			Cell cell= row.createCell(0);
			cell.setCellValue(user.getUId());
			sheet.autoSizeColumn(0);
			cell.setCellStyle(style);
			
			
			cell= row.createCell(1);
			cell.setCellValue(user.getEmail());
			sheet.autoSizeColumn(1);
			cell.setCellStyle(style);
			
			cell= row.createCell(2);
			cell.setCellValue(user.getFullName());
			sheet.autoSizeColumn(2);
			cell.setCellStyle(style);
			
			cell= row.createCell(3);
			cell.setCellValue(user.getEnabled());
			sheet.autoSizeColumn(3);
			cell.setCellStyle(style);
		}
		
	}
	
	public void export(HttpServletResponse response) throws IOException {
		writeHeaderRow();
		WriteDataRows();
		
	ServletOutputStream outputStream=	response.getOutputStream();
	workbook.write(outputStream);
	workbook.close();
	outputStream.close();
	

	}
}
