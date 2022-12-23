-- option ¹1-------------------------------------
CREATE TABLE car
(
    id    SERIAL PRIMARY KEY,
    brand CHARACTER VARYING(30) NOT NULL,
    model CHARACTER VARYING(30) NOT NULL,
    price NUMERIC(12, 2)        NOT NULL
);

CREATE TABLE Person
(
    id             SERIAL PRIMARY KEY,
    name           CHARACTER VARYING(30) NOT NULL,
    age            INTEGER               NOT NULL,
    driver_license BOOLEAN               NOT NULL,
    car_id         INTEGER,
    FOREIGN KEY (car_id) REFERENCES car (id)
);

-- option ¹2---------------------------------------

CREATE TABLE person
(
    id             SERIAL PRIMARY KEY,
    name           CHARACTER VARYING(30) NOT NULL,
    age            INTEGER               NOT NULL,
    driver_license BOOLEAN               NOT NULL
);

CREATE TABLE car
(
    id    SERIAL PRIMARY KEY,
    brand CHARACTER VARYING(30) NOT NULL,
    model CHARACTER VARYING(30) NOT NULL,
    price NUMERIC(12, 2)        NOT NULL
);

CREATE TABLE trip
(
    person_id INTEGER PRIMARY KEY,
    car_id    INTEGER,
    FOREIGN KEY (person_id) REFERENCES person(id),
    FOREIGN KEY (car_id) REFERENCES car (id)
);