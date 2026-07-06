package repository;

import model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface DoctorsRepository extends JpaRepository<Doctor, UUID> {

}

