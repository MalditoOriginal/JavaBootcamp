drop schema if exists Chat cascade;
create schema if not exists Chat;

CREATE TABLE Chat.User (
    UserId SERIAL PRIMARY KEY,
    Login varchar(50) NOT NULL UNIQUE,
    Password VARCHAR(50) NOT NULL
);

CREATE INDEX idx_user_login ON Chat.User(Login);

CREATE TABLE Chat.ChatRoom (
    ChatRoomId SERIAL PRIMARY KEY,
    ChatRoomName varchar(50) NOT NULL UNIQUE,
    ChatRoomOwner INT NOT NULL,
    CreationDate timestamp default CURRENT_TIMESTAMP,
    foreign key (ChatRoomOwner) references Chat.User(UserId)
);

CREATE INDEX idx_chatroom_name ON Chat.ChatRoom(ChatRoomName);
CREATE INDEX idx_chatroom_owner ON Chat.ChatRoom(ChatRoomOwner);

CREATE TABLE Chat.Message (
    MessageId SERIAL PRIMARY KEY,
    MessageAuthor INT NOT NULL,
    MessageRoom INT NOT NULL,
    MessageText text NOT NULL,
    MessageDate timestamp default CURRENT_TIMESTAMP,
    foreign key (MessageAuthor) references Chat.User(UserId),
    foreign key (MessageRoom) references Chat.ChatRoom(ChatRoomId)
);

CREATE INDEX idx_message_author ON Chat.Message(MessageAuthor);
CREATE INDEX idx_message_room ON Chat.Message(MessageRoom);
CREATE INDEX idx_message_date ON Chat.Message(MessageDate);

CREATE TABLE Chat.User_ChatRooms (
    ChatRoomsId SERIAL PRIMARY KEY,
    UserId INT NOT NULL,
    ChatRoomId INT NOT NULL,
    JoinDate timestamp default CURRENT_TIMESTAMP,
    foreign key (UserId) references Chat.User(UserId),
    foreign key (ChatRoomId) references Chat.ChatRoom(ChatRoomId),
    UNIQUE(UserId, ChatRoomId)
);

CREATE INDEX idx_user_chatrooms_user ON Chat.User_ChatRooms(UserId);
CREATE INDEX idx_user_chatrooms_room ON Chat.User_ChatRooms(ChatRoomId);
