package processors;

import ParserExpression.RPN;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ProcessingJsonFile {
    private static String inputFileName;
    public ProcessingJsonFile(String inputFName) {
        inputFileName = inputFName;
    }

    public static List<List<String>> readFromFile() throws ParseException, IOException {
        List<List<String>> readFile = new ArrayList<>();
        readFile.add(0, new ArrayList<>());
        FileReader reader = new FileReader(inputFileName);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(reader);
        JSONArray array = (JSONArray) jsonObject.get("expressions");
        for(int i = 0; i < array.size(); i++) {
            JSONObject object = (JSONObject) array.get(i);
            for(int j = 0; j < object.size(); j++) {
                readFile.get(i).add(object.get("expression" + Integer.toString(j + 1)).toString());
            }
            if(i + 1 != array.size()) {
                readFile.add(i + 1, new ArrayList<>());
            }
        }
        return readFile;
    }
    public static void writeToFile(String outputFileName) throws Exception {
        JSONArray resultArray = new JSONArray();
        List<List<String>> dataFromFile = readFromFile();
        List<List<String>> calculatedData = calculate(dataFromFile);
        for (int i = 0; i < calculatedData.size(); i++) {
            JSONObject object = new JSONObject();
            for (int j = 0; j < calculatedData.get(i).size(); j++) {
                object.put("expression" + Integer.toString(j + 1), calculatedData.get(i).get(j));
            }
            resultArray.add(object);
        }
        Files.write(Paths.get(outputFileName),resultArray.toJSONString().getBytes());
    }

    public static List<List<String>> calculate(List<List<String>> expressions) throws Exception {
        List<List<String>> calculated = new ArrayList<>();
        calculated.add(0, new ArrayList<>());
        for (int i = 0; i < expressions.size(); i++) {
            for (int j = 0; j < expressions.get(i).size(); j++) {
                RPN rpn = new RPN(expressions.get(i).get(j));
                String result = rpn.RPNToAnswer().toString();
                calculated.get(i).add(result);
            }
            calculated.add(i + 1, new ArrayList<>());
        }
        return calculated;
    }
}
