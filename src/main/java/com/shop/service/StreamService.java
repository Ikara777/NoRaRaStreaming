package com.shop.service;

import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;  // 여기를 수정
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

@Service
@Slf4j
@RequiredArgsConstructor
public class StreamService {

    // 방송 상태 확인 메서드
    public boolean streamLiveCheck(String streamKey) {
        try {
            String rtmpStatUrl = "http://localhost:8080/stat";
            RestTemplate restTemplate = new RestTemplate();
            String xmlStats = restTemplate.getForObject(rtmpStatUrl, String.class);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlStats)));

            NodeList streamNodes = doc.getElementsByTagName("stream");
            for (int i = 0; i < streamNodes.getLength(); i++) {
                Node streamNode = streamNodes.item(i);
                if (streamNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) streamNode;
                    String currentStreamKey = element.getElementsByTagName("name").item(0).getTextContent();
                    if (currentStreamKey.equals(streamKey)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("RTMP 상태 조회 실패: " + e);
        }
        return false;
    }
}