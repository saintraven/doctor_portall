-- для индексов
CREATE INDEX idx_appointments_doctor ON appointments(doctor);
CREATE INDEX idx_appointments_analytics ON appointments(doctor, status);
CREATE INDEX idx_appointments_date ON appointments(appointment_date);
