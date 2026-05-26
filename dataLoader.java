import java.io.*;
import java.util.*;

public class dataLoader {

    public static List<Instance> loadData(String filename) throws Exception {
        List<Instance> data = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(filename));
        
        br.readLine();

        String line;

        while((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            double[] features = new double[parts.length -1];

            for(int i = 0; i < parts.length -1; i++) {
                features[i] =  Double.parseDouble(parts[i].trim());
            }

            int label = Integer.parseInt(parts[parts.length - 1].trim());
            data.add(new Instance(features, label));
        }

        br.close();
        return data;
    }
    
}
