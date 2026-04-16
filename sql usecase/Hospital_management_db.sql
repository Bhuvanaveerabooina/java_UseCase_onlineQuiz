CREATE DATABASE hospital_management;
USE hospital_management;
CREATE TABLE Patients (
    patient_id NUMBER PRIMARY KEY,
    name VARCHAR2(100),
    age NUMBER,
    gender VARCHAR2(10)
);
CREATE TABLE Doctors (
    doctor_id NUMBER PRIMARY KEY,
    name VARCHAR2(100),
    specialization VARCHAR2(100)
);
CREATE TABLE Appointments (
    appointment_id NUMBER PRIMARY KEY,
    patient_id NUMBER,
    doctor_id NUMBER,
    appointment_date DATE,
    CONSTRAINT fk_ap_patient FOREIGN KEY (patient_id) REFERENCES Patients(patient_id),
    CONSTRAINT fk_ap_doctor FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id)
);
CREATE TABLE Treatments (
    treatment_id NUMBER PRIMARY KEY,
    appointment_id NUMBER,
    patient_id NUMBER,
    diagnosis VARCHAR2(100),
    cost NUMBER(10,2),
    treatment_date DATE,
    CONSTRAINT fk_tr_appointment FOREIGN KEY (appointment_id) REFERENCES Appointments(appointment_id),
    CONSTRAINT fk_tr_patient FOREIGN KEY (patient_id) REFERENCES Patients(patient_id)
);
INSERT INTO Patients VALUES (1, 'Aarav', 30, 'Male');
INSERT INTO Patients VALUES (2, 'Diya', 25, 'Female');
INSERT INTO Patients VALUES (3, 'Rohan', 40, 'Male');
INSERT INTO Patients VALUES (4, 'Sneha', 35, 'Female');
INSERT INTO Patients VALUES (5, 'Kiran', 28, 'Male');
INSERT INTO Doctors VALUES (101, 'Dr. Mehta', 'Cardiology');
INSERT INTO Doctors VALUES (102, 'Dr. Sharma', 'Neurology');
INSERT INTO Doctors VALUES (103, 'Dr. Rao', 'Orthopedics');
INSERT INTO Doctors VALUES (104, 'Dr. Priya', 'General Medicine');
INSERT INTO Appointments VALUES (1001, 1, 101, TO_DATE('2026-01-10', 'YYYY-MM-DD'));
INSERT INTO Appointments VALUES (1002, 2, 102, TO_DATE('2026-01-15', 'YYYY-MM-DD'));
INSERT INTO Appointments VALUES (1003, 1, 104, TO_DATE('2026-02-05', 'YYYY-MM-DD'));
INSERT INTO Appointments VALUES (1004, 3, 103, TO_DATE('2026-02-12', 'YYYY-MM-DD'));
INSERT INTO Appointments VALUES (1005, 4, 104, TO_DATE('2026-03-01', 'YYYY-MM-DD'));
INSERT INTO Appointments VALUES (1006, 5, 101, TO_DATE('2026-03-10', 'YYYY-MM-DD'));
INSERT INTO Appointments VALUES (1007, 2, 104, TO_DATE('2026-03-15', 'YYYY-MM-DD'));
INSERT INTO Appointments VALUES (1008, 3, 102, TO_DATE('2026-04-01', 'YYYY-MM-DD'));
INSERT INTO Appointments VALUES (1009, 1, 101, TO_DATE('2026-04-05', 'YYYY-MM-DD'));
INSERT INTO Treatments VALUES (201, 1001, 1, 'Heart Checkup', 5000, TO_DATE('2026-01-10', 'YYYY-MM-DD'));
INSERT INTO Treatments VALUES (202, 1002, 2, 'Migraine', 3000, TO_DATE('2026-01-15', 'YYYY-MM-DD'));
INSERT INTO Treatments VALUES (203, 1003, 1, 'Fever', 1500, TO_DATE('2026-02-05', 'YYYY-MM-DD'));
INSERT INTO Treatments VALUES (204, 1004, 3, 'Fracture', 8000, TO_DATE('2026-02-12', 'YYYY-MM-DD'));
INSERT INTO Treatments VALUES (205, 1005, 4, 'Diabetes', 4000, TO_DATE('2026-03-01', 'YYYY-MM-DD'));
INSERT INTO Treatments VALUES (206, 1006, 5, 'Heart Checkup', 5000, TO_DATE('2026-03-10', 'YYYY-MM-DD'));
INSERT INTO Treatments VALUES (207, 1007, 2, 'Cold and Fever', 1200, TO_DATE('2026-03-15', 'YYYY-MM-DD'));
INSERT INTO Treatments VALUES (208, 1008, 3, 'Back Pain', 2500, TO_DATE('2026-04-01', 'YYYY-MM-DD'));
INSERT INTO Treatments VALUES (209, 1009, 1, 'Heart Checkup', 5500, TO_DATE('2026-04-05', 'YYYY-MM-DD'));
COMMIT;
SELECT * FROM Patients;
SELECT * FROM Doctors;
SELECT * FROM Appointments;
SELECT * FROM Treatments;
SELECT d.doctor_id,
       d.name AS doctor_name,
       d.specialization,
       COUNT(a.appointment_id) AS total_consultations
FROM Doctors d
JOIN Appointments a
ON d.doctor_id = a.doctor_id
GROUP BY d.doctor_id, d.name, d.specialization
ORDER BY total_consultations DESC;
SELECT TO_CHAR(treatment_date, 'YYYY-MM') AS month,
       SUM(cost) AS total_revenue
FROM Treatments
GROUP BY TO_CHAR(treatment_date, 'YYYY-MM')
ORDER BY month;
SELECT diagnosis,
       COUNT(*) AS disease_count
FROM Treatments
GROUP BY diagnosis
ORDER BY disease_count DESC;
SELECT p.patient_id,
       p.name AS patient_name,
       COUNT(a.appointment_id) AS total_visits
FROM Patients p
JOIN Appointments a
ON p.patient_id = a.patient_id
GROUP BY p.patient_id, p.name
ORDER BY total_visits DESC;
SELECT d.doctor_id,
       d.name AS doctor_name,
       d.specialization,
       COUNT(DISTINCT a.appointment_id) AS total_appointments,
       COUNT(t.treatment_id) AS total_treatments,
       NVL(SUM(t.cost), 0) AS total_revenue_generated
FROM Doctors d
LEFT JOIN Appointments a
ON d.doctor_id = a.doctor_id
LEFT JOIN Treatments t
ON a.appointment_id = t.appointment_id
GROUP BY d.doctor_id, d.name, d.specialization
ORDER BY total_revenue_generated DESC;
SELECT gender, COUNT(*) AS total_patients
FROM Patients
GROUP BY gender;



