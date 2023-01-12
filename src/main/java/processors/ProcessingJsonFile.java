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

public class ProcessingJsonFile implements ProcessingFile{
    private static String inputFileName;
    public ProcessingJsonFile(String inputFName) {
        inputFileName = inputFName;
    }
    public List<String> readFromFile() throws IOException, ParseException {
        List<String> readFile = new ArrayList<>();
        FileReader reader = new FileReader(inputFileName);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(reader);
        JSONArray array = (JSONArray) jsonObject.get("expressions");
        for(int i = 0; i < array.size(); i++) {
            JSONObject object = (JSONObject) array.get(i);
            for(int j = 0; j < object.size(); j++) {
                readFile.add(object.get("expression" + Integer.toString(j + 1)).toString());
            }
        }
        return readFile;
    }
    public void writeToFile(String outputFileName) throws Exception {
        List<String> dataFromFile = readFromFile();
        List<String> calculatedData = calculate(dataFromFile);
        JSONObject resultObject = new JSONObject();
        for (int i = 0; i < calculatedData.size(); i++) {
                resultObject.put("expression" + Integer.toString(i + 1), calculatedData.get(i));
        }
        Files.write(Paths.get(outputFileName),resultObject.toJSONString().getBytes());
    }

    public List<String> calculate(List<String> expressions) throws Exception {
        List<String> calculated = new ArrayList<>();
        for (int i = 0; i < expressions.size(); i++) {
                RPN rpn = new RPN(expressions.get(i));
                String result = rpn.RPNToAnswer().toString();
                calculated.add(result);
        }
        return calculated;
    }
}
