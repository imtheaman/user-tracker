# todo list in kotlin
todo list demo project created in kotlin using room database. and also added notification to notify user at specific date

# Ideation
## What do we need to analyse user information or behaviour?
1. When does a user open the app?
2. How many times do they open the app in a day?
3. What are the apps available in user's phone? (for tracking user personality)
4. When they close it?
5. How much time do they spend using our app?
6. Do they click on the nots when we send it?
7. How long did the nots stayed in the notification Tray?
8. How much time do they spend using the app after clicking a specific type of notification(time spending would differ in payment status/follower gain/like gain nots)?
9. How many times do the user dismisses our app's nots?
10. Do our users allow the permissions we ask?
11. How much time do the user use their mobile phone(to check if our app's even relevant to productive users)?
12. What device does the user have(to check what our user's personality are, how much % of users could afford to spend money if we offer some paid services in the app)?
13. Does our user opens/use our app in case of low battery(is the app useful for 'em in such case as well)?
14. Can Track their location as well, but play store would suspend our app for using location just for tracking without having a proper use of it. so avoiding it as of now.
We can track more activities as well, if it's relevant for the modelling of user data. For now i think it's good enough data points to analyse our users.
We can make frequent network requests as the action happen, but i don't wanna do that and would wanna save bandwidth as well as make a complete single network request when the user closes the app.


# Implementation Overview
```
Better approach would be to ship the analytics code into a module/package and then import that module back into the project
if (data is on):
    check if `SERVER_OKAY` is `true`
    send the data to the server
    if (success):
        done
    if (failure):
        add the [error message] into `retries` array
        resend after [n seconds]
        if (`retries`.length >= [`n retries`]):
            set `SERVER_OKAY` to `false`
            make a prioritised network req. to another endpoint named PQ(assuming hosted on another server)
            till then keep the data saved into the db
            when get the success response back from PQ[via notification payload], then set `SERVER_OKAY` to `true` & try sending the data again

if (data is off):
    keep the data saved into the db, after sending data to server delete the data from db

Broadcast is set for checking changes in data connectivity
```
