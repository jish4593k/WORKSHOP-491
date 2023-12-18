import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MetadataExtractor {

 
    public static class DateTimeEncoder {
        public String encodeDateTime(Date date) {
            // Function to encode Date as ISO format
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            return dateFormat.format(date);
        }
    }

    public static void toJson(Map<String, Object> data, String fileName) throws IOException {
      
        try (FileWriter fileWriter = new FileWriter(fileName)) {
          
            String jsonString = serializeToJson(data);
            fileWriter.write(jsonString);
        }
    }

    public static Map<String, Object> processFiles(String directory) {
        // Function to process files in a directory and extract metadata
        Map<String, Object> metadataMap = new HashMap<>();
        Map<String, String> errorMap = new HashMap<>();

        File[] files = new File(directory).listFiles();
        if (files != null) {
            for (File file : files) {
                String filename = file.getName();

                try {
                    System.out.println("------------------");
                    System.out.println(filename);

                    if (filename.endsWith(".pptx") || filename.endsWith(".docx") || filename.endsWith(".xlsx")) {
                       
                        Map<String, Object> temp = msofficeMetadata(file);
                        metadataMap.put(filename, temp);
                    } else if (filename.endsWith(".pdf")) {
                      
                        Map<String, Object> temp = pdfMetadata(file);
                        metadataMap.put(filename, temp);
                    } else {
                        continue;  
                    }

                    System.out.println(metadataMap.get(filename));
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMap.put(filename, e.toString());
                }
            }
        }

        return metadataMap;
    }

  

    public static void main(String[] args) {
        

        Map<String, Object> metadata = processFiles(directoryPath);

        
        try {
            toJson(metadata, "metadata.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
