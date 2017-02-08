/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.visumbu.vb.model.TabWidget;
import com.visumbu.vb.model.WidgetColumn;
import com.visumbu.vb.utils.ApiUtils;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.TextAnchor;

/**
 *
 * @author user
 */
public class CustomReportDesigner {

    private static List<CalcualtedFunction> calcualtedFunctions = new ArrayList<>();

    static {
        calcualtedFunctions.add(new CalcualtedFunction("ctr", "clicks", "impressions"));
        calcualtedFunctions.add(new CalcualtedFunction("cpa", "cost", "conversions"));
    }

    private Boolean isZeroRow(Map<String, String> mapData, List<WidgetColumn> columns) {
        for (Iterator<WidgetColumn> iterator = columns.iterator(); iterator.hasNext();) {
            WidgetColumn column = iterator.next();
            if (ApiUtils.toDouble(mapData.get(column.getFieldName())) != 0) {
                return false;
            }
        }
        return true;
    }

    private Double sum(List<Map<String, String>> data, String fieldName) {
        Double sum = 0.0;
        for (Iterator<Map<String, String>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, String> mapData = iterator.next();
            sum += ApiUtils.toDouble(mapData.get(fieldName));
        }
        return sum;
    }

    private Double min(List<Map<String, String>> data, String fieldName) {
        Double min = null;
        for (Iterator<Map<String, String>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, String> mapData = iterator.next();
            if (min == null || ApiUtils.toDouble(mapData.get(fieldName)) < min) {
                min = ApiUtils.toDouble(mapData.get(fieldName));
            }
        }
        return min;
    }

    private Double calulatedMetric(List<Map<String, String>> data, CalcualtedFunction calcualtedFunction) {
        String name = calcualtedFunction.getName();
        String field1 = calcualtedFunction.getField1();
        String field2 = calcualtedFunction.getField2();
        Double sum1 = sum(data, field1);
        Double sum2 = sum(data, field2);
        if (sum1 != 0 && sum2 != 0) {
            return sum1 / sum2;
        }
        return 0.0;
    }

    private Double max(List<Map<String, String>> data, String fieldName) {
        Double max = null;
        for (Iterator<Map<String, String>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, String> mapData = iterator.next();
            if (max == null || ApiUtils.toDouble(mapData.get(fieldName)) > max) {
                max = ApiUtils.toDouble(mapData.get(fieldName));
            }
        }
        return max;
    }

    private String format(WidgetColumn column, String value) {
        return value;
    }

    private List<Map<String, String>> sortData(List<Map<String, String>> data, List<SortType> sortType) {
        if (1 == 1) {
            return data;
        }
        Collections.sort(data, (Map<String, String> o1, Map<String, String> o2) -> {
            for (Iterator<SortType> iterator = sortType.iterator(); iterator.hasNext();) {
                SortType sortType1 = iterator.next();
                int order = 1;
                if (sortType1.getSortOrder().equalsIgnoreCase("desc")) {
                    order = -1;
                }
                if (sortType1.getFieldType().equalsIgnoreCase("number")) {
                    Double value1 = ApiUtils.toDouble(o1.get(sortType1.getFieldName()));
                    Double value2 = ApiUtils.toDouble(o2.get(sortType1.getFieldName()));
                    if (value1 != value2) {
                        return order * new Double(value1 - value2).intValue();
                    }
                } else {
                    String value1 = o1.get(sortType1.getFieldName());
                    String value2 = o2.get(sortType1.getFieldName());
                    if (value1.compareTo(value2) != 0) {
                        return order * value1.compareTo(value2);
                    }
                }
            }
            return 0;
        });

//        Collections.sort(data, new Comparator<Map<String, String>>() {
//            @Override
//            public int compare(Map<String, String> o1, Map<String, String> o2) {
//                for (Iterator<SortType> iterator = sortType.iterator(); iterator.hasNext();) {
//                    SortType sortType = iterator.next();
//                    
//                }
//                return 0;
//            }
//        });
        return data;
    }

