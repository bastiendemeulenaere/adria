DROP table IF EXISTS quotes;
DROP table IF EXISTS categories;
DROP TABLE IF EXISTS events;
DROP table IF EXISTS interests;
DROP TABLE IF EXISTS user_events;
DROP TABLE IF EXISTS user_interests;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS notifications;

create table quotes
(
    id    int auto_increment,
    quote varchar(255)
);

create table categories
(
    id int auto_increment PRIMARY KEY,
    category VARCHAR(255) NOT NULL
);

create table events
(
    id int auto_increment PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    eventType VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    amountOfPeople int NOT NULL,
    categoryId int NOT NULL,
    organiserId VARCHAR(255) NOT NULL,
    sector int NOT NULL,
    startDateTime DATETIME NOT NULL,
    hours int NOT NULL,
    canceled BOOLEAN DEFAULT false,
    locX int DEFAULT -1,
    locY int DEFAULT -1
);
CREATE TABLE users (
                       id VARCHAR(255) PRIMARY KEY,
                       first_name VARCHAR(255),
                       last_name VARCHAR(255),
                       about_me VARCHAR(255)
);

CREATE TABLE user_events (
                             userId VARCHAR(255),
                             eventId INT
);

CREATE TABLE user_interests (
                                userId VARCHAR(255),
                                categoryId INT
);

create table notifications (
                               id INT AUTO_INCREMENT PRIMARY KEY,
                               userId VARCHAR(255),
                               eventId INT NOT NULL,
                               title VARCHAR(255) NOT NULL,
                               startTime DATETIME NOT NULL,
                               description VARCHAR(255) NOT NULL,
                               read BOOLEAN NOT NULL
);