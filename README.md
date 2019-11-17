## KSP-IPH-2019-Table06
# Unified Communications Application
The main goal of the idea was to build an end-product to facilitate police officers to share their thoughts to one another. 
Along with this, communication among the police officers for formal purposes along with assignment of day-to-day functionalities of every officer, by higher authorities to their subordinates. 
The end product should be able to be intertwined with the official databases and be scaled for practical use. 
The highest priority in the development of application was security of data (either the messages in the chats or the task given to an officer), as confidential messages would be shared in the network. 
To bring the same into effect, any data that was transferred was stored permanently on the server in an encrypted form with the implementation of E2EE (End-to-End Encryption). On the contrary, the data is supposed to be wiped on the client side of the system every ten hours. 
Along with this measure, an option should be provided so that the data to be transferred can be marked as confidential which would be wiped out on the client systems on the completion of a minute after the receiver has read it. 
Apart from the security concerns, it is to be made sure to grant special privileges based on the position of an officer in the hierarchy. It was also to be made sure that only a higher authority like the Superintendent of Police should be able to assign the tasks.

List of tools used:
- AndroidStudio version 3.5.2 (open source)
- FireBase version 7.7.0 (commercial)

### Execution Procedure
- Install AndroidStudio in a desktop/laptop with the appropriate settings (default settings form our working environment).
- After installing, connect an android phone to the working device and run the application source code on the AndroidStudio IDE. 

### Design diagrams
The structure of the database resembles a tree where every collection has a set of documents holding the necessary data. Every document in a collection may or may not be associated with another collection. The flow of database for application takes the latter approach in the above sentence only for the chats, while the former for all the others. An example of the flow for the “chats collection” is:


![](https://i.ibb.co/jh6D1TW/Screen-Shot-2019-11-17-at-3-36-51-PM.png) ![](https://i.ibb.co/tHG4dVd/screener-1573985730145.png) ![](https://i.ibb.co/GMxjGg5/screener-1573985757281.png) ![](https://i.ibb.co/5hJgjcw/screener-1573985786776.png) ![](https://i.ibb.co/2yZhSQX/screener-1573985807898.png)


