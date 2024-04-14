package toy.ojm.entity;

public enum FoodCategoryName {
    KOREAN("한식"),
    JAPANESE("일식"),
    CHINESE("중식"),
    WESTERN("양식");

    private final String categoryName;

    FoodCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public static FoodCategoryName fromString(String text) {
        for (FoodCategoryName category : FoodCategoryName.values()) {
            if (category.categoryName.equalsIgnoreCase(text)) {
                return category;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}
