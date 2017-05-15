/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.visumbu.vb.bean.map.auth.SecurityAuthBean;
import com.visumbu.vb.bean.map.auth.SecurityTokenBean;
import com.visumbu.vb.utils.Rest;
import static com.visumbu.vb.utils.Rest.getData;
import static com.visumbu.vb.utils.Rest.postRawForm;
import com.visumbu.vb.utils.Settings;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author user
 */
public class MapServiceConsumer {

    public static void main(String args[]) {
       SecurityTokenBean token = getMapAccessToken();
        getMapData(token, "8,16");
    }

    public static void getMapData(SecurityTokenBean token, String dealerMapIds) {

        // String url = "https://phv4ebmu97.execute-api.us-east-1.amazonaws.com/qa/mdl/paidsearch?offset=0&limit=20&startDate=2016-01-01&endDate=2016-01-20&level=GROUP_CLIENTS";
        String url = "https://phv4ebmu97.execute-api.us-east-1.amazonaws.com/qa/mdl/paidsearch";
        // String url = "https://phv4ebmu97.execute-api.us-east-1.amazonaws.com/qa/mdl/displayadcreative";

        Map<String, String> accessHeader = new HashMap<>();

        String authToken = "{\"token\":\"" + token.getAccessToken() + "\", \"clientIds\":[" + dealerMapIds + "]}";
        
        accessHeader.put("Authorization", authToken);
        MultiValueMap<String, String> dataMap = new LinkedMultiValueMap<>();
        dataMap.set("offset", "0");
        dataMap.set("limit", "20");
        dataMap.set("startDate", "2016-01-01");
        dataMap.set("endDate", "2016-01-20");
        dataMap.set("level", "CAMPAIGN");
        dataMap.set("segments", "DEVICE");
        System.out.println("======> " + authToken);
        String dataOut = getData(url, dataMap, accessHeader);

        ObjectMapper mapper = new ObjectMapper();
    }

    public static SecurityTokenBean getMapAccessToken() {
        try {
            String output = postRawForm(Settings.getSecurityTokenUrl(), "client_id=f8f06d06436f4104ade219fd7d535654&client_secret=ba082149c90f41c49e86f4862e22e980&grant_type=password&scope=FullControl&username=admin&password=admin123");
            ObjectMapper mapper = new ObjectMapper();
            SecurityTokenBean token = mapper.readValue(output, SecurityTokenBean.class);
            return token;
        } catch (IOException ex) {
            Logger.getLogger(MapServiceConsumer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
