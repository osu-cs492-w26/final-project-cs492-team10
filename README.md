# Project Name: Higher or Lower: Movie Ratings Edition
### Team: Group 10
### Members: Aaron Underhill, Bernardo Mendes, Luke Scovel, Seojin Lee

## High Level Project Description
Our app will be based on the popular Higher or Lower game. The app starts with the user being given a starting movie (which we can call movie A) and then another movie (which we can call movie B). With the two movies, the user will have to choose which of the two movies is higher rated and click on it. If the user guesses correctly, the higher rated movie will take the position of the starting movie (movie A) and then a new movie will be shown to repeat the cycle. This cycle/game will end when the user chooses the wrong option. The app will also have a scoreboard screen to show the users their highest score/round achieved and settings screen for chosing genre and gamemode. 

## API Usage
Our group will use The Movie Database (TMDB) API for our app. Our app will use their Details API call function which is used like this: https://api.themoviedb.org/3/movie/{movie_id}. This function is used by passing a movie ID into the call to get a top level detail of a movie. 

Our app will have a function to randomly choose two movie IDs and then use the random IDs to call for their movie details. With the API response, the app will parse out the details such as their title, poster, rating, genre and overview which is listed in the response as “original_title”, “poster_path”, “vote_average”, “genres[]”, and “overview”. Our app will then use this data for the game function. 

Link: https://developer.themoviedb.org/reference/movie-details
## Project UI/Organization
Screens/UI:
Home screen:
The home screen is where it has the name of the app and navigation buttons on the bottom of the title for scoreboard, setting and start game.

Scoreboard screen :
The scoreboard screen is a scoreboard where the user can see their top 10 best scores using stored application data. The scoreboard is local so it will not be a global scoreboard connected with other apps. It will show the score with the date, the game mode and the genre chosen when the game was played. The scoreboard also has a "clear board" button and a "share scores" button along with the back button to go back to the home screen. 

Settings screen:
The settings screen will have a checklist where they can choose the genre of movies that will show up. This checklist will default to all but the user will be able to scroll through and choose certain genres to show up during the game. The user will also have the option to chose which game mode they want to play. "Classic mode" is like the original game where the higher rated movie stays after each round and "Random mode" is where both movies will be replaced each round. Other than the two settings options, there will be a back button to go back to the previous screen. 

Game screen: 
The game screen is where all of the game will take place. It will start by showing the user two movies and the user will then have the option to click on one of the movies which they think is higher rated. It will keep cycling through the movies until the user chooses the wrong option and loses. This screen will function differently depending on the options set in settings but the general functionality of the screen will be the same no matter what options are applied. Other than the buttons for choosing which movie is higher rated, playing next round/new game and show movie details, it will also have a quit button to go back into the home screen. 

Movie details screen:
During the game, along with the movie title and poster, the user will be able to click on a small “?” button to see more details about the movie. This screen will show information like the genre, runtime, release date, budget, box office numbers and the trailer. The user can view the trailer in app. Other than that the user can go back to the game to continue on.

## Additional Feature Not Covered in Class
Our group will include a video embedding feature into our app. It will use the API call to get the Youtube trailer information of each movie and embed that movie trailer into the movie details screen so that the user can view the movie trailer while they are playing the game. 

## Division of Labor
Aaron: UI Designer: They will work on the ui design of the app like how the different screens will look. 

Bernardo: API Handler: They will work on setting up the API connection. They will work on getting the API response and parsing it so that the game system can use the data. 

Luke: Game System Designer: They will work on getting the main game system working. From the data obtained from the API, they will work on how to get the rating comparison and game cycle to function. 

Seojin: App functionality Developer: They will work on getting the scoreboard, options, screen navigation and other app functionality not directly tied to the game system working.
