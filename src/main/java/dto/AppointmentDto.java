package dto;

import model.Status;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class AppointmentDto {
    private UUID doctorId;
    private String patientName;
    private LocalDateTime appointmentDate;
    private Status status;

}
