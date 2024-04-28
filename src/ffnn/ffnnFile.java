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

    public static void writeFfnnToFile(File file, ffnn nn, boolean append) throws Exception {
        if(file == null)
            throw new Exception("File can't be null");
        try {
            FileWriter myWriter = new FileWriter(file.getName(),append);
            myWriter.write(nn.toString());
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public static void writeFfnnToFileTotal(File file, ffnn[] nn) throws Exception {
        if(file == null)
            throw new Exception("File can't be null");
        for (ffnn ffnn : nn) {
            writeFfnnToFile(file, ffnn, true);
        }
    }

    public static ffnn readFfnnFromFileSingle(File file) throws Exception {
        if (file == null) {
            throw new Exception("File can't be null");
        }
        StringBuilder contentBuilder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String currentLine;
            int i = 0;
            while ((currentLine = reader.readLine()) != null && i < 4) {
                contentBuilder.append(currentLine).append("\n");
                i++;
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
    public static ffnn[] readFfnnFromFileTotal(File file) throws Exception {
        if (file == null) {
            throw new Exception("File can't be null");
        }

        StringBuilder contentBuilder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            int lines = 0;
            while (reader.readLine() != null) lines++;
            ffnn[] population = new ffnn[lines/4];
            String currentLine;
            int i = 1;
            reader= new BufferedReader(new FileReader(file));
            while ((currentLine = reader.readLine()) != null) {
                contentBuilder.append(currentLine).append("\n");
                if( i%4 == 0){
                    String content = contentBuilder.toString();
                    population[(i/4) - 1] = ffnn.fromString(content);
                    contentBuilder = new StringBuilder();
                }
                i++;
            }
            reader.close();
            return population;
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
            return null;
        }
    }

}
