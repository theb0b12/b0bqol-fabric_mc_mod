package b0b.b0bqol.features;

public abstract class Feature {

    private boolean state = false;

    public String name;

    public Feature(String name) {
        this.name = name;
    }

    public Feature setState(boolean s) {
        this.state = s;
        return this;
    }
    public boolean getState() {
        return this.state;
    }

}
