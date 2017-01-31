/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.dynamic;

import net.sf.dynamicreports.jasper.builder.export.JasperXlsExporterBuilder;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.report.builder.chart.AreaChartBuilder;
import net.sf.dynamicreports.report.builder.chart.BarChartBuilder;
import net.sf.dynamicreports.report.builder.chart.LineChartBuilder;
import net.sf.dynamicreports.report.builder.chart.PieChartBuilder;
import net.sf.dynamicreports.report.builder.chart.StackedBar3DChartBuilder;
import net.sf.dynamicreports.report.builder.chart.StackedBarChartBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

/**
 * @author Ricardo Mariaca (dynamicreports@gmail.com)
 */
public class ChartShowPercentagesReport {

    public ChartShowPercentagesReport() {
        build();
    }

    private void build() {
        TextColumnBuilder<String> itemColumn = col.column("Item", "item", type.stringType());
        TextColumnBuilder<Integer> stock1Column = col.column("Stock 1", "stock1", type.integerType());
        TextColumnBuilder<Integer> stock2Column = col.column("Stock 2", "stock2", type.integerType());
        TextColumnBuilder<Integer> stock3Column = col.column("Stock 3", "stock3", type.integerType());

        BarChartBuilder barChart = cht.barChart()
                .setShowPercentages(true)
                .setShowValues(true)
                .setValuePattern("#,##0")
                .setCategory(itemColumn)
                .series(cht.serie(stock1Column), cht.serie(stock2Column), cht.serie(stock3Column));
        StackedBarChartBuilder stackedBarChart = cht.stackedBarChart()
                .setShowPercentages(true)
                .setShowValues(true)
                .setCategory(itemColumn)
                .series(cht.serie(stock1Column), cht.serie(stock2Column), cht.serie(stock3Column));
        LineChartBuilder lineChart = cht.lineChart()
                .setShowPercentages(true)
                .setShowValues(true)
                .setCategory(itemColumn)
                .series(cht.serie(stock1Column), cht.serie(stock2Column), cht.serie(stock3Column));
        AreaChartBuilder areaChart = cht.areaChart()
                .setShowPercentages(true)
                .setShowValues(true)
                .setCategory(itemColumn)
                .series(cht.serie(stock1Column), cht.serie(stock2Column), cht.serie(stock3Column));
        StackedBar3DChartBuilder stackedBar3DChart = cht.stackedBar3DChart()
                .setShowPercentages(true)
                .setShowValues(true)
                .setCategory(itemColumn)
                .series(cht.serie(stock1Column), cht.serie(stock2Column), cht.serie(stock3Column));
        PieChartBuilder pieChart = cht.pieChart()
                .setShowPercentages(true)
                .setShowValues(true)
                .setKey(itemColumn)
                .series(cht.serie(stock1Column));
        try {
            JasperXlsExporterBuilder xlsExporter = export.xlsExporter("/tmp/report.xls")
			                                             .setDetectCellType(false)
			                                             .setIgnorePageMargins(true)
			                                             .setWhitePageBackground(false)
.setRemoveEmptySpaceBetweenColumns(true);
            report()
                    .setTemplate(Templates.reportTemplate)
                    .columns(itemColumn, stock1Column, stock2Column, stock3Column)
                    .title(Templates.createTitleComponent("ChartShowPercentages"))
                    .summary(
                            cmp.horizontalList(barChart, stackedBarChart),
                            cmp.horizontalList(lineChart, areaChart),
                            cmp.horizontalList(stackedBar3DChart, pieChart))
                    .pageFooter(Templates.footerComponent)
                    .setDataSource(createDataSource())
                    .toXls(xlsExporter);
//                    .show();
        } catch (DRException e) {
            e.printStackTrace();
        }
    }

    private JRDataSource createDataSource() {
        DRDataSource dataSource = new DRDataSource("item", "stock1", "stock2", "stock3");
        dataSource.add("Notebook", 95, 80, 50);
        dataSource.add("Book", 170, 100, 80);
        dataSource.add("PDA", 120, 80, 60);
        return dataSource;
    }

    public static void main(String[] args) {
        new ChartShowPercentagesReport();
    }
}
