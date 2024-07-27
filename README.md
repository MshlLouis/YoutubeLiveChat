# A Bot for Youtube that allows the user to connect to Youtube Channels to read Livestream Chat Messsages and Super Chats. Saves all these events to a Database.


## 1: Requirements

- IDE + Gradle v8.4+
- Online Database


## 2: Initializing

First, prepare your credentials.txt file. The three inputs MUST BE:
1. Full Database URL (e.g. [url]:[port]/[Database Name])
2. Username
3. Password

Next, prepare your channels.txt file. The inputs in your file MUST BE:

-[channelname],[number]

The channelname can be acquired by opening the channel in a new tab and looking at the URL. It should say "https://www.youtube.com/@[channelname]". Just copy the name without the @ symbol. For the second input you have to put a number which represents the timer
for every request. For big channels with active chats, the value should be between 1-5 (preferably 1), for smaller channels this number can be as high as 1000 which is roughly 15 minutes (Youtube approximately stores the last 100 messages).



Finally, create the required tables using the file "mySQLFile.java". The files main method provides the 2 method calls (if you decide to rename the table, make sure you also rename it in the insertData method).


## 3: Running

Now you should be able to execute the program. More features such as detecting and saving deleted messages or timeout/bans might be implemented in the future.
