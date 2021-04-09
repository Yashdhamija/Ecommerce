CREATE TABLE `Address` (
  `cid` int NOT NULL,
  `street` varchar(100) NOT NULL,
  `province` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `country` varchar(20) NOT NULL,
  `zip` varchar(20) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `city` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  KEY `cid` (`cid`),
  CONSTRAINT `Address_ibfk_1` FOREIGN KEY (`cid`) REFERENCES `Users` (`customerid`);

INSERT INTO bookstore.Address (cid,street,province,country,zip,phone,city) VALUES
	 (2,'1234 Main Street','Alberta','Canada','M1W-2C7','123-567-4567','Toronto'),
	 (5,'2022 Weston Rd','Alberta','Canada','W1H-2C7','647-444-5654','Toronto'),
	 (95,'111 Jarvis St','Ontario','Canada','X1J-1M3','123-456-7890','Toronto'),
	 (97,'122 York St','Ontario','Canada','L7A-1M3','416-474-2343','Toronto');


  CREATE TABLE `AdminBookStore` (
  `fname` varchar(40) DEFAULT NULL,
  `lname` varchar(40) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`email`) );

INSERT INTO bookstore.AdminBookStore (fname,lname,email,password) VALUES
	 ('Matthew','Thomson','adminofbookstore@gmail.com','rxAiQIcg0EWFwpg5/0A/64QIVnAbSnG/99rWMIyYT/g=');


  CREATE TABLE `Book` (
  `bid` varchar(20) NOT NULL,
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `price` int NOT NULL,
  `category` enum('Fiction','Science','Engineering','Crime','Romance','Sci-fi','Literacy','History','Fantasy','Arts') DEFAULT NULL,
  `imageurl` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`bid`));

INSERT INTO bookstore.Book (bid,title,price,category,imageurl) VALUES
	 ('111-0-3343-2656-3','Labyrinth',43,'Science','20.jpg'),
	 ('111-0-6764-2654-3','My Sister''s Keeper',20,'Romance','23.jpg'),
	 ('121-3-3434-4545-5',' Elon Musk: Tesla, SpaceX',35,'Engineering','27.jpg'),
	 ('123-1-1234-1234-1','Jack Kerouac Is Dead to Me',55,'Science','5.jpg'),
	 ('232-2-4342-2343-3','Cracking the Coding Interview',77,'Engineering','28.jpg'),
	 ('232-4-3454-3462-5','The Lost Boy',71,'Sci-fi','26.jpg'),
	 ('234-7-3434-5674-4','Amber Spyglass	',70,'Science','25.jpg'),
	 ('247-5-3453-3532-6','Dune',56,'Sci-fi','38.jpg'),
	 ('334-5-7334-3453-6','Billy Connolly	',63,'Crime','44.jpg'),
	 ('343-3-6454-6756-4','Fabric of a Nation',41,'Arts','34.jpg');
INSERT INTO bookstore.Book (bid,title,price,category,imageurl) VALUES
	 ('343-4-5454-4545-4','Anastasia''s Secret    ',50,'Fiction','7.jpg'),
	 ('345-4-4664-3435-7','In Cold Blood',58,'Crime','37.jpg'),
	 ('345-9-4531-2644-6','Fifty Shades of Grey	',35,'Romance','13.jpg'),
	 ('355-7-3445-4545-6','Neuromancer',44,'Sci-fi','39.jpg'),
	 ('444-3-6543-5753-6','PS, I Love You	',37,'Romance','22.jpg'),
	 ('445-4-5656-3435-5','Bridget Jones''s Diary	',59,'Sci-fi','41.jpg'),
	 ('454-6-4845-4856-6','Eclipse	',80,'History','17.jpg'),
	 ('456-5-7654-2444-2','Doctor Sleep',100,'Literacy','3.jpg'),
	 ('478-2-1664-4321-4','Shakespeare''s Daughter  ',76,'Literacy','8.jpg'),
	 ('478-4-5684-7585-3','Helgoland',21,'Science','36.jpg');
