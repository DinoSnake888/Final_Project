public class Book{
    String title;
    String author;
    String ISBN;
    int publicationDate;
    boolean checked;

    public Book(String title, String author, String ISBN, int publicationDate, boolean checked){
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.publicationDate = publicationDate;
        this.checked = checked;
    }
}