    private Map<String, List<Map<String, String>>> groupBy(List<Map<String, String>> data, String groupField) {
        Map<String, List<Map<String, String>>> returnMap = new HashMap<>();
        for (Iterator<Map<String, String>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, String> dataMap = iterator.next();
            String fieldValue = dataMap.get(groupField);
            List<Map<String, String>> groupDataList = returnMap.get(fieldValue);

            if (groupDataList == null) {
                groupDataList = new ArrayList<>();
            }
            groupDataList.add(dataMap);
            returnMap.put(fieldValue, groupDataList);
        }
        return returnMap;
    }

    private List groupData(List<Map<String, String>> data, List<String> groupByFields, List<Aggregation> aggreagtionList) {
        List<String> currentFields = groupByFields;
        if (groupByFields.size() == 0) {
            return data;
        }
        List<Map<String, String>> actualList = data;
        List<Map<String, String>> groupedData = new ArrayList<>();
        String groupingField = currentFields.get(0);
        Map<String, List<Map<String, String>>> currentListGrouped = groupBy(actualList, groupingField);
        groupByFields.remove(0);
        for (Map.Entry<String, List<Map<String, String>>> entrySet : currentListGrouped.entrySet()) {
            String key = entrySet.getKey();
            List<Map<String, String>> value = entrySet.getValue();
            Map dataToPush = new HashMap<>();
            dataToPush.put("_key", key);
            dataToPush.put(groupingField, key);
            dataToPush.put("_groupField", groupingField);
            // Merge aggregation
            dataToPush.putAll(aggregateData(value, aggreagtionList));
            dataToPush.put("data", groupData(value, groupByFields, aggreagtionList));
            groupedData.add(dataToPush);
        }
        return groupedData;
    }

    private Map<String, String> aggregateData(List<Map<String, String>> data, List<Aggregation> aggreagtionList) {
        Map<String, String> returnMap = new HashMap<>();
        for (Iterator<Aggregation> iterator = aggreagtionList.iterator(); iterator.hasNext();) {
            Aggregation aggregation = iterator.next();
            if (aggregation.getAggregationType().equalsIgnoreCase("sum")) {
                returnMap.put(aggregation.getFieldName(), sum(data, aggregation.getFieldName()) + "");
            }
            if (aggregation.getAggregationType().equalsIgnoreCase("avg")) {
                returnMap.put(aggregation.getFieldName(), (sum(data, aggregation.getFieldName()) / data.size()) + "");
            }
            if (aggregation.getAggregationType().equalsIgnoreCase("count")) {
                returnMap.put(aggregation.getFieldName(), data.size() + "");
            }
            if (aggregation.getAggregationType().equalsIgnoreCase("min")) {
                returnMap.put(aggregation.getFieldName(), min(data, aggregation.getFieldName()) + "");
            }
            if (aggregation.getAggregationType().equalsIgnoreCase("max")) {
                returnMap.put(aggregation.getFieldName(), max(data, aggregation.getFieldName()) + "");
            }
            for (Iterator<CalcualtedFunction> iterator1 = calcualtedFunctions.iterator(); iterator1.hasNext();) {
                CalcualtedFunction calcualtedFunction = iterator1.next();
                if (aggregation.getAggregationType().equalsIgnoreCase(calcualtedFunction.getName())) {
                    returnMap.put(aggregation.getFieldName(), calulatedMetric(data, calcualtedFunction) + "");
                }
            }
        }
        return returnMap;
    }

