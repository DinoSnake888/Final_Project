import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;



public class BookManagement {
    static ArrayList<Book> books = new ArrayList<>();
    public BookManagement() {
        books = new ArrayList<>();
    }
    public static void removeBook(Book book) { books.remove(book); }

    public static void createBook(String title, String author, String isbn, String year, boolean available) {
        books.add(new Book(title, author, isbn, year, available));
    }

    public static void updateBook(Book book, String valueToChange, String newValue) {
        if (book != null) {
            switch (valueToChange) {
                case "title" -> book.title = newValue;
                case "author" -> book.author = newValue;
                case "year" -> book.year = newValue;
            }
        }
    }

    public static void createBookWithISBN(String isbn) {
        try {
            StringBuilder rawJSON = new StringBuilder();
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest dataRequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://openlibrary.org/api/books?bibkeys=" + isbn + "&format=json&jscmd=data"))
                    .build();
            client.sendAsync(dataRequest, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(rawJSON::append)
                    .join();
            JSONObject jsonObject = new JSONObject(rawJSON.toString());
            JSONObject properties = jsonObject.getJSONObject(isbn);
            String title = properties.optString("title", "");
            String publishDate = properties.optString("publish_date", "");
            JSONArray authors = properties.getJSONArray("authors");
            JSONObject firstAuthor = authors.getJSONObject(0);
            String authorName = firstAuthor.getString("name");

            BookManagement.createBook(title,authorName,isbn,publishDate,true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    };


}
