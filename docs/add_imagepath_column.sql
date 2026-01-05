-- Migration: Add ImagePath column to package table
-- Run this SQL script in your MySQL database to add image support for tour packages

-- Add the ImagePath column to the package table
ALTER TABLE `package` 
ADD COLUMN `ImagePath` VARCHAR(255) DEFAULT NULL 
AFTER `CreatedByEmployeeID`;

-- Verify the column was added
DESCRIBE `package`;

-- Example: Update existing packages with placeholder images (optional)
-- UPDATE `package` SET `ImagePath` = 'Images/packages/default.jpg' WHERE `ImagePath` IS NULL;