    public PdfPTable dynamicPdfTable(TabWidget tabWidget) throws DocumentException {
        List<WidgetColumn> columns = tabWidget.getColumns();

        List<Map<String, String>> originalData = tabWidget.getData();
        List<Map<String, String>> data = new ArrayList<>(originalData);

        List<Map<String, String>> tempData = tabWidget.getData();
        if (data == null || data.isEmpty()) {
            PdfPTable table = new PdfPTable(columns.size());
            PdfPCell cell;
            cell = new PdfPCell(new Phrase(tabWidget.getWidgetTitle()));
            cell.setHorizontalAlignment(1);
            cell.setColspan(columns.size());
            table.addCell(cell);
            table.setWidthPercentage(95f);
            for (Iterator<WidgetColumn> iterator = columns.iterator(); iterator.hasNext();) {
                WidgetColumn column = iterator.next();
                PdfPCell dataCell = new PdfPCell(new Phrase(column.getFieldName()));
                dataCell.setBackgroundColor(BaseColor.GRAY);
                table.addCell(dataCell);
            }
            return table;
        }
        if (tabWidget.getZeroSuppression()) {
            for (Iterator<Map<String, String>> iterator = data.iterator(); iterator.hasNext();) {
                Map<String, String> dataMap = iterator.next();
                if (!isZeroRow(dataMap, columns)) {
                    tempData.add(dataMap);
                }
            }
            data = tempData;
        }
        List<SortType> sortFields = new ArrayList<>();
        List<Aggregation> aggreagtionList = new ArrayList<>();
        List<String> groupByFields = new ArrayList<>();

        for (Iterator<WidgetColumn> iterator = columns.iterator(); iterator.hasNext();) {
            WidgetColumn column = iterator.next();
            if (column.getSortOrder() != null) {
                sortFields.add(new SortType(column.getFieldName(), column.getSortOrder(), column.getFieldType()));
            }
            if (column.getAgregationFunction() != null) {
                aggreagtionList.add(new Aggregation(column.getFieldName(), column.getAgregationFunction()));
            }
            if (column.getGroupPriority() != null) {
                groupByFields.add(column.getFieldName());
            }
        }
        if (sortFields.size() > 0) {
            data = sortData(data, sortFields);
        }
        if (tabWidget.getMaxRecord() != null && tabWidget.getMaxRecord() > 0) {
            data = data.subList(0, tabWidget.getMaxRecord());
        }
        Map groupedMapData = new HashMap();

        System.out.println("Group by Fields --> " + groupByFields.size());
        System.out.println(groupByFields);
        List<String> originalGroupByFields = new ArrayList<>(groupByFields);
        if (groupByFields.size() > 0) {
            List groupedData = groupData(data, groupByFields, aggreagtionList);

            groupedMapData.putAll(aggregateData(data, aggreagtionList));
            groupedMapData.put("_groupFields", originalGroupByFields);
            groupedMapData.put("data", groupedData);
        } else {
            groupedMapData.putAll(aggregateData(data, aggreagtionList));
            groupedMapData.put("data", data);
        }
        System.out.println("Grouped Data");
        System.out.println(groupedMapData.get("_groupFields"));

        return generateTable(groupedMapData, tabWidget);

    }

    private void generateGroupedRows(Map groupedData, TabWidget tabWidget, PdfPTable table) {
        List<WidgetColumn> columns = tabWidget.getColumns();
        List data = (List) groupedData.get("data");
        for (Iterator iterator = data.iterator(); iterator.hasNext();) {
            Map mapData = (Map) iterator.next();
            for (Iterator<WidgetColumn> iterator1 = columns.iterator(); iterator1.hasNext();) {
                WidgetColumn column = iterator1.next();
                table.addCell(mapData.get(column.getFieldName()) + "");
            }

            if (mapData.get("data") != null) {
                generateGroupedRows(mapData, tabWidget, table);
            }
        }
    }

