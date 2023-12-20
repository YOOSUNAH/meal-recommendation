package toy.ojm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ApiExploer {
    public static void main(String[] args) throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088/6f524d4876746a73383056664d6843/xml/LOCALDATA_072404/1/5/");
        urlBuilder.append("/" + URLEncoder.encode("6f524d4876746a73383056664d6843", "UTF-8")); // 인증키
        urlBuilder.append("/" + URLEncoder.encode("json", "UTF-8")); // 요청 파일 타입 xml, xmlf, xls, json
        urlBuilder.append("/" + URLEncoder.encode("OJM", "UTF-8")); // 서비스명
        urlBuilder.append("/" + URLEncoder.encode("1", "UTF-8")); // 요청 시작위치
        urlBuilder.append("/" + URLEncoder.encode("5", "UTF-8")); // 요청 종료 위치

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode()); // 연결 자체에 대한 확인
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append("\r\n");
            sb.append(line);
        }

        rd.close();
        conn.disconnect();
        System.out.println("out:" + sb.toString());
        String result = sb.toString();
    }
}
