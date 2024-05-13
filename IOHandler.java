import java.io.*;

public class IOHandler {

    private File file;

    public IOHandler(String path) {
        this.file = new File(path);
    }

    public int readLastPoints() {

        BufferedReader bufferedReader = null;
        int readValue = 0;

        try {
            FileReader fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                readValue = Integer.parseInt(line);
            }
            bufferedReader.close();

        } catch(IOException ex) {
            ex.printStackTrace();
        } catch(NumberFormatException ex) {
            ex.printStackTrace();
            readValue=0;
        } finally {
            try {
                bufferedReader.close();
            } catch(IOException  ex) {
                ex.printStackTrace();
            }
        }

        return readValue;
    }

    public void writeLastPoints(int lastPoints) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(this.file);
            writer.write(Integer.toString(Math.round(lastPoints)));
            writer.close();

        } catch(IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch(IOException  ex) {
                ex.printStackTrace();
            }
        }
    }
}