INSERT INTO bookstore.Book (bid,title,price,category,imageurl) VALUES
	 ('543-1-4324-5888-4','My Uncle Napoleon ',60,'Fiction','6.jpg'),
	 ('545-6-3435-7878-9','Life of Pi	',23,'Engineering','43.jpg'),
	 ('546-6-5734-8437-7','Angels and Demons	',45,'Crime','14.jpg'),
	 ('555-3-3423-4545-6','Introduction to Algorithm',88,'Engineering','29.jpg'),
	 ('564-8-8685-5744-5','New Moon	',75,'Engineering','16.jpg'),
	 ('565-3-4535-5656-2','My Booky Wook	',95,'History','24.jpg'),
	 ('568-3-4353-7231-9','Katherine',86,'Fantasy','9.jpg'),
	 ('653-5-6767-3286-8','The Art Spirit',40,'Arts','33.jpg'),
	 ('656-7-5454-4564-4','One Day	',69,'Fantasy','40.jpg'),
	 ('657-6-3343-5656-3','Jamie''s 30-Minute Meals	',43,'Science','19.jpg');
INSERT INTO bookstore.Book (bid,title,price,category,imageurl) VALUES
	 ('756-1-3434-3434-5','Help,The	',23,'Literacy','21.jpg'),
	 ('756-8-8565-5465-2','The Lion, the Witch and the Wardrobe',84,'Sci-fi','32.jpg'),
	 ('764-6-4343-4541-2','Da Vinci Code,The  ',37,'History','10.jpg'),
	 ('765-2-3456-1213-3','Grant',40,'History','47.jpg'),
	 ('765-5-3456-3453-7','Phaidon',120,'Arts','35.jpg'),
	 ('843-2-4353-353-6','Great gatsby',40,'Romance','4.jpg'),
	 ('865-5-6565-4535-7','Lovely Bones,The	',100,'Engineering','18.jpg'),
	 ('867-7-6754-4545-9','Captain Corelli''s Mandolin	',39,'Sci-fi','42.jpg'),
	 ('876-2-5386-6321-2','Harry Potter Philosopher''s Stone',30,'Fiction','12.jpg'),
	 ('876-2-5386-8921-2','Harry Potter Deathly Hallows',40,'Fiction','11.jpg');
