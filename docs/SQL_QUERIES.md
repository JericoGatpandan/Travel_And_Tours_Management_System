# SQL Queries Documentation

This document provides a comprehensive list of SQL queries used in the Travel Management System, categorized by their functionality.

## 1. Authentication & Employee Management
**Source:** `EmployeeRepositoryImpl.java`

### Login Verification
Verifies user credentials against the database.
```sql
SELECT COUNT(1) FROM employee WHERE email = ? AND password = ? AND isActive = 1
-- Appended for Admins: AND isManager = 1
```

### Get Authenticated User
Retrieves full details of the currently logged-in user.
```sql
SELECT * FROM employee WHERE email = ?
```

### Get All Employees
Retrieves a list of all employees, sorted by name.
```sql
SELECT * FROM employee ORDER BY name ASC
```

### Get Active Employees
Retrieves only active employees.
```sql
SELECT * FROM employee WHERE isActive = 1 ORDER BY name ASC
```

### Search Employees
Searches for employees by name or email.
```sql
SELECT * FROM employee WHERE name LIKE ? OR email LIKE ? ORDER BY name ASC
```

### Create Employee
Inserts a new employee record.
```sql
INSERT INTO employee (name, email, password, contactNumber, isManager, isActive) VALUES (?, ?, ?, ?, ?, ?)
```

### Update Employee
Updates an existing employee's details.
```sql
-- With Password Update
UPDATE employee SET name = ?, email = ?, password = ?, contactNumber = ?, isManager = ?, isActive = ? WHERE employeeId = ?

-- Without Password Update
UPDATE employee SET name = ?, email = ?, contactNumber = ?, isManager = ?, isActive = ? WHERE employeeId = ?
```

### Delete Employee
Deletes an employee record.
```sql
DELETE FROM employee WHERE employeeId = ?
```

### Check Bookings (Before Delete)
Checks if an employee has associated bookings before deletion.
```sql
SELECT COUNT(*) FROM booking WHERE EmployeeID = ?
```

### Toggle Employee Status
Deactivates or activates an employee account (used when deletion is not possible).
```sql
UPDATE employee SET isActive = ? WHERE employeeId = ?
```

### Employee Performance
Retrieves booking counts for each employee.
```sql
SELECT e.name, COUNT(b.BookingID) as bookingCount 
FROM employee e 
LEFT JOIN booking b ON e.employeeId = b.EmployeeID 
WHERE e.isActive = 1 
GROUP BY e.employeeId, e.name 
ORDER BY bookingCount DESC
```

### Top Performers
Retrieves detailed performance stats (bookings + total sales).
```sql
SELECT e.name, COUNT(b.BookingID) as bookingCount, 
COALESCE(SUM(pay.amount), 0) as totalSales 
FROM employee e 
LEFT JOIN booking b ON e.employeeId = b.EmployeeID 
LEFT JOIN payment pay ON b.BookingID = pay.bookingId AND pay.status = 'PAID' 
WHERE e.isActive = 1 
GROUP BY e.employeeId, e.name 
ORDER BY bookingCount DESC, totalSales DESC 
LIMIT ?
```

## 2. Client Management
**Source:** `ClientRepositoryImpl.java`

### Get All Clients
Retrieves all clients.
```sql
SELECT * FROM client
```

### Search Clients
Searches for clients by name, email, or ID.
```sql
SELECT * FROM client WHERE name LIKE ? OR email LIKE ? OR clientId = ? LIMIT 10
```

### Create Client
Inserts a new client record.
```sql
INSERT INTO client (name, email, address, contactNumber, customerType, dateRegistered) VALUES (?, ?, ?, ?, ?, ?)
```

### Update Client
Updates client details.
```sql
UPDATE client SET name = ?, email = ?, contactNumber = ?, address = ?, customerType = ? WHERE clientId = ?
```

### Delete Client
Removes a client record.
```sql
DELETE FROM client WHERE clientId = ?
```

### Employee's Client List
Retrieves clients assigned to a specific employee with their latest trip status.
```sql
SELECT DISTINCT c.clientId, c.name, c.email, c.contactNumber, p.Destination, b.Status, 
CONCAT(MIN(t.StartDate), ' to ', MAX(t.EndDate)) AS TripDates 
FROM client c 
LEFT JOIN booking b ON c.clientId = b.ClientID 
LEFT JOIN package p ON b.PackageID = p.PackageID 
LEFT JOIN packagetrips pt ON p.PackageID = pt.PackageID 
LEFT JOIN trip t ON pt.TripID = t.TripID 
GROUP BY c.clientId, c.name, c.email, c.contactNumber, p.Destination, b.Status 
ORDER BY c.name ASC
```

## 3. Tour Package Management
**Source:** `TourPackageRepositoryImpl.java`

### Get All Packages
Retrieves all tour packages.
```sql
SELECT * FROM package
```

### Get Active Packages
Retrieves only active packages.
```sql
SELECT * FROM package WHERE IsActive = 1
```

### Search Packages
Searches packages by name, destination, or description.
```sql
SELECT * FROM package WHERE Name LIKE ? OR Destination LIKE ? OR Description LIKE ?
```

### Create Package
Inserts a new tour package.
```sql
INSERT INTO package (Name, Description, Destination, Duration, MaxPax, Inclusions, Price, IsActive, CreatedByEmployeeId, ImagePath) 
VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
```

