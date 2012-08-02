/*SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';*/

DROP SCHEMA IF EXISTS `shakespeare` ;
CREATE SCHEMA IF NOT EXISTS `shakespeare` DEFAULT CHARACTER SET latin1 ;


-- -----------------------------------------------------
-- Table `shakespeare`.`works`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shakespeare`.`works` ;

CREATE  TABLE IF NOT EXISTS `shakespeare`.`works` (
  `WorkID` VARCHAR(30) NOT NULL DEFAULT '' ,
  `Title` VARCHAR(30) NULL DEFAULT NULL ,
  `LongTitle` VARCHAR(255) NULL DEFAULT NULL ,
  `Date` SMALLINT(6) NULL DEFAULT NULL ,
  `GenreType` ENUM('h','c','t','p','s') NULL DEFAULT NULL ,
  `Notes` LONGTEXT NULL DEFAULT NULL ,
  `Source` VARCHAR(30) NULL DEFAULT NULL ,
  `TotalWords` BIGINT(20) NULL DEFAULT NULL ,
  `TotalParagraphs` BIGINT(20) NULL DEFAULT NULL ,
  PRIMARY KEY (`WorkID`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `shakespeare`.`characters`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shakespeare`.`characters` ;

CREATE  TABLE IF NOT EXISTS `shakespeare`.`characters` (
  `CharID` VARCHAR(50) NOT NULL DEFAULT '' ,
  `CharName` VARCHAR(100) NULL DEFAULT NULL ,
  `Abbrev` VARCHAR(50) NULL DEFAULT NULL ,
  `Description` TEXT NULL DEFAULT NULL ,
  `SpeechCount` INT(11) NULL DEFAULT NULL ,
  `WorkID` VARCHAR(30) NOT NULL ,
  PRIMARY KEY (`CharID`),
  INDEX `FK_characters_work` (`WorkID` ASC) ,
  CONSTRAINT `FK_characters_work`
    FOREIGN KEY (`WorkID` )
    REFERENCES `shakespeare`.`works` (`WorkID` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `shakespeare`.`characterworks`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shakespeare`.`character_works` ;

/*CREATE  TABLE IF NOT EXISTS `shakespeare`.`character_works` (
  `Character` VARCHAR(50) NULL DEFAULT NULL ,
  `Work` VARCHAR(30) NULL DEFAULT NULL ,
  INDEX `FK_character_works_character` (`Character` ASC) ,
  INDEX `FK_character_works_work` (`Work` ASC) ,
  CONSTRAINT `FK_character_works_character`
    FOREIGN KEY (`Character` )
    REFERENCES `shakespeare`.`characters` (`CharID` ),
  CONSTRAINT `FK_character_works_work`
    FOREIGN KEY (`Work` )
    REFERENCES `shakespeare`.`works` (`WorkID` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;*/

-- -----------------------------------------------------
-- Table `shakespeare`.`sections`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shakespeare`.`sections` ;

CREATE  TABLE IF NOT EXISTS `shakespeare`.`sections` (
  `WorkID` VARCHAR(30) NOT NULL ,
  `SectionID` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `Section` SMALLINT(6) NULL DEFAULT NULL ,
  PRIMARY KEY (`SectionID`) ,
  INDEX `FK_sections_work` (`WorkID` ASC) ,
  CONSTRAINT `FK_sections_work`
    FOREIGN KEY (`WorkID` )
    REFERENCES `shakespeare`.`works` (`WorkID` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `shakespeare`.`chapters`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shakespeare`.`chapters` ;

CREATE  TABLE IF NOT EXISTS `shakespeare`.`chapters` (
  `WorkID` VARCHAR(30) NOT NULL ,
  `ChapterID` BIGINT(20) NOT NULL ,
  `Section` SMALLINT(6) NULL DEFAULT NULL ,
  `Chapter` SMALLINT(6) NULL DEFAULT NULL ,
  `Description` TEXT NULL DEFAULT NULL ,
  `SectionID` BIGINT(20) NOT NULL,
  PRIMARY KEY (`ChapterID`) ,
  INDEX `FK_chapters_work` (`WorkID` ASC) ,
  INDEX `FK_chapters_section` (`SectionID` ASC) ,
  CONSTRAINT `FK_chapters_work`
    FOREIGN KEY (`WorkID` )
    REFERENCES `shakespeare`.`works` (`WorkID` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `shakespeare`.`paragraphs`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shakespeare`.`paragraphs` ;

CREATE  TABLE IF NOT EXISTS `shakespeare`.`paragraphs` (
  `WorkID` VARCHAR(30) NOT NULL ,
  `ParagraphID` BIGINT(20) NOT NULL DEFAULT '0' ,
  `ParagraphNum` BIGINT(20) NULL DEFAULT NULL ,
  `CharID` VARCHAR(50) NULL DEFAULT NULL ,
  `PlainText` LONGTEXT NULL DEFAULT NULL ,
  `PhoneticText` LONGBLOB NULL DEFAULT NULL ,
  `StemText` LONGBLOB NULL DEFAULT NULL ,
  `ParagraphType` SMALLINT(6) NULL DEFAULT NULL ,
  `Section` SMALLINT(6) NULL DEFAULT NULL ,
  `Chapter` SMALLINT(6) NULL DEFAULT NULL ,
  `CharCount` BIGINT(20) NULL DEFAULT NULL ,
  `WordCount` BIGINT(20) NULL DEFAULT NULL ,
  `SectionID` BIGINT(20) NULL DEFAULT NULL ,
  `ChapterID` BIGINT(20) NULL DEFAULT NULL ,
  PRIMARY KEY (`ParagraphID`) ,
  INDEX `FK_paragraphs_work` (`WorkID` ASC) ,
  INDEX `FK_paragraphs_character` (`CharID` ASC) ,
  CONSTRAINT `FK_paragraphs_work`
    FOREIGN KEY (`WorkID` )
    REFERENCES `shakespeare`.`works` (`WorkID` ),
  CONSTRAINT `FK_paragraphs_character`
    FOREIGN KEY (`CharID` )
    REFERENCES `shakespeare`.`characters` (`CharID` ),
  CONSTRAINT `FK_paragraphs_section`
    FOREIGN KEY (`SectionID` )
    REFERENCES `shakespeare`.`sections` (`SectionID` ),
    CONSTRAINT `FK_paragraphs_chapter`
    FOREIGN KEY (`ChapterID` )
    REFERENCES `shakespeare`.`chapters` (`ChapterID` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `shakespeare`.`actors`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shakespeare`.`actors` ;

CREATE  TABLE IF NOT EXISTS `shakespeare`.`actors` (
  `Name` VARCHAR(100) NOT NULL ,
  /*`WorkID` VARCHAR(30) NULL DEFAULT NULL,*/
  `Character` VARCHAR(50) NULL DEFAULT NULL,
  PRIMARY KEY (`Name`),
  /*CONSTRAINT `FK_actor_work`
    FOREIGN KEY (`WorkID` )
    REFERENCES `shakespeare`.`works` (`WorkID` ),*/
  CONSTRAINT `FK_actor_character`
    FOREIGN KEY (`Character` )
    REFERENCES `shakespeare`.`characters` (`CharID` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `shakespeare`.`casting`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shakespeare`.`casting` ;

CREATE  TABLE IF NOT EXISTS `shakespeare`.`casting` (
  `Actor` VARCHAR(100) NULL DEFAULT NULL ,
  /*`Work` VARCHAR(30) NULL DEFAULT NULL ,*/
  `Character` VARCHAR(50) NULL DEFAULT NULL ,
  INDEX `FK_casting_actor` (`Actor` ASC) ,
  /*INDEX `FK_casting_work` (`Work` ASC) ,*/
  INDEX `FK_casting_character` (`Character` ASC) ,
  CONSTRAINT `FK_casting_actor`
    FOREIGN KEY (`Actor` )
    REFERENCES `shakespeare`.`actors` (`Name` ),
  /*CONSTRAINT `FK_casting_work`
    FOREIGN KEY (`Work` )
    REFERENCES `shakespeare`.`works` (`WorkID` ),*/
  CONSTRAINT `FK_casting_character`
    FOREIGN KEY (`Character` )
    REFERENCES `shakespeare`.`characters` (`CharID` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


/*SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;*/
