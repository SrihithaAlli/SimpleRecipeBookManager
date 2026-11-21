
package recipebook;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

public class RecipeService {

    private ArrayList<Recipe> recipes;
    private HashSet<String> recipeNames;

    // Constructor
    public RecipeService() {
        recipes = new ArrayList<>();
        recipeNames = new HashSet<>();
    }

    // Normalize recipe names to prevent duplicates
    private String normalizeName(String name) {
        return name.trim().toLowerCase(Locale.ROOT);
    }

    // Add recipe
    public boolean addRecipe(Recipe recipe) {

        if (recipe == null) {
            return false;
        }

        String key = normalizeName(recipe.getName());

        if (recipeNames.contains(key)) {
            System.out.println("Recipe name already exists!");
            return false;
        }

        recipes.add(recipe);
        recipeNames.add(key);

        
        return true;
    }

    // Return all recipes
    public ArrayList<Recipe> getAllRecipes() {
        return new ArrayList<>(recipes);
    }

    // Find recipe by ID
    public Recipe findById(int id) {

        for (Recipe recipe : recipes) {
            if (recipe.getId() == id) {
                return recipe;
            }
        }

        return null;
    }

    // Search recipes by name
    public ArrayList<Recipe> searchByName(String keyword) {

        ArrayList<Recipe> results = new ArrayList<>();

        if (keyword == null || keyword.trim().isEmpty()) {
            return results;
        }

        String search = keyword.trim().toLowerCase(Locale.ROOT);

        for (Recipe recipe : recipes) {
            if (recipe.getName().toLowerCase(Locale.ROOT)
                    .contains(search)) {
                results.add(recipe);
            }
        }

        return results;
    }

    // Search recipes by ingredient
    public ArrayList<Recipe> searchByIngredient(String keyword) {

        ArrayList<Recipe> results = new ArrayList<>();

        if (keyword == null || keyword.trim().isEmpty()) {
            return results;
        }

        String search = keyword.trim().toLowerCase(Locale.ROOT);

        for (Recipe recipe : recipes) {

            for (String ingredient : recipe.getIngredients()) {

                if (ingredient.toLowerCase(Locale.ROOT)
                        .contains(search)) {

                    results.add(recipe);
                    break;
                }
            }
        }

        return results;
    }

    // Mark or unmark favourite
    public boolean markFavourite(int id, boolean favourite) {

        Recipe recipe = findById(id);

        if (recipe == null) {
            return false;
        }

        recipe.setFavourite(favourite);
        return true;
    }

    // Get favourite recipes
    public ArrayList<Recipe> getFavourites() {

        ArrayList<Recipe> favourites = new ArrayList<>();

        for (Recipe recipe : recipes) {

            if (recipe.isFavourite()) {
                favourites.add(recipe);
            }
        }

        return favourites;
    }

    // Get total recipe count
    public int getTotalRecipes() {
        return recipes.size();
    }

    // Count vegetarian recipes
    public int getVegetarianCount() {

        int count = 0;

        for (Recipe recipe : recipes) {
            if (recipe.getTypeCode().equals("VEG")) {
                count++;
            }
        }

        return count;
    }

    // Count nonvegetarian recipes
    public int getNonVegetarianCount() {

        int count = 0;

        for (Recipe recipe : recipes) {
            if (recipe.getTypeCode().equals("NONVEG")) {
                count++;
            }
        }

        return count;
    }

    // Count favourite recipes
    public int getFavouriteCount() {
        return getFavourites().size();
    }

    // Get next available recipe ID
    public int getNextId() {

        int maxId = 0;

        for (Recipe recipe : recipes) {
            if (recipe.getId() > maxId) {
                maxId = recipe.getId();
            }
        }

        return maxId + 1;
    }

    // Load recipes into service
    public void loadRecipes(ArrayList<Recipe> loadedRecipes) {

        HashSet<String> names = new HashSet<>();
        HashSet<Integer> ids = new HashSet<>();

        for (Recipe recipe : loadedRecipes) {

            if (recipe == null ||
                !names.add(normalizeName(recipe.getName())) ||
                !ids.add(recipe.getId())) {

                throw new IllegalArgumentException(
                    "Duplicate or invalid recipe record");
            }
        }

        recipes.clear();
        recipeNames.clear();

        recipes.addAll(loadedRecipes);
        recipeNames.addAll(names);
    }
}
