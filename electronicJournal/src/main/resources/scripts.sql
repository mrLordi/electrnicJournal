CREATE SCHEMA `electronic_journal` DEFAULT CHARACTER SET utf8 ;

CREATE TABLE `electronic_journal`.`persons` (
  `person_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `patronymic` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `birth` DATE NOT NULL,
  `login` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `class_id` INT NULL,
  `school_id` INT NULL,
  `discriminator` VARCHAR(45) NOT NULL,
  `qualification` VARCHAR(45) NULL,
  `phone_number` VARCHAR(10) NOT NULL,
  `address` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`person_id`),
  UNIQUE INDEX `person_id_UNIQUE` (`person_id` ASC),
  UNIQUE INDEX `login_UNIQUE` (`login` ASC),
  UNIQUE INDEX `school_id_UNIQUE` (`school_id` ASC));

CREATE TABLE `electronic_journal`.`disciplines` (
  `discipline_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`discipline_id`),
  UNIQUE INDEX `discipline_id_UNIQUE` (`discipline_id` ASC),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC));

CREATE TABLE `electronic_journal`.`class` (
  `class_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `class_teacher_id` INT NOT NULL,
  `school_id` INT NOT NULL,
  PRIMARY KEY (`class_id`),
  UNIQUE INDEX `class_id_UNIQUE` (`class_id` ASC),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC),
  UNIQUE INDEX `class_teacher_id_UNIQUE` (`class_teacher_id` ASC),
  UNIQUE INDEX `school_id_UNIQUE` (`school_id` ASC),
  CONSTRAINT `class_teacher`
  FOREIGN KEY (`class_teacher_id`)
  REFERENCES `electronic_journal`.`persons` (`person_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

CREATE TABLE `electronic_journal`.`teacher_class` (
  `tc_id` INT NOT NULL AUTO_INCREMENT,
  `person_id` INT NOT NULL,
  `class_id` INT NOT NULL,
  PRIMARY KEY (`tc_id`),
  UNIQUE INDEX `tc_id_UNIQUE` (`tc_id` ASC),
  INDEX `tc_person_idx` (`person_id` ASC),
  INDEX `tc_class_idx` (`class_id` ASC),
  CONSTRAINT `tc_person`
  FOREIGN KEY (`person_id`)
  REFERENCES `electronic_journal`.`persons` (`person_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `tc_class`
  FOREIGN KEY (`class_id`)
  REFERENCES `electronic_journal`.`class` (`class_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

CREATE TABLE `electronic_journal`.`teacher_discipline` (
  `td_id` INT NOT NULL AUTO_INCREMENT,
  `person_id` INT NOT NULL,
  `discipline_id` INT NOT NULL,
  PRIMARY KEY (`td_id`),
  UNIQUE INDEX `td_id_UNIQUE` (`td_id` ASC),
  INDEX `td_teacher_idx` (`person_id` ASC),
  INDEX `td_discipline_idx` (`discipline_id` ASC),
  CONSTRAINT `td_person`
  FOREIGN KEY (`person_id`)
  REFERENCES `electronic_journal`.`persons` (`person_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `td_discipline`
  FOREIGN KEY (`discipline_id`)
  REFERENCES `electronic_journal`.`disciplines` (`discipline_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

CREATE TABLE `electronic_journal`.`school` (
  `school_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `phone` VARCHAR(10) NOT NULL,
  `address` VARCHAR(45) NOT NULL,
  `information` LONGTEXT NOT NULL,
  `director_id` INT NOT NULL,
  PRIMARY KEY (`school_id`),
  UNIQUE INDEX `school_id_UNIQUE` (`school_id` ASC),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC),
  UNIQUE INDEX `phone_UNIQUE` (`phone` ASC),
  UNIQUE INDEX `address_UNIQUE` (`address` ASC),
  UNIQUE INDEX `director_id_UNIQUE` (`director_id` ASC),
  CONSTRAINT `director`
  FOREIGN KEY (`director_id`)
  REFERENCES `electronic_journal`.`persons` (`person_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `class`
  FOREIGN KEY (`school_id`)
  REFERENCES `electronic_journal`.`class` (`class_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);




