use RtoU;

#  name 값이 이미 존재하는 경우 업데이트, 없는 경우에는 삽입
insert into characterinfo (name, voicename, pitch, langcode)
values ('Hindung-e', 'en-US-Neural2-G', 13.6, 'en-US')
on duplicate key update voicename = 'en-US-Neural2-G',
                        pitch     = 13.6,
                        langcode  = 'en-US';


#  name 값이 이미 존재하는 경우 업데이트, 없는 경우에는 삽입
insert into characterinfo (name, voicename, pitch, langcode)
values ('Mongmong-e', 'en-US-Neural2-A', -10.0, 'en-US')
on duplicate key update voicename = 'en-US-Neural2-A',
                        pitch     = -10.0,
                        langcode  = 'en-US';


#  name 값이 이미 존재하는 경우 업데이트, 없는 경우에는 삽입
insert into characterinfo (name, voicename, pitch, langcode)
values ('Yangyangi', 'en-US-Neural2-D', 8.8, 'en-US')
on duplicate key update voicename = 'en-US-Neural2-D',
                        pitch     = 8.8,
                        langcode  = 'en-US';
