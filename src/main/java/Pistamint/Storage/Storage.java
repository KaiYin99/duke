package Pistamint.Storage;
import Pistamint.General.Task;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import Pistamint.Parser.Parser;
import Pistamint.TaskList.TaskList;
import Pistamint.Ui.Ui;

public class Storage {
    private static String directoryPath;
    private static String filePath;

    public Storage(String directoryPath, String filePath) {
        this.filePath = filePath;
        this.directoryPath = directoryPath;
    }

    /**
     * Load the data in file storage into the task list
     * @return the Array list of tasks that is in the file storage
     * @throws IOException when there is IO issues when processing the entry.
     */
    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();
        File directory = new File(directoryPath);
        File file = new File(filePath);
        try {
            if (!directory.exists()) {
                System.out.println("Directory does not exist. Creating directory...");
                boolean createDir = directory.mkdir();
                if (!createDir) {
                    System.out.println("Failed to create directory.");
                } else {
                    System.out.println("Directory created successfully.");
                }
            }
            // Check if the file exists, if not create it
            if (!file.exists()) {
                System.out.println("File does not exist. Creating file...");
                boolean createFile = file.createNewFile();
                if (!createFile) {
                    System.out.println("Failed to create file.");
                } else {
                    System.out.println("File created successfully.");
                }
            }
            Scanner scanner = new Scanner(file);

            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                // Parse each line into a Task object and add to tasks
                Parser.parseTask(line);
            }
            return tasks;
        } catch (IOException e) {
            Ui.runTimeException(e.getMessage());
        }
        return tasks;
    }

    /**
     * Append new line of data into file storage when user adds new task.
     * @throws IOException when there is IO issues when processing the entry.
     */
    public static void appendToFile(Task t) throws IOException {
        FileWriter fw = new FileWriter(filePath, true); // create a FileWriter in append mode
        String task= t.getSymbol()+"|"+(t.getStatusIcon().equals("X") ? "1":"0")+"|"+t.getDescription();
        fw.write(System.lineSeparator()+task);
        fw.close();
    }

    /**
     * Remove specific line of data from file storage when user initiates to delete of the task.
     * @throws IOException when there is IO issues when processing the entry.
     */
    public static void removeFromFile(ArrayList<Task> taskList) throws IOException {
        FileWriter fw = new FileWriter(filePath);
        for (Task task : taskList) {
            fw.write(task.getSymbol()+"|"+(task.getStatusIcon().equals("X") ? "1":"0")+"|"+task.getDescription());
            fw.write(System.lineSeparator());
        }
        fw.close();
    }
}