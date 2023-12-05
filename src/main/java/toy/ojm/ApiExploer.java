package toy.ojm;

import toy.ojm.client.dto.SearchLocalRes;

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
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300){
            rd = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        }else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while((line = rd.readLine()) != null){
            sb.append("\r\n");
            sb.append(line);
        }

        rd.close();
        conn.disconnect();
        System.out.println("out:" +sb.toString());
        String result =  sb.toString();


        /*
        // 1. 문자열 형태의 JSON을 파싱하기 위한 JSONParser 객체 생성.
        JSONParser parser = new JSONParser();
        // 2. 문자열을 JSON 형태로 JSONObject 객체에 저장.
        JSONObject obj = (JSONObject)parser.parse(sb.toString());
        // 3. 필요한 리스트 데이터 부분만 가져와 JSONArray로 저장.
        JSONArray dataArr = (JSONArray) obj.get("data");
	// 4. model에 담아준다.
        model.addAttribute("data",dataArr);
         */
    }

    public static SearchLocalRes search(String query) {
        return null;
    }
}
