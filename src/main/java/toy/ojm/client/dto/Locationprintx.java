package toy.ojm.client.dto;

import java.util.ArrayList;

public class Locationprintx {
    public void printx(ArrayList<RestaurantDTO> list) {
        for (RestaurantDTO dto : list) {
            System.out.println("DTLSTATENM: " + dto.getDTLSTATENM());
            System.out.println("SITEWHLADDR: " + dto.getSITEWHLADDR());
            System.out.println("RDNWHLADDR: " + dto.getRDNWHLADDR());
            System.out.println("BPLCNM: " + dto.getBPLCNM());
            System.out.println("UPTAENM: " + dto.getUPTAENM());
            System.out.println("X: " + dto.getX());
            System.out.println("Y: " + dto.getY());
            System.out.println("-----------------------");
        }
    }
}