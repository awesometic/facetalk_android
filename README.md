# Face Talk Project <br> - Converting to Android Application
First semester 2016, Web Application Project at Suwon Univ.
Created by 11050038, Yang Deokgyu

~~If you running this application, make sure you have to make your own database.~~
That database should has 3 tables like this,

* users<br>
idx int(11) primary key auto_increment<br>
email varchar(30)<br>
password varchar(41)<br>
nickname varchar(30)<br>
age int(11) unsigned<br>
gender varchar(10)<br>

* friends<br>
idx int(11) primary key auto_increment<br>
user int(11)<br>
friend int(11)<br>

* messages<br>
idx int(11) primary key auto_increment<br>
user int(11)<br>
to_user int(11)<br>
message text<br>

~~This may run on localhost only, see session.php file~~

-----------------------------------------------
<h2>This android application<br>will connect to my own ODROID server for now...</h2>
