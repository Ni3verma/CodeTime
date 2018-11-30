# CodeTime
Capstone project : Udacity android dev last project

To run or contribute to this project:
1. first you need to get your own API key from this [website](https://clist.by/api/v1/doc/).You will get name and apikey.

2. make **api_auth.xml** file in CodeTime/app/src/main/res/values

3. inside this file write this code and add name,key that you got from first step
```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="username">WRITE_NAME_HERE</string>
    <string name="key">WRITE_APIKEY_HERE</string>
</resources>
```
If your credentials are correct then the app will run perfectly fine.
