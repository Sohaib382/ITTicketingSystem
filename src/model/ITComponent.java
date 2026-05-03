package model;

public class ITComponent {
    private String componentId;
    private String name;
    private String type;

    public ITComponent(String componentId, String name, String type) {
        this.componentId = componentId;
        this.name = name;
        this.type = type;
    }

    public String getComponentId() { return componentId; }
    public String getName()        { return name; }
    public String getType()        { return type; }

    @Override
    public String toString() { return name + " [" + type + "]"; }
}