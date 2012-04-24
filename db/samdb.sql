SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP SCHEMA IF EXISTS `samdb` ;
CREATE SCHEMA IF NOT EXISTS `samdb` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `samdb` ;

-- -----------------------------------------------------
-- Table `samdb`.`sam`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `samdb`.`sam` ;

CREATE  TABLE IF NOT EXISTS `samdb`.`sam` (
  `samId` BIGINT NOT NULL AUTO_INCREMENT ,
  `fileName` VARCHAR(1024) NOT NULL ,
  `createdDate` BIGINT NOT NULL ,
  `header` TEXT NOT NULL ,
  `lengths` TEXT NOT NULL ,
  `mapped` INT NOT NULL ,
  `numberOfChromosomes` INT NOT NULL ,
  `chromosomes` TEXT NOT NULL ,
  PRIMARY KEY (`samId`) ,
  INDEX `FILENAMEINDEX` (`fileName` ASC) ,
  INDEX `CREATEDDATEINDEX` (`createdDate` ASC) )
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;


-- -----------------------------------------------------
-- Table `samdb`.`chromosome`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `samdb`.`chromosome` ;

CREATE  TABLE IF NOT EXISTS `samdb`.`chromosome` (
  `chrId` BIGINT NOT NULL AUTO_INCREMENT ,
  `chromosome` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`chrId`) ,
  UNIQUE INDEX `chromosome_UNIQUE` (`chromosome` ASC) )
ENGINE = MyISAM;


-- -----------------------------------------------------
-- Table `samdb`.`shortRead`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `samdb`.`shortRead` ;

CREATE  TABLE IF NOT EXISTS `samdb`.`shortRead` (
  `shortReadId` BIGINT NOT NULL AUTO_INCREMENT ,
  `refStart` BIGINT NOT NULL ,
  `refEnd` BIGINT NOT NULL ,
  `name` VARCHAR(1024) NOT NULL ,
  `chrId` BIGINT NOT NULL ,
  `refLength` BIGINT NOT NULL ,
  `queryStart` BIGINT NOT NULL ,
  `queryEnd` BIGINT NOT NULL ,
  `queryLength` BIGINT NOT NULL ,
  `queryBin` BIGINT NOT NULL ,
  `queryFlag` INT NOT NULL ,
  `queryTags` TEXT NOT NULL ,
  `sequence` TEXT NOT NULL ,
  `samId` BIGINT NOT NULL ,
  PRIMARY KEY (`shortReadId`) ,
  INDEX `fk_shortread_sam` (`samId` ASC) ,
  INDEX `REFSTARTENDINDEX` (`refStart` ASC, `refEnd` ASC) ,
  INDEX `fk_shortRead_chromosome1` (`chrId` ASC) )
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;


-- -----------------------------------------------------
-- Table `samdb`.`samHistogram`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `samdb`.`samHistogram` ;

CREATE  TABLE IF NOT EXISTS `samdb`.`samHistogram` (
  `samId` BIGINT NOT NULL ,
  `binSize` BIGINT NOT NULL ,
  `createdDate` BIGINT NOT NULL ,
  `samHistogramId` BIGINT NOT NULL AUTO_INCREMENT ,
  INDEX `fk_histogram_sam1` (`samId` ASC) ,
  PRIMARY KEY (`samHistogramId`) ,
  INDEX `CREATEDDATEINDEX` (`createdDate` ASC) )
ENGINE = MyISAM;


-- -----------------------------------------------------
-- Table `samdb`.`histogramBin`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `samdb`.`histogramBin` ;

CREATE  TABLE IF NOT EXISTS `samdb`.`histogramBin` (
  `samHistogramId` BIGINT NOT NULL ,
  `value` BIGINT NOT NULL ,
  `position` BIGINT NOT NULL ,
  `histogramBinId` BIGINT NOT NULL AUTO_INCREMENT ,
  `chrId` BIGINT NOT NULL ,
  INDEX `fk_histogramBin_samHistogram1` (`samHistogramId` ASC) ,
  PRIMARY KEY (`histogramBinId`) ,
  INDEX `COUNTINDEX` (`value` ASC) ,
  INDEX `POSITIONINDEX` (`position` ASC) ,
  INDEX `fk_histogramBin_chromosome1` (`chrId` ASC) )
ENGINE = MyISAM;


-- -----------------------------------------------------
-- Table `samdb`.`bed`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `samdb`.`bed` ;

