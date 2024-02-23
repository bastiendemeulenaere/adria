
INSERT INTO categories (category) VALUES
                                      ('Sports'),
                                      ('Music'),
                                      ('Food & Drink'),
                                      ('Arts & Culture'),
                                      ('Technology'),
                                      ('Health & Fitness');
INSERT INTO events (name, eventType, description, amountOfPeople, categoryId, organiserId, sector, startDateTime, hours) VALUES
                                                                                                                             ('Soccer Tournament', 'normal', 'Local soccer tournament', 60, 1, 'user1', 2, '2023-12-06 10:00:00', 2),
                                                                                                                             ('Music Festival', 'company', 'Annual music festival', 1000, 2, 'user2', 1, '2023-12-19 18:30:00', 3),
                                                                                                                             ('Food Expo', 'payed', 'Food exhibition', 200, 3, 'user3', 3, '2023-12-14 12:00:00', 9),
                                                                                                                             ('Art Gallery Opening', 'normal', 'Grand opening of an art gallery', 50, 4, 'user4', 4, '2023-12-24 15:45:00', 5),
                                                                                                                             ('Marathon', 'normal', 'Running a marathon at the AdriaTrack', 15, 6, 'user2', 6, '2024-01-08 08:00:00', 13),
                                                                                                                             ('Tech Conference', 'company', 'Technology conference', 500, 5, 'user5', 5, '2023-12-29 09:30:00', 11),
                                                                                                                             ('ongoing', 'company', 'ongoing event', 500, 5, 'user5', 5, '2023-12-13 09:30:00', 11),
                                                                                                                             ('Conference X', 'normal', 'Annual conference', 200, 5, 'user2', 7, '2024-05-15 09:00:00', 8),
                                                                                                                             ('Summer Music Fest', 'company', 'Outdoor music festival', 800, 2, 'user3', 1, '2024-07-20 17:00:00', 12),
                                                                                                                             ('Community Fair', 'normal', 'Local community fair', 150, 1, 'user5', 8, '2024-09-08 11:30:00', 6),
                                                                                                                             ('Artisan Market', 'payed', 'Artisanal products exhibition', 80, 3, 'user4', 9, '2024-10-12 10:00:00', 5),
                                                                                                                             ('Science Expo', 'normal', 'Exploring scientific advancements', 300, 5, 'user1', 6, '2024-11-05 09:00:00', 10),
                                                                                                                             ('Holiday Gala', 'company', 'Annual holiday celebration', 400, 4, 'user1', 7, '2024-12-20 19:00:00', 5),
                                                                                                                             ('Tech Summit', 'company', 'Cutting-edge tech summit', 600, 5, 'user2', 4, '2025-02-10 08:30:00', 9),
                                                                                                                             ('Fashion Show', 'normal', 'Glamorous fashion event', 100, 4, 'user3', 5, '2025-04-05 15:00:00', 4),
                                                                                                                             ('Gastronomy Expo', 'payed', 'Celebrating culinary arts', 120, 3, 'user4', 3, '2025-06-15 12:30:00', 7),
                                                                                                                             ('Film Festival', 'company', 'International film screenings', 700, 2, 'user5', 8, '2025-08-22 10:45:00', 11);


INSERT INTO users (id, first_name, last_name, about_me) VALUES
                                                            ('user1', 'John', 'Doe', 'Outdoor enthusiast who loves hiking and exploring new trails. Nature lover and photography enthusiast!'),
                                                            ('user2', 'Alice', 'Smith', 'Passionate about cycling and outdoor adventures. Always up for a bike ride or a weekend camping trip. Join me in the great outdoors!'),
                                                            ('user3', 'Bob', 'Johnson', 'Avid runner and fitness enthusiast. Enjoying the beauty of nature while staying active. Let''s run together and explore the scenic routes '),
                                                            ('user4', 'Eve', 'Wilson', 'Outdoor yoga and meditation practitioner. Finding peace and tranquility in nature. Connect with me for outdoor yoga sessions and positive vibes '),
                                                            ('user5', 'Charlie', 'Brown', 'Dog lover and park enthusiast. You can find me exploring parks with my furry friends. Let''s plan a dog-friendly meetup and enjoy the outdoors together ');

