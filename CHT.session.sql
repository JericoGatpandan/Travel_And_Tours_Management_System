SELECT *
FROM employee;

SELECT SUM(CASE WHEN TripEndDate < CURRENT_DATE THEN 1 ELSE 0 END)                                    AS CompletedTrips,
       SUM(CASE WHEN TripStartDate <= CURRENT_DATE AND TripEndDate >= CURRENT_DATE THEN 1 ELSE 0 END) AS OngoingTrips,
       SUM(CASE WHEN TripStartDate > CURRENT_DATE THEN 1 ELSE 0 END)                                  AS UpcomingTrips
FROM (SELECT MIN(T.StartDate) AS TripStartDate,
             MAX(T.EndDate)   AS TripEndDate
      FROM booking B
               JOIN packagetrips PT ON B.PackageID = PT.PackageID
               JOIN trip T ON PT.TripID = T.TripID
      WHERE B.Status = 'Confirmed'
      GROUP BY B.BookingID) AS BookingDates;


SELECT b.BookingID      AS TripID,
       c.name           AS CustomerName,
       p.Name           AS PackageName,
       p.Destination,
       MIN(t.StartDate) AS StartDate,
       CASE
           WHEN MAX(t.EndDate) < CURRENT_DATE THEN 'Completed'
           WHEN MIN(t.StartDate) <= CURRENT_DATE AND MAX(t.EndDate) >= CURRENT_DATE THEN 'Ongoing'
           ELSE 'Upcoming'
           END          AS Status
FROM booking b
         JOIN client c ON b.ClientID = c.clientId
         JOIN package p ON b.PackageID = p.PackageID
         JOIN packagetrips pt ON p.PackageID = pt.PackageID
         JOIN trip t ON pt.TripID = t.TripID
WHERE b.Status = 'confirmed'
GROUP BY b.BookingID, c.name, p.Name, p.Destination
ORDER BY StartDate ASC
LIMIT 10;

// Full list without LIMIT
SELECT b.BookingID             AS TripID,
       c.name                  AS CustomerName,
       p.Name                  AS PackageName,
       p.Destination,
       MIN(t.StartDate)        AS StartDate,
       CASE
           WHEN MAX(t.EndDate) < CURRENT_DATE THEN 'Completed'
           WHEN MIN(t.StartDate) <= CURRENT_DATE AND MAX(t.EndDate) >= CURRENT_DATE THEN 'Ongoing'
           ELSE 'Upcoming' END AS Status
FROM booking b
         JOIN client c ON b.ClientID = c.clientId
         JOIN package p ON b.PackageID = p.PackageID
         JOIN packagetrips pt ON p.PackageID = pt.PackageID
         JOIN trip t ON pt.TripID = t.TripID
WHERE b.Status = 'confirmed'
GROUP BY b.BookingID, c.name, p.Name, p.Destination
ORDER BY StartDate ASC;

select * from booking;
SELECT * from client;



