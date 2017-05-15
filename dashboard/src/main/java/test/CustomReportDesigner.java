/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.visumbu.vb.model.TabWidget;
import com.visumbu.vb.model.WidgetColumn;
import com.visumbu.vb.pdf.L2TReportHeader;
import com.visumbu.vb.pdf.ReportHeader;
import com.visumbu.vb.utils.ApiUtils;
import com.visumbu.vb.utils.Formatter;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import org.apache.commons.lang.WordUtils;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.ui.TextAnchor;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;

import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.renderer.category.AreaRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import java.util.Iterator;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
public class CustomReportDesigner {

    private static List<CalcualtedFunction> calcualtedFunctions = new ArrayList<>();
//    private static BaseColor widgetTitleColor = new BaseColor(90, 113, 122, 28);
//    private static BaseColor tableHeaderColor = new BaseColor(255, 0, 0);
//    private static BaseColor tableFooterColor = new BaseColor(241, 241, 241);
//    private static BaseColor widgetBorderColor = BaseColor.DARK_GRAY;

    private static BaseColor widgetTitleColor = new BaseColor(231, 231, 231);
    private static BaseColor tableTitleColor = new BaseColor(209, 215, 218);
    private static BaseColor tableHeaderColor = new BaseColor(241, 241, 241);
    private static BaseColor tableFooterColor = new BaseColor(241, 241, 241);
    private static BaseColor widgetBorderColor = new BaseColor(204, 204, 204);

    private static final String FONT = CustomReportDesigner.class.getResource("") + "../../../static/lib/fonts/proxima/proximanova-reg-webfont.woff"; // "E:\\work\\vizboard\\dashboard\\src\\main\\webapp\\static\\lib\\fonts\\proxima\\proximanova-reg-webfont.woff";
    private static final Rectangle pageSize = PageSize.A2;
    private static final float widgetWidth = pageSize.getWidth() - 100;
    private static final float widgetHeight = 300;
    private static final ReportHeader reportHeader = new L2TReportHeader();

    final static Logger log = Logger.getLogger(CustomReportDesigner.class);

    static {
        FontFactory.register(FONT, "proxima_nova_rgregular");
        calcualtedFunctions.add(new CalcualtedFunction("ctr", "clicks", "impressions"));
        calcualtedFunctions.add(new CalcualtedFunction("cpa", "cost", "conversions"));
        calcualtedFunctions.add(new CalcualtedFunction("cpc", "cost", "clicks"));
        calcualtedFunctions.add(new CalcualtedFunction("cps", "spend", "clicks"));
        calcualtedFunctions.add(new CalcualtedFunction("cpagee", "spend", "actions_page_engagement"));
        calcualtedFunctions.add(new CalcualtedFunction("cplc", "spend", "actions_link_click"));
        calcualtedFunctions.add(new CalcualtedFunction("cpr", "spend", "actions_post_reaction"));
        calcualtedFunctions.add(new CalcualtedFunction("cpcomment", "spend", "actions_comment"));
        calcualtedFunctions.add(new CalcualtedFunction("cposte", "spend", "actions_post_engagement"));
        calcualtedFunctions.add(new CalcualtedFunction("cpp", "spend", "actions_post"));
        calcualtedFunctions.add(new CalcualtedFunction("ctl", "spend", "actions_like"));
    }
    Font pdfFont = FontFactory.getFont("proxima_nova_rgregular", "Cp1253", true);
    Font pdfFontTitle = FontFactory.getFont("proxima_nova_rgregular", "Cp1253", true);
    Font pdfFontHeader = FontFactory.getFont("proxima_nova_rgregular", "Cp1253", true);

    private Boolean isZeroRow(Map<String, Object> mapData, List<WidgetColumn> columns) {
        log.debug("Calling isZeroRow function with return type Boolean with parameters mapData " + mapData + " and columns " + columns);
        log.debug("MapData ---> " + mapData);
        for (Iterator<WidgetColumn> iterator = columns.iterator(); iterator.hasNext();) {
            WidgetColumn column = iterator.next();
            if (ApiUtils.toDouble(mapData.get(column.getFieldName()) + "") != 0) {
                return false;
            }
        }
        return true;
    }

