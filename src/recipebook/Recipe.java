
package recipebook;

public abstract class Recipe {

    private int id;
    private String name;
    private String instructions;
    private String[] ingredients;
    private boolean favourite;

    // Constructor
    public Recipe(int id, String name, String[] ingredients,
                  String instructions, boolean favourite) {

        if (id <= 0) {
            throw new IllegalArgumentException("ID must be positive");
        }

        this.id = id;
        this.name = validateText(name, "Recipe name");
        this.instructions = validateText(instructions, "Instructions");

        if (ingredients == null || ingredients.length != 3) {
            throw new IllegalArgumentException(
                "Exactly three ingredients are required");
        }

        this.ingredients = ingredients.clone();

        for (int i = 0; i < this.ingredients.length; i++) {
            this.ingredients[i] =
                validateText(this.ingredients[i], "Ingredient");
        }

        this.favourite = favourite;
    }

    // Validate text fields
    private String validateText(String value, String fieldName) {

        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(
                fieldName + " cannot be empty");
        }

        if (value.contains("|") ||
            value.contains("\n") ||
            value.contains("\r")) {
            throw new IllegalArgumentException(
                fieldName + " contains invalid characters");
        }

        return value.trim();
    }

    // Getter methods
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInstructions() {
        return instructions;
    }

    public String[] getIngredients() {
        return ingredients.clone();
    }

    public boolean isFavourite() {
        return favourite;
    }

    // Update favourite status
    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    // Abstract methods
    public abstract String getTypeLabel();

    public abstract String getTypeCode();
}
