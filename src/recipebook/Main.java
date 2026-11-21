
package recipebook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    private static final RecipeService service = new RecipeService();
    private static final FileManager fileManager = new FileManager();



    // Main method
    public static void main(String[] args) {

        try {
            // Create data folder
            fileManager.createDataFolder();

            // Load previously saved recipes
            ArrayList<Recipe> loadedRecipes =
                    fileManager.loadRecipes();

            service.loadRecipes(loadedRecipes);

            System.out.println("Recipes loaded successfully!");

        } catch (IOException | IllegalArgumentException e) {

            System.out.println("Startup error: " + e.getMessage());
            System.out.println("Please correct the data file and restart.");
            return;
        }

        System.out.println("\nWelcome to Simple Recipe Book Manager!");

        boolean running = true;

        while (running) {

            showMenu();

            int choice = readInt("Enter your choice: ");

            switch (choice) {

                case 1:
                    addRecipe();
                    break;

                case 2:
                    viewAllRecipes();
                    break;

                case 3:
                    searchRecipes();
                    break;

                case 4:
                    viewRecipeDetails();
                    break;

                case 5:
                    updateFavourite();
                    break;

                case 6:
                    viewFavourites();
                    break;

                case 7:
                    viewAndSaveReport();
                    break;

                case 0:
                    System.out.println("Thank you for using Recipe Book!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }

        sc.close();
    }

    // Display main menu
    private static void showMenu() {

        System.out.println("\n========== RECIPE BOOK MENU ==========");
        System.out.println("1. Add Recipe");
        System.out.println("2. View All Recipes");
        System.out.println("3. Search by Name or Ingredient");
        System.out.println("4. View Recipe Details");
        System.out.println("5. Mark or Unmark Favourite");
        System.out.println("6. View Favourite Recipes");
        System.out.println("7. View and Save Collection Report");
        System.out.println("0. Exit");
        System.out.println("======================================");
    }

    // Read integer safely
    private static int readInt(String message) {

        while (true) {

            System.out.print(message);
            String input = sc.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    // Read required text
    private static String readText(String message) {

        while (true) {

            System.out.print(message);
            String input = sc.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("This field cannot be empty.");
                continue;
            }

            if (input.contains("|") ||
                input.contains("\n") ||
                input.contains("\r")) {

                System.out.println("Special separator characters are not allowed.");
                continue;
            }

            return input;
        }
    }

    // Add new recipe

private static void addRecipe() {

    System.out.println("\n========== ADD RECIPE ==========");

    String name = readText("Enter recipe name: ");

    System.out.println("\nSelect Recipe Type:");
    System.out.println("1. Vegetarian");
    System.out.println("2. Nonvegetarian");

    int type = readInt("Enter type: ");

    if (type != 1 && type != 2) {
        System.out.println("Invalid recipe type!");
        return;
    }

    String[] ingredients = new String[3];

    for (int i = 0; i < 3; i++) {
        ingredients[i] =
                readText("Enter ingredient " + (i + 1) + ": ");
    }

    String instructions =
            readText("Enter preparation instructions: ");

    int id = service.getNextId();

    Recipe recipe;

    if (type == 1) {

        recipe = new VegetarianRecipe(
                id, name, ingredients, instructions, false);

    } else {

        recipe = new NonVegetarianRecipe(
                id, name, ingredients, instructions, false);
    }

    ArrayList<Recipe> backup = service.getAllRecipes();

    if (service.addRecipe(recipe)) {
        saveAfterChange(backup);
    }
}
    // Save changes or restore previous records on failure
    private static void saveAfterChange(ArrayList<Recipe> backup) {

        try {

            fileManager.saveRecipes(service.getAllRecipes());

            System.out.println("Changes saved successfully!");

        } catch (IOException e) {

            service.loadRecipes(backup);

            System.out.println("Could not save changes: " +
                    e.getMessage());

            System.out.println("Previous records have been restored.");
        }
    }

    // Display all recipes
    private static void viewAllRecipes() {

        System.out.println("\n========== ALL RECIPES ==========");

        ArrayList<Recipe> recipes = service.getAllRecipes();

        if (recipes.isEmpty()) {
            System.out.println("No records found.");
            return;
        }

        for (Recipe recipe : recipes) {
            displayRecipeSummary(recipe);
        }
    }

    // Display basic recipe information
    private static void displayRecipeSummary(Recipe recipe) {

        System.out.println(
                recipe.getId() + " | " +
                recipe.getName() + " | " +
                recipe.getTypeLabel() +
                (recipe.isFavourite() ? " | Favourite" : ""));
    }

    // Search recipes
    private static void searchRecipes() {

        System.out.println("\n========== SEARCH RECIPES ==========");
        System.out.println("1. Search by Name");
        System.out.println("2. Search by Ingredient");

        int choice = readInt("Enter choice: ");

        if (choice != 1 && choice != 2) {
            System.out.println("Invalid search option!");
            return;
        }

        String keyword = readText("Enter search keyword: ");

        ArrayList<Recipe> results;

        if (choice == 1) {
            results = service.searchByName(keyword);
        } else {
            results = service.searchByIngredient(keyword);
        }

        if (results.isEmpty()) {
            System.out.println("No matching recipes found.");
        } else {

            System.out.println("\nSearch Results:");

            for (Recipe recipe : results) {
                displayRecipeSummary(recipe);
            }
        }
    }

    // View complete recipe details
    private static void viewRecipeDetails() {

        System.out.println("\n========== RECIPE DETAILS ==========");

        int id = readInt("Enter Recipe ID: ");

        Recipe recipe = service.findById(id);

        if (recipe == null) {
            System.out.println("Recipe not found!");
            return;
        }

        System.out.println("\nRecipe ID: " + recipe.getId());
        System.out.println("Recipe Name: " + recipe.getName());
        System.out.println("Type: " + recipe.getTypeLabel());

        System.out.println("\nIngredients:");

        String[] ingredients = recipe.getIngredients();

        for (int i = 0; i < ingredients.length; i++) {
            System.out.println((i + 1) + ". " + ingredients[i]);
        }

        System.out.println("\nInstructions: " +
                recipe.getInstructions());

        System.out.println("Favourite: " +
                (recipe.isFavourite() ? "Yes" : "No"));
    }

    // Mark or unmark favourite
    private static void updateFavourite() {

        System.out.println("\n========== UPDATE FAVOURITE ==========");

        int id = readInt("Enter Recipe ID: ");

        Recipe recipe = service.findById(id);

        if (recipe == null) {
            System.out.println("Recipe not found!");
            return;
        }

        System.out.println("1. Mark as Favourite");
        System.out.println("2. Unmark Favourite");

        int choice = readInt("Enter choice: ");

        if (choice != 1 && choice != 2) {
            System.out.println("Invalid choice!");
            return;
        }

        boolean newStatus = (choice == 1);

        if (recipe.isFavourite() == newStatus) {
            System.out.println("Favourite status is already set.");
            return;
        }

        ArrayList<Recipe> backup = service.getAllRecipes();

        if (service.markFavourite(id, newStatus)) {

            saveAfterChange(backup);

            // Check whether the update was saved
            Recipe updated = service.findById(id);

            if (updated != null &&
                updated.isFavourite() == newStatus) {

                System.out.println("Favourite status updated!");
            }
        }
    }

    // Display favourite recipes
    private static void viewFavourites() {

        System.out.println("\n========== FAVOURITE RECIPES ==========");

        ArrayList<Recipe> favourites = service.getFavourites();

        if (favourites.isEmpty()) {
            System.out.println("No favourite recipes.");
            return;
        }

        for (Recipe recipe : favourites) {
            displayRecipeSummary(recipe);
        }
    }

    // Display and save collection report
    private static void viewAndSaveReport() {

        System.out.println("\n========== COLLECTION REPORT ==========");

        System.out.println("Total Recipes: " +
                service.getTotalRecipes());

        System.out.println("Vegetarian Recipes: " +
                service.getVegetarianCount());

        System.out.println("Nonvegetarian Recipes: " +
                service.getNonVegetarianCount());

        System.out.println("Favourite Recipes: " +
                service.getFavouriteCount());

        System.out.println("\n1. Save Report");
        System.out.println("2. Back to Main Menu");

        int choice = readInt("Enter choice: ");

        if (choice == 1) {

            try {

                fileManager.saveReport(service);

                System.out.println(
                        "Report saved successfully in data/report.txt");

            } catch (IOException e) {

                System.out.println(
                        "Report saving failed: " + e.getMessage());
            }

        } else if (choice != 2) {

            System.out.println("Invalid choice!");
        }
    }
}
