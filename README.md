
# laundrytimemanager

## Prerequisites
- Maven
- Java 17
- Docker
- IntelliJ
- Git

## Setting up
1. Clone the project.
2. Open the project using IntelliJ.
3. Set the project to use Java 17 and make sure to have maven configured in your system.
4. Then build the project using IntelliJ.
5. Start up Docker.

## Steps to run the main method.
1. Start the mysql database container using the following command. Make sure to replace <PROJECT_PATH> to the directory where the project is.
```
docker run --name mysql-local -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=laundry_room_booking_db -v <PROJECT_PATH>/src/main/resources:/docker-entrypoint-initdb.d/ mysql:8.0.25
```
The path `<PROJECT_PATH>/src/main/resources` points to the database schema. i.e https://github.com/UdeshikaSen/laundrytimemanager/tree/main/src/main/resources.

​This would start up a mysql container which is required for this program to run. 

2. Once the container has started, run the `LaundryRoomManager` java class which has a main method and it would run few basic actions of the system.

## Unit tests
Run the `BookingTimeSlotServiceTest` java class which contains the unit tests.

## Integration tests

1. Make sure to shutdown the container that was started when running the application. This can be done by the following command:
```
docker rm -f mysql-local  
```
2. Run the `LaundryRoomManagerTest` java test class.
