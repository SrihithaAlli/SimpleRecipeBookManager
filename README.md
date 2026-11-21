
# 🍽️ Simple Recipe Book Manager

A Java-based console application that allows users to manage their favourite recipes. The project demonstrates Object-Oriented Programming (OOP), Java Collections, and File Handling.

## 📌 Project Overview

The Simple Recipe Book Manager helps users add, view, search, and organize vegetarian and non-vegetarian recipes through a simple menu-driven interface.

## ✨ Features

- Add vegetarian and non-vegetarian recipes.
- View all saved recipes.
- Search recipes by name or ingredient.
- View complete recipe details.
- Mark or unmark recipes as favourites.
- View favourite recipes.
- Generate and save a collection report.
- Store recipe information using file handling.

## 🛠️ Technologies Used

- Java
- JDK 21
- Eclipse IDE
- ArrayList
- HashSet
- File Handling
- Object-Oriented Programming

## 📂 Project Structure

```text
SimpleRecipeBookManager/
│
├── src/
│   └── recipebook/
│       ├── Recipe.java
│       ├── VegetarianRecipe.java
│       ├── NonVegetarianRecipe.java
│       ├── RecipeService.java
│       ├── FileManager.java
│       └── Main.java
│
├── data/
│   ├── recipes.txt
│   └── report.txt
│
└── README.md
```

## 🧠 OOP Concepts Implemented

- **Abstraction:** Recipe is an abstract parent class.
- **Inheritance:** VegetarianRecipe and NonVegetarianRecipe extend Recipe.
- **Encapsulation:** Recipe data is managed through private fields and methods.
- **Polymorphism:** Child classes provide their own recipe type implementations.

## ▶️ How to Run

1. Clone or download this repository.
2. Open Eclipse IDE.
3. Import the project as an existing Java project.
4. Make sure JDK 21 is configured.
5. Open `Main.java`.
6. Run the application as a Java Application.

## 💾 Data Storage

The application uses text files to store recipe information and generated collection reports.

- `recipes.txt` – Stores recipe details.
- `report.txt` – Stores the collection report.

## 👩‍💻 Author

**Srihitha Alli**

---

*Built as a Java project to practice Object-Oriented Programming, Collections, and File Handling.*
