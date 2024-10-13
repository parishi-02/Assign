
# EdProgress Track 


## Overview

EdPProgress Tracker is a comprehensive management tool tailored for educational institutions. This platform streamlines various administrative tasks, enhancing efficiency and organization within schools, colleges, and training centers. Key functionalities include batch management, course planning, mentoring sessions, and detailed assignment and assessment management.
## Features

This tool includes three distinct modules: Admin, Trainer, and Trainee. Users can log in using their credentials, and based on their role, a tailored dashboard will be displayed.

### 1. Admin Module
The Admin module offers extensive rights and functionalities to manage various aspects of the educational institution:

 #### Trainer and Trainee Management:

* The admin can add both trainers and trainees either manually or through a convenient bulk upload option. Furthermore, the admin possesses the capability to update existing information for trainers and trainees as necessary, ensuring that all records remain current and accurate.
* Admin can also download lists of both trainees and trainers.

#### Batch Management:

* The admin has the ability to create new batches by selecting from the available trainers for each group. 
* During this process, the admin can upload relevant course plans to ensure that all instructional materials are readily accessible. 
* Moreover, the admin can specify holidays that fall within the training period, aiding in effective planning and scheduling for both trainers and trainees. 
* Importantly, the admin can also add trainees to the batches, ensuring that each group is fully populated and ready for instruction.
* Additionally, the admin can download the course plan and also modify the planned dates of the individual topics as needed to accommodate any changes.

#### Attendance Management:

* Admin can upload attendance records for all trainees, ensuring accurate tracking of participation. 
* The admin also has the ability to update attendance information as needed, maintaining the integrity of the records. 
* Furthermore, attendance reports can be easily viewed based on specific dates and trainee IDs, facilitating efficient record-keeping and analysis. 
* The admin also has the ability to download attendance records by entering a date range, from the start date to the end date, ensuring comprehensive access to attendance data.


#### Assignment Management:

* Admin can upload marks for all trainees and view the results for specific assignments, ensuring transparency and accessibility of performance data.
* Additionally, the admin has the capability to update assignment marks as needed, allowing for accurate and timely adjustments based on ongoing assessments. 
* This functionality is complemented by the Assessment Management features, which enable comprehensive oversight of trainee evaluations.
* The admin can download assignment marks for all trainees, specifically for those that have been recorded up to that date. 

#### Assessment Management:

* The admin can add assessments based on available dates and subsequently upload marks for all trainees, ensuring transparency and accessibility of performance data. 
* Additionally, the admin can update assignment marks as needed for accurate adjustments. This functionality, along with the Assessment Management features, provides comprehensive oversight of trainee evaluations.
* The admin can download assessments marks for all trainees, specifically for those that have been recorded up to that date. 

#### Mentoring Sessions:

* Admin can schedule mentoring sessions, update and download them as necessary, ensuring that trainees receive timely guidance and support.
* This capability allows for flexible planning to meet the evolving needs of both trainers and trainees, fostering a productive mentoring environment.


#### Reporting:

* Admin can download trainee reports tailored to specific requirements. This includes the option to select various trainee attributes such as name, college name, tenth marks etc. and module-wise performance.
* Additionally, the admin can choose to download an overall report, providing a comprehensive view of each trainee’s progress and achievements.

### 2. Trainer Module

* Trainers can manage the batches assigned to them, similar to the admin's capabilities, but they do not have the ability to create new batches. 
* They can oversee attendance and assignments, as well as update batch-related information as necessary. However, trainers cannot add new trainees to their batches or create and update new mentoring sessions; they can only view the available mentoring sessions.
* This distinction allows trainers to effectively support their trainees while adhering to the established structure set by the admin.


### 3. Trainee Module

* Trainees can view only their assigned batches, and all information is accessible in view mode. They do not have the ability to create or update any records; their interaction is limited to observing details related to their batch. 
* This design ensures that trainees can focus on their learning without modifying any administrative data.
## Important Instructions

### 1. Database related

* After running the application, it is essential to manually execute the following SQL queries in your database to populate the track_users and user_roles tables. 
* This step is necessary for enabling admin login with the appropriate credentials and performing the required tasks:

```sql
INSERT INTO track_users VALUES ('admin', 1, '$2a$12$yysPhlNi8sJcuCDAnfiQbe68fQ3jsxCyV9DHkFPlu.JTfJqd77/sC');
INSERT INTO user_roles VALUES ('admin', 'ADMIN'); 
```

* Once this data has been successfully added, the application will allow the admin to log in using their credentials.

### 2. Username and Password Generation

* Please note the following formats for generating usernames and passwords for trainers and trainees:

  ### Trainer Credentials:
    - **Username**: `nameTraineeId` (e.g., `parishi10000`)
    - **Password**: First three letters of the trainer's name + `@` + trainer ID (e.g., `par@10000`)

    ### Trainee Credentials:
    - **Username**: `name+traineeID` (e.g., `parishi10000`)
    - **Password**: Trainee ID (e.g., `10000`)

### 3. Excel Sheet Upload Formats

* #### **Course Plan Upload Format**
    Please ensure that the Excel sheet used for uploading course plans follows the specified format. You can download the template from the repository [here](https://github.com/parishi-02/EdProgress-Tracker/blob/main/Excels/CoursePlanFormat.xlsx).


* #### **Trainee Bulk Upload Format**
    Similarly, the Excel sheet for bulk uploading trainees must adhere to the required format. You can download the template from the repository [here](https://github.com/parishi-02/EdProgress-Tracker/blob/main/Excels/TraineeUploadFormat.xlsx).

## Tech Stack

- **Backend**: Java, Spring, Hibernate
- **Frontend**: JSP, JavaScript, Bootstrap, AJAX
- **Database**: Oracle Database
- **Tools**: Maven
- **Version Control**: Git


## Installation and Setup

Follow these steps to install and set up the project on your local machine:

### Prerequisites

- Ensure you have the following installed on your machine:
  - Java Development Kit (JDK) [version 8 or more]
  - Apache Tomcat [8.0.33]
  - IntelliJ IDEA
  - Maven
  - Oracle Database
  - Git

### 1. Clone the Repository


   ```bash
   git clone https://github.com/parishi-02/EdProgress-Tracker.git
   cd EdProgress-Tracker
   ```

### 2. Set up the Database
Configure your dataabse setting in the application.properties file

### 3. Configure Apache Tomcat

* Go to **Run > Edit Configurations**.
* Click the + icon and select **Tomcat Server > Local**.
* Fill in the necessary information, including the Tomcat home directory.
* Click **Apply**, then click **OK**.
* Now you can run the server by clicking the **Run** button.
    
### 4. Access the Application

After running the server, access the application by visiting the following URL in your web browser:

[http://localhost:8890/EdTrack](http://localhost:8890/EdTrack)

Replace `<your-port>` like **8890** with the port number you configured while setting up the server.



## Contact

For questions or support, feel free to reach out to me at parishijainiit@gmail.com or through my [LinkedIn profile](https://www.linkedin.com/in/parishi-jain-bbb218210/)