CREATE  TABLE IF NOT EXISTS `samdb`.`bed` (
  `bedId` BIGINT NOT NULL AUTO_INCREMENT ,
  `fileName` VARCHAR(1024) NOT NULL ,
  `createdDate` BIGINT NOT NULL ,
  `trackName` TEXT NOT NULL ,
  `description` TEXT NOT NULL ,
  `visibility` INT NOT NULL ,
  `itemRgb` INT NOT NULL ,
  PRIMARY KEY (`bedId`) ,
  INDEX `BEDFILENAMEINDEX` (`fileName` ASC) ,
  INDEX `BEDCREATEDDATEINDEX` (`createdDate` ASC) )
ENGINE = MyISAM;


-- -----------------------------------------------------
-- Table `samdb`.`bedFragment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `samdb`.`bedFragment` ;

CREATE  TABLE IF NOT EXISTS `samdb`.`bedFragment` (
  `bedFragmentId` BIGINT NOT NULL AUTO_INCREMENT ,
  `chrId` BIGINT NOT NULL ,
  `chrStart` BIGINT NOT NULL ,
  `chrEnd` BIGINT NOT NULL ,
  `name` TEXT NOT NULL ,
  `score` INT NOT NULL ,
  `strand` INT NOT NULL ,
  `thickStart` BIGINT NOT NULL ,
  `thickEnd` BIGINT NOT NULL ,
  `itemR` INT NOT NULL ,
  `itemG` INT NOT NULL ,
  `itemB` INT NOT NULL ,
  `blockCount` BIGINT NOT NULL ,
  `blockSizes` TEXT NOT NULL ,
  `blockStarts` TEXT NOT NULL ,
  `bedId` BIGINT NOT NULL ,
  PRIMARY KEY (`bedFragmentId`) ,
  INDEX `fk_bedData_bed1` (`bedId` ASC) ,
  INDEX `CHRSTARTENDINDEX` (`chrStart` ASC, `chrEnd` ASC) ,
  INDEX `fk_bedData_chromosome1` (`chrId` ASC) )
ENGINE = MyISAM;


-- -----------------------------------------------------
-- Table `samdb`.`cytoBand`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `samdb`.`cytoBand` ;

CREATE  TABLE IF NOT EXISTS `samdb`.`cytoBand` (
  `cytobandId` BIGINT NOT NULL AUTO_INCREMENT ,
  `chrId` BIGINT NOT NULL ,
  `chrStart` BIGINT NOT NULL ,
  `chrEnd` BIGINT NOT NULL ,
  `name` TEXT NOT NULL ,
  `gieStain` VARCHAR(255) NOT NULL ,
  PRIMARY KEY (`cytobandId`) ,
  INDEX `fk_cytoBand_chromosome1` (`chrId` ASC) ,
  INDEX `CYTOBANDCHRINDEX` (`chrId` ASC) ,
  INDEX `CYTOBANDPOSITIONINDEX` (`chrStart` ASC, `chrEnd` ASC) )
ENGINE = MyISAM;


-- -----------------------------------------------------
-- Table `samdb`.`refGene`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `samdb`.`refGene` ;

CREATE  TABLE IF NOT EXISTS `samdb`.`refGene` (
  `refGeneId` BIGINT NOT NULL AUTO_INCREMENT ,
  `bin` INT NOT NULL ,
  `name` TEXT NOT NULL ,
  `chrId` BIGINT NOT NULL ,
  `strand` INT NOT NULL ,
  `txStart` BIGINT NOT NULL ,
  `txEnd` BIGINT NOT NULL ,
  `cdsStart` BIGINT NOT NULL ,
  `cdsEnd` BIGINT NOT NULL ,
  `exonCount` BIGINT NOT NULL ,
  `exonStarts` TEXT NOT NULL ,
  `exonEnds` TEXT NOT NULL ,
  `score` BIGINT NOT NULL ,
  `geneName` VARCHAR(255) NOT NULL ,
  `cdsStartStat` INT NOT NULL ,
  `cdsEndStat` INT NOT NULL ,
  `exonFrames` TEXT NOT NULL ,
  PRIMARY KEY (`refGeneId`) ,
  INDEX `fk_refGene_chromosome1` (`chrId` ASC) ,
  INDEX `REFGENENAMEINDEX` (`geneName` ASC) ,
  INDEX `REFGENECHRINDEX` (`chrId` ASC) ,
  INDEX `REFGENETXPOSINDEX` (`txStart` ASC, `txEnd` ASC) )
ENGINE = MyISAM;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
