package io.jenkins.plugins.entry;

import net.sf.json.JSONObject;
import org.apache.tools.ant.ExtensionPoint;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

public class TriggerNotifier extends ExtensionPoint {
    private static final Logger logger = (new Loggers()).log();

    private static String CreateJson(String jenkins,String id,String name,boolean state){
        JSONObject jobJsonObj = new JSONObject();
        jobJsonObj.put("id",id);
        jobJsonObj.put("name",name);
        jobJsonObj.put("state",state);

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("name",jenkins);
        jsonObj.put("job",jobJsonObj);

        return jsonObj.toString();
    }
    /**
     * Posts a new series data point.
     *
     * @param id the job id
     * @param name the job name
     * @param jenkins the jenkins name
     * @param Url the callback url
     */
    public void notifyBuildStatus(String Url,String id,String name,String jenkins,boolean state) throws IOException {
        URL obj = new URL(Url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("POST");


        // For POST only - START
        String POST_PARAMS = CreateJson(jenkins,id,name,state);
        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        os.write(POST_PARAMS.getBytes("utf-8"));
        os.flush();
        os.close();
        // For POST only - END

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            logger.info("jenkins monitor triggered success.");
        }
    }
}

