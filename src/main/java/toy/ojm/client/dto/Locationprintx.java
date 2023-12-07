package toy.ojm.client.dto;

public class Locationprintx {
    public static void main(String[] args) {
        try {

            new Locationprintx().printx();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String printx() {
        String x = null;

        RestaurantDTO rdto = new RestaurantDTO();

        x = rdto.getX(x);

        System.out.println(x);
        return x;

    }
}