    private Double sum(List<Map<String, Object>> data, String fieldName) {
        log.debug("Calling sum function with return type Boolean with parameters data " + data + " and fieldName " + fieldName);
        Double sum = 0.0;
        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> mapData = iterator.next();
            log.debug(fieldName + " " + mapData.get(fieldName));
            sum += ApiUtils.toDouble(mapData.get(fieldName) + "");
        }
        return sum;
    }

    private Double min(List<Map<String, Object>> data, String fieldName) {
        log.debug("Calling min function with return type Boolean with parameters data " + data + " and fieldName " + fieldName);
        Double min = null;
        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> mapData = iterator.next();
            if (min == null || ApiUtils.toDouble(mapData.get(fieldName) + "") < min) {
                min = ApiUtils.toDouble(mapData.get(fieldName) + "");
            }
        }
        return min;
    }

    private Double calulatedMetric(List<Map<String, Object>> data, CalcualtedFunction calcualtedFunction) {
        log.debug("Calling calulatedMetric function with return type Boolean with parameters data " + data + " and calculatedFunction " + calcualtedFunction);
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

    private Double max(List<Map<String, Object>> data, String fieldName) {
        log.debug("Calling max function with return type Boolean with parameters data " + data + " and fieldName " + fieldName);
        Double max = null;
        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> mapData = iterator.next();
            if (max == null || ApiUtils.toDouble(mapData.get(fieldName) + "") > max) {
                max = ApiUtils.toDouble(mapData.get(fieldName) + "");
            }
        }
        return max;
    }

    private String format(WidgetColumn column, String value) {
        return value;
    }

    enum DAY {
        Monday("Monday", 2), Tuesday("Tuesday", 3), Wednesday("Wednesday", 4), Thursday("Thursday", 5), Friday("Friday", 6), Saturday("Saturday", 7), Sunday("Sunday", 1);
        private String m_name;
        private int m_weight;

        DAY(String name, int weight) {
            m_name = name;
            m_weight = weight;
        }

        public int getWeight() {
            return this.m_weight;
        }
    }

    DateFormat primaryFormat = new SimpleDateFormat("h:mm a");
    DateFormat secondaryFormat = new SimpleDateFormat("H:mm");

    public int timeInMillis(String time) throws ParseException {
        log.debug("Calling timeInMillis function with return type Boolean with parameter time " + time);
        log.debug("time: " + time);
        return timeInMillis(time, primaryFormat);
    }

    private int timeInMillis(String time, DateFormat format) throws ParseException {
        // you may need more advanced logic here when parsing the time if some times have am/pm and others don't.
        try {
            Date date = format.parse(time);
            log.debug("Date: " + date);
            return (int) date.getTime();
        } catch (ParseException e) {
            if (format != secondaryFormat) {
                return timeInMillis(time, secondaryFormat);
            } else {
                throw e;
            }
        }
    }

    private List<Map<String, Object>> sortData(List<Map<String, Object>> data, List<SortType> sortType) {
        log.debug("Calling sortData function with return type List with parameters data " + data + " and sortType " + sortType);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        Collections.sort(data, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                for (Iterator<SortType> iterator = sortType.iterator(); iterator.hasNext();) {
                    SortType sortType1 = iterator.next();
                    int order = 1;
                    log.debug("sort Order: " + sortType1.getSortOrder());
                    log.debug("sort type: " + sortType1.getFieldType());
                    String day1 = o1.get(sortType1.getFieldName()) + "";
                    String day2 = o2.get(sortType1.getFieldName()) + "";
                    log.debug("day1: " + day1);
                    log.debug("day2: " + day2);
                    log.debug("day1 length: " + day1.length());

                    if (day1.length() == 1 && day2.length() == 1 && sortType1.getSortOrder().equalsIgnoreCase("asc")) {
                        log.debug("numbers --- >");
                        if (day1.compareTo(day2) != 0) {
                            return order * day1.compareTo(day2);
                        } else {
                            continue;
                        }
                    }

                    if (sortType1.getFieldType() == null || sortType1.getFieldType().isEmpty()) {

                        if (day1.length() >= 6) {
                            log.debug("Days ------>");
                            if (day1.substring(day1.length() - 3, day1.length()).equalsIgnoreCase("day") && day2.substring(day2.length() - 3, day2.length()).equalsIgnoreCase("day")) {
                                DAY dayOne = DAY.valueOf(day1);
                                log.debug("dayOne: " + dayOne);
                                DAY dayTwo = DAY.valueOf(day2);
                                log.debug("dayTwo: " + dayTwo);
                                return dayOne.getWeight() - dayTwo.getWeight();
                            } else {
                                continue;
                            }
                        }
                        if (day1.length() == 4 || day1.length() == 5) {
                            log.debug("Time ------>");
                            if ((day1.substring(day1.length() - 2, day1.length()).equalsIgnoreCase("pm") || day1.substring(day1.length() - 2, day1.length()).equalsIgnoreCase("am")) && (day2.substring(day2.length() - 2, day2.length()).equalsIgnoreCase("pm") || day2.substring(day2.length() - 2, day2.length()).equalsIgnoreCase("am"))) {
                                try {
                                    StringBuilder time1, time2;
                                    if (day1.length() == 5 && day2.length() == 5) {
                                        log.debug("if ---> 1");
                                        time1 = new StringBuilder(day1);
                                        time2 = new StringBuilder(day2);
                                        if (time1.substring(0, 2).equalsIgnoreCase("10")) {
                                            time1.replace(1, 2, "0:00");
                                        } else if (time1.substring(0, 2).equalsIgnoreCase("11")) {
                                            time1.replace(1, 2, "1:00");
                                        } else {
                                            time1.replace(1, 2, ":00");
                                        }
                                        if (time2.substring(0, 2).equalsIgnoreCase("10")) {
                                            time2.replace(1, 2, "0:00");
                                        } else if (time2.substring(0, 2).equalsIgnoreCase("11")) {
                                            time2.replace(1, 2, "1:00");
                                        } else {
                                            time2.replace(1, 2, ":00");
                                        }
                                        return timeInMillis(time1.toString()) - timeInMillis(time2.toString());
                                    } else if (day1.length() == 5 && day2.length() == 4) {
                                        log.debug("if ---> 2");
                                        time1 = new StringBuilder(day1);
                                        time2 = new StringBuilder(day2);
                                        if (time1.substring(0, 2).equalsIgnoreCase("10")) {
                                            time1.replace(1, 2, "0:00");
                                        } else if (time1.substring(0, 2).equalsIgnoreCase("11")) {
                                            time1.replace(1, 2, "1:00");
                                        } else {
                                            time1.replace(1, 2, ":00");
                                        }
                                        time2.replace(1, 1, ":00");
                                        return timeInMillis(time1.toString()) - timeInMillis(time2.toString());
                                    } else if (day1.length() == 4 && day2.length() == 5) {
                                        log.debug("if ---> 3");
                                        time1 = new StringBuilder(day1);
                                        time2 = new StringBuilder(day2);
                                        if (time2.substring(0, 2).equalsIgnoreCase("10")) {
                                            time2.replace(1, 2, "0:00");
                                        } else if (time2.substring(0, 2).equalsIgnoreCase("11")) {
                                            time2.replace(1, 2, "1:00");
                                        } else {
                                            time2.replace(1, 2, ":00");
                                        }
                                        time1.replace(1, 1, ":00");
                                        return timeInMillis(time1.toString()) - timeInMillis(time2.toString());
                                    } else if (day1.length() == 4 && day2.length() == 4) {
                                        log.debug("if ---> 4");
                                        time1 = new StringBuilder(day1);
                                        time2 = new StringBuilder(day2);
                                        time1.replace(1, 1, ":00");
                                        time2.replace(1, 1, ":00");
                                        return timeInMillis(time1.toString()) - timeInMillis(time2.toString());
                                    } else {
                                        continue;
                                    }
                                } catch (ParseException ex) {
                                    log.error("Error in converting StringBuilder to String " + ex);
                                }
                            } else {
                                continue;
                            }
                        }
                        continue;
                    }

                    if (sortType1.getSortOrder().equalsIgnoreCase("desc")) {
                        order = -1;
                    }

                    if (sortType1.getFieldType().equalsIgnoreCase("date")) {
                        try {
                            Date date1 = sdf.parse(o1.get(sortType1.getFieldName()).toString());
                            Date date2 = sdf.parse(o2.get(sortType1.getFieldName()).toString());
                            return date1.compareTo(date2);
                        } catch (ParseException ex) {
                            log.error("Error in converting String " + o1.get(sortType1.getFieldName()).toString() + " to date" + ex);
                        }
                    }

                    if (sortType1.getFieldType().equalsIgnoreCase("number")) {
                        Double value1 = ApiUtils.toDouble(o1.get(sortType1.getFieldName()) + "");
                        Double value2 = ApiUtils.toDouble(o2.get(sortType1.getFieldName()) + "");
                        if (value1 != value2) {
                            return order * new Double(value1 - value2).intValue();
                        }
                    } else {
                        String value1 = o1.get(sortType1.getFieldName()) + "";
                        String value2 = o2.get(sortType1.getFieldName()) + "";
                        if (value1.compareTo(value2) != 0) {
                            return order * value1.compareTo(value2);
                        }
                    }
                }
                return 0;
            }
        });
        return data;
    }

    private Map<String, List<Map<String, Object>>> groupBy(List<Map<String, Object>> data, String groupField) {
        log.debug("Calling groupBy with return type Map with parameters data " + data + " and groupField " + groupField);
        Map<String, List<Map<String, Object>>> returnMap = new HashMap<>();
        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> dataMap = iterator.next();
            String fieldValue = dataMap.get(groupField) + "";
            List<Map<String, Object>> groupDataList = returnMap.get(fieldValue);

            if (groupDataList == null) {
                groupDataList = new ArrayList<>();
            }
            groupDataList.add(dataMap);
            returnMap.put(fieldValue, groupDataList);
        }
        return returnMap;
    }

    private List groupData(List<Map<String, Object>> data, List<String> groupByFields, List<Aggregation> aggreagtionList) {
        log.debug("Calling groupData function with return type List with parameters data " + data + " groupByFields " + groupByFields + " and aggreagtionList " + aggreagtionList);
        List<String> currentFields = groupByFields;
        if (groupByFields.size() == 0) {
            return data;
        }
        List<Map<String, Object>> actualList = data;
        List<Map<String, Object>> groupedData = new ArrayList<>();
        String groupingField = currentFields.get(0);
        Map<String, List<Map<String, Object>>> currentListGrouped = groupBy(actualList, groupingField);
        //groupByFields.remove(0);
        for (Map.Entry<String, List<Map<String, Object>>> entrySet : currentListGrouped.entrySet()) {
            String key = entrySet.getKey();
            List<Map<String, Object>> value = entrySet.getValue();
            Map dataToPush = new HashMap<>();
            dataToPush.put("_key", key);
            dataToPush.put(groupingField, key);
            dataToPush.put("_groupField", groupingField);
            // Merge aggregation
            dataToPush.putAll(aggregateData(value, aggreagtionList));
            dataToPush.put("data", groupData(value, groupByFields.subList(1, groupByFields.size()), aggreagtionList));
            log.debug("Group Data -------->");
            log.debug(dataToPush);
            groupedData.add(dataToPush);
        }
        return groupedData;
    }

    private Map<String, Object> aggregateData(List<Map<String, Object>> data, List<Aggregation> aggreagtionList) {
        log.debug("Calling aggregateData function with return type Map with parameters data " + data + " and aggreagtionList " + aggreagtionList);
        Map<String, Object> returnMap = new HashMap<>();
        for (Iterator<Aggregation> iterator = aggreagtionList.iterator(); iterator.hasNext();) {
            Aggregation aggregation = iterator.next();
            log.debug("FieldName: " + aggregation.getFieldName());

            if (aggregation.getAggregationType().equalsIgnoreCase("sum")) {
                log.debug(aggregation.getFieldName() + " " + data);
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
        log.debug("Calling dynamicPdfTable function with return type PdfPtable with parameter tabWidget " + tabWidget);
//        BaseColor textHighlightColor = new BaseColor(242, 156, 33);
        BaseColor tableTitleFontColor = new BaseColor(61, 70, 77);

        List<WidgetColumn> columns = tabWidget.getColumns();

        List<Map<String, Object>> originalData = tabWidget.getData();
        List<Map<String, Object>> data;
        if (originalData == null || originalData.isEmpty()) {
            data = new ArrayList<>();
        } else {
            data = new ArrayList<>(originalData);
        }

        List<Map<String, Object>> tempData = new ArrayList<>();
        if (data == null || data.isEmpty()) {
            PdfPTable table = new PdfPTable(columns.size());
            PdfPCell cell;
            pdfFontTitle.setSize(14);
            pdfFontTitle.setStyle(Font.BOLD);
            pdfFontTitle.setColor(tableTitleFontColor);
            cell = new PdfPCell(new Phrase(tabWidget.getWidgetTitle(), pdfFontTitle));
            cell.setHorizontalAlignment(1);
            cell.setColspan(columns.size());
            cell.setBorderColor(widgetBorderColor);
            cell.setBackgroundColor(tableTitleColor);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            cell.setPadding(10);
            table.addCell(cell);
            table.setWidthPercentage(95f);
            for (Iterator<WidgetColumn> iterator = columns.iterator(); iterator.hasNext();) {
                WidgetColumn column = iterator.next();
                pdfFontHeader.setSize(13);
                pdfFontHeader.setColor(tableTitleFontColor);

                PdfPCell dataCell = new PdfPCell(new Phrase(WordUtils.capitalize(column.getFieldName()), pdfFontHeader));
                dataCell.setPadding(5);
                dataCell.setBorderColor(widgetBorderColor);
                dataCell.setBackgroundColor(tableHeaderColor);
                if (column.getAlignment() != null) {
                    dataCell.setHorizontalAlignment(column.getAlignment().equalsIgnoreCase("right") ? PdfPCell.ALIGN_RIGHT : column.getAlignment().equalsIgnoreCase("center") ? PdfPCell.ALIGN_CENTER : PdfPCell.ALIGN_LEFT);
                }
                table.addCell(dataCell);
            }
            return table;
        }
        // log.debug(tabWidget.getWidgetTitle() + " Grouped Data Size****5 " + data.size());

        if (tabWidget.getZeroSuppression() != null && tabWidget.getZeroSuppression()) {
            for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
                Map<String, Object> dataMap = iterator.next();
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
        log.debug("Group By Fields -----> ");
        log.debug(groupByFields);
        if (tabWidget.getMaxRecord() != null && tabWidget.getMaxRecord() > 0) {
            log.debug(tabWidget.getMaxRecord());
            data = data.subList(0, tabWidget.getMaxRecord());
        }
        Map groupedMapData = new HashMap();

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
        log.debug("Grouped Data ---->");
        log.debug(groupedMapData);
        return generateTable(groupedMapData, tabWidget);
    }

    private void generateGroupedRows(Map groupedData, TabWidget tabWidget, PdfPTable table) {
        log.debug("Calling generateGroupedRows function with parameters tabWidget " + tabWidget + " groupedData " + groupedData + " and table " + table);
        BaseColor tableTitleFontColor = new BaseColor(61, 70, 77);
        List<WidgetColumn> columns = tabWidget.getColumns();
        List data = (List) groupedData.get("data");
        log.debug("List Data : " + data);
        log.debug("List Data size: " + data.size());
        log.debug("Column size: " + columns.size());

        PdfPCell dataCell;
        String groupValue = null;
        int count = 0;
        for (Iterator iterator = data.iterator(); iterator.hasNext();) {
            log.debug("for Column 1 ---->");
            Map mapData = (Map) iterator.next();
            log.debug("Map Data : " + mapData);
            if (mapData.get(mapData.get("_groupField")) != null) {
                count = 1;
                groupValue = mapData.get(mapData.get("_groupField")) + "";
                pdfFont.setColor(tableTitleFontColor);
                dataCell = new PdfPCell(new Phrase(groupValue, pdfFont));
                log.debug("Get GroupField Data Name ---> " + groupValue);
                dataCell.setBorderColor(widgetBorderColor);
                table.addCell(dataCell);
            } else {
                if (data.size() > 1) {
                    count = 2;
                    dataCell = new PdfPCell(new Phrase(""));
                    log.debug("Get GroupField Else Data Name ---> ");
                    dataCell.setBorderColor(widgetBorderColor);
                    table.addCell(dataCell);
                }
            }
            if (count == 1 || count == 2) {
                for (Iterator<WidgetColumn> iterator1 = columns.iterator(); iterator1.hasNext();) {
                    log.debug("for widget Column 1 ---->");
                    WidgetColumn column = iterator1.next();
                    if (column.getColumnHide() == null || column.getColumnHide() == 0) {
                        if (mapData.get(column.getFieldName()) != null) {
                            String value = mapData.get(column.getFieldName()) + "";
                            if (column.getDisplayFormat() != null) {
                                value = Formatter.format(column.getDisplayFormat(), value);
                            }
                            pdfFont.setColor(tableTitleFontColor);
                            log.debug("Get GroupField Widget Column Name ---> " + value);
                            dataCell = new PdfPCell(new Phrase(value, pdfFont));
                            if (column.getAlignment() != null) {
                                dataCell.setHorizontalAlignment(column.getAlignment().equalsIgnoreCase("right") ? PdfPCell.ALIGN_RIGHT : column.getAlignment().equalsIgnoreCase("center") ? PdfPCell.ALIGN_CENTER : PdfPCell.ALIGN_LEFT);
                            }
                            dataCell.setBorderColor(widgetBorderColor);
                            table.addCell(dataCell);
                        } else {
                            dataCell = new PdfPCell(new Phrase(" "));
                            log.debug("Get GroupField Else Widget Column Name -------> ");
                            dataCell.setBorderColor(widgetBorderColor);
                            table.addCell(dataCell);
                        }
                    }
                }
            }
            if (mapData.get("data") != null) {
                log.debug("if data ---->");
                generateGroupedRows(mapData, tabWidget, table);
            }
        }
    }

    private Integer countColumns(List<WidgetColumn> columns) {
        log.debug("Calling countColumns function with return type Integer with parameters columns " + columns);
        Integer count = 0;
        for (Iterator<WidgetColumn> iterator = columns.iterator(); iterator.hasNext();) {
            WidgetColumn column = iterator.next();
            if (column.getColumnHide() == null || column.getColumnHide() == 0) {
                count++;
            }
        }
        return count;
    }

    private PdfPTable generateTable(Map groupedData, TabWidget tabWidget) {
        log.debug("Calling generateTable function with return type PdfPTable with parameter groupData " + groupedData + " and tabWiget " + tabWidget);
        BaseColor tableTitleFontColor = new BaseColor(61, 70, 77);

        List<WidgetColumn> columns = tabWidget.getColumns();
        List<Map<String, Object>> data = tabWidget.getData();
        List<String> groupFields = (List< String>) groupedData.get("_groupFields");
        Integer noOfColumns = countColumns(columns); //.size();
        if (groupFields != null && groupFields.size() > 0) {
            noOfColumns++;
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
//            if (column.getGroupPriority() != null) {
//                groupByFields.add(column.getFieldName());
//            }
        }
        if (sortFields.size() > 0) {
            data = sortData(data, sortFields);
        }

        if (tabWidget.getMaxRecord() != null && tabWidget.getMaxRecord() > 0) {
            data = data.subList(0, tabWidget.getMaxRecord());
        }

        PdfPTable table = new PdfPTable(noOfColumns);
        PdfPCell cell;
        pdfFontTitle.setSize(14);
        pdfFontTitle.setStyle(Font.BOLD);
        pdfFontTitle.setColor(tableTitleFontColor);
        cell = new PdfPCell(new Phrase(tabWidget.getWidgetTitle(), pdfFontTitle));
        cell.setFixedHeight(30);
        cell.setBorderColor(widgetBorderColor);
        cell.setBackgroundColor(tableTitleColor);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(noOfColumns);
        cell.setPaddingTop(5);
        cell.setPaddingLeft(10);
        table.addCell(cell);
        table.setWidthPercentage(95f);
        if (groupFields != null && groupFields.size() > 0) {
            pdfFontHeader.setSize(13);
            pdfFontHeader.setColor(tableTitleFontColor);
            PdfPCell dataCell = new PdfPCell(new Phrase("Group", pdfFontHeader));
            dataCell.setPadding(5);
            dataCell.setBorderColor(widgetBorderColor);
            dataCell.setBackgroundColor(tableHeaderColor);
            table.addCell(dataCell);
        }
        for (Iterator<WidgetColumn> iterator = columns.iterator(); iterator.hasNext();) {
            WidgetColumn column = iterator.next();
            if (column.getColumnHide() == null || column.getColumnHide() == 0) {
                PdfPCell dataCell = new PdfPCell(new Phrase(column.getDisplayName(), pdfFontHeader));
                log.debug("Get Display Name ---> " + column.getDisplayName());
                dataCell.setPadding(5);
                dataCell.setBorderColor(widgetBorderColor);
                dataCell.setBackgroundColor(tableHeaderColor);
                if (column.getAlignment() != null) {
                    dataCell.setHorizontalAlignment(column.getAlignment().equalsIgnoreCase("right") ? PdfPCell.ALIGN_RIGHT : column.getAlignment().equalsIgnoreCase("center") ? PdfPCell.ALIGN_CENTER : PdfPCell.ALIGN_LEFT);
                }
                table.addCell(dataCell);
            }
        }
        if (groupFields == null || groupFields.isEmpty()) {
            for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
                Map<String, Object> dataMap = iterator.next();
                for (Iterator<WidgetColumn> iterator1 = columns.iterator(); iterator1.hasNext();) {
                    WidgetColumn column = iterator1.next();
                    PdfPCell dataCell;
                    String value = dataMap.get(column.getFieldName()) + "";
                    if (column.getDisplayFormat() != null) {
                        value = Formatter.format(column.getDisplayFormat(), value);
                    }
                    pdfFont.setColor(tableTitleFontColor);
                    log.debug("Get Value ---> " + value);
                    value = value.replaceAll("<br/>", "");
                    dataCell = new PdfPCell(new Phrase(value, pdfFont));
                    if (column.getAlignment() != null) {
                        dataCell.setHorizontalAlignment(column.getAlignment().equalsIgnoreCase("right") ? PdfPCell.ALIGN_RIGHT : column.getAlignment().equalsIgnoreCase("center") ? PdfPCell.ALIGN_CENTER : PdfPCell.ALIGN_LEFT);
                    }
                    dataCell.setBorderColor(widgetBorderColor);
                    table.addCell(dataCell);
                }
            }
        } else {
            generateGroupedRows(groupedData, tabWidget, table);
        }

        if (tabWidget.getTableFooter() != null && tabWidget.getTableFooter()) {
            Boolean totalDisplayed = false;
            if (groupFields != null && groupFields.size() > 0) {
                pdfFont.setColor(tableTitleFontColor);
                PdfPCell dataCell = new PdfPCell(new Phrase("Total:", pdfFont));
                dataCell.setBorderColor(widgetBorderColor);
                dataCell.setBackgroundColor(tableFooterColor);
                table.addCell(dataCell);
                totalDisplayed = true;
            }
            for (Iterator<WidgetColumn> iterator = columns.iterator(); iterator.hasNext();) {
                WidgetColumn column = iterator.next();
                if (column.getColumnHide() == null || column.getColumnHide() == 0) {
                    if (totalDisplayed == false) {
                        pdfFont.setColor(tableTitleFontColor);
                        PdfPCell dataCell = new PdfPCell(new Phrase("Total:", pdfFont));
                        dataCell.setBorderColor(widgetBorderColor);
                        dataCell.setBackgroundColor(tableFooterColor);
                        table.addCell(dataCell);
                        totalDisplayed = true;
                    } else {
                        String value = (String) groupedData.get(column.getFieldName());
                        if (column.getDisplayFormat() != null) {
                            value = Formatter.format(column.getDisplayFormat(), value);
                        }
                        pdfFont.setColor(tableTitleFontColor);
                        log.debug("Get Total Value ---> " + value);

                        PdfPCell dataCell = new PdfPCell(new Phrase(value, pdfFont));
                        if (column.getAlignment() != null) {
                            dataCell.setHorizontalAlignment(column.getAlignment().equalsIgnoreCase("right") ? PdfPCell.ALIGN_RIGHT : column.getAlignment().equalsIgnoreCase("center") ? PdfPCell.ALIGN_CENTER : PdfPCell.ALIGN_LEFT);
                        }
                        dataCell.setBorderColor(widgetBorderColor);
                        dataCell.setBackgroundColor(tableFooterColor);
                        table.addCell(dataCell);
                    }
                }
            }
        }
        return table;
    }

    public void addReportHeader(String dealerName, Document document, TabWidget widget) {
        log.debug("Calling addReportHeader function with parameter dealerName " + dealerName + " document " + document + " and widget " + widget);
        try {
            // 236, 255, 224
            BaseColor backgroundColor = new BaseColor(244, 250, 245);
            BaseColor reportTitleColor = new BaseColor(241, 136, 60);
            BaseColor textHighlightColor = new BaseColor(242, 156, 33);
            BaseColor bobSmithBMWColor = new BaseColor(1, 67, 98);

            Integer headerCellCount = 4;
            Font f = new Font(pdfFont);
            Font pdfFontNormal = new Font(pdfFont);
            Font pdfFontNormalLight = new Font(pdfFont);
            Font pdfFontBold = new Font(pdfFont);
            Font pdfFontHighlight = new Font(pdfFont);
            Font pdfFontBoldSmall = new Font(pdfFont);
            Font pdfFontBoldLarge = new Font(pdfFont);

            pdfFontNormal.setColor(BaseColor.GRAY);
            pdfFontNormal.setSize(14);

            pdfFontNormalLight.setColor(BaseColor.LIGHT_GRAY);
            pdfFontNormalLight.setSize(14);

            pdfFontBold.setStyle(Font.BOLD);
            pdfFontBold.setColor(BaseColor.GRAY);
            pdfFontBold.setSize(14);
            pdfFontBoldSmall.setStyle(Font.BOLD);
            pdfFontBoldSmall.setColor(BaseColor.GRAY);
            pdfFontBoldSmall.setSize(12);
            pdfFontBoldLarge.setStyle(Font.BOLD);
            pdfFontBoldLarge.setColor(bobSmithBMWColor);
            pdfFontBoldLarge.setSize(16);

            pdfFontHighlight.setSize(14);
            pdfFontHighlight.setColor(textHighlightColor);

            f.setSize(20);
            f.setColor(reportTitleColor);
            f.setStyle(Font.BOLD);

            Paragraph reportTitle = new Paragraph("Month End Report".toUpperCase(), f);
            reportTitle.setAlignment(Paragraph.ALIGN_LEFT);
            //reportTitle.setFirstLineIndent(25);
            reportTitle.setIndentationLeft(25);

            LineSeparator dottedline = new LineSeparator();
            BaseColor dottedLineColor = new BaseColor(90, 113, 122);

            dottedline.setOffset(6);
            dottedline.setLineWidth(4);
            dottedline.setPercentage(95);
            dottedline.setLineColor(dottedLineColor);
            dottedline.setAlignment(Element.ALIGN_TOP);
//            reportTitle.add(dottedline);
            document.add(dottedline);
            document.add(reportTitle);
            document.add(new Phrase("\n"));

            PdfPTable table = new PdfPTable(headerCellCount);
            table.setWidths(new float[]{20, 20, 13, 1});
            table.setWidthPercentage(95f);

            Paragraph leftParagraph = new Paragraph("September 2016", pdfFontNormal);
            leftParagraph.add(new Phrase("\n"));
            leftParagraph.add(new Paragraph("Facebook Monthly Budget", pdfFontBold));
            leftParagraph.add(new Phrase("\n"));
            leftParagraph.add(new Paragraph("Budget ", pdfFontNormal));
            leftParagraph.add(new Paragraph("$1,500", pdfFontHighlight));
            leftParagraph.add(new Phrase("\n\n"));
            leftParagraph.add(new Paragraph(dealerName, pdfFontBoldLarge));

            Paragraph rightParagraph = new Paragraph("DIGITAL ADVISOR", pdfFontHighlight);
            rightParagraph.add(new Phrase("\n"));
            rightParagraph.add(new Paragraph("Zoe Suffety", pdfFontBoldSmall));
            rightParagraph.add(new Phrase("\n"));
            rightParagraph.add(new Paragraph("EMAIL: ", pdfFontNormalLight));
            rightParagraph.add(new Paragraph("zsuffety@l2tmedia.com", pdfFontBoldSmall));
            rightParagraph.add(new Phrase("\n"));
            rightParagraph.add(new Paragraph("PHONE: ", pdfFontNormalLight));
            rightParagraph.add(new Paragraph("847-901-8156", pdfFontBoldSmall));

            PdfPCell bottomCell = new PdfPCell(new Phrase("\n\n"));
            bottomCell.setBackgroundColor(backgroundColor);
            bottomCell.setColspan(headerCellCount);
            PdfPCell topCell = new PdfPCell(new Phrase(""));
            topCell.setBackgroundColor(backgroundColor);
            topCell.setColspan(headerCellCount);
            PdfPCell leftCell = new PdfPCell(leftParagraph);
            PdfPCell middleCell = new PdfPCell();
            PdfPCell rightCornorCell = new PdfPCell(new Phrase("\n"));
            rightCornorCell.setBackgroundColor(backgroundColor);

            PdfPCell rightCell = new PdfPCell(rightParagraph);
            rightCell.setBackgroundColor(BaseColor.WHITE);

            topCell.setBorder(PdfPCell.NO_BORDER);
            bottomCell.setBorder(PdfPCell.NO_BORDER);
            leftCell.setBorder(PdfPCell.NO_BORDER);
            leftCell.setBackgroundColor(backgroundColor);
            middleCell.setBorder(PdfPCell.NO_BORDER);
            middleCell.setBackgroundColor(backgroundColor);

            rightCornorCell.setBorder(PdfPCell.NO_BORDER);
            rightCell.setBorder(PdfPCell.NO_BORDER);

            topCell.setBorderColorBottom(backgroundColor);
            topCell.setBorderColorTop(backgroundColor);
            topCell.setBorderColorLeft(backgroundColor);
            topCell.setBorderColorRight(backgroundColor);

            leftCell.setBorderColorBottom(backgroundColor);
            leftCell.setBorderColorTop(backgroundColor);
            leftCell.setBorderColorLeft(backgroundColor);
            leftCell.setBorderColorRight(backgroundColor);

            middleCell.setBorderColorBottom(backgroundColor);
            middleCell.setBorderColorTop(backgroundColor);
            middleCell.setBorderColorLeft(backgroundColor);
            middleCell.setBorderColorRight(backgroundColor);

            rightCell.setBorderColorBottom(backgroundColor);
            rightCell.setBorderColorTop(backgroundColor);
            rightCell.setBorderColorLeft(backgroundColor);
            rightCell.setBorderColorRight(backgroundColor);

            rightCornorCell.setBorderColorBottom(backgroundColor);
            rightCornorCell.setBorderColorTop(backgroundColor);
            rightCornorCell.setBorderColorLeft(backgroundColor);
            rightCornorCell.setBorderColorRight(backgroundColor);

            bottomCell.setBorderColorBottom(backgroundColor);
            bottomCell.setBorderColorTop(backgroundColor);
            bottomCell.setBorderColorLeft(backgroundColor);
            bottomCell.setBorderColorRight(backgroundColor);

            leftCell.setPaddingLeft(10);
            rightCell.setPadding(10);
            topCell.setBorderWidthTop(5);
            table.addCell(topCell);
            table.addCell(leftCell);
            table.addCell(middleCell);
            table.addCell(rightCell);
            table.addCell(rightCornorCell);
            table.addCell(bottomCell);

            document.add(table);

        } catch (DocumentException ex) {
            log.debug("Error in document format " + ex);
        }
    }

    public void dynamicPdfTable(String dealerName, List<TabWidget> tabWidgets, OutputStream out) {
        log.debug("Calling dynamicPdfTable function with parameters dealerName " + dealerName + " tabWidgets " + tabWidgets + " and out" + out);
        try {
            PdfWriter writer = null;
            Document document = new Document(pageSize, 36, 36, 72, 72);
            BaseColor tableTitleFontColor = new BaseColor(61, 70, 77);

            writer = PdfWriter.getInstance(document, out);
            document.open();
            HeaderFooterTable event = new HeaderFooterTable();
            writer.setPageEvent(event);
            PageNumeration pevent = new PageNumeration();
            writer.setPageEvent(pevent);

            TabWidget widget = tabWidgets.get(0);
            addReportHeader(dealerName, document, widget);

            reportHeader.getReportHeader(document);
            for (Iterator<TabWidget> iterator = tabWidgets.iterator(); iterator.hasNext();) {
                TabWidget tabWidget = iterator.next();
                if (tabWidget.getChartType().equalsIgnoreCase("table")) {
                    PdfPTable pdfTable = dynamicPdfTable(tabWidget);
                    document.add(new Phrase("\n"));
                    document.add(pdfTable);
                } else if (tabWidget.getChartType().equalsIgnoreCase("pie")) {
                    PdfPTable table = new PdfPTable(1);
                    PdfPCell cell;
                    table.setWidthPercentage(95f);
                    pdfFontTitle.setSize(14);
                    pdfFontTitle.setStyle(Font.BOLD);
                    pdfFontTitle.setColor(tableTitleFontColor);
                    cell = new PdfPCell(new Phrase(tabWidget.getWidgetTitle(), pdfFontTitle));
                    cell.setFixedHeight(30);
                    cell.setBorderColor(widgetBorderColor);
                    cell.setBackgroundColor(tableTitleColor);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    cell.setColspan(1);
                    cell.setPaddingTop(5);
                    cell.setPaddingLeft(10);
                    table.addCell(cell);
                    Image pieChart = generatePieChart(writer, tabWidget);

                    if (pieChart != null) {
                        PdfPCell chartCell = new PdfPCell(pieChart);
                        chartCell.setBorderColor(widgetBorderColor);
                        chartCell.setPadding(10);
                        table.addCell(chartCell);
                        document.add(new Phrase("\n"));
                        document.add(table);
                    } else {
                        PdfPCell chartCell = new PdfPCell();
                        chartCell.setBorderColor(widgetBorderColor);
                        chartCell.setPadding(10);
                        table.addCell(chartCell);
                        document.add(new Phrase("\n"));
                        document.add(table);
                    }

                } else if (tabWidget.getChartType().equalsIgnoreCase("bar")) {
                    PdfPTable table = new PdfPTable(1);
                    PdfPCell cell;
                    table.setWidthPercentage(95f);
                    pdfFontTitle.setSize(14);
                    pdfFontTitle.setStyle(Font.BOLD);
                    pdfFontTitle.setColor(tableTitleFontColor);
                    cell = new PdfPCell(new Phrase(tabWidget.getWidgetTitle(), pdfFontTitle));
                    cell.setFixedHeight(30);
                    cell.setBorderColor(widgetBorderColor);
                    cell.setBackgroundColor(tableTitleColor);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    cell.setColspan(1);
                    cell.setPaddingTop(5);
                    cell.setPaddingLeft(10);
                    table.addCell(cell);

                    Image barChart = multiAxisBarChart(writer, tabWidget);
                    if (barChart != null) {
                        PdfPCell chartCell = new PdfPCell(barChart);
                        chartCell.setBorderColor(widgetBorderColor);
                        chartCell.setPadding(10);
                        table.addCell(chartCell);
                        document.add(new Phrase("\n"));
                        document.add(table);
                    } else {
                        PdfPCell chartCell = new PdfPCell();
                        chartCell.setBorderColor(widgetBorderColor);
                        chartCell.setPadding(10);
                        table.addCell(chartCell);
                        document.add(new Phrase("\n"));
                        document.add(table);
                    }

                } else if (tabWidget.getChartType().equalsIgnoreCase("line")) {
                    PdfPTable table = new PdfPTable(1);
                    PdfPCell cell;
                    table.setWidthPercentage(95f);
                    pdfFontTitle.setSize(14);
                    pdfFontTitle.setStyle(Font.BOLD);
                    cell = new PdfPCell(new Phrase(tabWidget.getWidgetTitle(), pdfFontTitle));
                    cell.setFixedHeight(30);
                    cell.setBorderColor(widgetBorderColor);
                    cell.setBackgroundColor(widgetTitleColor);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    cell.setColspan(1);
                    cell.setPaddingTop(5);
                    cell.setPaddingLeft(10);
                    table.addCell(cell);
                    Image lineChart = multiAxisLineChart(writer, tabWidget);
                    if (lineChart != null) {
                        PdfPCell chartCell = new PdfPCell(lineChart);
                        chartCell.setBorderColor(widgetBorderColor);
                        chartCell.setPadding(5);
                        table.addCell(chartCell);
                        document.add(new Phrase("\n"));
                        document.add(table);
                    } else {
                        PdfPCell chartCell = new PdfPCell();
                        chartCell.setBorderColor(widgetBorderColor);
                        chartCell.setPadding(5);
                        table.addCell(chartCell);
                        document.add(new Phrase("\n"));
                        document.add(table);
                    }
                } else if (tabWidget.getChartType().equalsIgnoreCase("areaChart")) {
                    document.add(multiAxisAreaChart(writer, tabWidget));
                    document.add(new Phrase("\n"));
                }
            }
            document.close();
            out.flush();
            out.close();

        } catch (DocumentException ex) {
            log.error("Error in document format" + ex);
        } catch (IOException ex) {
            log.error("Error in closing out " + out + " " + ex);
        }
    }

    public static Image generateLineChart(PdfWriter writer, TabWidget tabWidget) throws BadElementException {
        log.debug("Calling generateLineChart function with parameters writer " + writer + " tabWidget " + tabWidget);
        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();

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

//        final ItemLabelPosition p = new ItemLabelPosition(
//                ItemLabelAnchor.CENTER, TextAnchor.CENTER, TextAnchor.CENTER, 45.0
//        );
//        renderer.setPositiveItemLabelPosition(p);
        plot.setRenderer(renderer);

        plot.setDrawingSupplier(new ChartDrawingSupplier());

        PdfContentByte contentByte = writer.getDirectContent();

        PdfTemplate templatePie = contentByte.createTemplate(widgetWidth, widgetHeight);
        Graphics2D graphics2dPie = templatePie.createGraphics(widgetWidth, widgetHeight,
                new DefaultFontMapper());
        Rectangle2D rectangle2dPie = new Rectangle2D.Double(0, 0, widgetWidth,
                widgetHeight);

        chart.draw(graphics2dPie, rectangle2dPie);

        graphics2dPie.dispose();

        // contentByte.addTemplate(templatePie, 30, 30);
        Image img = Image.getInstance(templatePie);
        return img;
    }

    public Image multiAxisLineChart(PdfWriter writer, TabWidget tabWidget) {
        log.debug("Calling multiAxisLineChart function with parameters writer " + writer + " tabWidget " + tabWidget);
        try {

            List<WidgetColumn> columns = tabWidget.getColumns();

            List<Map<String, Object>> originalData = tabWidget.getData();

            List<Map<String, Object>> tempData = tabWidget.getData();
            if (originalData == null || originalData.isEmpty()) {
                return null;
            }
            List<Map<String, Object>> data = new ArrayList<>(originalData);

            List<SortType> sortFields = new ArrayList<>();
            List<Aggregation> aggreagtionList = new ArrayList<>();
            List<FirstAxis> firstAxis = new ArrayList<>();
            List<SecondAxis> secondAxis = new ArrayList<>();
            String xAxis = null;

            for (Iterator<WidgetColumn> iterator = columns.iterator(); iterator.hasNext();) {
                WidgetColumn column = iterator.next();
                if (column.getSortOrder() != null) {
                    sortFields.add(new SortType(column.getFieldName(), column.getSortOrder(), column.getFieldType()));
                }
                if (column.getAgregationFunction() != null) {
                    aggreagtionList.add(new Aggregation(column.getFieldName(), column.getAgregationFunction()));
                }
                if (column.getyAxis() != null && ApiUtils.toDouble(column.getyAxis()) == 1) {
                    firstAxis.add(new FirstAxis(column.getFieldName(), column.getDisplayName()));
                }
                if (column.getyAxis() != null && ApiUtils.toDouble(column.getyAxis()) > 1) {
                    secondAxis.add(new SecondAxis(column.getFieldName(), column.getDisplayName()));
                }
                if (column.getxAxis() != null) {
                    xAxis = column.getFieldName();
                }
            }

            if (sortFields.size() > 0) {
                data = sortData(data, sortFields);
            }

            if (tabWidget.getMaxRecord() != null && tabWidget.getMaxRecord() > 0) {
                data = data.subList(0, tabWidget.getMaxRecord());
            }

//            final CategoryDataset dataset1 = createDataset3();
//            final CategoryDataset dataset2 = createDataset4();
            final CategoryDataset dataset1 = createDataset1(data, firstAxis, secondAxis, xAxis);
            final CategoryDataset dataset2 = createDataset2(data, secondAxis, firstAxis, xAxis);
            final CategoryAxis domainAxis = new CategoryAxis(xAxis);
            // final NumberAxis rangeAxis = new NumberAxis("Value");
            final NumberAxis rangeAxis = new NumberAxis();
            final LineAndShapeRenderer renderer1 = new LineAndShapeRenderer();
            final CategoryPlot plot = new CategoryPlot(dataset1, domainAxis, rangeAxis, renderer1) {

                /**
                 * Override the getLegendItems() method to handle special case.
                 *
                 * @return the legend items.
                 */
                public LegendItemCollection getLegendItems() {

                    final LegendItemCollection result = new LegendItemCollection();

                    if (firstAxis.isEmpty()) {
                    } else {
                        final CategoryDataset data = getDataset();
                        if (data != null) {
                            final CategoryItemRenderer r = getRenderer();
                            r.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
                            r.setBaseItemLabelsVisible(true);
                            ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
                                    TextAnchor.BASELINE_LEFT);
                            r.setBasePositiveItemLabelPosition(position);
                            if (r != null) {
                                final LegendItem item = r.getLegendItem(0, 0);
                                result.add(item);
                            }
                        }
                    }

                    // the JDK 1.2.2 compiler complained about the name of this
                    // variable 
                    if (secondAxis.isEmpty()) {
                    } else {
                        final CategoryDataset dset2 = getDataset(1);
                        if (dset2 != null) {
                            final CategoryItemRenderer renderer2 = getRenderer(1);
                            renderer2.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
                            renderer2.setBaseItemLabelsVisible(true);
                            ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
                                    TextAnchor.BASELINE_RIGHT);
                            renderer2.setBasePositiveItemLabelPosition(position);
                            if (renderer2 != null) {
                                final LegendItem item = renderer2.getLegendItem(1, 1);
                                result.add(item);
                            }
                        }
                    }
                    return result;
                }
            };

            plot.setRangeGridlinesVisible(true);
            plot.setDomainGridlinesVisible(true);
            plot.setOrientation(PlotOrientation.VERTICAL);
            //final JFreeChart chart = new JFreeChart(tabWidget.getWidgetTitle(), plot);
            final JFreeChart chart = new JFreeChart(plot);
            CategoryAxis axis = chart.getCategoryPlot().getDomainAxis();
            axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

            chart.setBackgroundPaint(Color.white);
            plot.setBackgroundPaint(Color.white);
            plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
            plot.setDataset(1, dataset2);
            plot.mapDatasetToRangeAxis(1, 1);
            final ValueAxis axis2 = new NumberAxis();
            plot.setRangeAxis(1, axis2);
            plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
            Paint[] paintSequence = new Paint[]{
                new Color(31, 119, 180),
                new Color(225, 127, 14)
            };
            final LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();
            renderer2.setSeriesPaint(0, paintSequence[0]);
            renderer2.setSeriesPaint(1, paintSequence[1]);
            plot.setRenderer(0, renderer2);
            plot.setRenderer(1, renderer2);
            plot.setDatasetRenderingOrder(DatasetRenderingOrder.REVERSE);
            // OPTIONAL CUSTOMISATION COMPLETED.

            plot.setDrawingSupplier(new ChartDrawingSupplier());

            PdfContentByte contentByte = writer.getDirectContent();

            PdfTemplate templatePie = contentByte.createTemplate(widgetWidth, widgetHeight);
            Graphics2D graphics2dPie = templatePie.createGraphics(widgetWidth, widgetHeight,
                    new DefaultFontMapper());
            Rectangle2D rectangle2dPie = new Rectangle2D.Double(0, 0, widgetWidth,
                    widgetHeight);

            chart.draw(graphics2dPie, rectangle2dPie);

            graphics2dPie.dispose();

            // contentByte.addTemplate(templatePie, 30, 30);
            Image img = Image.getInstance(templatePie);
            return img;

        } catch (BadElementException ex) {
            log.error("Error in passing of getInstance value " + ex);
        }
        return null;
    }

    public Image multiAxisAreaChart(PdfWriter writer, TabWidget tabWidget) {
        log.debug("Calling multiAxisAreaChart function with parameters writer " + writer + " tabWidget " + tabWidget);
        try {

            List<WidgetColumn> columns = tabWidget.getColumns();

            List<Map<String, Object>> originalData = tabWidget.getData();
            List<Map<String, Object>> data = new ArrayList<>(originalData);

            List<SortType> sortFields = new ArrayList<>();
            List<Aggregation> aggreagtionList = new ArrayList<>();
            List<FirstAxis> firstAxis = new ArrayList<>();
            List<SecondAxis> secondAxis = new ArrayList<>();
            String xAxis = null;

            for (Iterator<WidgetColumn> iterator = columns.iterator(); iterator.hasNext();) {
                WidgetColumn column = iterator.next();
                if (column.getSortOrder() != null) {
                    sortFields.add(new SortType(column.getFieldName(), column.getSortOrder(), column.getFieldType()));
                }
                if (column.getAgregationFunction() != null) {
                    aggreagtionList.add(new Aggregation(column.getFieldName(), column.getAgregationFunction()));
                }
                if (column.getyAxis() != null && ApiUtils.toDouble(column.getyAxis()) == 1) {
                    firstAxis.add(new FirstAxis(column.getFieldName(), column.getDisplayName()));
                }
                if (column.getyAxis() != null && ApiUtils.toDouble(column.getyAxis()) > 1) {
                    secondAxis.add(new SecondAxis(column.getFieldName(), column.getDisplayName()));
                }
                if (column.getxAxis() != null) {
                    xAxis = column.getFieldName();
                }
            }

            if (sortFields.size() > 0) {
                data = sortData(data, sortFields);
            }
            if (tabWidget.getMaxRecord() != null && tabWidget.getMaxRecord() > 0) {
                data = data.subList(0, tabWidget.getMaxRecord());
            }

            final CategoryDataset dataset1 = createDataset1(data, firstAxis, secondAxis, xAxis);
            final CategoryDataset dataset2 = createDataset2(data, secondAxis, firstAxis, xAxis);
            final CategoryAxis domainAxis = new CategoryAxis(xAxis);
            final NumberAxis rangeAxis = new NumberAxis();
            final AreaRenderer renderer1 = new AreaRenderer();
            final CategoryPlot plot = new CategoryPlot(dataset1, domainAxis, rangeAxis, renderer1) {

                /**
                 * Override the getLegendItems() method to handle special case.
                 *
                 * @return the legend items.
                 */
                public LegendItemCollection getLegendItems() {

                    final LegendItemCollection result = new LegendItemCollection();

                    if (firstAxis.isEmpty()) {
                    } else {
                        final CategoryDataset data = getDataset();
                        if (data != null) {
                            final CategoryItemRenderer r = getRenderer();
                            r.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
                            r.setBaseItemLabelsVisible(true);
                            ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
                                    TextAnchor.BASELINE_LEFT);
                            r.setBasePositiveItemLabelPosition(position);

                            if (r != null) {
                                final LegendItem item = r.getLegendItem(0, 0);
                                result.add(item);
                            }
                        }
                    }
                    // the JDK 1.2.2 compiler complained about the name of this
                    // variable 
                    if (secondAxis.isEmpty()) {
                    } else {
                        final CategoryDataset dset2 = getDataset(1);
                        if (dset2 != null) {
                            final CategoryItemRenderer renderer2 = getRenderer(1);
                            renderer2.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
                            renderer2.setBaseItemLabelsVisible(true);
                            ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
                                    TextAnchor.BASELINE_RIGHT);
                            renderer2.setBasePositiveItemLabelPosition(position);
                            if (renderer2 != null) {
                                final LegendItem item = renderer2.getLegendItem(1, 1);
                                result.add(item);
                            }
                        }
                    }
                    return result;
                }
            };
            plot.setRangeGridlinesVisible(true);
            plot.setDomainGridlinesVisible(true);
            final JFreeChart chart = new JFreeChart(tabWidget.getWidgetTitle(), plot);
            CategoryAxis axis = chart.getCategoryPlot().getDomainAxis();
            axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
            chart.setBackgroundPaint(Color.white);
            plot.setBackgroundPaint(Color.white);
            plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
            plot.setDataset(1, dataset2);
            plot.mapDatasetToRangeAxis(1, 1);
            final ValueAxis axis2 = new NumberAxis();
            plot.setRangeAxis(1, axis2);
            plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
            final AreaRenderer renderer2 = new AreaRenderer();
            plot.setRenderer(1, renderer2);
            plot.setDatasetRenderingOrder(DatasetRenderingOrder.REVERSE);
            // OPTIONAL CUSTOMISATION COMPLETED.

            plot.setDrawingSupplier(new ChartDrawingSupplier());

            PdfContentByte contentByte = writer.getDirectContent();

            PdfTemplate templatePie = contentByte.createTemplate(widgetWidth, widgetHeight);
            Graphics2D graphics2dPie = templatePie.createGraphics(widgetWidth, widgetHeight,
                    new DefaultFontMapper());
            Rectangle2D rectangle2dPie = new Rectangle2D.Double(0, 0, widgetWidth,
                    widgetHeight);

            chart.draw(graphics2dPie, rectangle2dPie);

            graphics2dPie.dispose();

            // contentByte.addTemplate(templatePie, 30, 30);
            Image img = Image.getInstance(templatePie);
            return img;

        } catch (BadElementException ex) {
            log.error("Error in passing of getInstance value " + ex);
        }
        return null;
    }

    public Image multiAxisBarChart(PdfWriter writer, TabWidget tabWidget) {
        log.debug("Calling multiAxisBarChart function with parameters writer " + writer + " tabWidget " + tabWidget);
        try {

            List<WidgetColumn> columns = tabWidget.getColumns();

            List<Map<String, Object>> originalData = tabWidget.getData();
            if (originalData == null || originalData.isEmpty()) {
                return null;
            }
            List<Map<String, Object>> data = new ArrayList<>(originalData);

            List<Map<String, Object>> tempData = tabWidget.getData();

            List<SortType> sortFields = new ArrayList<>();
            List<Aggregation> aggreagtionList = new ArrayList<>();
            List<FirstAxis> firstAxis = new ArrayList<>();
            List<SecondAxis> secondAxis = new ArrayList<>();
            String xAxis = null;

            for (Iterator<WidgetColumn> iterator = columns.iterator(); iterator.hasNext();) {
                WidgetColumn column = iterator.next();
                if (column.getSortOrder() != null) {
                    sortFields.add(new SortType(column.getFieldName(), column.getSortOrder(), column.getFieldType()));
                }
                if (column.getAgregationFunction() != null) {
                    aggreagtionList.add(new Aggregation(column.getFieldName(), column.getAgregationFunction()));
                }
                if (column.getyAxis() != null && ApiUtils.toDouble(column.getyAxis()) == 1) {
                    firstAxis.add(new FirstAxis(column.getFieldName(), column.getDisplayName()));
                }
                if (column.getyAxis() != null && ApiUtils.toDouble(column.getyAxis()) > 1) {
                    secondAxis.add(new SecondAxis(column.getFieldName(), column.getDisplayName()));
                }
                if (column.getxAxis() != null) {
                    xAxis = column.getFieldName();
                }
            }

            log.debug("sortField: " + sortFields.size());
            if (sortFields.size() > 0) {
                data = sortData(data, sortFields);
            }

            if (tabWidget.getMaxRecord() != null && tabWidget.getMaxRecord() > 0) {
                data = data.subList(0, tabWidget.getMaxRecord());
            }

            final CategoryDataset dataset1 = createDataset1(data, firstAxis, secondAxis, xAxis);
            final CategoryDataset dataset2 = createDataset2(data, secondAxis, firstAxis, xAxis);
            final CategoryAxis domainAxis = new CategoryAxis(xAxis);
            //final NumberAxis rangeAxis = new NumberAxis("Value");
            final NumberAxis rangeAxis = new NumberAxis();
            final BarRenderer renderer1 = new BarRenderer();
            final CategoryPlot plot = new CategoryPlot(dataset1, domainAxis, rangeAxis, renderer1) {

                /**
                 * Override the getLegendItems() method to handle special case.
                 *
                 * @return the legend items.
                 */
                public LegendItemCollection getLegendItems() {

                    final LegendItemCollection result = new LegendItemCollection();

                    if (firstAxis.isEmpty()) {
                    } else {
                        final CategoryDataset data = getDataset();
                        if (data != null) {
                            final CategoryItemRenderer r = getRenderer();
                            r.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
                            r.setBaseItemLabelsVisible(true);
                            ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
                                    TextAnchor.BASELINE_CENTER);
                            r.setBasePositiveItemLabelPosition(position);
                            if (r != null) {
                                final LegendItem item = r.getLegendItem(0, 0);
                                result.add(item);
                            }
                        }
                    }
                    // the JDK 1.2.2 compiler complained about the name of this
                    // variable 
                    if (secondAxis.isEmpty()) {
                    } else {
                        final CategoryDataset dset2 = getDataset(1);
                        if (dset2 != null) {
                            final CategoryItemRenderer renderer2 = getRenderer(1);
                            renderer2.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
                            renderer2.setBaseItemLabelsVisible(true);
                            ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
                                    TextAnchor.BASELINE_CENTER);
                            renderer2.setBasePositiveItemLabelPosition(position);
                            if (renderer2 != null) {
                                final LegendItem item = renderer2.getLegendItem(1, 1);
                                result.add(item);
                            }
                        }
                    }
                    return result;
                }

            };
            plot.setRangeGridlinesVisible(true);
            plot.setDomainGridlinesVisible(true);
            plot.setOrientation(PlotOrientation.VERTICAL);
            // final JFreeChart chart = new JFreeChart(tabWidget.getWidgetTitle(), plot);
            final JFreeChart chart = new JFreeChart(plot);
            CategoryAxis axis = chart.getCategoryPlot().getDomainAxis();
            axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
            chart.setBackgroundPaint(Color.white);
            plot.setBackgroundPaint(Color.white);
            plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
            plot.setDataset(1, dataset2);
            plot.mapDatasetToRangeAxis(1, 1);
            final ValueAxis axis2 = new NumberAxis();
            plot.setRangeAxis(1, axis2);
            plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
            Paint[] paintSequence = new Paint[]{
                new Color(31, 119, 180),
                new Color(225, 127, 14)
            };
            final BarRenderer renderer2 = new BarRenderer();
            renderer2.setSeriesPaint(0, paintSequence[0]);
            renderer2.setSeriesPaint(1, paintSequence[1]);
            renderer2.setShadowVisible(false);
            plot.setRenderer(0, renderer2);
            plot.setRenderer(1, renderer2);
            plot.setDatasetRenderingOrder(DatasetRenderingOrder.REVERSE);
            // OPTIONAL CUSTOMISATION COMPLETED.

            plot.setDrawingSupplier(new ChartDrawingSupplier());

            PdfContentByte contentByte = writer.getDirectContent();

            PdfTemplate templatePie = contentByte.createTemplate(widgetWidth, widgetHeight);
            Graphics2D graphics2dPie = templatePie.createGraphics(widgetWidth, widgetHeight,
                    new DefaultFontMapper());
            Rectangle2D rectangle2dPie = new Rectangle2D.Double(0, 0, widgetWidth,
                    widgetHeight);

            chart.draw(graphics2dPie, rectangle2dPie);

            graphics2dPie.dispose();

            // contentByte.addTemplate(templatePie, 30, 30);
            Image img = Image.getInstance(templatePie);
            return img;

        } catch (BadElementException ex) {
            log.error("Error in passing of getInstance value " + ex);
        }
        return null;
    }

    /**
     * Creates a sample dataset.
     *
     * @return The dataset.
     */
    private CategoryDataset createDataset1(List<Map<String, Object>> data, List<FirstAxis> firstAxis, List<SecondAxis> secondAxis, String xAxis) {
        log.debug("Calling createDatset1 function with return type CategoryDataSet with  parameters data" + data + " firstAxis " + firstAxis + " secondAxis " + secondAxis + " and xAxis " + xAxis);
        // row keys...
//        final String series1 = "Series 1";
//        final String series2 = "Dummy 1";
//
//        // column keys...
//        final String category1 = "Category 1";
//        final String category2 = "Category 2";
//        final String category3 = "Category 3";
//        final String category4 = "Category 4";

        // create the dataset...
        DecimalFormat df = new DecimalFormat(".##");
        String value;
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> dataMap = iterator.next();
            for (Iterator<FirstAxis> iterator1 = firstAxis.iterator(); iterator1.hasNext();) {
                FirstAxis axis = iterator1.next();
                log.debug(ApiUtils.toDouble(dataMap.get(axis.getFieldName()) + "") + "---" + axis.getDisplayName() + "----" + dataMap.get(xAxis) + "");
                log.debug("---->");
                log.debug("-----> " + dataMap.get(axis.getFieldName()));
                if (dataMap.get(axis.getFieldName()) == null) {
                    value = "0.00";
                    String data1 = value.getClass().getSimpleName();
                    if (data1.equalsIgnoreCase("String")) {
                        log.debug("if");
                        dataset.addValue(ApiUtils.toDouble(df.format(Float.parseFloat(value)) + ""), axis.getDisplayName(), dataMap.get(xAxis) + "");
                    }
                } else {
                    String data1 = dataMap.get(axis.getFieldName()).getClass().getSimpleName();
                    log.debug("******");
                    log.debug("Type: " + data1);
                    if (data1.equalsIgnoreCase("String")) {
                        log.debug("if");
                        dataset.addValue(ApiUtils.toDouble(df.format(Float.parseFloat(dataMap.get(axis.getFieldName()).toString())) + ""), axis.getDisplayName(), dataMap.get(xAxis) + "");
                    } else {
                        log.debug("else");
                        dataset.addValue(ApiUtils.toDouble(df.format(dataMap.get(axis.getFieldName())) + ""), axis.getDisplayName(), dataMap.get(xAxis) + "");
                    }
                }
            }
        }
        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> dataMap = iterator.next();

            for (Iterator<SecondAxis> iterator1 = secondAxis.iterator(); iterator1.hasNext();) {
                SecondAxis axis = iterator1.next();
                log.debug(null + "---" + axis.getDisplayName() + "----" + dataMap.get(xAxis) + "");
                dataset.addValue(null, axis.getDisplayName(), dataMap.get(xAxis) + "");
            }
        }
        return dataset;
    }

    /**
     * Creates a sample dataset.
     *
     * @return The dataset.
     */
    private CategoryDataset createDataset2(List<Map<String, Object>> data, List<SecondAxis> secondAxis, List<FirstAxis> firstAxis, String xAxis) {
        log.debug("Calling createDataset2 function with return type CategoryDataSet with  parameters data" + data + " firstAxis " + firstAxis + " secondAxis " + secondAxis + " and xAxis " + xAxis);
        // create the dataset...
        DecimalFormat df = new DecimalFormat(".##");
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> dataMap = iterator.next();
            for (Iterator<FirstAxis> iterator1 = firstAxis.iterator(); iterator1.hasNext();) {
                FirstAxis axis = iterator1.next();
                log.debug(ApiUtils.toDouble(dataMap.get(axis.getFieldName()) + "") + "---" + axis.getDisplayName() + "----" + dataMap.get(xAxis) + "");
                dataset.addValue(null, axis.getDisplayName(), dataMap.get(xAxis) + "");
            }
        }
        String value;
        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> dataMap = iterator.next();
            for (Iterator<SecondAxis> iterator1 = secondAxis.iterator(); iterator1.hasNext();) {
                SecondAxis axis = iterator1.next();
                log.debug(null + "---" + axis.getDisplayName() + "----" + dataMap.get(xAxis) + "");
                if (dataMap.get(axis.getFieldName()) == null) {
                    value = "0.00";
                    String data1 = value.getClass().getSimpleName();
                    if (data1.equalsIgnoreCase("String")) {
                        dataset.addValue(ApiUtils.toDouble(df.format(Float.parseFloat(value)) + ""), axis.getDisplayName(), dataMap.get(xAxis) + "");
                    }
                } else {
                    String data1 = dataMap.get(axis.getFieldName()).getClass().getSimpleName();
                    if (data1.equalsIgnoreCase("String")) {
                        dataset.addValue(ApiUtils.toDouble(df.format(Float.parseFloat(dataMap.get(axis.getFieldName()).toString())) + ""), axis.getDisplayName(), dataMap.get(xAxis) + "");
                    } else {
                        dataset.addValue(ApiUtils.toDouble(df.format(dataMap.get(axis.getFieldName())) + ""), axis.getDisplayName(), dataMap.get(xAxis) + "");
                    }
                }

            }
        }
        return dataset;
    }

    private CategoryDataset createDataset1() {
        // row keys...
        final String series1 = "Series 1";
        final String series2 = "Dummy 1";
//
//        // column keys...
        final String category1 = "Category 1";
        final String category2 = "Category 2";
        final String category3 = "Category 3";
        final String category4 = "Category 4";

        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(1.0, series1, category1);
        dataset.addValue(4.0, series1, category2);
        dataset.addValue(3.0, series1, category3);
        dataset.addValue(5.0, series1, category4);

        dataset.addValue(null, series2, category1);
        dataset.addValue(null, series2, category2);
        dataset.addValue(null, series2, category3);
        dataset.addValue(null, series2, category4);
        return dataset;

    }

    /**
     * Creates a sample dataset.
     *
     * @return The dataset.
     */
    private static CategoryDataset createDataset2() {

        // row keys...
        final String series1 = "Dummy 2";
        final String series2 = "Series 2";

        // column keys...
        final String category1 = "Category 1";
        final String category2 = "Category 2";
        final String category3 = "Category 3";
        final String category4 = "Category 4";

        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(null, series1, category1);
        dataset.addValue(null, series1, category2);
        dataset.addValue(null, series1, category3);
        dataset.addValue(null, series1, category4);

        dataset.addValue(75.0, series2, category1);
        dataset.addValue(87.0, series2, category2);
        dataset.addValue(96.0, series2, category3);
        dataset.addValue(68.0, series2, category4);

        return dataset;

    }

    private CategoryDataset createDataset3() {
        // row keys...

        final String series1 = "Series 1";
        final String series2 = "Dummy 1";
//        // column keys...
        final String category1 = "Category 1";
        final String category2 = "Category 2";
        final String category3 = "Category 3";
        final String category4 = "Category 4";

        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(785, "clicks", "Monday");
        dataset.addValue(572, "clicks", "Tuesday");
        dataset.addValue(558, "clicks", "Wednessday");
        dataset.addValue(391, "clicks", "Thursday");
        dataset.addValue(536, "clicks", "Friday");
        dataset.addValue(490, "clicks", "Saturday");
        dataset.addValue(731, "clicks", "Sunday");

        dataset.addValue(null, "conversions", "Monday");
        dataset.addValue(null, "conversions", "Tuesday");
        dataset.addValue(null, "conversions", "Wednessday");
        dataset.addValue(null, "conversions", "Thursday");
        dataset.addValue(null, "conversions", "Friday");
        dataset.addValue(null, "conversions", "Saturday");
        dataset.addValue(null, "conversions", "Sunday");
        return dataset;

    }

    /**
     * Creates a sample dataset.
     *
     * @return The dataset.
     */
    private static CategoryDataset createDataset4() {

        // row keys...
        final String series1 = "Dummy 2";
        final String series2 = "Series 2";

        // column keys...
        final String category1 = "Category 1";
        final String category2 = "Category 2";
        final String category3 = "Category 3";
        final String category4 = "Category 4";

        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(null, "clicks", "Monday");
        dataset.addValue(null, "clicks", "Tuesday");
        dataset.addValue(null, "clicks", "Wednessday");
        dataset.addValue(null, "clicks", "Thursday");
        dataset.addValue(null, "clicks", "Friday");
        dataset.addValue(null, "clicks", "Saturday");
        dataset.addValue(null, "clicks", "Sunday");

        dataset.addValue(132, "conversions", "Monday");
        dataset.addValue(79, "conversions", "Tuesday");
        dataset.addValue(72, "conversions", "Wednessday");
        dataset.addValue(18, "conversions", "Thursday");
        dataset.addValue(68, "conversions", "Friday");
        dataset.addValue(73, "conversions", "Saturday");
        dataset.addValue(34, "conversions", "Sunday");

        return dataset;

    }

    public static Image generateBarChart(PdfWriter writer, TabWidget tabWidget) throws BadElementException {
        log.debug("Calling generateBarChart function with return type Image with parameters writer " + writer + "and tabWidget " + tabWidget);
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

//        final ItemLabelPosition p = new ItemLabelPosition(
//                ItemLabelAnchor.CENTER, TextAnchor.CENTER, TextAnchor.CENTER, 45.0
//        );
//        renderer.setPositiveItemLabelPosition(p);
        plot.setRenderer(renderer);

        plot.setDrawingSupplier(new ChartDrawingSupplier());

        PdfContentByte contentByte = writer.getDirectContent();

        PdfTemplate templatePie = contentByte.createTemplate(widgetWidth, widgetHeight);
        Graphics2D graphics2dPie = templatePie.createGraphics(widgetWidth, widgetHeight,
                new DefaultFontMapper());
        Rectangle2D rectangle2dPie = new Rectangle2D.Double(0, 0, widgetWidth,
                widgetHeight);

        chart.draw(graphics2dPie, rectangle2dPie);

        graphics2dPie.dispose();

        // contentByte.addTemplate(templatePie, 30, 30);
        Image img = Image.getInstance(templatePie);
        return img;
    }

    public static Image generatePieChart(PdfWriter writer, TabWidget tabWidget) throws BadElementException {
        log.debug("Calling generatePieChart function  with return type Image with parameters writer " + writer + " and tabWidget " + tabWidget);
        List<WidgetColumn> columns = tabWidget.getColumns();
        List<Map<String, Object>> originalData = tabWidget.getData();
        if (originalData == null || originalData.isEmpty()) {
            return null;
        }
        List<Map<String, Object>> data;
        data = new ArrayList<>(originalData);

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

        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> dataMap = iterator.next();
            dataSet.setValue(dataMap.get(xAxis) + "", ApiUtils.toDouble(dataMap.get(yAxis) + ""));
            legends.add(dataMap.get(xAxis) + "");
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "", dataSet, true, false, false);

        Paint[] paintSequence = new Paint[]{
            new Color(31, 119, 180),
            new Color(225, 127, 14),
            new Color(44, 160, 44),
            new Color(214, 39, 40),
            new Color(148, 103, 189)
        };
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setDrawingSupplier(new ChartDrawingSupplier());
        plot.setBackgroundPaint(Color.white);
        plot.setOutlineVisible(false);
        plot.setShadowXOffset(0);
        plot.setShadowYOffset(0);
        int i = 0;
        for (Iterator<String> iterator = legends.iterator(); iterator.hasNext();) {
            if (i > 4) {
                i = 2;
            }
            String legend = iterator.next();
            plot.setSectionPaint(legend, paintSequence[i++]);
        }

        PdfContentByte contentByte = writer.getDirectContent();
        PdfTemplate templateBar = contentByte.createTemplate(widgetWidth, widgetHeight);
        Graphics2D graphics2dBar = templateBar.createGraphics(widgetWidth, widgetHeight,
                new DefaultFontMapper());
        Rectangle2D rectangle2dBar = new Rectangle2D.Double(0, 0, widgetWidth,
                widgetHeight);

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

    public class FirstAxis {

        private String fieldName;
        private String displayName;

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public FirstAxis(String fieldName, String displayName) {
            this.fieldName = fieldName;
            this.displayName = displayName;
        }
    }

    public class SecondAxis {

        private String fieldName;
        private String displayName;

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public SecondAxis(String fieldName, String displayName) {
            this.fieldName = fieldName;
            this.displayName = displayName;
        }
    }

    public static class PageNumeration extends PdfPageEventHelper {

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            log.debug("Calling  onEndPage function with parameter writer " + writer + " and Document " + document);

            PdfPTable table = new PdfPTable(1);

            table.setTotalWidth(523);
            PdfPCell cell = new PdfPCell(new Phrase("Page Number " + writer.getPageNumber()));
            cell.setBorder(Rectangle.NO_BORDER);
            //cell.setBackgroundColor(BaseColor.ORANGE);
            table.addCell(cell);
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
            log.debug("Calling onEndPage function with return type Image with parameter writer " + writer + " and document " + document);
            try {

                Rectangle rectangle = pageSize; // new Rectangle(10, 900, 100, 850);
                Image img = Image.getInstance(CustomReportDesigner.class.getResource("") + "/../images/l2tmedia-logo-dark.png");
                img.scaleToFit(200, 200);
                img.setAbsolutePosition(60, rectangle.getTop() - 85);
                img.setAlignment(Element.ALIGN_TOP);
                writer.getDirectContent().addImage(img);

                if (footer != null) {
                    footer.writeSelectedRows(0, -1, 36, 64, writer.getDirectContent());
                }
            } catch (IOException ex) {
                log.error("Error in reading file name /../images/l2tmedia-logo-dark.png " + ex);
            } catch (DocumentException ex) {
                log.error("Error in passing getInstance value " + ex);
            }
        }
    }
}
