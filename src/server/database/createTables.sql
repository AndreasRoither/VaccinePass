CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS users (
	user_id uuid PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
	name VARCHAR(25) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
	password VARCHAR(255) NOT NULL,
    mail VARCHAR(50) NOT NULL,
    birth DATE NOT NULL,
    weight decimal NOT NULL,
    height smallint NOT NULL,
    bloodtype VARCHAR(10) NOT NULL,
    maindoctor VARCHAR(50),
    UNIQUE(mail)
);

CREATE TABLE IF NOT EXISTS doctors (
	user_id uuid PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
	name VARCHAR(25) NOT NULL,
	password VARCHAR(255) NOT NULL,
    mail VARCHAR(50) NOT NULL,
    public_key VARCHAR(255) NOT NULL,
    UNIQUE(mail)
);

CREATE TABLE IF NOT EXISTS vaccinations (
	vaccination_id uuid PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    name VARCHAR(50) NOT NULL,
    user_id uuid NOT NULL,
    vaccination_date DATE NOT NULL,
    due_date DATE NOT NULL,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id) 
        REFERENCES users(user_id)
);


