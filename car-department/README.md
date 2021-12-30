# Car Department

## Table of content
1. [Download](#Download)
2. [Set up](#Set up)
3. [Features](#Features)
## Download
* ```git clone https://github.com/griddynamics/internship-tasks.git```
## Set up
* From the project root open terminal and throw ```docker-compose up```
* Enter ```docker exec -it car_department bash``` to connect to the ```car_department``` container
* Enter ```psql -U admin -d car_department_database``` to connect to database
* At this point you can do anything in the database. For example:
    * ```\dt``` to show all tables
    * ```SELECT * FROM car;``` to show all table records, or any other sql query. To make it work, 
tables actually should be created.
* ```docker-compose down -v``` to remove containers. V flag actually matter, because without it database won't 
be removed and with the next creating container the same database will be used
## Features