    private PdfPTable generateTable(Map groupedData, TabWidget tabWidget) {

        List<WidgetColumn> columns = tabWidget.getColumns();
        List<Map<String, String>> data = tabWidget.getData();
        List<String> groupFields = (List< String>) groupedData.get("_groupFields");
        System.out.println("GROUP FIELDS");
        System.out.println(groupFields);

        Integer noOfColumns = columns.size();
        PdfPTable table = new PdfPTable(noOfColumns);
        PdfPCell cell;
        cell = new PdfPCell(new Phrase(tabWidget.getWidgetTitle()));
        cell.setHorizontalAlignment(1);
        cell.setColspan(noOfColumns);
        table.addCell(cell);
        table.setWidthPercentage(95f);
        for (Iterator<WidgetColumn> iterator = columns.iterator(); iterator.hasNext();) {
            WidgetColumn column = iterator.next();
            PdfPCell dataCell = new PdfPCell(new Phrase(column.getFieldName()));
            dataCell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(dataCell);
        }
        if (groupFields == null || groupFields.isEmpty()) {
            for (Iterator<Map<String, String>> iterator = data.iterator(); iterator.hasNext();) {
                Map<String, String> dataMap = iterator.next();
                for (Iterator<WidgetColumn> iterator1 = columns.iterator(); iterator1.hasNext();) {
                    WidgetColumn column = iterator1.next();
                    PdfPCell dataCell;
                    dataCell = new PdfPCell(new Phrase(dataMap.get(column.getFieldName())));
                    // dataCell.setBackgroundColor(BaseColor.GRAY);
                    table.addCell(dataCell);
                }
            }
        } else {
            generateGroupedRows(groupedData, tabWidget, table);
        }

        if (tabWidget.getTableFooter()) {
            for (Iterator<WidgetColumn> iterator = columns.iterator(); iterator.hasNext();) {
                WidgetColumn column = iterator.next();
                PdfPCell dataCell = new PdfPCell(new Phrase((String) groupedData.get(column.getFieldName())));
                dataCell.setBackgroundColor(BaseColor.GRAY);
                table.addCell(dataCell);
            }
        }

        return table;
    }