INSERT INTO bookstore.Book (bid,title,price,category,imageurl) VALUES
	 ('876-3-5677-1233-3','The Famous Five',20,'Arts','1.jpg'),
	 ('879-3-1213-1249-2','Iran: A Modern History',100,'History','48.jpg'),
	 ('909-1-1218-1154-4','The Proposal',60,'Romance','46.jpg'),
	 ('912-3-1232-1131-2','Blitt',90,'Arts','45.jpg'),
	 ('947-7-8943-2756-2','The Fellowship of the Ring ',60,'Fantasy','31.jpg'),
	 ('967-4-9864-5452-5','A Game of Thrones ',99,'Fantasy','30.jpg'),
	 ('971-9-6542-1367-1','Animal Form',99,'Literacy','50.jpg'),
	 ('976-2-1369-1356-7','Kite Runner',30,'Literacy','49.jpg'),
	 ('987-7-5456-3745-9','Fifty Shades Darker	',70,'Crime','15.jpg');


  CREATE TABLE `PO` (
  `orderid` int unsigned NOT NULL,
  `lname` varchar(20) NOT NULL,
  `fname` varchar(20) NOT NULL,
  `status` enum('ORDERED','PROCESSED','DENIED') NOT NULL,
  `cid` int NOT NULL,
  `date` varchar(20) NOT NULL,
  PRIMARY KEY (`orderid`),
  KEY `cid` (`cid`),
  CONSTRAINT `PO_ibfk_1` FOREIGN KEY (`cid`) REFERENCES `Users` (`customerid`)

INSERT INTO bookstore.PO (orderid,lname,fname,status,cid,`date`) VALUES
	 (41967,'Yash','Dhamija','ORDERED',5,'2021-4-8'),
	 (139102,'Qasim','Ahmed','DENIED',2,'2021-4-4'),
	 (197577,'Qasim','Ahmed','DENIED',2,'2021-4-5'),
	 (232168,'Qasim','Ahmed','ORDERED',2,'2021-4-6'),
	 (234144,'Qasim','Ahmed','ORDERED',2,'2021-4-4'),
	 (304170,'Qasim','Ahmed','ORDERED',2,'2021-4-7'),
	 (355308,'Yonis','Abokar','ORDERED',97,'2021-4-9'),
	 (356191,'Yash','Dhamija','ORDERED',5,'2021-4-8'),
	 (375425,'Yash','Dhamija','ORDERED',5,'2021-4-9'),
	 (376159,'Yash','Dhamija','ORDERED',5,'2021-4-9');
INSERT INTO bookstore.PO (orderid,lname,fname,status,cid,`date`) VALUES
	 (404220,'Qasim','Ahmed','ORDERED',2,'2021-4-7'),
	 (410765,'Qasim','Ahmed','ORDERED',2,'2021-4-4'),
	 (422030,'Qasim','Ahmed','ORDERED',2,'2021-4-6'),
	 (433364,'Qasim','Ahmed','ORDERED',2,'2021-4-6'),
	 (439365,'Qasim','Ahmed','ORDERED',2,'2021-4-6'),
	 (521666,'Yash','Dhamija','ORDERED',5,'2021-4-9'),
	 (534115,'Qasim','Ahmed','ORDERED',2,'2021-4-8'),
	 (535728,'Qasim','Ahmed','ORDERED',2,'2021-4-6'),
	 (550715,'Qasim','Ahmed','DENIED',2,'2021-4-5'),
	 (551758,'Qasim','Ahmed','ORDERED',2,'2021-4-5');
INSERT INTO bookstore.PO (orderid,lname,fname,status,cid,`date`) VALUES
	 (554515,'Qasim','Ahmed','DENIED',2,'2021-4-6'),
	 (561723,'Qasim','Ahmed','ORDERED',2,'2021-4-4'),
	 (627109,'Qasim','Ahmed','DENIED',2,'2021-4-5'),
	 (653751,'Qasim','Ahmed','ORDERED',2,'2021-4-9'),
	 (680902,'JK','Rowling','ORDERED',95,'2021-4-9'),
	 (699806,'Qasim','Ahmed','ORDERED',2,'2021-4-5'),
	 (708913,'Qasim','Ahmed','ORDERED',2,'2021-4-6'),
	 (834694,'Qasim','Ahmed','DENIED',2,'2021-4-4'),
	 (901839,'Yash','Dhamija','DENIED',5,'2021-4-8'),
	 (967227,'Qasim','Ahmed','ORDERED',2,'2021-4-4');
INSERT INTO bookstore.PO (orderid,lname,fname,status,cid,`date`) VALUES
	 (982145,'Qasim','Ahmed','ORDERED',2,'2021-4-9');

  CREATE TABLE `POItem` (
  `orderid` int unsigned NOT NULL,
  `bid` varchar(20) NOT NULL,
  `price` int unsigned NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`orderid`,`bid`),
  KEY `orderid` (`orderid`),
  KEY `bid` (`bid`),
  CONSTRAINT `POItem_ibfk_1` FOREIGN KEY (`orderid`) REFERENCES `PO` (`orderid`) ON DELETE CASCADE,
  CONSTRAINT `POItem_ibfk_2` FOREIGN KEY (`bid`) REFERENCES `Book` (`bid`) ON DELETE CASCADE
);

INSERT INTO bookstore.POItem (orderid,bid,price,quantity) VALUES
	 (41967,'123-1-1234-1234-1',55,1),
	 (232168,'123-1-1234-1234-1',55,1),
	 (234144,'111-0-6764-2654-3',20,1),
	 (234144,'343-3-6454-6756-4',41,1),
	 (304170,'123-1-1234-1234-1',55,1),
	 (355308,'121-3-3434-4545-5',35,2),
	 (355308,'445-4-5656-3435-5',59,1),
	 (356191,'232-4-3454-3462-5',71,1),
	 (375425,'987-7-5456-3745-9',70,1),
	 (376159,'121-3-3434-4545-5',35,1);
