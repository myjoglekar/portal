    /*
     * To change this license header, choose License Headers in Project Properties.
     * To change this template file, choose Tools | Templates
     * and open the template in the editor.
     */
    package test;

    import java.util.List;
    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.Map;
    import java.util.stream.Collectors;

    /**
     *
     * @author user
     */
    public class TestFilter {

        public static void main(String argv[]) {
            String[] firstnames = {"john", "david", "mathew", "john", "jerry", "Uffe", "Sekar", "Suresh", "Ramesh", "Raja"};
            String[] secondnames = {"Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen", "Twenty"};
            String[] salary = {"10000", "20000", "15000", "5323", "2000", "5346", "1000", "4889", "7854", "2438"};
            String[] location = {"India", "Iceland", "Mexico", "Slovenia", "Poland", "Australia", "1000", "USA", "England", "Canada"};

            List<Map<String, Object>> list = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("firstname", firstnames[i]);
                dataMap.put("secondname", secondnames[i]);
                dataMap.put("salary", salary[i]);
                dataMap.put("location", location[i]);
                list.add(dataMap);
            }
            String filterRule = "((firstname = john AND Lastname = Eleven) OR (salary = 15000 AND location = Mexico OR (firstname = mathew AND lastname = Thirteen)))";
            System.out.println(filter(list, filterRule));

        }

        public static List<Map<String, Object>> filter(List<Map<String, Object>> list, String filterRules) {
            List<Map<String, Object>> filtered = list.stream()
                    .filter(p -> checkFilter(p, filterRules)).collect(Collectors.toList());
            return filtered;

        }

        public static Boolean checkFilter(Map<String, Object> mapData, String filterRules) {
            // Apply condition here and return true or false
             // return (mapData.get("firstname") + "").equalsIgnoreCase("john");
            return true;
        }

        public static Boolean isOperator(String operator) {
            if(operator.equalsIgnoreCase("and") || operator.equalsIgnoreCase("or") || operator.equalsIgnoreCase("=")) {
                return true;
            }

            return false;
        }
    }
