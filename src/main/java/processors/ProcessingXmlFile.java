package processors;

import ParserExpression.RPN;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ProcessingXmlFile implements ProcessingFile{
    private static String inputFileName;

    public ProcessingXmlFile(String inputFName) {
        inputFileName = inputFName;
    }
    public List<String> readFromFile() throws Exception {
        File file = new File(inputFileName);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc = null;
        List<String> list = new ArrayList<>();
        doc = dbf.newDocumentBuilder().parse(file);
        NodeList nodeList = doc.getElementsByTagName("expression");
        for(int i = 0; i < nodeList.getLength();i++){
            list.add(nodeList.item(i).getTextContent());
        }
        return list;
    }

    public void writeResultToFile(String outputFileName) throws Exception {
        String root = "root";
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element rootElement = document.createElement(root);

        document.appendChild(rootElement);

        List<String> dataFromFile = readFromFile();
        List<String> calculatedData = calculate(dataFromFile);
        for(int i = 0; i < calculatedData.size(); i++){
            String element = "expression";
            Element em = document.createElement(element);
            em.appendChild(document.createTextNode(calculatedData.get(i)));
            rootElement.appendChild(em);
        }

        OutputStream fileOutputStream = new FileOutputStream(outputFileName);
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(fileOutputStream);
        transformer.transform(source, result);
        fileOutputStream.close();

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

    public String getFileName() {
        return inputFileName;
    }
}
