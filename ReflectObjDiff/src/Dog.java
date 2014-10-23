/**
 * @author rfoote
 */
public class Dog {
    private int age;
    private Breed breed;
    private int[] dogArr;
    
    public Dog(){
        this.age = 0;
        this.breed = new Breed("mutt");
        this.dogArr = new int[2];
        this.dogArr[0] = 10;
        this.dogArr[1] = 8;
    }
    
    public Dog(int ageIn, Breed breedIn){
        this.age = ageIn;
        this.breed = breedIn;
        this.dogArr = new int[2];
        this.dogArr[0] = 10;
        this.dogArr[1] = 9;
    }
    
    public void setArray(int index, int value){
        this.dogArr[index] = value;
    }
    
    public void setAge(int ageIn){
        this.age = ageIn;
    }
    
    public int getAge(){
        return age; 
    }
}