INSERT INTO user_events (userId, eventId) VALUES
                                              ('user1', 1),
                                              ('user2', 2),
                                              ('user3', 3),
                                              ('user4', 4),
                                              ('user2', 5),
                                              ('user4', 5),
                                              ('user5', 6),
                                              ('user5', 7);

INSERT INTO user_interests (userId, categoryId) VALUES
                                                    ('user1', 1),
                                                    ('user1', 2),
                                                    ('user2', 2),
                                                    ('user2', 3),
                                                    ('user3', 4),
                                                    ('user4', 5);

INSERT INTO notifications (userId, eventId, title, startTime, description, `read`) VALUES
                                                                                       ('user1', 1, 'New Tech Conference Announcement', '2023-10-21 12:00:00', 'Exciting tech conference coming up!', false),
                                                                                       ('user2', 2, 'Art Exhibition Reminder', '2023-09-29 12:00:00', 'Don''t miss the upcoming art exhibition!', false),
                                                                                       ('user2', 2, 'Art Exhibition Reminder', '2023-09-29 12:00:00', 'Don''t miss the upcoming art exhibition!', false),
                                                                                       ('user2', 2, 'Art Exhibition Reminder', '2023-09-29 12:00:00', 'Don''t miss the upcoming art exhibition!', false),
                                                                                       ('user2', 2, 'Art Exhibition Reminder', '2023-09-29 12:00:00', 'Don''t miss the upcoming art exhibition!', false),
                                                                                       ('user2', 2, 'Art Exhibition Reminder', '2023-09-29 12:00:00', 'Don''t miss the upcoming art exhibition!', false),
                                                                                       ('user2', 2, 'Art Exhibition Reminder', '2023-09-29 12:00:00', 'Don''t miss the upcoming art exhibition!', false),
                                                                                       ('user2', 2, 'Art Exhibition Reminder', '2023-09-29 12:00:00', 'Don''t miss the upcoming art exhibition!', false),
                                                                                       ('user2', 2, 'Art Exhibition Reminder', '2023-09-29 12:00:00', 'Don''t miss the upcoming art exhibition!', false),
                                                                                       ('user2', 2, 'Art Exhibition Reminder', '2023-09-29 12:00:00', 'Don''t miss the upcoming art exhibition!', false),
                                                                                       ('user2', 2, 'Art Exhibition Reminder', '2023-09-29 12:00:00', 'Don''t miss the upcoming art exhibition!', false),
                                                                                       ('user2', 2, 'Art Exhibition Reminder', '2023-09-29 12:00:00', 'Don''t miss the upcoming art exhibition!', false),
                                                                                       ('user2', 2, 'Art Exhibition Reminder', '2023-09-29 12:00:00', 'Don''t miss the upcoming art exhibition!', false),
                                                                                       ('user2', 2, 'Art Exhibition Reminder', '2023-09-29 12:00:00', 'Don''t miss the upcoming art exhibition!', false),
                                                                                       ('user2', 2, 'Art Exhibition Reminder', '2023-09-29 12:00:00', 'Don''t miss the upcoming art exhibition!', false),
                                                                                       ('user2', 2, 'Art Exhibition Reminder', '2023-09-29 12:00:00', 'Don''t miss the upcoming art exhibition!', false),
                                                                                       ('user2', 2, 'Art Exhibition Reminder', '2023-09-29 12:00:00', 'Don''t miss the upcoming art exhibition!', false),
                                                                                       ('user2', 2, 'Art Exhibition Reminder', '2023-09-29 12:00:00', 'Don''t miss the upcoming art exhibition!', false),
                                                                                       ('user2', 2, 'Art Exhibition Reminder', '2023-09-29 12:00:00', 'Don''t miss the upcoming art exhibition!', false),
                                                                                       ('user2', 2, 'Art Exhibition Reminder', '2023-09-29 12:00:00', 'Don''t miss the upcoming art exhibition!', false),
                                                                                       ('user3', 3, 'Music Festival Update', '2023-03-14 12:00:00', 'Get ready for a musical extravaganza!', false),
                                                                                       ('user4', 4, 'Science Symposium Alert', '2023-09-14 12:00:00', 'Stay informed about the latest scientific discoveries!', false),
                                                                                       ('user5', 5, 'Sports Tournament Notification', '2023-01-14 12:00:00', 'Prepare for the upcoming sports tournament!', false);

