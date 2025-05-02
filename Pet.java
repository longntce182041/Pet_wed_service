/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author Nguyen Thanh Long - CE182041
 */
public class Pet {
    private String pet_id;
    private String name_pet;
    private String type;
    private int age;
    private String user_id;
    private double weight_kg;
    private String note;
    private String gender;

    public Pet() {
    }

    public Pet(String pet_id, String name_pet, String type, int age, String user_id, double weight_kg, String note, String gender) {
        this.pet_id = pet_id;
        this.name_pet = name_pet;
        this.type = type;
        this.age = age;
        this.user_id = user_id;
        this.weight_kg = weight_kg;
        this.note = note;
        this.gender = gender;
    }

  
    public Pet(String pet_id, String name_pet, String type, int age, double weight_kg, String note, String gender) {
        this.pet_id = pet_id;
        this.name_pet = name_pet;
        this.type = type;
        this.age = age;
        this.weight_kg = weight_kg;
        this.note = note;
        this.gender = gender;
    }

    public String getPet_id() {
        return pet_id;
    }

    public void setPet_id(String pet_id) {
        this.pet_id = pet_id;
    }

    public String getName_pet() {
        return name_pet;
    }

    public void setName_pet(String name_pet) {
        this.name_pet = name_pet;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public double getWeight_kg() {
        return weight_kg;
    }

    public void setWeight_kg(double weight_kg) {
        this.weight_kg = weight_kg;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    
    
  
}
