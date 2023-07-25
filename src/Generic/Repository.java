package Generic;

import java.io.FileOutputStream;
import java.io.IOException;

import java.io.ObjectOutputStream;

public class Repository<T> {
    
    public void add(T t) {
        // Add item to file
        try {

            FileOutputStream output = new FileOutputStream(DataEnum.USER.getFileName());
            ObjectOutputStream oos = new ObjectOutputStream(output);
            oos.writeObject(t);

            output.close();
            oos.close();

        } catch (IOException e) {

        }
    }

}
