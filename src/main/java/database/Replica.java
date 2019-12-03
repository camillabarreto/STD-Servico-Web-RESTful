package database;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Replica {
    private String id;
    private String endpoint;

    public Replica(){}

    public Replica(String id, String endpoint) {
        this.id = id;
        this.endpoint = endpoint;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getEndpoint() { return endpoint; }

    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
}
