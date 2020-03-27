 -- md5(CURRENT_TIMESTAMP())
CREATE TABLE `available_courses` (
  `course_id` varchar(255) NOT NULL,
  `teacher` varchar(255) NOT NULL,
  `category` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  PRIMARY KEY( `course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `available_courses` VALUES ('33eb3c2ba9dac72f0e96aae218d9d3b4','elif.pekmezci@gmail.com','English/Exams/Toefl','Toefl Grammar Course 1');
INSERT INTO `available_courses` VALUES ('e2bb8b2593a093e3df22e67b67f18870','mhakaydin@gmail.com','English/Exams/Toefl','Toefl Vocabulary Course 1');

CREATE TABLE `category` (
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `category` VALUES ('English/Exams/Ielts'),('English/Exams/Toefl'),('English/School/College-Level'),('English/School/HighSchool-Level'),('English/School/Primary-Level');

CREATE TABLE `location` (
  `name` varchar(255) NOT NULL,
  address varchar(255) ,
  map_url varchar(255) ,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `location` VALUES ('Internet','Internet','Internet');
INSERT INTO `location` VALUES ('Istanbul/Suadiye','suadiye mah. suadiye sk. no:1','googlemaps.com');

CREATE TABLE `server` (
  `name` varchar(255) NOT NULL,
  `ip` varchar(255) NOT NULL,
  company varchar(255) ,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `server` VALUES ('istanbul-00','127.0.0.1','test server');
INSERT INTO `server` VALUES ('istanbul-01','195.19.40.19','garantiserver.com');



CREATE TABLE `schedule` (
 `session_id` varchar(255) NOT NULL,
  `course_id` varchar(255) NOT NULL,
  `server` varchar(255) NOT NULL,
  `location` varchar(255) NOT NULL,
  `course_date` date NOT NULL,
  `course_start_hour` int(11) NOT NULL,
  `course_start_minute` int(11) NOT NULL,
  `duration` int(11) NOT NULL,
  `confirmed_by_teacher` int(1),
   PRIMARY KEY (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `schedule` VALUES ('53e271afe82693e9006fe5962e6ecbf6','33eb3c2ba9dac72f0e96aae218d9d3b4','istanbul-00','Internet','2015-07-11',18,15,2,1); -- md5(CURRENT_TIMESTAMP())
INSERT INTO `schedule` VALUES ('7318c1ff2d52e6982c01b77eb25b680d','e2bb8b2593a093e3df22e67b67f18870','istanbul-01','Istanbul/Suadiye','2015-07-12',18,15,2,0);

CREATE TABLE `attendees` (
 `session_id` varchar(255) NOT NULL,
  `attendee` varchar(255) NOT NULL,
   PRIMARY KEY (`session_id`,`attendee`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `attendees` VALUES ('53e271afe82693e9006fe5962e6ecbf6','burcu.pekmezci@gmail.com');
INSERT INTO `attendees` VALUES ('53e271afe82693e9006fe5962e6ecbf6','huray.kok@gmail.com');
INSERT INTO `attendees` VALUES ('7318c1ff2d52e6982c01b77eb25b680d','burcu.pekmezci@gmail.com');
INSERT INTO `attendees` VALUES ('7318c1ff2d52e6982c01b77eb25b680d','huray.kok@gmail.com');

CREATE TABLE `users` (
  `email` varchar(255) NOT NULL,
  `password`  varchar(255) NOT NULL,
  `realname` varchar(255) NOT NULL,
  `level` ENUM('0.manager','1.teacher','2.student') default '2.student', 
  `phone` varchar(255), 
  `address` varchar(255), 
  `schoolname` varchar(255), 
  `country` varchar(255), 
  `city` varchar(255), 
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
insert into users values ('burcu.pekmezci@gmail.com',md5('qwe123'),'Burcu PEKMEZCI','2.student','02163100320','salack uskudar','oxford','England','london');
insert into users values ('mehmet.pekmezci@gmail.com',md5('qwe123'),'Mehmet PEKMEZCI','0.manager','02163100320','salack uskudar','oxford','England','london');
insert into users values ('elif.pekmezci@gmail.com',md5('qwe123'),'Elif PEKMEZCI','1.teacher','02163100320','salack uskudar','oxford','England','london');
insert into users values ('huray.kok@gmail.com',md5('qwe123'),'Hüray Kök','2.student','02163100320','salack uskudar','oxford','England','london');
insert into users values ('erdem.kok@gmail.com',md5('qwe123'),'Erdem Kök','0.manager','02163100320','salack uskudar','oxford','England','london');
insert into users values ('mhakaydin@gmail.com',md5('qwe123'),'Hakan Akaydin','1.teacher','02163100320','salack uskudar','oxford','England','london');
