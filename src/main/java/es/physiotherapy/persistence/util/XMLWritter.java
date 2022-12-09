package es.physiotherapy.persistence.util;

import es.physiotherapy.persistence.entity.Appointment;
import es.physiotherapy.persistence.entity.Client;
import es.physiotherapy.persistence.entity.TreatedArea;
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

    /*
    // Experimental generic method
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
     */

    public static void createClientXmlFile(Object[] clients, String filename) throws IOException {
        Document doc = documentBuilder();
        Element rootElement = doc.createElement("clients");
        for (Object object : clients) {
            Client client = (Client) object;
            Element clientElement = doc.createElement("client");
            clientElement.setAttribute("dni", client.getDni());
            clientElement.setIdAttribute("dni", true);
            Element nameElement = doc.createElement("name");
            nameElement.setTextContent(client.getName());
            clientElement.appendChild(nameElement);
            Element surnameElement = doc.createElement("surname");
            surnameElement.setTextContent(client.getSurname());
            clientElement.appendChild(surnameElement);
            Element phoneElement = doc.createElement("phone");
            phoneElement.setTextContent(client.getPhone());
            clientElement.appendChild(phoneElement);
            Element cityElement = doc.createElement("city");
            cityElement.setTextContent(client.getCity());
            clientElement.appendChild(cityElement);
            Element birthDateElement = doc.createElement("birthDate");
            birthDateElement.setTextContent(client.getBirthDate().toString());
            clientElement.appendChild(birthDateElement);
            rootElement.appendChild(clientElement);
        }
        doc.appendChild(rootElement);
        saveXmlFile(doc, filename + ".xml");
    }

    public static void createAppointmentXmlFile(Object[] appointments, String filename) throws IOException {
        Document doc = documentBuilder();
        Element rootElement = doc.createElement("appointments");
        for (Object object : appointments) {
            Appointment appointment = (Appointment) object;
            Element appointmentElement = doc.createElement("appointment");
            appointmentElement.setAttribute("id", appointment.getId().toString());
            appointmentElement.setIdAttribute("id", true);
            Element clientElement = doc.createElement("client_dni");
            clientElement.setTextContent(appointment.getClient().getDni());
            appointmentElement.appendChild(clientElement);
            Element dateElement = doc.createElement("date");
            dateElement.setTextContent(appointment.getDate().toString());
            appointmentElement.appendChild(dateElement);
            Element timeElement = doc.createElement("time");
            timeElement.setTextContent(appointment.getTime().toString());
            appointmentElement.appendChild(timeElement);
            Element durationElement = doc.createElement("duration");
            durationElement.setTextContent(appointment.getDuration().toString());
            appointmentElement.appendChild(durationElement);
            Element treatedAreaElement = doc.createElement("treatedArea");
            createTreatedAreaXml(treatedAreaElement, appointment.getTreatedArea());
            appointmentElement.appendChild(treatedAreaElement);
            rootElement.appendChild(appointmentElement);
        }
        doc.appendChild(rootElement);
        saveXmlFile(doc, filename + ".xml");
    }

    private static void createTreatedAreaXml(Element parentElement, TreatedArea treatedArea) {
        Document doc = parentElement.getOwnerDocument();
        for (Field field : TreatedArea.class.getDeclaredFields()) {
            if (field.getType() == Appointment.class) continue;
            String fieldName = field.getName();
            field.setAccessible(true);
            try {
                Object fieldValue = field.get(treatedArea);
                Element element = doc.createElement(fieldName);
                String textContent = (fieldValue != null) ? fieldValue.toString() : null;
                element.setTextContent(textContent);
                parentElement.appendChild(element);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error getting field value of " +
                        fieldName + " from TreatedArea", e);
            }
        }
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
