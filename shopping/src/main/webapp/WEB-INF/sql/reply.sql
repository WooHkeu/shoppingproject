use webtest;
 create table review(
   rnum int not null auto_increment primary key,
   content varchar(500) not null,
   regdate date not null,
   id varchar(10) not null,
   contentsno int(7) not null,
   foreign key(contentsno) references contents(contentsno)
);
select * from contents;
insert into review(content, regdate, id, contentsno)
values('의견',sysdate(),'user1',6);

-- create(댓글등록)
select * from review;

select rnum, content, regdate, id, contentsno
from review
where contentsno = 6
order by rnum DESC
limit 0, 3;
