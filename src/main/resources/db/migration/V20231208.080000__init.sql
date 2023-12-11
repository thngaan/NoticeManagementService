START TRANSACTION;

# Account
CREATE TABLE IF NOT EXISTS `Account`(
	`Id` BIGINT AUTO_INCREMENT NOT NULL,
	`Username` VARCHAR(255) NULL,
	`AccountStatus` VARCHAR(255) NULL,
	`FullName` VARCHAR(255) NULL,
	`Password` VARCHAR(255) NULL,
	`PhoneNumber` VARCHAR(255) NULL,
	`Email` VARCHAR(255) NULL,
	`CreatedDate` DATETIME NOT NULL,
	`UpdatedDate` DATETIME NULL,
    PRIMARY KEY (Id)
);


# Notice
CREATE TABLE IF NOT EXISTS `Notice`(
	`Id` BIGINT AUTO_INCREMENT NOT NULL,
	`AuthorId` BIGINT NOT NULL,
	`StartDate` DATETIME NOT NULL,
	`EndDate` DATETIME NOT NULL,
	`NoticeStatus` VARCHAR(50) NULL,
	`Title` VARCHAR(500) NULL,
	`ContentDetail` VARCHAR(50) NULL,
	`IsDeleted` BIT NULL,
	`CreatedDate` DATETIME NOT NULL,
	`UpdatedDate` DATETIME NULL,
    PRIMARY KEY (Id),
    KEY `AuthorId` (`AuthorId`),
        CONSTRAINT `FkNoticeAuthor` FOREIGN KEY (`AuthorId`) REFERENCES `Account` (`Id`)
);

# Document
CREATE TABLE IF NOT EXISTS `Document`(
	`Id` BIGINT AUTO_INCREMENT NOT NULL,
	`FileContent` TINYBLOB NULL,
	`FileName` VARCHAR(255) NULL,
	`FileSize` BIGINT NULL,
	`Thumbnail` TINYBLOB NULL,
	`ContentType` VARCHAR(255) NULL,
	`NoticeId` BIGINT NOT NULL,
	`CreatedDate` DATETIME NOT NULL,
	`UpdatedDate` DATETIME NULL,
    PRIMARY KEY (`Id`),
    KEY `NoticeId` (`NoticeId`),
    CONSTRAINT `FkDocumentNotice` FOREIGN KEY (`NoticeId`) REFERENCES `Notice` (`Id`)
);

# ViewNotice
CREATE TABLE IF NOT EXISTS `ViewNotice`(
	`Id` BIGINT AUTO_INCREMENT NOT NULL,
	`NoticeId` BIGINT NOT NULL,
	`AccountId` BIGINT NOT NULL,
	`Score` INT NOT NULL,
	`CreatedDate` DATETIME NOT NULL,
	`UpdatedDate` DATETIME NULL,
    PRIMARY KEY (Id),
    KEY `AccountId` (`AccountId`),
    CONSTRAINT `FkViewViewer` FOREIGN KEY (`AccountId`) REFERENCES `Account` (`Id`),
    KEY `NoticeId` (`NoticeId`),
    CONSTRAINT `FkViewNotice` FOREIGN KEY (`NoticeId`) REFERENCES `Notice` (`Id`)
);

COMMIT;