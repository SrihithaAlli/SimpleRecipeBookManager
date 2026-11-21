
package recipebook;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FileManager {

    private final Path dataFolder = Path.of("data");
    private final Path recipeFile = dataFolder.resolve("recipes.txt");
    private final Path reportFile = dataFolder.resolve("report.txt");

    // Create data folder if it does not exist
    public void createDataFolder() throws IOException {
        Files.createDirectories(dataFolder);
    }

    // Load recipes from file
    public ArrayList<Recipe> loadRecipes() throws IOException {

        createDataFolder();

        ArrayList<Recipe> loadedRecipes = new ArrayList<>();

        if (!Files.exists(recipeFile)) {
            return loadedRecipes;
        }

        Set<Integer> ids = new HashSet<>();
        Set<String> names = new HashSet<>();

        try (BufferedReader reader =
                Files.newBufferedReader(recipeFile, StandardCharsets.UTF_8)) {

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {

                lineNumber++;

                try {
                    String[] fields = line.split("\\|", -1);

                    if (fields.length != 8) {
                        throw new IllegalArgumentException(
                            "Expected exactly 8 fields");
                    }

                    int id = Integer.parseInt(fields[0]);

                    if (id <= 0) {
                        throw new IllegalArgumentException(
                            "ID must be positive");
                    }

                    if (!ids.add(id)) {
                        throw new IllegalArgumentException(
                            "Duplicate recipe ID");
                    }

                    String type = fields[1];
                    String name = fields[2];

                    if (name.trim().isEmpty()) {
                        throw new IllegalArgumentException(
                            "Recipe name cannot be empty");
                    }

                    if (!names.add(
                            name.trim().toLowerCase(java.util.Locale.ROOT))) {
                        throw new IllegalArgumentException(
                            "Duplicate recipe name");
                    }

                    String[] ingredients = {
                        fields[3], fields[4], fields[5]
                    };

                    String instructions = fields[6];

                    if (instructions.trim().isEmpty()) {
                        throw new IllegalArgumentException(
                            "Instructions cannot be empty");
                    }

                    boolean favourite;

                    if (fields[7].equals("true")) {
                        favourite = true;
                    } else if (fields[7].equals("false")) {
                        favourite = false;
                    } else {
                        throw new IllegalArgumentException(
                            "Favourite must be true or false");
                    }

                    Recipe recipe;

                    if (type.equals("VEG")) {

                        recipe = new VegetarianRecipe(
                            id, name, ingredients,
                            instructions, favourite);

                    } else if (type.equals("NONVEG")) {

                        recipe = new NonVegetarianRecipe(
                            id, name, ingredients,
                            instructions, favourite);

                    } else {

                        throw new IllegalArgumentException(
                            "Invalid recipe type");
                    }

                    loadedRecipes.add(recipe);

                } catch (RuntimeException e) {

                    throw new IOException(
                        recipeFile + " - Line " + lineNumber +
                        ": " + e.getMessage(), e);
                }
            }

        } catch (IOException e) {
            throw new IOException(
                "Could not read " + recipeFile + ": " +
                e.getMessage(), e);
        }

        return loadedRecipes;
    }

    // Save all recipes
    public void saveRecipes(ArrayList<Recipe> recipes)
            throws IOException {

        createDataFolder();

        Path tempFile = Files.createTempFile(
            dataFolder, "recipes_", ".tmp");

        try {

            try (BufferedWriter writer =
                    Files.newBufferedWriter(
                        tempFile, StandardCharsets.UTF_8)) {

                for (Recipe recipe : recipes) {

                    String[] ingredients = recipe.getIngredients();

                    String line =
                        recipe.getId() + "|" +
                        recipe.getTypeCode() + "|" +
                        recipe.getName() + "|" +
                        ingredients[0] + "|" +
                        ingredients[1] + "|" +
                        ingredients[2] + "|" +
                        recipe.getInstructions() + "|" +
                        recipe.isFavourite();

                    writer.write(line);
                    writer.newLine();
                }
            }

            replaceFile(tempFile, recipeFile);

        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    // Replace original file safely
    private void replaceFile(Path tempFile, Path target)
            throws IOException {

        try {

            Files.move(
                tempFile,
                target,
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.ATOMIC_MOVE
            );

        } catch (AtomicMoveNotSupportedException e) {

            Files.move(
                tempFile,
                target,
                StandardCopyOption.REPLACE_EXISTING
            );
        }
    }

    // Save collection report
    public void saveReport(RecipeService service)
            throws IOException {

        createDataFolder();

        Path tempFile = Files.createTempFile(
            dataFolder, "report_", ".tmp");

        try {

            try (BufferedWriter writer =
                    Files.newBufferedWriter(
                        tempFile, StandardCharsets.UTF_8)) {

                writer.write("========== RECIPE BOOK REPORT ==========");
                writer.newLine();

                writer.write("Total Recipes: " +
                    service.getTotalRecipes());
                writer.newLine();

                writer.write("Vegetarian Recipes: " +
                    service.getVegetarianCount());
                writer.newLine();

                writer.write("Nonvegetarian Recipes: " +
                    service.getNonVegetarianCount());
                writer.newLine();

                writer.write("Favourite Recipes: " +
                    service.getFavouriteCount());
                writer.newLine();

                writer.write("========================================");
                writer.newLine();
            }

            replaceFile(tempFile, reportFile);

        } finally {
            Files.deleteIfExists(tempFile);
        }
    }
}
