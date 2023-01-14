package processors;

import java.util.List;

public interface ProcessingFile {
    public List<String> readFromFile() throws Exception;
    public void writeResultToFile(String inputFileName) throws Exception;

    public List<String> calculate(List<String> data) throws Exception;
    public String getFileName();
}
