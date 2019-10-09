create table ADDRESS
(
	id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) CONSTRAINT ADDRESS_PK PRIMARY KEY,
	STREET VARCHAR(256),
	NUM VARCHAR(256),
	ZIP VARCHAR(32),
	PROVINCE VARCHAR(256),
	COUNTRY VARCHAR(256),
	LAT DOUBLE,
	LON DOUBLE
);

create table USERS
(
	id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) CONSTRAINT USERS_PK PRIMARY KEY,
	username VARCHAR(256),
	password VARCHAR(512),
	email VARCHAR(256),
	address_id INTEGER NOT NULL,
	validated BOOLEAN,
	banned BOOLEAN,
	created TIMESTAMP,
	karma INTEGER,
	CONSTRAINT address_id_ref FOREIGN KEY (address_id) REFERENCES ADDRESS(id)
)

------------------------- Part 1

create table CATEGORY
(
	id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) CONSTRAINT CATEG_PK PRIMARY KEY,
	name VARCHAR(256)
)

------------------------ Part 2

create table ITEM
(
	id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) CONSTRAINT ITEM_PK PRIMARY KEY,
	description VARCHAR(512),
	picUrl VARCHAR(512)
);
create table ITEM_CATEGORY
(
	item_id INTEGER NOT NULL,
	categ_id INTEGER NOT NULL,
	CONSTRAINT categ_id_ref FOREIGN KEY (categ_id) REFERENCES CATEGORY(id),
	CONSTRAINT item_id_ref FOREIGN KEY (item_id) REFERENCES ITEM(id)
);


create table DONATION
(
	id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) CONSTRAINT DONATION_PK PRIMARY KEY,
	user_id INTEGER NOT NULL,
	item_id INTEGER NOT NULL,
	picking_time TIMESTAMP,
	resolving_time TIMESTAMP,
	address_id INTEGER,
	karma INTEGER,
	created TIMESTAMP,
	status VARCHAR(128),
	CONSTRAINT item_id_donation_ref FOREIGN KEY (item_id) REFERENCES ITEM(id),
	CONSTRAINT user_id_donation_ref FOREIGN KEY (user_id) REFERENCES USERS(id),
	CONSTRAINT address_id_donation_ref FOREIGN KEY (address_id) REFERENCES ADDRESS(id)
);

create table PETITION
(
	id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) CONSTRAINT PETITION_PK PRIMARY KEY,
	user_id INTEGER NOT NULL,
	donation_id INTEGER NOT NULL,
	karma INTEGER,
	created TIMESTAMP,
	CONSTRAINT donation_id_petition_ref FOREIGN KEY (donation_id) REFERENCES DONATION(id),
	CONSTRAINT user_id_petition_ref FOREIGN KEY (user_id) REFERENCES USERS(id)
);

CREATE TABLE DONATIONS_LOG(
	id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) CONSTRAINT DONATION_LOG_PK PRIMARY KEY,
	donation_id INTEGER NOT NULL,
	action_done VARCHAR(256)
);

ALTER TABLE USERS ADD gold_date TIMESTAMP;
ALTER TABLE USERS ADD last_subs_date TIMESTAMP;
ALTER TABLE USERS ADD years TIMESTAMP;
ALTER TABLE USERS ADD discr INTEGER;
