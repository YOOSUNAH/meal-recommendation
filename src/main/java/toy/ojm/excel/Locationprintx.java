package toy.ojm.excel;

import toy.ojm.excel.RestaurantDTO;

import java.util.ArrayList;

public class Locationprintx {
    public void printx(ArrayList<RestaurantDTO> list) {
        for (RestaurantDTO dto : list) {
            System.out.println("dtlStateNm: " + dto.getDtlStateNm());
            System.out.println("siteWhLaDdr: " + dto.getSiteWhLaDdr());
            System.out.println("rdNWhLaDdr: " + dto.getRdNWhLaDdr());
            System.out.println("bpLcNm: " + dto.getBpLcNm());
            System.out.println("upTadNm: " + dto.getUpTadNm());
            System.out.println("x: " + dto.getX());
            System.out.println("y: " + dto.getY());
            System.out.println("-----------------------");
        }
    }
}