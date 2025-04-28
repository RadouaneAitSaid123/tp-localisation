# Location Tracking Application

This application tracks the user's location and sends the data to a PHP backend server for storage in a MySQL database.

## Project Structure

- **app/**: Android application code
  - **src/main/java/com/example/localisation_app/**: Java source code
    - **MainActivity.java**: Entry point of the app with a button to show the map
    - **MapsActivity.java**: Handles location tracking and sending data to the server
  - **src/main/res/**: Resources (layouts, strings, etc.)
    - **layout/activity_main.xml**: Main screen layout
    - **layout/activity_maps.xml**: Map screen layout

- **backend/**: PHP backend code
  - **Position.php**: Contains the Position class, IDao interface, and PositionService class
  - **createPosition.php**: Handles POST requests to save location data
  - **database.sql**: SQL script to create the database and tables
  - **README.md**: Setup instructions for the backend

## Changes Made

1. Fixed inconsistent server URLs in the Android app by introducing a `SERVER_IP` constant in `MapsActivity.java`.
2. Created PHP backend files based on the provided code:
   - `Position.php`: Contains the Position class and PositionService
   - `createPosition.php`: Handles POST requests from the Android app
3. Created a SQL script (`database.sql`) to set up the database schema.
4. Added comprehensive setup instructions in the backend README.

## Setup Instructions

### Android App

1. Open the project in Android Studio.
2. Update the `SERVER_IP` variable in `MapsActivity.java` to point to your server's IP address:
   ```java
   private static final String SERVER_IP = "your_server_ip";
   ```
3. Build and run the app on your device.

### Backend

See the [backend README](backend/README.md) for detailed setup instructions.

## How It Works

1. The app starts with a main screen that has a button to show the map.
2. When the user clicks the button, the app opens the map screen.
3. The map screen requests location permissions if not already granted.
4. Once permissions are granted, the app starts tracking the user's location.
5. When the location changes, the app sends the new location data to the PHP backend.
6. The PHP backend saves the location data to the MySQL database.
7. The app displays a toast message indicating whether the location was saved successfully.

## Requirements

- Android device with location services enabled
- Web server with PHP and MySQL support
- Both the Android device and the server should be on the same network (for local testing)