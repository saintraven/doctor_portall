import random
import datetime
from locust import HttpUser, task, between

class DoctorPortalLoadTester(HttpUser):
    wait_time = between(0.05, 0.2)

    def on_start(self):
        self.doctor_ids = []
        
        pixel_1x1 = (
            b'\x89PNG\r\n\x1a\n\x00\x00\x00\rIHDR\x00\x00\x00\x01\x00\x00\x00\x01'
            b'\x08\x06\x00\x00\x00\x1f\x15c4\x00\x00\x00\rIDATx\x9cc`\x00\x00\x00'
            b'\x02\x00\x01H\xaf\xa4q\x00\x00\x00\x00IEND\xaeB`\x82'
        )
        
        for i in range(5):
            response = self.client.post("/api/v1/doctors", json={
                "fullName": f"Doctor {i}",
                "speciality": "therapist",
                "experienceYears": random.randint(1, 40),
                "pricePerVisit": 2000.00
            })
            
            if response.status_code == 201:
                doctor_id = response.json()["id"]
                self.doctor_ids.append(doctor_id)
                
                self.client.put(
                    f"/api/v1/doctors/{doctor_id}/image",
                    files={"file": ("avatar.png", pixel_1x1, "image/png")}
                )

    @task(40)
    def get_appointments_by_doctor(self):
        if self.doctor_ids:
            random_doctor_id = random.choice(self.doctor_ids)
            self.client.get(f"/api/v1/doctors/{random_doctor_id}/appointments")

    @task(4)
    def create_new_appointment(self):
        if self.doctor_ids:
            random_doctor_id = random.choice(self.doctor_ids)
            
            base_date = datetime.datetime.now()
            random_minutes = random.randint(1, 10000000)
            unique_date = (base_date + datetime.timedelta(minutes=random_minutes)).strftime("%Y-%m-%dT%H:%M:%S")
            
            self.client.post("/api/v1/appointments", json={
                "doctorId": random_doctor_id,
                "patientName": f"Patient {random.randint(1, 1000000)}",
                "appointmentDate": unique_date,
                "status": random.choice(["scheduled", "completed", "no_show"])
            })

    @task(1)
    def get_analytics_summary(self):
        self.client.get("/api/v1/analytics")