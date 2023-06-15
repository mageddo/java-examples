package vanillajavaexamples.exemplo_uml;


import java.util.HashSet;
import java.util.Set;

/**
* @generated
*/
public class Vehicle {

    /**
    * @generated
    */
    private String model;


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
    public String getModel() {
        return this.model;
    }

    /**
    * @generated
    */
    public void setModel(String model) {
        this.model = model;
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
    public Plate getPlate() {
        return this.plate;
    }

    /**
    * @generated
    */
    public void setPlate(Plate plate) {
        this.plate = plate;
    }



}