    public void dynamicPdfTable(List<TabWidget> tabWidgets, OutputStream out) {
        try {
            PdfWriter writer = null;
            Document document = new Document(PageSize.A4, 36, 36, 72, 72);
            writer = PdfWriter.getInstance(document, out);
            document.open();
            HeaderFooterTable event = new HeaderFooterTable();
            writer.setPageEvent(event);
            PageNumeration pevent = new PageNumeration();
            writer.setPageEvent(pevent);
            for (Iterator<TabWidget> iterator = tabWidgets.iterator(); iterator.hasNext();) {
                TabWidget tabWidget = iterator.next();
                if (tabWidget.getChartType().equalsIgnoreCase("table")) {
                    PdfPTable pdfTable = dynamicPdfTable(tabWidget);
                    document.add(pdfTable);
                } else if (tabWidget.getChartType().equalsIgnoreCase("pieChart")) {
                    document.add(generatePieChart(writer, tabWidget));
                }
                document.add(Chunk.NEWLINE);
                document.add(Chunk.NEWLINE);
            }
            document.close();
            out.flush();
            out.close();
        } catch (DocumentException ex) {
            Logger.getLogger(CustomReportDesigner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CustomReportDesigner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Image generateBarChart(PdfWriter writer, TabWidget tabWidget) throws BadElementException {
        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();

//        for (Iterator<FrequencyReportBean> iterator = frequencyData.iterator(); iterator.hasNext();) {
//            FrequencyReportBean frequencyReportBean = iterator.next();
//            dataSet.setValue(frequencyReportBean.getCount(), "No of Times", frequencyReportBean.getNoOfTimes());
//        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Number of times user visit", "Count", "Number Of Visits",
                dataSet, PlotOrientation.VERTICAL, false, true, false);
        chart.setBackgroundPaint(Color.white);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.white);
        final CategoryItemRenderer renderer = new CustomRenderer(
                new Paint[]{new Color(116, 196, 198), new Color(116, 196, 198),
                    new Color(116, 196, 198), new Color(116, 196, 198),
                    new Color(116, 196, 198)
                });
//        renderer.setLabelGenerator(new StandardCategoryLabelGenerator());
        renderer.setItemLabelsVisible(true);
        final ItemLabelPosition p = new ItemLabelPosition(
                ItemLabelAnchor.CENTER, TextAnchor.CENTER, TextAnchor.CENTER, 45.0
        );
        renderer.setPositiveItemLabelPosition(p);
        plot.setRenderer(renderer);

        plot.setDrawingSupplier(new ChartDrawingSupplier());

        PdfContentByte contentByte = writer.getDirectContent();

        PdfTemplate templatePie = contentByte.createTemplate(500, 300);
        Graphics2D graphics2dPie = templatePie.createGraphics(500, 300,
                new DefaultFontMapper());
        Rectangle2D rectangle2dPie = new Rectangle2D.Double(0, 0, 500,
                300);

        chart.draw(graphics2dPie, rectangle2dPie);

        graphics2dPie.dispose();

        // contentByte.addTemplate(templatePie, 30, 30);
        Image img = Image.getInstance(templatePie);
        return img;
    }

    public static Image generatePieChart(PdfWriter writer, TabWidget tabWidget) throws BadElementException {
        List<WidgetColumn> columns = tabWidget.getColumns();
        List<Map<String, String>> originaldata = tabWidget.getData();
        List<Map<String, String>> data = new ArrayList<>(originaldata);

        String xAxis = null;
        String yAxis = null;
        for (Iterator<WidgetColumn> iterator = columns.iterator(); iterator.hasNext();) {
            WidgetColumn column = iterator.next();
            if (column.getxAxis() != null) {
                xAxis = column.getFieldName();
            }
            if (column.getyAxis() != null) {
                yAxis = column.getFieldName();
            }
        }

        DefaultPieDataset dataSet = new DefaultPieDataset();
        List<String> legends = new ArrayList<>();

        for (Iterator<Map<String, String>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, String> dataMap = iterator.next();
            dataSet.setValue(dataMap.get(xAxis), ApiUtils.toDouble(dataMap.get(yAxis)));
            legends.add(dataMap.get(xAxis));
        }

        JFreeChart chart = ChartFactory.createPieChart(
                tabWidget.getWidgetTitle(), dataSet, true, false, false);

        Paint[] paintSequence = new Paint[]{
            new Color(116, 196, 198),
            new Color(34, 137, 149),
            new Color(90, 113, 122),
            new Color(61, 70, 77),
            new Color(241, 136, 60)
        };
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setDrawingSupplier(new ChartDrawingSupplier());
        plot.setBackgroundPaint(Color.white);
        int i = 0;
        for (Iterator<String> iterator = legends.iterator(); iterator.hasNext();) {
            if (i > 4) {
                i = 2;
            }
            String legend = iterator.next();
            plot.setSectionPaint(legend, paintSequence[i++]);
        }

        PdfContentByte contentByte = writer.getDirectContent();
        PdfTemplate templateBar = contentByte.createTemplate(500, 300);
        Graphics2D graphics2dBar = templateBar.createGraphics(500, 300,
                new DefaultFontMapper());
        Rectangle2D rectangle2dBar = new Rectangle2D.Double(0, 0, 500,
                300);

        chart.draw(graphics2dBar, rectangle2dBar);

        graphics2dBar.dispose();
        //contentByte.addTemplate(templateBar, 30, 30);
        Image img = Image.getInstance(templateBar);
        return img;
    }

    public static class CustomRenderer extends BarRenderer {

        /**
         * The colors.
         */
        private Paint[] colors;

        /**
         * Creates a new renderer.
         *
         * @param colors the colors.
         */
        public CustomRenderer(final Paint[] colors) {
            this.colors = colors;
        }

        /**
         * Returns the paint for an item. Overrides the default behaviour
         * inherited from AbstractSeriesRenderer.
         *
         * @param row the series.
         * @param column the category.
         *
         * @return The item color.
         */
        public Paint getItemPaint(final int row, final int column) {
            return this.colors[column % this.colors.length];
        }
    }

    public static class ChartDrawingSupplier extends DefaultDrawingSupplier {

        public Paint[] paintSequence;
        public int paintIndex;
        public int fillPaintIndex;

        {
            paintSequence = new Paint[]{
                new Color(227, 26, 28),
                new Color(000, 102, 204),
                new Color(102, 051, 153),
                new Color(102, 51, 0),
                new Color(156, 136, 48),
                new Color(153, 204, 102),
                new Color(153, 51, 51),
                new Color(102, 51, 0),
                new Color(204, 153, 51),
                new Color(0, 51, 0)};
        }

        @Override
        public Paint getNextPaint() {
            Paint result
                    = paintSequence[paintIndex % paintSequence.length];
            paintIndex++;
            return result;
        }

        @Override
        public Paint getNextFillPaint() {
            Paint result
                    = paintSequence[fillPaintIndex % paintSequence.length];
            fillPaintIndex++;
            return result;
        }
    }

    public static class CalcualtedFunction {

        private String name;
        private String field1;
        private String field2;

        public CalcualtedFunction(String name, String field1, String field2) {
            this.name = name;
            this.field1 = field1;
            this.field2 = field2;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getField1() {
            return field1;
        }

        public void setField1(String field1) {
            this.field1 = field1;
        }

        public String getField2() {
            return field2;
        }

        public void setField2(String field2) {
            this.field2 = field2;
        }
    }

    public class Aggregation {

        private String fieldName;
        private String aggregationType;

        public Aggregation(String fieldName, String aggregationType) {
            this.fieldName = fieldName;
            this.aggregationType = aggregationType;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getAggregationType() {
            return aggregationType;
        }

        public void setAggregationType(String aggregationType) {
            this.aggregationType = aggregationType;
        }

    }

    public class SortType {

        private String fieldName;
        private String sortOrder;
        private String fieldType;

        public SortType(String fieldName, String sortOrder, String fieldType) {
            this.fieldName = fieldName;
            this.sortOrder = sortOrder;
            this.fieldType = fieldType;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getSortOrder() {
            return sortOrder;
        }

        public void setSortOrder(String sortOrder) {
            this.sortOrder = sortOrder;
        }

        public String getFieldType() {
            return fieldType;
        }

        public void setFieldType(String fieldType) {
            this.fieldType = fieldType;
        }
    }

    public static class PageNumeration extends PdfPageEventHelper {

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            //            ColumnText ct = new ColumnText(writer.getDirectContent());
//            ct.setSimpleColumn(new Rectangle(36, 832, 559, 810));
//            for (Element e : header) {
//                ct.addElement(e);
//            }
            PdfPTable table = new PdfPTable(1);

            table.setTotalWidth(523);
            PdfPCell cell = new PdfPCell(new Phrase("Page Number " + writer.getPageNumber()));
            //cell.setBackgroundColor(BaseColor.ORANGE);
            table.addCell(cell);
            //cell = new PdfPCell(new Phrase("This is a copyright notice"));
            //cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //table.addCell(cell);
            table.writeSelectedRows(0, -1, 36, 64, writer.getDirectContent());

        }
    }

    public static class HeaderFooterTable extends PdfPageEventHelper {

        protected PdfPTable footer;

        public HeaderFooterTable() {

        }

        public HeaderFooterTable(PdfPTable footer) {
            this.footer = footer;
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                //            ColumnText ct = new ColumnText(writer.getDirectContent());
//            ct.setSimpleColumn(new Rectangle(36, 832, 559, 810));
//            for (Element e : header) {
//                ct.addElement(e);
//            }
                System.out.println("LOCATION PATH " + getClass().getProtectionDomain().getCodeSource().getLocation());
                Rectangle rectangle = new Rectangle(10, 900, 100, 850);
                Image img = Image.getInstance(CustomReportDesigner.class.getResource("") + "/../images/l2tmedia-logo.png");
                img.scaleToFit(100, 100);
                img.setAbsolutePosition((rectangle.getLeft() + rectangle.getRight()) / 2 - 45, rectangle.getTop() - 50);
                img.setAlignment(Element.ALIGN_TOP);
                writer.getDirectContent().addImage(img);
                if (footer != null) {
                    footer.writeSelectedRows(0, -1, 36, 64, writer.getDirectContent());
                }
            } catch (BadElementException ex) {
                Logger.getLogger(CustomReportDesigner.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(CustomReportDesigner.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DocumentException ex) {
                Logger.getLogger(CustomReportDesigner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
