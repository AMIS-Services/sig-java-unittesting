insert into subscription (api, subscriber, client_id, session_id, channel_id, subscribed_at, unsubscribed_at, session_removed_at) values ('API01', 'FooChannel##FooSecret', 'Client01', 'FooSession01', 'FooChannel', '2021-05-17 10:15:45.00', null, null);

insert into subscription (api, subscriber, client_id, session_id, channel_id, subscribed_at, unsubscribed_at, session_removed_at) values ('API01', 'BarChannel##BarSecret', 'Client02', 'BarSession01', 'BarChannel02', '2021-05-17 10:16:45.00', '2021-05-17 10:18:45.00', null)

insert into subscription (api, subscriber, client_id, session_id, channel_id, subscribed_at, unsubscribed_at, session_removed_at) values ('API01', 'BazChannel##BazSecret', 'Client03', 'BazSession01', 'BazChannel', '2021-05-17 10:26:45.00', null, null)

insert into notification (channel_id, message, created_at ) values ('FooChannel##FooSecret', 'Foo 01', '2021-05-17 10:15:45.00' )

insert into notification (channel_id, message, created_at ) values ('FooChannel##FooSecret', 'Foo 02', '2021-05-17 10:15:46.00' )

insert into notification (channel_id, message, created_at ) values ('FooChannel##FooSecret', 'Foo 03', '2021-05-17 10:16:31.00' )

insert into notification (channel_id, message, created_at ) values ('BazChannel##BazSecret', 'Baz 01', '2021-05-17 10:15:45.00' )

