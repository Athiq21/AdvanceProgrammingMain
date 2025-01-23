//package model;
//
//public class SubCategory {
//
//    private int Id;
//    private String Name;
//
//    private int CategoryId;
//
//    public int getId() {
//        return Id;
//    }
//
//    public void setId(int id) {
//        Id = id;
//    }
//
//    public String getName() {
//        return Name;
//    }
//
//    public void setName(String name) {
//        Name = name;
//    }
//
//    public int getCategoryId() {
//        return CategoryId;
//    }
//
//    public void setCategoryId(int categoryId) {
//        CategoryId = categoryId;
//    }
//}

package model;

public class SubCategory {

    private int id;
    private String name;
    private Category category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
