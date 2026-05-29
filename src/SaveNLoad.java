import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.IOException;
import java.util.Scanner;

public class SaveNLoad {
    public static void saveTime() throws IOException{
        Scanner in = new Scanner(System.in);
        String text = in.nextLine();
        String file = "Books.txt";
        Files.writeString(Path.of(file), text);

        Scanner x = new Scanner(Paths.get(file));
        String output = x.nextLine();

        System.out.println(output);
    }
}
