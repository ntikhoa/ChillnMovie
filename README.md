# ChillnMovie
demo link: https://www.youtube.com/watch?v=JE2nJsIrVoA&ab_channel=KhoaNguyen
# Introduction
ChillnMovie is an movies rating source where users can get the information related to movies and rates their favorite movies. It also updates and recommends movies 
to user in categories like: trending, top rated, upcoming, now playing and Vietnamese movies.
# How it works
ChillnMovie has two type of end users: editors and users. Editors is control the data that the users can see. Editor can edit movies, add a new movies and add a summary
reviews for the film, Editor can also put movies in different categories.
# Architecture (MVVM model)
![](Preview/architecture.png)
# Technique uses
* MVVM model (architecture component, live data)
* Hilt (dagger 2)
* RESTful API (Retrofit)
* Google Firebase (Firestore, Auth, Storage)
* Algolia (text search)
* Pagination (for api, firebase and algolia)
* Material Design
* View Binding
