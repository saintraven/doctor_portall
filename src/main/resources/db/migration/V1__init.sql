CREATE TABLE doctors
(
    id UUID PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    speciality VARCHAR(255) NOT NULL,
    experienced_years INTEGER NOT NULL,
    price_per_visit NUMERIC(38, 2) NOT NULL,
    image_url VARCHAR(255)
);


CREATE TABLE appointments
(
    id UUID PRIMARY KEY,
    doctor UUID NOT NULL,
    patient_name VARCHAR(255) NOT NULL,
    appointment_date TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    CONSTRAINT fk_appointment_doctor FOREIGN KEY (doctor) REFERENCES doctors(id)
);
