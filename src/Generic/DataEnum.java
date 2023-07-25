package Generic;

public enum DataEnum {
    USER("user.ser"),
    CHAT("chat.ser");

    private String fileName;

    DataEnum(String fileName){
        this.fileName = fileName;
    }
    
    public String getFileName() {
        return fileName;
    }
}
