# spring-batch-parser

## JAVA  
1. Parse and load the given log file to MySQL;  
  Done  
2. The tool takes "startDate", "duration" and "threshold" as command line arguments. "startDate" is of "yyyy-MM-dd.HH:mm:ss" format, "duration" can take only "hourly", "daily" as inputs and "threshold" can be an integer;  
  Done  
3. Process blocked IPs;  
  Done  

## SQL  
1. Write MySQL query to find IPs that mode more than a certain number of requests for a given time period;  
  SELECT * FROM access.access where access.dt_access between '2017-01-01.13:00:00' and '2017-01-02.13:00:00' group by access.ip_address having count(access.ip_address) > 250 limit 250;  
2. Write MySQL query to find requests made by a given IP;  
  SELECT * FROM access.access where access.ip_address="192.168.228.188"
