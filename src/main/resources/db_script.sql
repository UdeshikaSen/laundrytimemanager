	CREATE TABLE user (
	userId VARCHAR(5) PRIMARY KEY,
	name VARCHAR(255) 
	);

	CREATE TABLE laundryRoom (
	roomId VARCHAR(5) PRIMARY KEY,
	openTime TIME,
	closeTime TIME
	);

	CREATE TABLE bookingTimeSlot (
	timeSlotId INT PRIMARY KEY AUTO_INCREMENT,
	bookingDate DATE,
	startTime TIME,
	endTime TIME,
	bookedUserId VARCHAR(5),
	laundryRoomId VARCHAR(5),
	deletedAtUnixTime BIGINT DEFAULT -1,
	FOREIGN KEY(bookedUserId) REFERENCES user(userId),
	FOREIGN KEY(laundryRoomId) REFERENCES laundryRoom(roomId),
    CONSTRAINT UNIQUE_TIMESLOT UNIQUE(bookingDate, startTime, endTime, laundryRoomId, deletedAtUnixTime)   
	);