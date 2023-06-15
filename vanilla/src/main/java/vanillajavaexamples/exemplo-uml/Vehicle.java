package exemplo-uml;


/**
* @generated
*/
public class Vehicle {
    
    /**
    * @generated
    */
    private String moidel;
    
    
    /**
    * @generated
    */
    private Set<Wheel> wheel;
    
    /**
    * @generated
    */
    private Plate plate;
    
    

    /**
    * @generated
    */
    public String getMoidel() {
        return this.moidel;
    }
    
    /**
    * @generated
    */
    public String setMoidel(String moidel) {
        this.moidel = moidel;
    }
    
    
    
    /**
    * @generated
    */
    public Set<Wheel> getWheel() {
        if (this.wheel == null) {
            this.wheel = new HashSet<Wheel>();
        }
        return this.wheel;
    }
    
    /**
    * @generated
    */
    public Set<Wheel> setWheel(Wheel wheel) {
        this.wheel = wheel;
    }
    
    
    /**
    * @generated
    */
    public Plate getPlate() {
        return this.plate;
    }
    
    /**
    * @generated
    */
    public Plate setPlate(Plate plate) {
        this.plate = plate;
    }
    
    
    
}
