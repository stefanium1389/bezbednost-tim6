package bezbednosttim6.security;

import org.slf4j.MDC;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LogIdUtil {
    public void getNewLogId(){
        String logId = String.valueOf(getLastLogId()+1);
        MDC.put("logId", logId);
    }
    private static int getLastLogId() {
        String logFilePath = "src/main/resources/logs/application.log";
        String lastId = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int idStartIndex = line.indexOf("[") + 1;
                int idEndIndex = line.indexOf("]");
                lastId = line.substring(idStartIndex, idEndIndex);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(lastId!=null)
            return Integer.parseInt(lastId);
        return 0;
    }
}