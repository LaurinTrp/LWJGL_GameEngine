package main.java.utils.loaders;

import java.io.File;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import glm.vec._3.Vec3;
import main.java.data.light.Light;

public class LightDataLoader {

    public static Properties readLightData(String filePath) {
        Properties properties = new Properties();
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList lightList = doc.getElementsByTagName("Light");

            for (int i = 0; i < lightList.getLength(); i++) {
                Node lightNode = lightList.item(i);
                if (lightNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element lightElement = (Element) lightNode;

                    String name = lightElement.getElementsByTagName("Name").item(0).getTextContent();
                    String type = lightElement.getElementsByTagName("Type").item(0).getTextContent();
                    String energy = lightElement.getElementsByTagName("Energy").item(0).getTextContent();

                    Element locationElement = (Element) lightElement.getElementsByTagName("Location").item(0);
                    String locX = locationElement.getAttribute("x");
                    String locY = locationElement.getAttribute("y");
                    String locZ = locationElement.getAttribute("z");
                    
                    Element rotationElement = (Element) lightElement.getElementsByTagName("Rotation").item(0);
                    String rotX = rotationElement.getAttribute("x");
                    String rotY = rotationElement.getAttribute("y");
                    String rotZ = rotationElement.getAttribute("z");

                    Element globalFrontVectorElement = (Element) lightElement.getElementsByTagName("GlobalFrontVector").item(0);
                    String gfvX = globalFrontVectorElement.getAttribute("x");
                    String gfvY = globalFrontVectorElement.getAttribute("y");
                    String gfvZ = globalFrontVectorElement.getAttribute("z");

                    Element colorElement = (Element) lightElement.getElementsByTagName("Color").item(0);
                    String colorR = colorElement.getAttribute("r");
                    String colorG = colorElement.getAttribute("g");
                    String colorB = colorElement.getAttribute("b");

                    properties.setProperty(name + ".type", type);
                    properties.setProperty(name + ".energy", energy);
                    properties.setProperty(name + ".location.x", locX);
                    properties.setProperty(name + ".location.y", locY);
                    properties.setProperty(name + ".location.z", locZ);
                    properties.setProperty(name + ".rotation.x", rotX);
                    properties.setProperty(name + ".rotation.y", rotY);
                    properties.setProperty(name + ".rotation.z", rotZ);
                    properties.setProperty(name + ".globalFrontVector.x", gfvX);
                    properties.setProperty(name + ".globalFrontVector.y", gfvY);
                    properties.setProperty(name + ".globalFrontVector.z", gfvZ);
                    properties.setProperty(name + ".color.r", colorR);
                    properties.setProperty(name + ".color.g", colorG);
                    properties.setProperty(name + ".color.b", colorB);

                    if (lightElement.getElementsByTagName("SpotSize").getLength() > 0) {
                        String spotSize = lightElement.getElementsByTagName("SpotSize").item(0).getTextContent();
                        properties.setProperty(name + ".spotSize", spotSize);
                    }

                    if (lightElement.getElementsByTagName("SpotBlend").getLength() > 0) {
                        String spotBlend = lightElement.getElementsByTagName("SpotBlend").item(0).getTextContent();
                        properties.setProperty(name + ".spotBlend", spotBlend);
                    }

                    if (lightElement.getElementsByTagName("Shape").getLength() > 0) {
                        String shape = lightElement.getElementsByTagName("Shape").item(0).getTextContent();
                        properties.setProperty(name + ".shape", shape);
                    }

                    if (lightElement.getElementsByTagName("Size").getLength() > 0) {
                        String size = lightElement.getElementsByTagName("Size").item(0).getTextContent();
                        properties.setProperty(name + ".size", size);
                    }

                    if (lightElement.getElementsByTagName("SizeY").getLength() > 0) {
                        String sizeY = lightElement.getElementsByTagName("SizeY").item(0).getTextContent();
                        properties.setProperty(name + ".sizeY", sizeY);
                    }
                    
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static void main(String[] args) {
        Properties lightProperties = readLightData("/run/media/laurin/Festplatte/Programmieren/Java/3D-Workbench/LWJGL_GameEngineResource/src/resources/Models/Cottage/lights_data.xml");

        // Print all properties for debugging
        lightProperties.forEach((key, value) -> System.out.println(key + ": " + value));
    }
}
