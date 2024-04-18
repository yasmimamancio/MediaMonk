package application;


import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import java.io.FileOutputStream;

public class Main {
    public static void main(String[] args) {
        try {
            File file = new File(".\\feed_psel.xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);

            NodeList nodeList = document.getElementsByTagName("item");

            for(int i = 0; i < nodeList.getLength(); i++) {
                Element itens = (Element) nodeList.item(i);

                NodeList itemAvailability = itens.getElementsByTagName("availability");
                if (itemAvailability.getLength() > 0) {
                    Element availabilityElement = (Element) itemAvailability.item(0);

                    String inStock = availabilityElement.getTextContent().substring(1,
                            availabilityElement.getTextContent().length()-1);
                    
                    if (inStock.equals("Fora de estoque"))
                        itens.getParentNode().removeChild(itens);
                }

                NodeList itemColor = itens.getElementsByTagName("color");
                if (itemColor.getLength() > 0) {
                    Element itemElement = (Element) itemColor.item(0);

                    String color = itemElement.getTextContent();

                    if (color.equals("null")) {
                        NodeList itemTittle = itens.getElementsByTagName("title");
                        if (itemTittle.getLength() > 0) {
                            Element tittleElement = (Element) itemTittle.item(0);

                            String tittle = tittleElement.getTextContent().substring(
                                    tittleElement.getTextContent().lastIndexOf("-")+1,
                                    tittleElement.getTextContent().length()-1).trim();

                            itemElement.setTextContent(tittle);
                        }
                    }
                }

                NodeList itemImage = itens.getElementsByTagName("image_link");
                if (itemImage.getLength() > 0) {
                    Element imageElement = (Element) itemImage.item(0);

                    String imageLink = imageElement.getTextContent().substring(1,
                            imageElement.getTextContent().length()-1);

                    if (!imageLink.endsWith("jpg")) {
                        String newImageLink = imageLink.replace(".mp3", ".jpg");
                        imageElement.setTextContent(newImageLink);
                    }
                }
            }

            FileOutputStream fos = new FileOutputStream(".\\feed_psel.xml");
            javax.xml.transform.TransformerFactory.newInstance().newTransformer().transform(
                new javax.xml.transform.dom.DOMSource(document),
                new javax.xml.transform.stream.StreamResult(fos));

        }   catch (Exception e) {
            e.printStackTrace();
        }
    }
}