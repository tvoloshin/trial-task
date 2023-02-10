CREATE TABLE Users (
	id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	username varchar(100) UNIQUE NOT NULL,
	password varchar(100) NOT NULL,
	email varchar(100) NOT NULL,
	created_at timestamp NOT NULL
);

CREATE TABLE Quotes (
	id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	user_id int REFERENCES Users(id),
	quote varchar(1000) NOT NULL,
	score int NOT NULL DEFAULT 0,
	created_at timestamp NOT NULL,
	updated_at timestamp NOT NULL
);

CREATE TABLE Votes (
	id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	user_id int REFERENCES Users(id),
	quote_id int REFERENCES Quotes(id),
	score int NOT NULL CHECK (abs(score) <= 1),
	created_at timestamp NOT NULL
);