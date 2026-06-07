import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class SaveNLoad {

    private static final String FILE = "books.json";

    public static void save(ArrayList<Book> books) {
        try {
            JSONArray arr = new JSONArray();
            for (Book b : books) {
                JSONObject obj = new JSONObject();
                obj.put("title",     b.title);
                obj.put("author",    b.author);
                obj.put("isbn",      b.isbn);
                obj.put("year",      b.year);
                obj.put("available", b.available ? 1 : 0);
                arr.put(obj);
            }
            Files.writeString(Path.of(FILE), arr.toString(2));
        } catch (Exception e) {
            System.out.println("Save failed: " + e.getMessage());
        }
    }

    public static ArrayList<Book> load() {
        ArrayList<Book> books = new ArrayList<>();
        try {
            Path p = Path.of(FILE);
            if (!Files.exists(p)) return books;
            String raw = Files.readString(p);
            JSONArray arr = new JSONArray(raw);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                books.add(new Book(
                        obj.optString("title",  ""),
                        obj.optString("author", ""),
                        obj.optString("isbn",   ""),
                        obj.optString("year",   ""),
                        obj.optInt("available", 1) == 1
                ));
            }
        } catch (Exception e) {
            System.out.println("Load failed: " + e.getMessage());
        }
        return books;
    }
}