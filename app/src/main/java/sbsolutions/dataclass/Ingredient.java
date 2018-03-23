package sbsolutions.dataclass;

/**
 * Created by Code Breaker on 3/22/2018.
 */

public class Ingredient {
    public String ingredient_name;
    public String pic_url;

    public Ingredient(){

    }

    public Ingredient(String ingredient_name, String pic_url){
        this.ingredient_name = ingredient_name;
        this.pic_url = pic_url;
    }

    public String getIngredient_name() {
        return ingredient_name;
    }

    public void setIngredient_name(String ingredient_name) {
        this.ingredient_name = ingredient_name;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }
}
