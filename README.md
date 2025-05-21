# ContactsAdder
CSA White Tower Project - adding LBUSD emails to Google Contacts to show names

Proposal Summary: Google Contacts Adder using Downloading, Integration, and Uploading of Google Contacts CSV
The Problem: A few years ago, LBUSD removed searching for another student’s name in the Google “Share” function for Google Docs, Sheets, Slides, etc. This has made it more difficult to collaborate as, for each student, a nine-digit student ID number has to be entered to share. In large groups like IDP, with roughly 10 students, up to 90 digits must be remembered and entered. To address this, we propose an automated Google Contacts adder using Java. There are two phases to this application: 
1) collecting emails and associating them with student names, and 
2) saving these emails to the user’s Google Contacts.

Our project will have two options to be distributed: an application that can be downloaded and run from the user’s computer, or a website that the user can access. Users will download their Contacts (from "Other Contacts") as a Google Contacts CSV. The Java program will work on extracing LBUSD emails and the associated name and integrate with existing contacts. As this project is intended to address an LBUSD issue, its scope will be limited to the school district, and data will be handled for each school. Once a school is selected, the two options will show when they run the program or visit the website. The user must be logged in to ensure they are a district member.

Phase 1: Collecting Emails
While logged in, users may only add themselves to the database. Their contact will be associated with the school they selected. Contacts will remain in the database for 4 years or until the user is 19, whichever comes first.

Phase 2: Saving Emails
If users choose to import contacts, they can confirm which contacts from the database they want to add to their Google Contacts. Only contacts from the selected school will be available to import. The program will also add the user’s profile to the database if they have not already been added to ensure that other users can also recognize their email.

Documentation:
https://developers.google.com/people/v1/contacts
https://contacts.google.com
