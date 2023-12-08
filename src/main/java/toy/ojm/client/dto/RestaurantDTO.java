package toy.ojm.client.dto;

public class RestaurantDTO {
    private String DTLSTATENM;
    private String SITEWHLADDR;
    private String RDNWHLADDR;
    private String BPLCNM;
    private String UPTAENM;
    private String X;
    private String Y;

    public String getDTLSTATENM() {
        return DTLSTATENM;
    }

    public void setDTLSTATENM(String DTLSTATENM) {
        this.DTLSTATENM = DTLSTATENM;
    }

    public String getSITEWHLADDR() {
        return SITEWHLADDR;
    }

    public void setSITEWHLADDR(String SITEWHLADDR) {
        this.SITEWHLADDR = SITEWHLADDR;
    }

    public String getRDNWHLADDR() {
        return RDNWHLADDR;
    }

    public void setRDNWHLADDR(String RDNWHLADDR) {
        this.RDNWHLADDR = RDNWHLADDR;
    }

    public String getBPLCNM() {
        return BPLCNM;
    }

    public void setBPLCNM(String BPLCNM) {
        this.BPLCNM = BPLCNM;
    }

    public String getUPTAENM() {
        return UPTAENM;
    }

    public void setUPTAENM(String UPTAENM) {
        this.UPTAENM = UPTAENM;
    }

    public String getX() {
        return X;
    }

    public void setX(String X) {
        this.X = X;
    }

    public String getY() {
        return Y;
    }

    public void setY(String Y) {
        this.Y = Y;
    }
}