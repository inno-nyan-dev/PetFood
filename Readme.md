# Final report
Report is available at https://drive.google.com/file/d/1ttXd5R3F8LpRK9cwxm1mMzUIEC2UakD-/view?usp=sharing

#  Rabbit Nutrition Handbook

![build](https://github.com/inno-nyan-dev/PetFood/actions/workflows/android_build.yml/badge.svg)
[![docs](https://github.com/inno-nyan-dev/PetFood/actions/workflows/build_docs.yml/badge.svg)](https://inno-nyan-dev.github.io/PetFood)
![test](https://github.com/inno-nyan-dev/PetFood/actions/workflows/android_test.yml/badge.svg)

**Rabbit Nutrition Handbook** is a mobile application that helps its users to properly feed their pet rabbits. Current version of the application allows users to determine in what extend the food is dangerous for rabbits, in which quantities we can give it to them and when. The most approptiate application type is mobile application, Android especcially, since the phone is always with us and we can get info about food everywere with internet connection.

# Installation
Check out **Releases** page of the repository to latest available version of the app. There you can see `.apk` file, which is an installable executable. 

# Usage
Current version of app allows users to search for various products. When app is opened, user will see list of foods, together with their danger level. At the top 
of screen, there is a search bar, which allows to filter foods.

To see more information about particular product, one can tap its car in the list and then detail screen will be opened.

There is also a side navigation panel, that is opened with slide from left. There user can visit login
and signup pages to create an account. When logged in, user can mark foods as favorite by pressing star icon at food.

# Team members
- Anton Brisilin (BS18-SB)
- Grigoriy Dolgov (BS18-SE)
- Ravida Saitova (BS18-DS)

# Project development details

Application consist of 2 main parts: Android app itself and the server to store all products, user information, which allows user to connect from any devices and places to their personal account

## Useful links 

- [Server Repository](https://github.com/inno-nyan-dev/Server)
- [Project backlog](https://github.com/orgs/inno-nyan-dev/projects/1)
- [Android app documentation](https://inno-nyan-dev.github.io/PetFood/)
- [Screen design - products list](https://www.justinmind.com/usernote/tests/51948828/51952214/51952216/index.html#/screens/5e9a1d73-e5ba-4404-ac76-a6ec5c08827e)
- [Detailed product description design](https://www.justinmind.com/usernote/tests/51948828/52111644/52111646/index.html#/screens/c4f394c1-7fb1-478b-a909-b745186b5eeb)

### Backend technology used
- Spring
- Maven
- Docker

Server arcitecture is MC (Model-Connector) based and written in Kotlin language.

### Android application 
- Kotlin 
- Clean arcitecture 
- Arcitecture is MVVM ( Model View - View Model ).
