package medical.consultations;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Investigations {
    @Id
    private String id;

    private String name;

    private int duration;

    private String result;
}
