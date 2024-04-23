package ffnn;
import java.io.*;

public class ffnnFile {
    public static File createFile(String name){
        try {
            File file = new File(name + ".txt");
            file.createNewFile();
            return file;
        } catch (IOException e) {
            System.out.println("An error occurred. With file "+ name);
            e.printStackTrace();
            return null;
        }
    }

    public static void writeFfnnToFile(File file, ffnn nn) throws Exception {
        if(file == null)
            throw new Exception("File can't be null");
        try {
            FileWriter myWriter = new FileWriter(file.getName());
            myWriter.write(nn.toString());
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static ffnn readFfnnFromFile(File file) throws Exception {
        if (file == null) {
            throw new Exception("File can't be null");
        }
        StringBuilder contentBuilder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                contentBuilder.append(currentLine).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
            return null;
        }

        String content = contentBuilder.toString();
        return ffnn.fromString(content);  // Assuming there is a static method fromString in ffnn class to reconstruct the object
    }
}
