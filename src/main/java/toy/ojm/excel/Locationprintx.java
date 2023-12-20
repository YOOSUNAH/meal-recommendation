package toy.ojm.excel;

import toy.ojm.excel.RestaurantDTO;

import java.util.ArrayList;

public class Locationprintx {
    public void printx(ArrayList<RestaurantDTO> list) {
        for (RestaurantDTO dto : list) {
            System.out.println("BusinessStatus: " + dto.getBusinessStatus());
            System.out.println("StreetNumberAddress: " + dto.getStreetNumberAddress());
            System.out.println("RestaurantName: " + dto.getRestaurantName());
            System.out.println("Category: " + dto.getCategory ());
            System.out.println("longitude: " + dto.getLongitude());
            System.out.println("latitude: " + dto.getLatitude());
            System.out.println("-----------------------");
        }
    }
}