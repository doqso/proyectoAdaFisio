package es.physiotherapy.persistence.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class XMLWritter {
    private static Document documentBuilder() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (Exception e) {
            throw new RuntimeException("Error creating document builder", e);
        }
        return builder.newDocument();
    }

    public static <T> void createXmlFile(T[] entities, String filename) throws IOException {
        Class<?> entityClass = entities[0].getClass();
        Document doc = documentBuilder();
        String className = entityClass.getSimpleName().toLowerCase();
        Element rootElement = doc.createElement(className + "s");
        for (T entity : entities) {
            Element entityElement = doc.createElement(className);
            for (Field field : entityClass.getDeclaredFields()) {
                String fieldType = field.getType().getSimpleName();
                if (fieldType.equals("Client")
                        || fieldType.equals("TreatedArea")
                        || fieldType.equals("Appointment")
                        || fieldType.equals("List")) continue;
                String classProperty = field.getName();
                field.setAccessible(true);
                String objectValue = null;
                try {
                    objectValue = field.get(entity).toString();
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error getting field value", e);
                } catch (NullPointerException ignored) {
                }
                Element element = doc.createElement(classProperty);
                element.setTextContent(objectValue);
                entityElement.appendChild(element);
            }
            rootElement.appendChild(entityElement);
        }
        doc.appendChild(rootElement);
        saveXmlFile(doc, filename + ".xml");
    }

    private static void saveXmlFile(Document doc, String fileName) throws IOException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException("Error creating transformer", e);
        }
        String path = "output";
        Path dir = Paths.get(path);
        if (!Files.exists(dir)) Files.createDirectory(dir);
        try (FileOutputStream writer = new FileOutputStream(path + "/" + fileName)) {
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
        } catch (TransformerException e) {
            throw new RuntimeException("Error transforming document to create XML", e);
        }
    }
}
