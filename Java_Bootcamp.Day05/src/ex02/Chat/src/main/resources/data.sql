INSERT INTO Chat.User(login, password) VALUES
    ('Alex', '1111'),
    ('fletamar', '2222'),
    ('fleta' , '3333'),
    ('marcia', '4444'),
    ('Fleta_Marcia', '5555'),
    ('Aleks', '6666');

INSERT INTO Chat.chatroom(chatroomname, chatroomowner) VALUES
    (1, (SELECT UserId FROM chat.User WHERE login = 'Alex')),
    (2, (SELECT UserId FROM chat.User WHERE login = 'fletamar')),
    (3, (SELECT UserId FROM chat.User WHERE login = 'fleta')),
    (4, (SELECT UserId FROM chat.User WHERE login = 'marcia'));

INSERT INTO Chat.message(messageauthor, messageroom, messagetext) VALUES
    ((SELECT UserId FROM chat.user WHERE login = 'Alex'),
     (SELECT ChatRoomId FROM chat.chatroom WHERE chatroomname = '1'), 'It is all good'),
    ((SELECT UserId FROM chat.user WHERE login = 'fleta'),
     (SELECT ChatRoomId FROM chat.chatroom WHERE chatroomname = '1'), 'Yo!'),
    ((SELECT UserId FROM chat.user WHERE login = 'fletamar'),
     (SELECT ChatRoomId FROM chat.chatroom WHERE chatroomname = '1'), 'You are not pass'),
    ((SELECT UserId FROM chat.user WHERE login = 'marcia'),
     (SELECT ChatRoomId FROM chat.chatroom WHERE chatroomname = '4'), 'Hey, men'),
    ((SELECT UserId FROM chat.user WHERE login = 'Fleta_Marcia'),
     (SELECT ChatRoomId FROM chat.chatroom WHERE chatroomname = '4'), 'LOOOL'),
    ((SELECT UserId FROM chat.user WHERE login = 'Aleks'),
     (SELECT ChatRoomId FROM chat.chatroom WHERE chatroomname = '4'), 'Do not worry be happy');

INSERT INTO Chat.user_chatrooms(userid, chatroomid) VALUES
    ((SELECT UserId FROM chat.user WHERE login = 'Alex'),
     (SELECT ChatRoomId FROM chat.chatroom WHERE chatroomname = '1')),
    ((SELECT UserId FROM chat.user WHERE login = 'fleta'),
     (SELECT ChatRoomId FROM chat.chatroom WHERE chatroomname = '1')),
    ((SELECT UserId FROM chat.user WHERE login = 'fleta'),
     (SELECT ChatRoomId FROM chat.chatroom WHERE chatroomname = '3')),
    ((SELECT UserId FROM chat.user WHERE login = 'fletamar'),
     (SELECT ChatRoomId FROM chat.chatroom WHERE chatroomname = '1')),
    ((SELECT UserId FROM chat.user WHERE login = 'fletamar'),
     (SELECT ChatRoomId FROM chat.chatroom WHERE chatroomname = '2')),
    ((SELECT UserId FROM chat.user WHERE login = 'marcia'),
     (SELECT ChatRoomId FROM chat.chatroom WHERE chatroomname = '4')),
    ((SELECT UserId FROM chat.user WHERE login = 'Fleta_Marcia'),
     (SELECT ChatRoomId FROM chat.chatroom WHERE chatroomname = '4')),
    ((SELECT UserId FROM chat.user WHERE login = 'Aleks'),
     (SELECT ChatRoomId FROM chat.chatroom WHERE chatroomname = '4'));