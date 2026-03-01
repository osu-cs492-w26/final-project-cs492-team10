# Project Name: Higher or Lower: Movie Ratings Edition
### Team: Group 10
### Members: Aaron Underhill, Bernardo Mendes, Luke Scovel, Seojin Lee

## High Level Project Description
Our app will be based on the popular Higher or Lower game. The app starts with the user being given a starting movie (which we can call movie A) and then another movie (which we can call movie B). With the two movies, the user will have to choose whether or not their starting movie (movie A) is rated higher or lower than the other movie (movie B). If the user guesses correctly, then the second movie (movie B) will take the place of the starting movie (movie A) and then a new movie will be shown to repeat the cycle. This cycle/game will end when the user chooses the wrong option. The app will also have another screen to show the users their highest score/round achieved. 
## API Usage
Our group will use The Movie Database (TMDB) API for our app. Our app will use their Details API call function which is used like this: https://api.themoviedb.org/3/movie/{movie_id}. This function is used by passing a movie ID into the call to get a top level detail of a movie. 

Our app will have a function to randomly choose two movie IDs and then use the random IDs to call for their movie details. With the API response, the app will parse out the details such as their title, poster, rating, genre and overview which is listed in the response as “original_title”, “poster_path”, “vote_average”, “genres[]”, and “overview”. Our app will then use this data for the game function. 

Link: https://developer.themoviedb.org/reference/movie-details
## Project UI/Organization
Screens/UI:
Home screen:
The home screen is where it has the name of the app and navigation buttons on the bottom of the title for scoreboard, genre and start game.

Scoreboard screen :
The scoreboard screen is a scoreboard where the user can see their best scores using stored application data. The scoreboard is local so it will not be a global scoreboard connected with other apps. It will show the score with the date when it was achieved. The scoreboard is just a static screen so it will not have any user interactions other than the back button to go back into the home screen. 

Genre Options screen:
The genre options screen will have a checklist where they can choose the genre of movies that will show up. This checklist will default to all but the user will be able to scroll through and choose certain genres to show up during the game. Other than the genre checklist, there will be a back button to go back to the previous screen. 

Game screen: 
The game screen is where all of the game will take place. It will start by showing the user two movies and the user will then have the option to click on the “Higher” or “Lower” button to decide whether or not the first movie is higher or lower rated than the second movie. It will keep cycling through the movies until the user chooses the wrong option and loses. Other than the buttons for choosing higher or lower, it will also have a quit button to go back into the home screen. 

Game Over screen:
The game over screen will display when the user loses the game. This screen will show the score/how many in a row they got right. Next to the score, this screen will also have buttons to play again, go into the scoreboard, and home screen. 

Movie details screen:
During the game, along with the movie title and posture, the user will be able to click on a small “?” button to see more details about the movie. This screen will show information like the genre, small summary, release date. This screen will be a static page with no user interaction other than the back button to go back into the game screen. 
## Additional Feature Not Covered in Class (Change later)
Our group will use the Coil library to implement an image loading feature for our app. The TMDB API call already includes poster images in their response so our team will use that image with Coil to load in and display the movie posters within our game. 

Link: https://coil-kt.github.io/coil/
## Division of Labor
Aaron: UI Designer: They will work on the ui design of the app like how the different screens will look. 

Bernardo: API Handler: They will work on setting up the API connection. They will work on getting the API response and parsing it so that the game system can use the data. 

Luke: Game System Designer: They will work on getting the main game system working. From the data obtained from the API, they will work on how to get the rating comparison and game cycle to function. 

Seojin: App functionality Developer: They will work on getting the scoreboard, options, screen navigation and other app functionality not directly tied to the game system working.