### Update Package
Updates an existing package.
```sql
UPDATE package SET Name = ?, Description = ?, Destination = ?, Duration = ?, 
MaxPax = ?, Inclusions = ?, Price = ?, IsActive = ?, ImagePath = ? WHERE PackageId = ?
```

### Delete Package
Deletes a package.
```sql
DELETE FROM package WHERE PackageId = ?
```

### Check Bookings (Before Delete)
Checks if a package has existing bookings.
```sql
SELECT COUNT(*) FROM booking WHERE PackageID = ?
```

### Toggle Package Status
Activates or deactivates a package.
```sql
UPDATE package SET IsActive = ? WHERE PackageId = ?
```

### Popular Packages
Retrieves top 5 packages by booking count.
```sql
SELECT p.Name, COUNT(b.BookingID) as bookingCount 
FROM package p 
LEFT JOIN booking b ON p.PackageID = b.PackageID 
WHERE p.IsActive = 1 
GROUP BY p.PackageID, p.Name 
ORDER BY bookingCount DESC 
LIMIT 5
```

## 4. Booking & Dashboard Management
**Source:** `BookingRepositoryImpl.java`

### Get Recent Bookings
Retrieves the 10 most recent confirmed bookings with trip status (Completed, Ongoing, Upcoming).
```sql
SELECT b.BookingID AS TripID, c.name AS CustomerName, p.Name AS PackageName, p.Destination, MIN(t.StartDate) AS StartDate, 
CASE WHEN MAX(t.EndDate) < CURRENT_DATE THEN 'Completed' 
WHEN MIN(t.StartDate) <= CURRENT_DATE AND MAX(t.EndDate) >= CURRENT_DATE THEN 'Ongoing' 
ELSE 'Upcoming' END AS Status 
FROM booking b 
JOIN client c ON b.ClientID = c.clientId 
JOIN package p ON b.PackageID = p.PackageID 
JOIN packagetrips pt ON p.PackageID = pt.PackageID 
JOIN trip t ON pt.TripID = t.TripID 
WHERE b.Status = 'confirmed' 
GROUP BY b.BookingID, c.name, p.Name, p.Destination 
ORDER BY StartDate ASC 
LIMIT 10
```

### Create Booking
Inserts a new booking record.
```sql
INSERT INTO booking (EmployeeID, ClientID, PackageID, BookingDate, Status, PaxCount) VALUES (?, ?, ?, ?, ?, ?)
```

### Cancel Booking
Updates booking status to 'cancelled'.
```sql
UPDATE booking SET Status = 'cancelled' WHERE BookingID = ?
```

### Dashboard Statistics
Calculates the number of completed, ongoing, and upcoming trips based on booking dates.
```sql
SELECT 
    SUM(IF(TripEndDate < CURRENT_DATE, 1, 0)) AS CompletedTrips,
    SUM(IF(TripStartDate <= CURRENT_DATE AND TripEndDate >= CURRENT_DATE, 1, 0)) AS OngoingTrips,
    SUM(IF(TripStartDate > CURRENT_DATE, 1, 0)) AS UpcomingTrips
FROM (
    SELECT 
        MIN(T.StartDate) AS TripStartDate,
        MAX(T.EndDate) AS TripEndDate
    FROM booking B 
        JOIN packagetrips PT ON B.PackageID = PT.PackageID
        JOIN trip T ON PT.TripID = T.TripID
        WHERE B.Status = 'Confirmed'
    GROUP BY B.BookingID
) AS BookingDates
```

### Total Sales
Calculates total revenue from paid payments.
```sql
SELECT COALESCE(SUM(amount), 0) as totalSales FROM payment WHERE status = 'PAID'
```

### Total Bookings Count
Counts all bookings.
```sql
SELECT COUNT(*) as total FROM booking
```

## 5. Other Resources

### Trips
**Source:** `TripRepositoryImpl.java`
```sql
-- Get All Trips
SELECT * FROM trip ORDER BY StartDate

-- Get Trips for Specific Package
SELECT t.* FROM trip t 
INNER JOIN packagetrips pt ON t.TripID = pt.TripID 
WHERE pt.PackageID = ? 
ORDER BY pt.Sequence
```

### Payments
**Source:** `PaymentsRepositoryImpl.java`
```sql
-- Get All Payments with Details
SELECT p.*, c.name as clientName, pkg.Name as packageName 
FROM payment p 
LEFT JOIN booking b ON p.bookingId = b.BookingID 
LEFT JOIN client c ON b.ClientID = c.clientId 
LEFT JOIN package pkg ON b.PackageID = pkg.PackageID 
ORDER BY p.paymentDate DESC

-- Get Payments for Booking
SELECT * FROM payment WHERE bookingId = ? ORDER BY paymentDate DESC
```

### Accommodations (Hotels)
**Source:** `BookingRepositoryImpl.java` & `AccommodationRepositoryImpl.java`
```sql
SELECT accommodationId, name, address, contact, amenities, numberOfRooms, defaultRoomType FROM accommodation
```

### Vehicles
**Source:** `BookingRepositoryImpl.java`
```sql
SELECT VehicleID, Type, Capacity, PlateNumber, ProviderName FROM vehicle WHERE Type != 'Plane'
```
