INSERT INTO users VALUES(1,'green@test.com','Mr. Green');
INSERT INTO users VALUES(2,'brown@test.com','Mr. Brown');
INSERT INTO groups (id,title,description,created_by_id)
VALUES (1,'Group One', 'Group One description',1);
INSERT INTO memberships (id,member_id,group_id,role)
VALUES (1,1,1,0);
