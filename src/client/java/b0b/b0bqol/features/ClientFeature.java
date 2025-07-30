package b0b.b0bqol.features;

public abstract class ClientFeature {

    private boolean state = false;

    public String name;

    public ClientFeature(String name) {
        this.name = name;
    }

    public ClientFeature setState(boolean s) {
        this.state = s;
        return this;
    }
    public boolean getState() {
        return this.state;
    }


    
}