INSERT INTO bookstore.POItem (orderid,bid,price,quantity) VALUES
	 (376159,'123-1-1234-1234-1',55,11),
	 (404220,'232-4-3454-3462-5',71,1),
	 (410765,'232-4-3454-3462-5',71,1),
	 (422030,'232-2-4342-2343-3',77,1),
	 (433364,'232-4-3454-3462-5',71,1),
	 (439365,'123-1-1234-1234-1',55,9),
	 (521666,'445-4-5656-3435-5',59,3),
	 (534115,'123-1-1234-1234-1',55,1),
	 (534115,'232-4-3454-3462-5',71,10),
	 (535728,'111-0-3343-2656-3',43,1);
INSERT INTO bookstore.POItem (orderid,bid,price,quantity) VALUES
	 (535728,'334-5-7334-3453-6',63,1),
	 (551758,'123-1-1234-1234-1',55,25),
	 (561723,'232-4-3454-3462-5',71,1),
	 (653751,'121-3-3434-4545-5',35,1),
	 (653751,'123-1-1234-1234-1',55,1),
	 (653751,'232-2-4342-2343-3',77,1),
	 (680902,'971-9-6542-1367-1',99,2),
	 (699806,'111-0-6764-2654-3',20,1),
	 (708913,'111-0-3343-2656-3',43,1),
	 (967227,'232-4-3454-3462-5',71,2);
INSERT INTO bookstore.POItem (orderid,bid,price,quantity) VALUES
	 (982145,'121-3-3434-4545-5',35,6);


CREATE TABLE `PartnerKeys` (
  `email` varchar(100) NOT NULL,
  `apikey` varchar(36) NOT NULL,
  PRIMARY KEY (`email`),
  UNIQUE KEY `apikey` (`apikey`)
);

INSERT INTO bookstore.PartnerKeys (email,apikey) VALUES
	 ('mk@gmail.com','ad9abc4b-b5e0-4a7a-a119-c24a62d9bf94'),
	 ('jkrowling@gmail.com','bc629434-da50-416f-aead-c1c492b88271'),
	 ('tomhardy@gmail.com','e173642c-5112-4412-90ba-9665268942e0');

CREATE TABLE `Review` (
  `fname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `lname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `bid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `review` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `reviewid` int NOT NULL AUTO_INCREMENT,
  `title` varchar(40) NOT NULL,
  `rating` int NOT NULL,
  PRIMARY KEY (`reviewid`)
);

INSERT INTO bookstore.Review (fname,lname,bid,review,title,rating) VALUES
	 ('Qasim','Ahmed','111-0-6764-2654-3','This is the best book i have ever read!','Best book',3),
	 ('Yonis','Abokar','232-2-4342-2343-3','This book has changed my life and i m so happy.','Amazing book!',5),
	 ('Yash','Dh','756-1-3434-3434-5','standard','Stan',4),
	 ('Alan','Marker','232-4-3454-3462-5','I am loving this book and i really recommend it.','Best book in the town!',5);

CREATE TABLE `Users` (
  `customerid` int NOT NULL AUTO_INCREMENT,
  `fname` varchar(40) DEFAULT NULL,
  `lname` varchar(40) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `usertype` int DEFAULT NULL,
  `password` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`customerid`)
);

INSERT INTO bookstore.Users (fname,lname,email,usertype,password) VALUES
	 ('Qasim','Ahmed','qasim@gmail.com',0,'73l8gRjwLftklgfdXT+MdiMEjJwGPVMsyVxe16iYpk8='),
	 ('Yash','Dhamija','yash4@gmail.com',0,'73l8gRjwLftklgfdXT+MdiMEjJwGPVMsyVxe16iYpk8='),
	 ('JK','Rowling','jkrowling@gmail.com',1,'73l8gRjwLftklgfdXT+MdiMEjJwGPVMsyVxe16iYpk8='),
	 ('Yonis','Abokar','yabokar@gmail.com',0,'73l8gRjwLftklgfdXT+MdiMEjJwGPVMsyVxe16iYpk8=');