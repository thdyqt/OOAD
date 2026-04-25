CREATE DATABASE IF NOT EXISTS CalendarDB;
USE CalendarDB;

CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE Calendars (
    calendar_id INT AUTO_INCREMENT PRIMARY KEY,
    owner_id INT NOT NULL, 
    name VARCHAR(100) DEFAULT 'Lịch Mặc Định',
    time_zone VARCHAR(50) DEFAULT 'Asia/Ho_Chi_Minh',
    FOREIGN KEY (owner_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

CREATE TABLE Appointments (
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    calendar_id INT NOT NULL,
    name VARCHAR(200) NOT NULL,
    location VARCHAR(200),
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    is_group_meeting BOOLEAN DEFAULT FALSE, 
    FOREIGN KEY (calendar_id) REFERENCES Calendars(calendar_id) ON DELETE CASCADE
);

CREATE TABLE Reminders (
    reminder_id INT AUTO_INCREMENT PRIMARY KEY,
    appointment_id INT NOT NULL,
    reminder_type VARCHAR(50), 
    target_time DATETIME NOT NULL,
    message VARCHAR(255),
    FOREIGN KEY (appointment_id) REFERENCES Appointments(appointment_id) ON DELETE CASCADE
);

CREATE TABLE Meeting_Participants (
    appointment_id INT NOT NULL,
    user_id INT NOT NULL,
    joined_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (appointment_id, user_id), 
    FOREIGN KEY (appointment_id) REFERENCES Appointments(appointment_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

INSERT INTO Users (username, email, password) VALUES 
('Phan Thanh Duy', '102240130@sv1.dut.udn.vn', 123456),
('Giang Vien', 'gv@dut.udn.vn', 123456);

INSERT INTO Calendars (owner_id) VALUES 
(1), (2);

INSERT INTO Appointments (calendar_id, name, location, start_time, end_time, is_group_meeting) 
VALUES (1, 'Bảo vệ Đồ án PBL3', 'Phòng E2.204', '2026-06-10 07:00:00', '2026-06-10 10:00:00', FALSE);

INSERT INTO Reminders (appointment_id, reminder_type, target_time, message)
VALUES (1, '1_HOUR_BEFORE', '2026-06-10 06:00:00', 'Chuẩn bị slide bảo vệ!');

INSERT INTO Appointments (calendar_id, name, location, start_time, end_time, is_group_meeting) 
VALUES (2, 'Họp cố vấn học tập sinh viên', 'Online Teams', '2026-05-10 19:00:00', '2026-05-10 20:00:00', TRUE);

INSERT INTO Meeting_Participants (appointment_id, user_id)
VALUES (2, 1);