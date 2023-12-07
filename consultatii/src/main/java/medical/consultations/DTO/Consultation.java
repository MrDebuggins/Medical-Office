package medical.consultations;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document
public class Consultation {
    @Id
    private String id;

    private Long idPatient;

    private Long idDoctor;

    private Date date;

    private List<Investigations> investigations;
}
