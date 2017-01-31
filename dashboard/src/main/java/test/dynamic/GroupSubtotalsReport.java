/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.dynamic;


import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.group.ColumnGroupBuilder;
import net.sf.dynamicreports.report.constant.GroupHeaderLayout;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

/**
 * @author Ricardo Mariaca (r.mariaca@dynamicreports.org)
 */
public class GroupSubtotalsReport {

	public GroupSubtotalsReport() {
		build();
	}

	private void build() {
		TextColumnBuilder<Date> yearColumn = col.column("Order year", "orderdate", type.dateYearType());
		TextColumnBuilder<Date> monthColumn = col.column("Order month", "orderdate", type.dateMonthType());
		TextColumnBuilder<Integer> quantityColumn = col.column("Quantity", "quantity",  type.integerType());
		TextColumnBuilder<BigDecimal> unitPriceColumn = col.column("Unit price", "unitprice", type.bigDecimalType());

		ColumnGroupBuilder yearGroup  = grp.group(yearColumn)
			.groupByDataType();
		ColumnGroupBuilder monthGroup = grp.group(monthColumn)
			.groupByDataType()
			.setHeaderLayout(GroupHeaderLayout.EMPTY)
			.setHideColumn(false);

		try {
			report()
				.setTemplate(Templates.reportTemplate)
				.setSubtotalStyle(Templates.columnStyle)
				.setShowColumnValues(false)
				.columns(yearColumn, monthColumn, quantityColumn, unitPriceColumn)
				.groupBy(yearGroup, monthGroup)
				.subtotalsAtGroupFooter(monthGroup, sbt.first(monthColumn), sbt.sum(quantityColumn), sbt.sum(unitPriceColumn))
				.title(Templates.createTitleComponent("GroupSubtotals"))
				.pageFooter(Templates.footerComponent)
				.setDataSource(createDataSource())
				.show();
		} catch (DRException e) {
			e.printStackTrace();
		}
	}

	private JRDataSource createDataSource() {
		DRDataSource dataSource = new DRDataSource("orderdate", "quantity", "unitprice");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.YEAR, -1);
		for (int i = 0; i < 200; i++) {
			Date date = c.getTime();
			dataSource.add(date, (int) (Math.random() * 10) + 1, new BigDecimal(Math.random() * 100 + 1));
			c.add(Calendar.DAY_OF_MONTH, 1);
		}
		return dataSource;
	}

	public static void main(String[] args) {
		new GroupSubtotalsReport();
	}
}