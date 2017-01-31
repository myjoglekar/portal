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
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

/**
 * @author Ricardo Mariaca (r.mariaca@dynamicreports.org)
 */
public class GroupInNewPageReport {

    public GroupInNewPageReport() {
        build();
    }

    private void build() {
        TextColumnBuilder<String> itemColumn = col.column("Item", "item", type.stringType())
                .setWidth(30);

        ColumnGroupBuilder itemGroup = grp.group(itemColumn)
                .startInNewPage();

        try {
            report()
                    .setTemplate(Templates.reportTemplate)
                    .columns(
                            itemColumn,
                            col.column("Order date", "orderdate", type.dateType()),
                            col.column("Quantity", "quantity", type.integerType()),
                            col.column("Unit price", "unitprice", type.bigDecimalType()))
                    .groupBy(itemGroup)
                    .title(Templates.createTitleComponent("GroupInNewPage"))
                    .pageFooter(Templates.footerComponent)
                    .setDataSource(createDataSource())
                    .show();
        } catch (DRException e) {
            e.printStackTrace();
        }
    }

    private JRDataSource createDataSource() {
        DRDataSource dataSource = new DRDataSource("item", "orderdate", "quantity", "unitprice");
        dataSource.add("Tablet", toDate(2010, 1, 1), 5, new BigDecimal(300));
        dataSource.add("Tablet", toDate(2010, 1, 3), 1, new BigDecimal(280));
        dataSource.add("Tablet", toDate(2010, 1, 19), 5, new BigDecimal(320));
        dataSource.add("Laptop", toDate(2010, 1, 5), 3, new BigDecimal(580));
        dataSource.add("Laptop", toDate(2010, 1, 8), 1, new BigDecimal(620));
        dataSource.add("Laptop", toDate(2010, 1, 15), 5, new BigDecimal(600));
        dataSource.add("Smartphone", toDate(2010, 1, 18), 8, new BigDecimal(150));
        dataSource.add("Smartphone", toDate(2010, 1, 20), 8, new BigDecimal(310));
        return dataSource;
    }

    private Date toDate(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, day);
        return c.getTime();
    }

    public static void main(String[] args) {
        new GroupInNewPageReport();
    }
}
